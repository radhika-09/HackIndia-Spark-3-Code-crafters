package com.exchange.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.util.logging.Logger;

@WebServlet("/sell")
@MultipartConfig
public class SellServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private WekaClassifier wekaClassifier;
    private static final Logger logger = Logger.getLogger(SellServlet.class.getName());

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            String modelPath = getServletContext().getRealPath("/accuracy.model");
            logger.info("Model Path: " + modelPath);
            wekaClassifier = new WekaClassifier(modelPath);
        } catch (Exception e) {
            throw new ServletException("Failed to initialize WekaClassifier", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("name");
        String prodName = request.getParameter("prodname");
        String brand = request.getParameter("brand");
        String model = request.getParameter("model");
        String category = request.getParameter("category");
        Part prodImgPart = request.getPart("prodimg");
        String prodPriceStr = request.getParameter("prodprice");
        String prodDesc = request.getParameter("proddesc");

        // Log the received parameters
        logger.info("Received parameters - username: " + username + ", prodName: " + prodName + ", brand: " + brand + ", model: " + model + ", category: " + category + ", prodPrice: " + prodPriceStr + ", prodDesc: " + prodDesc);

        if (username == null) {
            logger.severe("User not logged in");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "User not logged in");
            return;
        }

        if (prodName == null || brand == null || model == null || prodPriceStr == null || prodDesc == null || prodImgPart == null) {
            logger.severe("One or more input parameters are missing or invalid.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input parameters");
            return;
        }

        BigDecimal prodPrice;
        try {
            prodPrice = new BigDecimal(prodPriceStr);
        } catch (NumberFormatException e) {
            logger.severe("Invalid product price: " + prodPriceStr);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product price");
            return;
        }

        if (category == null || category.isEmpty()) {
            try {
                category = wekaClassifier.classify(prodName, brand, model);
            } catch (Exception e) {
                logger.severe("Error predicting category: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error predicting category");
                return;
            }
        }

        try (Connection con = getConnection()) {
            // Check if username exists
            if (!usernameExists(con, username)) {
                logger.severe("Username does not exist in users table");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username does not exist");
                return;
            }

            String sql = "INSERT INTO products (prodname, brand, model, category, prodimg, prodprice, proddesc, username) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, prodName);
                pst.setString(2, brand);
                pst.setString(3, model);
                pst.setString(4, category);
                pst.setBytes(5, getBytesFromInputStream(prodImgPart.getInputStream()));
                pst.setBigDecimal(6, prodPrice);
                pst.setString(7, prodDesc);
                pst.setString(8, username);

                int rowsInserted = pst.executeUpdate();
                if (rowsInserted > 0) {
                    logger.info("Product saved successfully.");
                    response.sendRedirect("mysell.jsp");
                } else {
                    logger.severe("Failed to save product.");
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save product");
                }
            }
        } catch (SQLException e) {
            logger.severe("Error saving product to database: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error saving product to database");
        }
    }

    private boolean usernameExists(Connection con, String username) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        }
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/ExchangeAgency?allowPublicKeyRetrieval=true&useSSL=false";
        String username = "root";
        String password = "radhika@12";
        return DriverManager.getConnection(url, username, password);
    }

    private byte[] getBytesFromInputStream(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            byte[] data = new byte[1024];
            int nRead;
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            return buffer.toByteArray();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
