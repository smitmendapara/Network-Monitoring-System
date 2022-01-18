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

        <link rel="stylesheet" href="css/style.css">

        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css">

        <script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>

    </head>

    <body>

        <div class="first" id="myDiv" style="text-align: end; margin: 20px 20px 0 0;">

            <h2>Light - NMS</h2>

            <nav class="effect">

                <a href="login" class="btn in"><i class="bi bi-box-arrow-in-right"></i>&nbsp;login</a>

                <a href="logout" class="btn out"><i class="bi bi-box-arrow-left"></i>&nbsp;logout</a>

                <a href="signup" class="btn up"><i class="bi bi-person"></i>&nbsp;signUp</a>

                <a href="profile" class="btn file"><i class="bi bi-check2-circle"></i>&nbsp;profile</a>

            </nav>

            <br>

        </div>

        <%--<div id="myDIV">--%>
            <%--<button class="btn">1</button>--%>
            <%--<button class="btn">2</button>--%>
            <%--<button class="btn">3</button>--%>
            <%--<button class="btn">4</button>--%>
            <%--<button class="btn">5</button>--%>
        <%--</div>--%>

        <script>

            // Add active class to the current button (highlight it)
            var header = document.getElementById("myDIV");
            var btns = header.getElementsByClassName("btn");
            for (var i = 0; i < btns.length; i++) {
                btns[i].addEventListener("click", function() {
                    var current = document.getElementsByClassName("active");
                    current[0].className = current[0].className.replace(" active", "");
                    this.className += " active";
                });
            }
        </script>

        <%--<script>--%>

            <%--var header = document.getElementById("indexNav");--%>
            <%--var btns = header.getElementsByClassName("index__btn");--%>

            <%--for (var i = 0; i < btns.length; i++)--%>
            <%--{--%>
                <%--btns[i].addEventListener("click", function () {--%>
                    <%--var current = document.getElementsByClassName("active");--%>
                    <%--current[0].className = current[0].className.replace(" active", "");--%>
                    <%--this.className += " active";--%>
                <%--});--%>
            <%--}--%>

        <%--</script>--%>

    </body>

</html>
