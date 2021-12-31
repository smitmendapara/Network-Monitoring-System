<%--
  Created by IntelliJ IDEA.
  User: smit
  Date: 31/12/21
  Time: 3:19 PM
  To change this template use File | Settings | File Templates.
--%>
<jsp:include page="index.jsp"></jsp:include>
<hr/>
<%@ taglib uri="/struts-tags" prefix="s" %>

<s:form action="loginprocess" method="POST">

    <s:textfield name="username" label="Name"></s:textfield>

    <s:password name="password" label="Password"></s:password>

    <s:submit value="login"></s:submit>
</s:form>
