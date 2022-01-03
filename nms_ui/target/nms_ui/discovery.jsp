<%--
  Created by IntelliJ IDEA.
  User: smit
  Date: 3/1/22
  Time: 7:01 PM
  To change this template use File | Settings | File Templates.
--%>
<jsp:include page="index.jsp"></jsp:include>

<hr/>

<%@ taglib uri="/struts-tags" prefix="s" %>

<link rel="stylesheet" href="css/style.css">

<s:form action="discovery" method="POST">

    <div class="demo">

        <h3>SignUp Motadata</h3>

        <s:textfield name="username" label="Name"></s:textfield>

        <s:submit value="click"></s:submit>

    </div>

</s:form>
