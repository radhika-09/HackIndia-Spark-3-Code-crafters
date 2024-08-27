<%
if (session.getAttribute("name") == null) {
	response.sendRedirect("signin.jsp");
}
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.sql.*, java.io.*, com.exchange.model.Product" %>
<!DOCTYPE html>
<html>
<head>
    <title>Index</title>
    <%@ include file="includes/head.jsp" %>
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>

    <div class="container mt-4">
        <h2>Available Products</h2>
        <div class="row">
            <%
                Connection con = null;
                PreparedStatement pst = null;
                ResultSet rs = null;

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ExchangeAgency", "root", "Akhil@2001");
                    String sql = "SELECT * FROM products";
                    pst = con.prepareStatement(sql);
                    rs = pst.executeQuery();

                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("prodname");
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
            %>
            <div class="col-md-4">
                <div class="card mb-4 shadow-sm">
                    <img src="data:image/jpg;base64,<%= base64Image %>" class="card-img-top" alt="<%= name %>">
                    <div class="card-body">
                        <h5 class="card-title"><%= name %></h5>
                        <p class="card-text">Price: <%= price %></p>
                        <p class="card-text"><%= description %></p>
                        <form action="index" method="post">
                            <input type="hidden" name="id" value="<%= id %>">
                            <button type="submit" class="btn btn-primary">Inquire</button>
                        </form>
                    </div>
                </div>
            </div>
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
        </div>
    </div>

    <%@ include file="includes/footer.jsp" %>
</body>
</html>
