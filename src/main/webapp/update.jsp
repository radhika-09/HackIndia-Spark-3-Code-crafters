<!DOCTYPE html>
<html>
<head>
    <title>Update Product</title>
    <%@include file="includes/head.jsp"%>
</head>
<body>
    <%@include file="includes/navbar.jsp"%>
    <div class="container mt-5">
        <h2>Update Product</h2>
        <form action="UpdateServlet" method="post" enctype="multipart/form-data">
            <div class="form-group">
                <label for="prodname">Product Name</label>
                <input type="text" class="form-control" id="prodname" name="prodname" value="<%= request.getAttribute("prodname") %>">
            </div>
            <div class="form-group">
                <label for="prodprice">Product Price</label>
                <input type="text" class="form-control" id="prodprice" name="prodprice" value="<%= request.getAttribute("prodprice") %>">
            </div>
            <div class="form-group">
                <label for="prodimg">Product Image</label>
                <input type="file" class="form-control" id="prodimg" name="prodimg">
            </div>
            <div class="form-group">
                <label for="proddesc">Product Description</label>
                <textarea class="form-control" id="proddesc" name="proddesc" rows="3"><%= request.getAttribute("proddesc") %></textarea>
            </div>
            <input type="hidden" name="id" value="<%= request.getAttribute("id") %>">
            <button type="submit" class="btn btn-primary">Submit Update</button>
        </form>
    </div>
    <%@include file="includes/footer.jsp"%>
</body>
</html>
