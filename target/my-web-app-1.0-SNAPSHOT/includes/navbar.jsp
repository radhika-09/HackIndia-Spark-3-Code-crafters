<nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container-fluid">
        <a class="navbar-brand" href="#">Navbar</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link active" aria-current="page" href="index.jsp">Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" aria-current="page" href="sell.jsp">Exchange</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" aria-current="page" href="cart.jsp">Cart</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" aria-current="page" href="mysell.jsp">MySell</a>
                </li>
                <li class="nav-item">
   					 <a class="nav-link active" aria-current="page" href="#">Welcome <%= session.getAttribute("name") %></a>
				</li>
				</ul>
				<ul class="navbar-nav mr-auto">
				 <li class="nav-item">
                    <a class="nav-link active" aria-current="page" href="logout">Logout</a>
                </li>
            </ul>
        </div>
    </div>
</nav>
