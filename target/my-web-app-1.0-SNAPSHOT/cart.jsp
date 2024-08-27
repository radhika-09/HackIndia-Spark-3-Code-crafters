<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.sql.*, java.io.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Your Cart</title>
    <%@ include file="includes/head.jsp" %>
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>

    <div class="container mt-4">
        <h2>Your Inquired Products</h2>
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>Product Name</th>
                    <th>Product Price</th>
                    <th>Product Image</th>
                    <th>Product Description</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <%
                    String username = session.getAttribute("name").toString();
                    Connection con = null;
                    PreparedStatement pst = null;
                    ResultSet rs = null;

                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ExchangeAgency", "root", "Akhil@2001");
                        String sql = "SELECT id, prodname, prodprice, prodimg, proddesc FROM inquiries WHERE username = ?";
                        pst = con.prepareStatement(sql);
                        pst.setString(1, username);
                        rs = pst.executeQuery();

                        while (rs.next()) {
                            int inquiryId = rs.getInt("id");
                            String prodName = rs.getString("prodname");
                            String prodPrice = rs.getString("prodprice");
                            String prodDesc = rs.getString("proddesc");
                            Blob prodImgBlob = rs.getBlob("prodimg");

                            String base64Image = "";
                            if (prodImgBlob != null) {
                                InputStream inputStream = prodImgBlob.getBinaryStream();
                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                byte[] buffer = new byte[4096];
                                int bytesRead;
                                while ((bytesRead = inputStream.read(buffer)) != -1) {
                                    outputStream.write(buffer, 0, bytesRead);
                                }
                                byte[] imageBytes = outputStream.toByteArray();
                                base64Image = Base64.getEncoder().encodeToString(imageBytes);
                            }
                %>
                <tr>
                    <td><%= prodName %></td>
                    <td><%= prodPrice %></td>
                    <td><img src="data:image/jpg;base64,<%= base64Image %>" width="100" height="100"/></td>
                    <td><%= prodDesc %></td>
                    <td>
                        <form action="DeleteInquiryServlet" method="post">
                            <input type="hidden" name="id" value="<%= inquiryId %>"/>
                            <input type="submit" value="Delete" class="btn btn-danger"/>
                        </form>
                    </td>
                </tr>
                <%
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
                        if (pst != null) try { pst.close(); } catch (SQLException e) { e.printStackTrace(); }
                        if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
                    }
                %>
            </tbody>
        </table>
    </div>

    <%@ include file="includes/footer.jsp" %>
</body>
</html>