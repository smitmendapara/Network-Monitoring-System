<%@ page import="java.sql.ResultSet" %>

<%@ page import="dao.UserDAO" %>

<%@ page import="java.text.SimpleDateFormat" %>

<%@ page import="java.util.Date" %>

<%@ page import="java.util.Calendar" %>

<%@ page import="static action.helper.ServiceProvider.getFreeMemoryPercent" %>

<%@ page import="static action.helper.ServiceProvider.getUsedMemoryPercent" %>

<%@ page import="static action.helper.ServiceProvider.getUsedSwapPercent" %>

<%@ page import="static action.helper.ServiceProvider.*" %>

<%@ page import="static dao.UserDAO.getUpdatedMemory" %>

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

                <tbody>

                <%
                    try
                    {
                        ResultSet resultSet = UserDAO.getDashboardData();

                        while (resultSet.next())
                        {
                            int id = resultSet.getInt(1);

                            String IP = resultSet.getString(3);

                            String device = resultSet.getString(6);

                            String status = resultSet.getString(8);

                %>

                <tr>

                    <td><b>IP/Host</b>: <%=IP %>&nbsp;&nbsp;<i class="bi bi-activity" style="color: #2a92ff"></i></td>

                    <td><b>Profile</b>: <%=resultSet.getString(4)%></td>

                    <td><b>Poll Time</b>: <%=resultSet.getString(9)%>&nbsp;&nbsp;<a href="" title="Poll Now" onclick="getPolling('<%=id %>', '<%=IP %>', '<%=device%>')"><i class="bi bi-arrow-repeat" style="cursor:pointer;"></i></a></td>

                </tr>

                <tr>

                    <td><b>Id</b>: <%=resultSet.getString(1)%></td>

                    <%

                        if (status.equals("Up"))
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
                                       String IP = resultSet.getString(3);

                                       String status = resultSet.getString(8);

                        %>

                        title:{
                            text: "<%=IP %>"
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
                                       int id = resultSet.getInt(1);

                                       String IP = resultSet.getString(3);

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

                                    { label: "<%=time1 %>", y: <%=getFreeMemoryPercent(responseData) %> },

                                    <%
                                        currentTime.add(Calendar.MINUTE, -5);

                                        Date secondTime = currentTime.getTime();

                                        String time2 = dateFormat.format(secondTime);
                                    %>

                                    { label: "<%=time2 %>", y: <%=getUpdatedMemory(id, IP, time2) %> },

                                    <%

                                        currentTime.add(Calendar.MINUTE, -5);

                                        Date thirdTime = currentTime.getTime();

                                        String time3 = dateFormat.format(thirdTime);
                                    %>

                                    { label: "<%=time3 %>", y: <%=getUpdatedMemory(id, IP, time3) %> },

                                    <%
                                        currentTime.add(Calendar.MINUTE, -5);

                                        Date fourthTime = currentTime.getTime();

                                        String time4 = dateFormat.format(fourthTime);
                                    %>

                                    { label: "<%=time4 %>", y: <%=getUpdatedMemory(id, IP, time4) %> },

                                    <%
                                        currentTime.add(Calendar.MINUTE, -5);

                                        Date fifthTime = currentTime.getTime();

                                        String time5 = dateFormat.format(fifthTime);
                                    %>

                                    { label: "<%=time5 %>", y: <%=getUpdatedMemory(id, IP, time5) %> },

                                    <%
                                        currentTime.add(Calendar.MINUTE, -5);

                                        Date sixthTime = currentTime.getTime();

                                        String time6 = dateFormat.format(sixthTime);
                                    %>

                                    { label: "<%=time6 %>", y: <%=getUpdatedMemory(id, IP, time6) %> },

                                    <%
                                        currentTime.add(Calendar.MINUTE, -5);

                                        Date sevenTime = currentTime.getTime();

                                        String time7 = dateFormat.format(sevenTime);
                                    %>

                                    { label: "<%=time7 %>", y: <%=getUpdatedMemory(id, IP, time7) %> },

                                    <%
                                        currentTime.add(Calendar.MINUTE, -5);

                                        Date eightTime = currentTime.getTime();

                                        String time8 = dateFormat.format(eightTime);
                                    %>

                                    { label: "<%=time8 %>", y: <%=getUpdatedMemory(id, IP, time8) %> },

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

                        <div id="dougnutChart" style="height: 270px; width: 100%;"></div>

                    </td>

                <%
                    try
                    {
                        ResultSet resultSet = UserDAO.getDashboardData();

                        while (resultSet.next())
                        {
                            String status = resultSet.getString(8);

                            String responseData = resultSet.getString(7);

                            if (status.equals("Up"))
                            {

                %>

                    <%-- first widgets --%>
                    <td class="linux__Widget">

                        <div class="">

                            <table width="" style="margin-left: 12px">

                                <tbody>

                                <tr>

                                    <td><b>Monitor</b></td>

                                    <td><%=resultSet.getString(3) %></td>

                                </tr>

                                <tr>

                                    <td><b>Type</b></td>

                                    <td><%=getDeviceType(responseData) %></td>

                                </tr>

                                <tr>

                                    <td><b>System Name</b></td>

                                    <td><%=getSystemName(responseData) %></td>

                                </tr>

                                <tr>

                                    <td><b>CPU Type</b></td>

                                    <td><%=getCPUType(responseData) %></td>

                                </tr>

                                <tr>

                                    <td><b>OS Version</b></td>

                                    <td><%=getOSVersion(responseData) %></td>

                                </tr>

                                <tr>

                                    <td><b>OS Name</b></td>

                                    <td><%=getOSName(responseData) %></td>

                                </tr>


                                </tbody>

                            </table>

                        </div>

                    </td>

                    <%-- second widgets --%>
                    <td class="linux__Widget">

                        <div class="dash__widget">

                            <div>

                                <p>Memory Used (%)</p>

                            </div>

                            <div>

                                <b><p><%=getUsedMemoryPercent(responseData) + " %" %></p></b>

                            </div>

                        </div>

                    </td>

                    <%-- third widgets --%>
                    <td class="linux__Widget">

                        <div class="dash__widget">

                            <div>

                                <p>Memory Free (%)</p>

                            </div>

                            <div>

                                <b><p><%=getFreeMemoryPercent(responseData) + " %" %></p></b>

                            </div>

                        </div>

                    </td>

                    <%-- fourth widgets --%>
                    <td class="linux__Widget">

                        <div class="dash__widget">

                            <div>

                                <p>RTT (ms)</p>

                            </div>

                            <div>

                                <b><p>0 ms</p></b>

                            </div>

                        </div>

                    </td>

                </tr>

                <tr style="height: 300px">

                    <%-- dougnut chart --%>
                    <td class="linux__initial">

                        <div class="dash__widget">

                            <div>

                                <p>CPU User (%)</p>

                            </div>

                            <div>

                                <b><p><%=getUserCPUPercent(responseData) + " %" %></p></b>

                            </div>

                        </div>

                    </td>

                    <%-- first widgets --%>
                    <td class="linux__Widget">

                        <div class="dash__widget">

                            <div>

                                <p>CPU System (%)</p>

                            </div>

                            <div>

                                <b><p><%=getSystemCPUPercent(responseData) + " %" %></p></b>

                            </div>

                        </div>

                    </td>

                    <%-- second widgets --%>
                    <td class="linux__Widget">

                        <div class="dash__widget">

                            <div>

                                <p>Swap Used Memory (%)</p>

                            </div>

                            <div>

                                <b><p><%=getUsedSwapPercent(responseData) + " %" %></p></b>

                            </div>

                        </div>

                    </td>

                    <%-- third widgets --%>
                    <td class="linux__Widget">

                        <div class="dash__widget">

                            <div>

                                <p>Swap Free Memory (%)</p>

                            </div>

                            <div>

                                <b><p><%=getFreeSwapPercent(responseData) + " %" %></p></b>

                            </div>

                        </div>

                    </td>

                    <%-- fourth widgets --%>
                    <td class="linux__Widget">

                        <div class="dash__widget">

                            <div>

                                <p>Disk (%)</p>

                            </div>

                            <div>

                                <b><p><%=getDiskPercent(responseData) + " %" %></p></b>

                            </div>

                        </div>

                    </td>


                <%
                            }
                            if (status.equals("Down"))
                            {

                %>

                    <%-- first widgets --%>
                    <td class="linux__Widget">

                        <div class="" style="margin:20px 0 175px 10px">

                            <p>Device Details</p>

                        </div>

                    </td>

                    <%-- second widgets --%>
                    <td class="linux__Widget">

                        <div class="dash__widget">

                            <div>

                                <p>Memory Used (%)</p>

                            </div>

                            <div>

                                <b><p></p></b>

                            </div>

                        </div>

                    </td>

                    <%-- third widgets --%>
                    <td class="linux__Widget">

                        <div class="dash__widget">

                            <div>

                                <p>Memory Free (%)</p>

                            </div>

                            <div>

                                <b><p></p></b>

                            </div>

                        </div>

                    </td>

                    <%-- fourth widgets --%>
                    <td class="linux__Widget">

                        <div class="dash__widget">

                            <div>

                                <p>RTT (ms)</p>

                            </div>

                            <div>

                                <b><p></p></b>

                            </div>

                        </div>

                    </td>

                </tr>

                <tr style="height: 300px">

                    <%-- dougnut chart --%>
                    <td class="linux__initial">

                        <div class="dash__widget">

                            <div>

                                <p>CPU User (%)</p>

                            </div>

                            <div>

                                <b><p></p></b>

                            </div>

                        </div>

                    </td>

                    <%-- first widgets --%>
                    <td class="linux__Widget">

                        <div class="dash__widget">

                            <div>

                                <p>CPU System (%)</p>

                            </div>

                            <div>

                                <b><p></p></b>

                            </div>

                        </div>

                    </td>

                    <%-- second widgets --%>
                    <td class="linux__Widget">

                        <div class="dash__widget">

                            <div>

                                <p>Swap Used Memory (%)</p>

                            </div>

                            <div>

                                <b><p></p></b>

                            </div>

                        </div>

                    </td>

                    <%-- third widgets --%>
                    <td class="linux__Widget">

                        <div class="dash__widget">

                            <div>

                                <p>Swap Free Memory (%)</p>

                            </div>

                            <div>

                                <b><p></p></b>

                            </div>

                        </div>

                    </td>

                    <%-- fourth widgets --%>
                    <td class="linux__Widget">

                        <div class="dash__widget">

                            <div>

                                <p>Disk (%)</p>

                            </div>

                            <div>

                                <b><p></p></b>

                            </div>

                        </div>

                    </td>

                </tr>


                <%
                            }

                        }
                    }
                    catch (Exception exception)
                    {
                        exception.printStackTrace();
                    }
                %>

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