<%@ page import="java.sql.ResultSet" %>

<%@ page import="action.dao.UserDAO" %>

<%--
  Created by IntelliJ IDEA.
  User: smit
  Date: 24/1/22
  Time: 5:08 PM
  To change this template use File | Settings | File Templates.
--%>

<jsp:include page="index.jsp"></jsp:include>

<hr/>

    <%@ taglib uri="/struts-tags" prefix="s" %>

    <%-- css file --%>

    <link rel="stylesheet" href="../css/style.css">

    <%-- icon library --%>

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css">

    <%-- chart library --%>

    <script type="text/javascript" src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>

    <%--<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>--%>

    <%-- jQuery library --%>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

    <script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>

    <%-- js file --%>

    <script src="js/index.js"></script>

    <br/>

    <body>

        <div class="demo" style="float:left;margin-top: 10px">

            <nav>

                <a href="discover" class="lg__cover"><i class="bi bi-disc"></i>&nbsp;Discovery</a>

                <a href="monitor" class="lg__monitor"><i class="bi bi-tv"></i>&nbsp;Monitor</a>

                <a href="dashboard" class="lg__dashboard"><i class="bi bi-grid"></i>&nbsp;Dashboard</a>

            </nav>

        </div>

        <br><br>

        <hr/>

        <div class="dashboard__details">

            <table width="100%">

                <tbody>

                <%
                    try
                    {
                        ResultSet resultSet = UserDAO.getDashboardData();

                        while (resultSet.next())
                        {
                            String IP = resultSet.getString(3);

                %>
                <tr>

                    <td><b>IP/Host</b>: <%=IP %></td>

                    <td><b>Profile</b>: <%=resultSet.getString(4)%></td>

                    <td><b>DeviceType</b>: <%=resultSet.getString(6)%></td>

                    <td><b>Status</b>: <%=resultSet.getString(8)%></td>

                </tr>
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