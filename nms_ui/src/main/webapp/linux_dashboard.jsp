<%@ page import="java.sql.ResultSet" %>

<%@ page import="dao.UserDAO" %>

<%@ page import="java.text.SimpleDateFormat" %>

<%@ page import="java.util.Date" %>

<%@ page import="java.util.Calendar" %>

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

        <div class="dashboard__details">

            <table width="100%">

                <tbody id="linuxDashboardHeader">


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
                                       String ip = resultSet.getString(3);

                                       String ipStatus = resultSet.getString(8);

                        %>

                        title:{
                            text: "<%=ip %>"
                        },

                        data: [
                            {
                                type: "doughnut",

                                <%

                                    if (ipStatus.equals("Down"))
                                    {

                                %>


                                xValueFormatString:"Up # %",

                                yValueFormatString:"Down # %",

                                color: "#A21919",

                                dataPoints: [
                                    {  x: 0, y: 1.0, indexLabel: "<%=ip %>" },
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
                                    {  x: 0, y: 1.0, indexLabel: "<%=ip %>" },
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
                                       int id = resultSet.getInt(1);

                                       String ip = resultSet.getString(3);

                                       String responseData = resultSet.getString(7);

                                       String time1 = dateFormat.format(date);

                        %>


                        axisY: {
                            title: "Memory (%)",
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

                <tbody id="linuxDashboardTableBody">


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

            getLinuxDashboardHeader();

            getLinuxDashboardBody();
        }

    </script>