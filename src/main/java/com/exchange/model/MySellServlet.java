package com.exchange.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/mysell")
public class MySellServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("name");
        List<Product> products = new ArrayList<>();

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ExchangeAgency?allowPublicKeyRetrieval=true", "root", "radhika@12");
            String sql = "SELECT id, prodname, brand, model, category, prodprice, prodimg, proddesc FROM products WHERE username = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, username);
            rs = pst.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("prodname");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                String category = rs.getString("category");
                String price = rs.getString("prodprice");
                Blob imgBlob = rs.getBlob("prodimg");
                String description = rs.getString("proddesc");

                String base64Image = "";
                if (imgBlob != null) {
                    InputStream inputStream = imgBlob.getBinaryStream();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    byte[] imageBytes = outputStream.toByteArray();
                    base64Image = Base64.getEncoder().encodeToString(imageBytes);
                }

                Product product = new Product(id, name, price, model, brand, category, base64Image, description);
                products.add(product);
            }

            request.getSession().setAttribute("products", products);
            response.sendRedirect("mysell.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
            if (pst != null) try { pst.close(); } catch (Exception e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
