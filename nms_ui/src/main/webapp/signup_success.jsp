<%--
  Created by IntelliJ IDEA.
  User: smit
  Date: 3/1/22
  Time: 5:02 PM
  To change this template use File | Settings | File Templates.
--%>

<jsp:include page="index.jsp"></jsp:include>

<hr/>

    <%@ taglib uri="/struts-tags" prefix="s" %>

        <link rel="stylesheet" href="css/style.css">

        <br/>

        <%
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

        %>

        <h4 class="access middle">Welcome new user <s:property value="username"/>!</h4>
