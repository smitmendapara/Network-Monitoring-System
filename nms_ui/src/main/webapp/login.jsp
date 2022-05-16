<%--
  Created by IntelliJ IDEA.
  User: smit
  Date: 31/12/21
  Time: 3:19 PM
  To change this template use File | Settings | File Templates.
--%>

<html>
    <head>

        <title>Login Page</title>

        <%-- external css file  --%>
        <link rel="stylesheet" href="css/style.css">

        <%-- bootstrap icon --%>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css">

        <%-- jQuery --%>
<%--        <script type="text/javascript" src="./js/jquery-3.1.1.min.js" defer></script>--%>

        <%-- jQuery library --%>
        <script src="https://code.jquery.com/jquery-3.5.1.min.js" defer></script>

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>

        <%-- bootstrap file --%>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js" defer></script>

    </head>

    <%@ taglib uri="/struts-tags" prefix="s" %>

        <%-- login form --%>

        <s:form action="loginProcess" method="POST">

            <div class="demo">

                <h3 style="margin-top: 200px">Login Motadata</h3>

                <div class="demo">

                    <s:textfield name="username" label="Name" class="login__user"></s:textfield>

                    <s:password name="password" label="Password" class="login__pass"></s:password>

                    <s:submit value="login" class="log"></s:submit>

                </div>

            </div>

        </s:form>

</html>
