<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Sign In</title>
<%@include file="includes/head.jsp" %>
</head>
<body>
<input type="hidden" id="status" value="<%= request.getAttribute("status")%>">
<div class="container">
<div class="card w-50 mx-auto my-5">
<div class="card-header text-center">User Sign-In</div>
<div class="card-body">
<form action="signin" method="post">

<div class="form-group">
<label>User-name</label>
<input type="text" class="form-control" name="User-name" placeholder="Enter Username" required>
</div>

<div class="form-group">
<label>Password</label>
<input type="password" class="form-control" name="password" placeholder="Enter Password" required>
</div>
<br/>
<div class="text-center">
<button type="submit" class="btn btn-primary">Sign In</button>
</div>
</form>
<a href="signup.jsp">Don't have an account? Sign Up</a>
</div>
</div>
</div>

<!--JS-->
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
<script type="text/javascript">
    document.addEventListener("DOMContentLoaded", function() {
        var status = document.getElementById("status").value;
        if (status === "failed") {
            swal("Sorry", "Wrong Username or Password", "error");
        }
    });
</script>

<%@include file="includes/footer.jsp" %>
</body>
</html>