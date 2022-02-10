<%@ page import="java.sql.ResultSet" %>

<%@ page import="dao.UserDAO" %>

<%@ page import="static action.helper.ServiceProvider.getSentPacket" %>

<%@ page import="static action.helper.ServiceProvider.getPacketLoss" %>

<%@ page import="static action.helper.ServiceProvider.getReceivedPacket" %>

<%@ page import="static action.helper.ServiceProvider.*" %>

<%@ page import="java.util.Calendar" %>

<%@ page import="java.text.SimpleDateFormat" %>

<%@ page import="java.util.Date" %>

<%@ page import="static dao.UserDAO.getUpdatedPacket" %>

<%--
  Created by IntelliJ IDEA.
  User: smit
  Date: 19/1/22
  Time: 7:03 PM
  To change this template use File | Settings | File Templates.
--%>

<jsp:include page="index.jsp"></jsp:include>

<hr/>

    <%@ taglib uri="/struts-tags" prefix="s" %>

    <%-- css file --%>

    <link rel="stylesheet" href="/css/style.css">

    <%-- icon library --%>

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css">

    <%-- chart library --%>

    <script type="text/javascript" src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>

    <%-- jQuery library --%>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

    <script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>

    <%-- js file --%>

    <script src="js/index.js"></script>

    <br/>

    <body>

        <%
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

            if (session.getAttribute("username") == null)
            {
                response.sendRedirect("login.jsp");
            }
        %>

        <div class="demo" style="float:left;margin-top: 10px">

            <nav>

                <a href="discover" class="lg__cover"><i class="bi bi-disc"></i>&nbsp;Discovery</a>

                <a href="monitor" class="lg__monitor"><i class="bi bi-tv"></i>&nbsp;Monitor</a>

                <a href="dashboard" class="lg__dashboard"><i class="bi bi-grid"></i>&nbsp;Dashboard</a>

            </nav>

        </div>

        <br><br>

        <hr/>

        <div>

            <table width="100%">

                <tbody id="dashboardHeader">


                </tbody>

            </table>

        </div>

        <script type="text/javascript">

            window.onload = function () {

                var secondChart = new CanvasJS.Chart("areaChart",
                    {
                        width: 1420,

                        title: {
                            text: ""
                        },

                        <%
                               try
                               {
                                   ResultSet resultSet = UserDAO.getDashboardData();

                                   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                                   Date date = new Date();

                                   Calendar currentTime = Calendar.getInstance();

                                   currentTime.setTime(date);

                                   while (resultSet.next())
                                   {
                                       String IP = resultSet.getString(3);

                                       String responseData = resultSet.getString(7);

                                       int id = resultSet.getInt(1);

                                       String time1 = dateFormat.format(date);

                        %>

                        axisY: {
                            title: "Received Packet",
                            minimum: 0
                        },

                        axisX: {
                            valueFormatString: "##",
                            title: "Minutes Interval"
                        },

                        data: [
                            {

                                type: "column",

                                dataPoints: []


                            }
                        ]

                        <%
                                   }
                               }
                               catch (Exception exception)
                               {
                                   exception.printStackTrace();
                               }
                        %>
                    });

                secondChart.render();
            }

        </script>

        <div>

            <table width="100%">

                <tbody id="dashboardTableBody">



                </tbody>

            </table>

        </div>

        <div>

            <table width="100%">

                <tbody>

                <tr>

                    <td>

                        <%-- area chart --%>

                        <div id="areaChart" style="height: 300px;"></div>

                    </td>

                </tr>

                </tbody>

            </table>

        </div>

    </body>

    <script>

        window.onload = function () {

            getDashboardHeader();

            getDashboardBody();

        }

    </script>
