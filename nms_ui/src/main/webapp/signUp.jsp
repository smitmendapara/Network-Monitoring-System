<%--
  Created by IntelliJ IDEA.
  User: smit
  Date: 3/1/22
  Time: 2:32 PM
  To change this template use File | Settings | File Templates.
--%>

<jsp:include page="index.jsp"></jsp:include>

<hr/>

    <%@ taglib uri="/struts-tags" prefix="s" %>

    <link rel="stylesheet" href="css/style.css">

        <%
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

        %>

        <s:form action="signUpProcess" method="POST">

            <div class="demo">

                <h3>SignUp Motadata</h3>

                <div class="demo effect">

                    <s:textfield name="username" label="Name" class="signup__user"></s:textfield>

                    <s:password name="password" label="Password" class="signup__pass"></s:password>

                    <s:submit value="signUp" class="log"></s:submit>

                </div>

            </div>

        </s:form>
