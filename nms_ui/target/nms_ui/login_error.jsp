<%--
  Created by IntelliJ IDEA.
  User: smit
  Date: 31/12/21
  Time: 3:21 PM
  To change this template use File | Settings | File Templates.
--%>

<jsp:include page="login.jsp"></jsp:include>

    <link rel="stylesheet" href="css/style.css">

    <%
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

    %>

    <h4 class="denied middle">Sorry your username or password is wrong!</h4>