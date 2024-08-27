package com.exchange.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/index")
public class IndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("name");
        int productId = Integer.parseInt(request.getParameter("id"));

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ExchangeAgency", "root", "radhika@12");

            String sqlGetProduct = "SELECT * FROM products WHERE id = ?";
            pst = con.prepareStatement(sqlGetProduct);
            pst.setInt(1, productId);
            rs = pst.executeQuery();

            if (rs.next()) {
                String prodName = rs.getString("prodname");
                String prodPrice = rs.getString("prodprice");
                Blob prodImg = rs.getBlob("prodimg");
                String prodDesc = rs.getString("proddesc");

                String sqlInsertInquiry = "INSERT INTO inquiries (username, prodname, prodprice, prodimg, proddesc) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstInsert = con.prepareStatement(sqlInsertInquiry);
                pstInsert.setString(1, username);
                pstInsert.setString(2, prodName);
                pstInsert.setString(3, prodPrice);
                pstInsert.setBlob(4, prodImg);
                pstInsert.setString(5, prodDesc);

                pstInsert.executeUpdate();
                pstInsert.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception e) { e.printStackTrace(); }
            if (pst != null) try { pst.close(); } catch (Exception e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        response.sendRedirect("cart");
    }
}
