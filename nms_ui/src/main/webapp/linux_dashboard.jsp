<%@ page import="java.sql.ResultSet" %>

<%@ page import="action.dao.UserDAO" %>
<%@ page import="org.h2.util.StringUtils" %>
<%@ page import="java.text.DecimalFormat" %>

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

                            String status = resultSet.getString(8);

                %>

                <tr>

                    <td><b>IP/Host</b>: <%=IP %>&nbsp;&nbsp;<i class="bi bi-activity" style="color: #2a92ff"></i></td>

                    <td><b>Profile</b>: <%=resultSet.getString(4)%></td>

                    <td><b>Poll Time</b>: <%=resultSet.getString(9)%>&nbsp;&nbsp;<i class="bi bi-arrow-repeat" style="cursor:pointer;"></i></td>

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

                                       String ipStatus = resultSet.getString(8);

                        %>

                        title:{
                            text: "<%=IP %>"
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
            }

        </script>

        <div>

            <table width="100%">

                <tbody>

                <%
                    try
                    {
                        ResultSet resultSet = UserDAO.getDashboardData();

                        while (resultSet.next())
                        {
                            String Status = resultSet.getString(8);

                            if (Status.equals("Up"))
                            {
                                String responseRow = UserDAO.getLinuxDashboardData();

                                String[] responseData = responseRow.split(",");

                                StringBuffer CPU_System = new StringBuffer(responseData[13].trim());

                                StringBuffer Device_Type = new StringBuffer(responseData[0].trim());

                                CPU_System = CPU_System.deleteCharAt(CPU_System.length() - 1);

                                Device_Type = Device_Type.deleteCharAt(0);

                                double totalMemory = Double.parseDouble(responseData[3].trim());

                                double usedMemory = Double.parseDouble(responseData[4].trim());

                                double freeMemory = Double.parseDouble(responseData[5].trim());

                                double totalSwap = Double.parseDouble(responseData[8].trim());

                                double usedSwap = Double.parseDouble(responseData[9].trim());

                                double freeSwap = Double.parseDouble(responseData[10].trim());

                                double used = (usedMemory / totalMemory) * 100;

                                double free = (freeMemory / totalMemory) * 100;

                                double swapUsed = (usedSwap / totalSwap) * 100;

                                double swapFree = (freeSwap / totalSwap) * 100;

                                used = Double.parseDouble(new DecimalFormat("##.##").format(used));

                                free = Double.parseDouble(new DecimalFormat("##.##").format(free));

                                swapUsed = Double.parseDouble(new DecimalFormat("##.##").format(swapUsed));
                                
                                swapFree = Double.parseDouble(new DecimalFormat("##.##").format(swapFree));
                %>

                <tr style="height: 300px">

                    <%-- dougnut chart --%>
                    <td class="dash__firstRow">

                        <div id="dougnutChart" style="height: 270px; width: 100%;"></div>

                    </td>

                    <%-- first widgets --%>
                    <td class="linux__Widget">

                        <div class="">

                            <table width="" style="margin-left: 12px">

                                <tbody>

                                <tr>

                                    <td><b>Monitor</b></td>

                                    <td><%=resultSet.getString(3)%></td>

                                </tr>

                                <tr>

                                    <td><b>Type</b></td>

                                    <td><%=Device_Type %></td>

                                </tr>

                                <tr>

                                    <td><b>System</b></td>

                                    <td><%=responseData[1].trim() %></td>

                                </tr>

                                <tr>

                                    <td><b>CPU Type</b></td>

                                    <td><%=responseData[2].trim() %></td>

                                </tr>

                                <tr>

                                    <td><b>OS Version</b></td>

                                    <td><%=responseData[6].trim() %></td>

                                </tr>

                                <tr>

                                    <td><b>System</b></td>

                                    <td><%=responseData[7].trim() %></td>

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

                                <b><p><%=used + " %" %></p></b>

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

                                <b><p><%=free + " %" %></p></b>

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

                                <b><p><%=responseData[12].trim() + " %" %></p></b>

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

                                <b><p><%=CPU_System + " %" %></p></b>

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

                                <b><p><%=swapUsed + " %" %></p></b>

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

                                <b><p><%=swapFree + " %" %></p></b>

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

                                <b><p><%=responseData[11].trim() + " %" %></p></b>

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

    </body>