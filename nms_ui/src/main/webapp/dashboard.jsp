<%@ page import="java.sql.ResultSet" %>

<%@ page import="action.dao.UserDAO" %>

<%@ page import="static action.helper.ServiceProvider.getSentPacket" %>

<%@ page import="static action.helper.ServiceProvider.getPacketLoss" %>

<%@ page import="static action.helper.ServiceProvider.getReceivedPacket" %>

<%@ page import="static action.helper.ServiceProvider.*" %>

<%@ page import="java.util.Calendar" %>

<%@ page import="java.text.SimpleDateFormat" %>

<%@ page import="java.util.Date" %>

<%@ page import="static action.dao.UserDAO.getUpdatedPacket" %>

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
                            int id = resultSet.getInt(1);

                            String ip = resultSet.getString(3);

                            String deviceType = resultSet.getString(6);

                            String ipStatus = resultSet.getString(8);

                %>

                <tr>

                    <td><b>IP/Host</b>: <%=ip %>&nbsp;&nbsp;<i class="bi bi-activity" style="color: #2a92ff;"></i></td>

                    <td><b>Profile</b>: <%=resultSet.getString(4)%></td>

                    <td><b>Poll Time</b>: <%=resultSet.getString(9)%>&nbsp;&nbsp;<a href="" title="Poll Now" onclick="getPolling('<%=id %>', '<%=ip %>', '<%=deviceType%>')"><i class="bi bi-arrow-repeat" style="cursor:pointer;"></i></a></td>

                </tr>

                <tr>

                    <td><b>Id</b>: <%=resultSet.getString(1)%></td>

                    <%

                        if (ipStatus.equals("Up"))
                        {

                    %>

                    <td><b>Status</b>: <%=resultSet.getString(8)%> &nbsp;&nbsp;<i class="bi bi-arrow-up-circle"></i></td>

                    <%

                    }
                    else
                    {

                    %>

                    <td><b>Status</b>: <%=resultSet.getString(8)%> &nbsp;&nbsp;<i class="bi bi-arrow-down-circle"></i></td>

                    <%
                        }

                    %>

                    <td><b>DeviceType</b>: <%=resultSet.getString(6)%></td>

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
                                       String ip = resultSet.getString(3);

                                       String ipStatus = resultSet.getString(8);

                        %>

                        title:{
                            text: "<%=ip %>"
                        },

                        toolTip: {
                          shared: true
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
                                       String ip = resultSet.getString(3);

                                       String responseData = resultSet.getString(7);

                                       int id = resultSet.getInt(1);

                                       String time1 = dateFormat.format(date);

                        %>


                        axisY: {
                            title: "Received Packet"
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

                                    { label: "<%=time2 %>", y: <%=getUpdatedPacket(id, ip, time2) %> },

                                    <%

                                        currentTime.add(Calendar.MINUTE, -5);

                                        Date thirdTime = currentTime.getTime();

                                        String time3 = dateFormat.format(thirdTime);
                                    %>

                                    { label: "<%=time3 %>", y: <%=getUpdatedPacket(id, ip, time3) %> },

                                    <%
                                        currentTime.add(Calendar.MINUTE, -5);

                                        Date fourthTime = currentTime.getTime();

                                        String time4 = dateFormat.format(fourthTime);
                                    %>

                                    { label: "<%=time4 %>", y: <%=getUpdatedPacket(id, ip, time4) %> },

                                    <%
                                        currentTime.add(Calendar.MINUTE, -5);

                                        Date fifthTime = currentTime.getTime();

                                        String time5 = dateFormat.format(fifthTime);
                                    %>

                                    { label: "<%=time5 %>", y: <%=getUpdatedPacket(id, ip, time5) %> },

                                    <%
                                        currentTime.add(Calendar.MINUTE, -5);

                                        Date sixthTime = currentTime.getTime();

                                        String time6 = dateFormat.format(sixthTime);
                                    %>

                                    { label: "<%=time6 %>", y: <%=getUpdatedPacket(id, ip, time6) %> },

                                    <%
                                        currentTime.add(Calendar.MINUTE, -5);

                                        Date sevenTime = currentTime.getTime();

                                        String time7 = dateFormat.format(sevenTime);
                                    %>

                                    { label: "<%=time7 %>", y: <%=getUpdatedPacket(id, ip, time7) %> },

                                    <%
                                        currentTime.add(Calendar.MINUTE, -5);

                                        Date eightTime = currentTime.getTime();

                                        String time8 = dateFormat.format(eightTime);
                                    %>

                                    { label: "<%=time8 %>", y: 1 },
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

                <tbody>

                    <tr style="height: 300px">

                        <%-- dougnut chart --%>
                        <td class="dash__firstRow">

                            <div id="dougnutChart" style="height: 270px;"></div>

                        </td>

                            <%
                                try
                                {
                                    ResultSet resultSet = UserDAO.getDashboardData();

                                    while (resultSet.next())
                                    {
                                        String responseData = resultSet.getString(7);

                                        String ipStatus = resultSet.getString(8);

                                        if (ipStatus.equals("Up"))
                                        {

                            %>

                            <%-- first widgets --%>
                            <td class="dash__secondRow">

                                <div class="dash__widget">

                                    <div>

                                        <p>Sent Packet</p>

                                    </div>

                                    <div>

                                        <p class="dash__response"><%=getSentPacket(responseData) %></p>

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

                                        <p class="dash__response"><%=getPacketLoss(responseData) + " %" %></p>

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

                                        <p class="dash__response"><%=getReceivedPacket(responseData) %></p>

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

                                        <p class="dash__response"><%=getRTTTime(responseData) + " ms" %></p>

                                    </div>

                                </div>

                            </td>

                            <%
                                        }

                                        if (ipStatus.equals("Down"))
                                        {

                            %>

                            <%-- first widgets --%>
                            <td class="dash__secondRow">

                                <div class="dash__widget">

                                    <div>

                                        <p>Sent Packet</p>

                                    </div>

                                    <div>

                                        <p class="dash__response"><%=getSentPacket(responseData) %></p>

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

                                        <p class="dash__response"><%=getPacketLoss(responseData) + " %" %></p>

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

                                        <p class="dash__response"><%=getReceivedPacket(responseData) %></p>

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

                                        <p class="dash__response"><%=getRTTTime(responseData) %></p>

                                    </div>

                                </div>

                            </td>

                            <%
                                        }

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
