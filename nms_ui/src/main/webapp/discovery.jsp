<%@ page import="java.sql.Statement" %>
<%@ page import="action.dao.UserDAO" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.ResultSet" %><%--
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

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

    <script src="js/index.js"></script>

    <h3 class="middle">Create IP</h3>

    <div class="demo">

        <nav>

            <a href="" class="new middle" onclick="openForm('myForm')">new</a> <br><br>

        </nav>

    </div>

    <div class="demo">

        <table class="table" style="width: 65%" id="tb_data">
            <thead>
            <tr>
                <th scope="col">Name</th>
                <th scope="col">IP/Host</th>
                <th scope="col">Options</th>
            </tr>
            </thead>
            <tbody>

            <%
                try
                {
                    ResultSet resultSet = UserDAO.getResultSet();

                    while (resultSet.next())
                    {

            %>
                            <tr><td><%resultSet.getString("NAME");%></td></tr>
                            <tr><td><%resultSet.getString("IP");%></td></tr>
            <%
                    }

                    resultSet.close();
                }
                    catch (Exception exception)
                    {
                        exception.printStackTrace();
                    }

            %>
            </tbody>
        </table>



    </div>


    <div class="form-popUp" id="myForm">

        <form action="discoveryprocess" method="POST" class="form-container">

            <div class="demo">

                <h3>Discover IP</h3> <br>

            </div>

            <div id="type" class="sample">

                <p class="name">Name</p>

                <p  class="ip">IP/Host <input type="radio" name="type" value="1" class="rad"/></p><br>

            </div>

            <div class="sample">

                <input type="text" name="name" placeholder="Enter Name" class="disc" required>

                <input type="text" name="ip" placeholder="Enter IP/Host" class="address" required> <br>

            </div>

            <div class="demo">

                <p class="name">Device Type</p>

                <select name="deviceType" id="" class="list" style="width: 10%;height: 25px" data-drop-down="true" data-required="true" onchange="displayLinuxProfile('profile', this)">

                    <option value="0" class="ping">Ping</option>

                    <option value="1" class="linux">Linux</option>

                </select>

                <br><br>

            </div>

            <div class="event" id="profile">

                <div class="demo">

                    Username : <input type="text" name="discoveryUsername" class="linux_user"> &nbsp;&nbsp;

                    Password : <input type="discoveryPassword" class="linux_pass"> <br><br>

                </div>

            </div>

            <div class="demo">

                <button type="submit" class="btn">Create</button>

                <button type="button" class="btn cancel" onclick="closeForm('myForm')">Cancel</button>

            </div>

        </form>

    </div>


