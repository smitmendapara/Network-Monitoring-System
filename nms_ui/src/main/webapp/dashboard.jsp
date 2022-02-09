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

                var firstChart = new CanvasJS.Chart("dougnutChart",
                    {
                        width : 350,

                        <%
                               try
                               {
                                   ResultSet resultSet = UserDAO.getDashboardData();

                                   while (resultSet.next())
                                   {
                                       String IP = resultSet.getString(3);

                                       String status = resultSet.getString(8);

                        %>

                        title:{
                            text: "<%=IP %>"
                        },

                        toolTip: {
                            shared: true
                        },

                        data: [
                            {
                                type: "doughnut",

                                <%

                                    if (status.equals("Down"))
                                    {

                                %>

                                xValueFormatString:"Up # %",

                                yValueFormatString:"Down # %",

                                color: "#A21919",

                                dataPoints: [
                                    {  x: 0, y: 1.0, indexLabel: "<%=IP %>" },
                                ]

                                <%

                                    }
                                    else
                                    {

                                %>

                                xValueFormatString:"Down # %",

                                yValueFormatString:"Up # %",

                                color: "#008000",

                                dataPoints: [
                                    {  x: 0, y: 1.0, indexLabel: "<%=IP %>" },
                                ]

                                <%

                                    }

                                %>
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

                firstChart.render();

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

                                dataPoints: [

                                    { label: "<%=time1 %>", y: <%=getReceivedPacket(responseData) %> },

                                    <%
                                        currentTime.add(Calendar.MINUTE, -5);

                                        Date secondTime = currentTime.getTime();

                                        String time2 = dateFormat.format(secondTime);

                                    %>

                                    { label: "<%=time2 %>", y: <%=getUpdatedPacket(id, IP, time2) %> },

                                    <%

                                        currentTime.add(Calendar.MINUTE, -5);

                                        Date thirdTime = currentTime.getTime();

                                        String time3 = dateFormat.format(thirdTime);
                                    %>

                                    { label: "<%=time3 %>", y: <%=getUpdatedPacket(id, IP, time3) %> },

                                    <%
                                        currentTime.add(Calendar.MINUTE, -5);

                                        Date fourthTime = currentTime.getTime();

                                        String time4 = dateFormat.format(fourthTime);
                                    %>

                                    { label: "<%=time4 %>", y: <%=getUpdatedPacket(id, IP, time4) %> },

                                    <%
                                        currentTime.add(Calendar.MINUTE, -5);

                                        Date fifthTime = currentTime.getTime();

                                        String time5 = dateFormat.format(fifthTime);
                                    %>

                                    { label: "<%=time5 %>", y: <%=getUpdatedPacket(id, IP, time5) %> },

                                    <%
                                        currentTime.add(Calendar.MINUTE, -5);

                                        Date sixthTime = currentTime.getTime();

                                        String time6 = dateFormat.format(sixthTime);
                                    %>

                                    { label: "<%=time6 %>", y: <%=getUpdatedPacket(id, IP, time6) %> },

                                    <%
                                        currentTime.add(Calendar.MINUTE, -5);

                                        Date sevenTime = currentTime.getTime();

                                        String time7 = dateFormat.format(sevenTime);
                                    %>

                                    { label: "<%=time7 %>", y: <%=getUpdatedPacket(id, IP, time7) %> },

                                    <%
                                        currentTime.add(Calendar.MINUTE, -5);

                                        Date eightTime = currentTime.getTime();

                                        String time8 = dateFormat.format(eightTime);
                                    %>

                                    { label: "<%=time8 %>", y: <%=getUpdatedPacket(id, IP, time8) %> },
                                ],
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

                        <div id="areaChart" style="height: 300px; width: 100%;"></div>

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
