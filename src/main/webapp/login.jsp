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

        <%-- external css file --%>

        <link rel="stylesheet" href="css/style.css">

        <%-- login form --%>

        <s:form action="loginProcess" method="POST">

            <div class="demo">

                <h3>Login Motadata</h3>

                <div class="demo">

                    <s:textfield name="username" label="Name" class="login__user"></s:textfield>

                    <s:password name="password" label="Password" class="login__pass"></s:password>

                    <s:submit value="login" class="log"></s:submit>

                </div>

            </div>

        </s:form>
