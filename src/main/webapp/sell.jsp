<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Sell Product</title>
    <%@ include file="includes/head.jsp" %>
    <script>
    function predictCategory() {
        const prodName = document.getElementById("prodname").value;
        const brand = document.getElementById("brand").value;
        const model = document.getElementById("model").value;

        if (prodName.trim() === "" || brand.trim() === "" || model.trim() === "") {
            document.getElementById("category").value = "";
            return;
        }

        const xhr = new XMLHttpRequest();
        xhr.open("POST", "predictCategory", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    document.getElementById("category").value = xhr.responseText.trim();
                } else {
                    console.error("Error predicting category:", xhr.responseText);
                    document.getElementById("category").value = "Error";
                }
            }
        };
        xhr.send("prodname=" + encodeURIComponent(prodName) + "&brand=" + encodeURIComponent(brand) + "&model=" + encodeURIComponent(model));
    }

    function predictCategoryButtonClicked(event) {
        event.preventDefault();  // Prevent the form from submitting
        predictCategory();
    }
    </script>
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    <div class="container mt-4">
        <h3>Sell Your Product</h3>
        <form action="sell" method="post" enctype="multipart/form-data">
            <div class="mb-3 row">
                <label for="prodname" class="col-sm-2 col-form-label">Product Name</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" id="prodname" name="prodname" placeholder="Enter Product Name" required>
                </div>
            </div>
            <div class="mb-3 row">
                <label for="brand" class="col-sm-2 col-form-label">Brand</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" id="brand" name="brand" placeholder="Enter Brand" required>
                </div>
            </div>
            <div class="mb-3 row">
                <label for="model" class="col-sm-2 col-form-label">Model</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" id="model" name="model" placeholder="Enter Model" required>
                </div>
            </div>
            <div class="mb-3 row">
                <label for="category" class="col-sm-2 col-form-label">Predicted Category</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" id="category" name="category" readonly>
                </div>
            </div>
            <div class="mb-3">
                <button type="button" class="btn btn-secondary" onclick="predictCategoryButtonClicked(event)">Predict Category</button>
            </div>
            <div class="mb-3">
                <label for="prodimg" class="form-label">Add Product Image</label>
                <input class="form-control" type="file" id="prodimg" name="prodimg" required>
            </div>
            <div class="mb-3 row">
                <label for="prodprice" class="col-sm-2 col-form-label">Product Price</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" id="prodprice" name="prodprice" placeholder="Enter Product Price" required>
                </div>
            </div>
            <div class="mb-3 row">
                <label for="proddesc" class="col-sm-2 col-form-label">Product Description</label>
                <div class="col-sm-10">
                    <textarea class="form-control" id="proddesc" name="proddesc" rows="3" placeholder="Enter Product Description" required></textarea>
                </div>
            </div>
            <button type="submit" class="btn btn-primary">Submit</button>
        </form>
    </div>
    <%@ include file="includes/footer.jsp" %>
</body>
</html>
