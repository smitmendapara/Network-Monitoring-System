<%@ page import="java.sql.ResultSet" %>

<%@ page import="action.dao.UserDAO" %>

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

                        %>

                        title:{
                            text: "<%=IP %>"
                        },

                        data: [
                            {
                                type: "doughnut",
                                dataPoints: [
                                    {  y: 100.0, indexLabel: "<%=IP %>" },
                                ]
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

                        data: [
                            {
                                type: "area",
                                dataPoints: [ //array

                                    { x: new Date(2012, 00, 1), y: 2600 },
                                    { x: new Date(2012, 01, 1), y: 3800 },
                                    { x: new Date(2012, 02, 1), y: 4300 },
                                    { x: new Date(2012, 03, 1), y: 2900 },
                                    { x: new Date(2012, 04, 1), y: 4100 },
                                    { x: new Date(2012, 05, 1), y: 4500 },
                                    { x: new Date(2012, 06, 1), y: 8600 },
                                    { x: new Date(2012, 07, 1), y: 6400 },
                                    { x: new Date(2012, 08, 1), y: 5300 },
                                    { x: new Date(2012, 09, 1), y: 6000 }
                                ]
                            }
                        ]
                    });

                secondChart.render();
            }

        </script>

        <div>

            <table width="100%">

                <tbody>

                    <tr style="height: 300px">

                        <%-- dougnut chart --%>
                        <td class="dash__firstRow">

                            <div id="dougnutChart" style="height: 270px; width: 100%;"></div>

                        </td>

                            <%
                                try
                                {
                                    ResultSet resultSet = UserDAO.getDashboardData();

                                    while (resultSet.next())
                                    {
                                        String IP = resultSet.getString(3);

                                        String Response = resultSet.getString(7);

                            %>

                            <%-- first widgets --%>
                            <td class="dash__secondRow">

                                <div class="dash__widget">

                                    <div>

                                        <p>Sent Packet</p>

                                    </div>

                                    <div>

                                        <p class="dash__response"><%=Response.substring(33, 34) %></p>

                                    </div>

                                </div>

                            </td>

                            <%-- second widgets --%>
                            <td class="dash__thirdRow">

                                <div class="dash__widget">

                                    <div>

                                        <p>Packet Loss (%)</p>

                                    </div>

                                    <div>

                                        <p class="dash__response"><%=Response.substring(68, 69) %></p>

                                    </div>

                                </div>

                            </td>

                            <%-- third widgets --%>
                            <td class="dash__fourthRow">

                                <div class="dash__widget">

                                    <div>

                                        <p>Received Packet</p>

                                    </div>

                                    <div>

                                        <p class="dash__response"><%=Response.substring(56, 57) %></p>

                                    </div>

                                </div>

                            </td>

                            <%-- fourth widgets --%>
                            <td class="dash__fifthRow">

                                <div class="dash__widget">

                                    <div>

                                        <p>RTT (ms)</p>

                                    </div>

                                    <div>

                                        <p class="dash__response"><%=Response.substring(118, 119) %></p>

                                    </div>

                                </div>

                            </td>

                            <%
                                    }
                                }
                                catch (Exception exception)
                                {
                                    exception.printStackTrace();
                                }
                            %>

                    </tr>

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





