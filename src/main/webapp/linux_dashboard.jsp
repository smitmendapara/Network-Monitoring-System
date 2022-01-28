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

                %>
                <tr>

                    <td><b>IP/Host</b>: <%=IP %></td>

                    <td><b>Profile</b>: <%=resultSet.getString(4)%></td>

                    <td><b>Poll Time</b>: <%=resultSet.getString(9)%></td>

                </tr>

                <tr>

                    <td><b>Id</b>: <%=resultSet.getString(1)%></td>

                    <td><b>Status</b>: <%=resultSet.getString(8)%></td>

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

//                CanvasJS.addColorSet("colorSet",
//                        [
//                           "#A21919"
//                        ]);

                var firstChart = new CanvasJS.Chart("dougnutChart",
                    {
                        width : 350,

//                        colorSet: "colorSet",

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

                                color: "#A21919",

                                dataPoints: [
                                    {  y: 100.0, indexLabel: "<%=IP %>" },
                                ]

                                <%

                                    }
                                    else
                                    {

                                %>

                                color: "#008000",

                                dataPoints: [
                                    {  y: 100.0, indexLabel: "<%=IP %>" },
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
                                String Status = resultSet.getString(8);

                                if (Status.equals("Up"))
                                {
                                    String responseRow = UserDAO.getLinuxDashboardData();

                                    String[] responseData = responseRow.split(",");

                                    StringBuffer stringBuffer1 = new StringBuffer(responseData[5].trim());

                                    StringBuffer stringBuffer2 = new StringBuffer(responseData[0].trim());

                                    stringBuffer1 = stringBuffer1.deleteCharAt(stringBuffer1.length() - 1);

                                    stringBuffer2 = stringBuffer2.deleteCharAt(0);

                                    double totalMemory = Double.parseDouble(responseData[3].trim());

                                    double usedMemory = Double.parseDouble(responseData[4].trim());

                                    double freeMemory = Double.parseDouble(String.valueOf(stringBuffer1));

                                    double used = (usedMemory / totalMemory) * 100;

                                    double free = (freeMemory / totalMemory) * 100;

                                    used = Double.parseDouble(new DecimalFormat("##.##").format(used));

                                    free = Double.parseDouble(new DecimalFormat("##.##").format(free));

                    %>

                    <%-- first widgets --%>
                    <td class="linux__Widget">

                        <div class="">

                            <table width="" style="margin-left: 35px">

                                <tbody>

                                <tr>

                                    <td><b>Monitor</b></td>

                                    <td><%=resultSet.getString(3)%></td>

                                </tr>

                                <tr>

                                    <td><b>Type</b></td>

                                    <td><%=stringBuffer2 %></td>

                                </tr>

                                <tr>

                                    <td><b>System</b></td>

                                    <td><%=responseData[1].trim() %></td>

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

                                <p><%=used + "%" %></p>

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

                                <p><%=free + "%" %></p>

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

                                <p>Oms</p>

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

                <tr style="height: 300px">

                    <%-- dougnut chart --%>
                    <td class="linux__initial">

                        <div></div>

                    </td>

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

                                    StringBuffer stringBuffer1 = new StringBuffer(responseData[5].trim());

                                    StringBuffer stringBuffer2 = new StringBuffer(responseData[0].trim());

                                    stringBuffer1 = stringBuffer1.deleteCharAt(stringBuffer1.length() - 1);

                                    stringBuffer2 = stringBuffer2.deleteCharAt(0);

                                    double totalMemory = Double.parseDouble(responseData[3].trim());

                                    double usedMemory = Double.parseDouble(responseData[4].trim());

                                    double freeMemory = Double.parseDouble(String.valueOf(stringBuffer1));

                                    double used = (usedMemory / totalMemory) * 100;

                                    double free = (freeMemory / totalMemory) * 100;

                                    used = Double.parseDouble(new DecimalFormat("##.##").format(used));

                                    free = Double.parseDouble(new DecimalFormat("##.##").format(free));

                    %>

                    <%-- first widgets --%>
                    <td class="linux__Widget">

                        <div class="">

                            <table width="" style="margin-left: 35px">

                                <tbody>

                                <tr>

                                    <td><b>Monitor</b></td>

                                    <td><%=resultSet.getString(3)%></td>

                                </tr>

                                <tr>

                                    <td><b>Type</b></td>

                                    <td><%=stringBuffer2 %></td>

                                </tr>

                                <tr>

                                    <td><b>System</b></td>

                                    <td><%=responseData[1].trim() %></td>

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

                                <p><%=used + "%" %></p>

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

                                <p><%=free + "%" %></p>

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

                                <p>Oms</p>

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

    </body>