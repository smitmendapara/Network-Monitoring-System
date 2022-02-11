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

        <div>

            <div id="dashboardTitle" class="demo dash__title">

            </div>

        </div>

        <br><br>

        <div class="dashboard__details">

            <table width="100%">

                <tbody id="linuxDashboardHeader">


                </tbody>

            </table>

        </div>

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