<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Sign Up</title>
<%@include file="includes/head.jsp" %>
</head>
<body>

<input type="hidden" id="status" value="<%= request.getAttribute("status")%>">
<div class="container">
<div class="card w-50 mx-auto my-5">
<div class="card-header text-center">User Sign-Up</div>
<div class="card-body">
<form action="signup" method="post">

<div class="form-group">
<label>User-name</label>
<input type="text" class="form-control" name="User-name" placeholder="Enter Username" required>
</div>

<div class="form-group">
<label>Email</label>
<input type="email" class="form-control" name="email" placeholder="Enter email" required>
</div>
<br/>

<div class="form-group">
<label>Password</label>
<input type="password" class="form-control" name="password" placeholder="Enter Password" required>
</div>
<br/>
<div class="text-center">
<button type="submit" class="btn btn-primary">Sign Up</button>
</div>
</form>
<a href="signin.jsp">Already have an account? Sign In</a>
</div>
</div>
</div>

<!--JS-->
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
<script type="text/javascript">
	var status = document.getElementById("status").value;
	if(status == "success"){
		swal("Congrats", "Account Created Successfully", "success");
	}
</script>
<%@include file="includes/footer.jsp" %>
</body>
</html>