<%--
  Created by IntelliJ IDEA.
  User: smit
  Date: 31/12/21
  Time: 2:25 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>

    <head>

        <title>Home Page</title>

        <%-- external css file  --%>

        <link rel="stylesheet" href="css/style.css">

        <%-- bootstrap icon --%>

        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css">

        <%-- jQuery --%>

        <script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>

    </head>

    <body>

        <div class="first" style="text-align: end; margin: 20px 20px 0 0;">

            <h2>Light - NMS</h2>

            <nav class="effect">

                <a href="login" class="index__login"><i class="bi bi-box-arrow-in-right"></i>&nbsp;login</a>

                <a href="logout" class="index__logout"><i class="bi bi-box-arrow-left"></i>&nbsp;logout</a>

                <a href="signup" class="index__signup"><i class="bi bi-person"></i>&nbsp;signUp</a>

                <a href="profile" class="index__profile"><i class="bi bi-check2-circle"></i>&nbsp;profile</a>

            </nav>

            <br>

        </div>

    </body>

</html>
