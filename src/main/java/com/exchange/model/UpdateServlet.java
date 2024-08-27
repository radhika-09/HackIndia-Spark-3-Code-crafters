package com.exchange.model;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
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

@WebServlet("/UpdateServlet")
@MultipartConfig
public class UpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String dbURL = "jdbc:mysql://localhost:3306/ExchangeAgency";
        String dbUser = "root";
        String dbPass = "radhika@12";

        try (Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPass)) {
            String sql = "SELECT * FROM products WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                request.setAttribute("id", resultSet.getInt("id"));
                request.setAttribute("prodname", resultSet.getString("prodname"));
                request.setAttribute("prodprice", resultSet.getString("prodprice"));
                request.setAttribute("proddesc", resultSet.getString("proddesc"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        request.getRequestDispatcher("update.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String prodName = request.getParameter("prodname");
        String prodPrice = request.getParameter("prodprice");
        Part filePart = request.getPart("prodimg");
        String prodDesc = request.getParameter("proddesc");

        InputStream prodImgInputStream = null;
        if (filePart != null && filePart.getSize() > 0) {
            prodImgInputStream = filePart.getInputStream();
        }

        String dbURL = "jdbc:mysql://localhost:3306/ExchangeAgency";
        String dbUser = "root";
        String dbPass = "radhika@12";

        try (Connection connection = DriverManager.getConnection(dbURL, dbUser, dbPass)) {
            String sql;
            PreparedStatement statement;

            if (prodImgInputStream != null) {
                sql = "UPDATE products SET prodname = ?, prodprice = ?, prodimg = ?, proddesc = ? WHERE id = ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, prodName);
                statement.setString(2, prodPrice);
                statement.setBlob(3, prodImgInputStream);
                statement.setString(4, prodDesc);
                statement.setInt(5, id);
            } else {
                sql = "UPDATE products SET prodname = ?, prodprice = ?, proddesc = ? WHERE id = ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, prodName);
                statement.setString(2, prodPrice);
                statement.setString(3, prodDesc);
                statement.setInt(4, id);
            }

            int result = statement.executeUpdate();
            if (result > 0) {
                response.sendRedirect("mysell.jsp");
            } else {
                response.sendRedirect("error.jsp");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
