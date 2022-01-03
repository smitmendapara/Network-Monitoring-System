<%--
  Created by IntelliJ IDEA.
  User: smit
  Date: 3/1/22
  Time: 2:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>



<%--<html>--%>
<%--<head>--%>
    <%--<title>SignUp Page</title>--%>
<%--</head>--%>
<%--<body>--%>

<%--</body>--%>
<%--</html>--%>


<jsp:include page="index.jsp"></jsp:include>

<hr/>

<%@ taglib uri="/struts-tags" prefix="s" %>

<link rel="stylesheet" href="css/style.css">

    <s:form action="signupprocess" method="POST">

        <div class="demo">

            <h3>SignUp Motadata</h3>

            <s:textfield name="username" label="Name" class="user"></s:textfield>

            <s:password name="password" label="Password" class="pass"></s:password>

            <s:submit value="signUp" class="log"></s:submit>

        </div>

    </s:form>
