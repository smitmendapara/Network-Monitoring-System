<%--
  Created by IntelliJ IDEA.
  User: smit
  Date: 10/1/22
  Time: 5:53 PM
  To change this template use File | Settings | File Templates.
--%>
<jsp:include page="index.jsp"></jsp:include>

<hr/>

<%@ taglib uri="/struts-tags" prefix="s" %>

<link rel="stylesheet" href="css/style.css">

<br/>

<h4 class="access middle">Welcome, <s:property value="username"/></h4>

<div class="demo">

    <nav>

        <a href="discover" class="lg__cover">Discovery</a>

        <a href="monitor" class="lg__monitor">Monitor</a>

    </nav>

</div>
