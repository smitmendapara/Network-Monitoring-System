<%--
  Created by IntelliJ IDEA.
  User: smit
  Date: 31/12/21
  Time: 3:21 PM
  To change this template use File | Settings | File Templates.
--%>

<jsp:include page="index.jsp"></jsp:include>

<hr/>

<%@ taglib uri="/struts-tags" prefix="s" %>

    <link rel="stylesheet" href="css/style.css">

    <br/>

    <h4 class="access">Welcome, <s:property value="username"/></h4>

    <a href="discover" class="cover">Discovery</a>

    <a href="monitor" class="monitor">Monitor</a>