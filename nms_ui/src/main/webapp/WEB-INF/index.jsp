<%--
  Created by IntelliJ IDEA.
  User: smit
  Date: 3/1/22
  Time: 7:01 PM
  To change this template use File | Settings | File Templates.
--%>

    <%
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

        if (session.getAttribute("username") == null)
        {
            response.sendRedirect("login.jsp");
        }

    %>

    <div id="discoveryPage">

        <html>

        <head>

            <title>Home Page</title>

            <%-- external css file  --%>

            <link rel="stylesheet" href="css/style.css">

            <%-- bootstrap icon --%>

            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css">

        </head>

        <body>

            <%@ taglib uri="/struts-tags" prefix="s" %>

            <%-- css file --%>
            <link rel="stylesheet" href="css/style.css">

            <%-- icon library --%>
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css">

            <%-- chart library --%>
            <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

            <%-- chart library --%>
            <script type="text/javascript" src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>

            <%-- jQuery library --%>
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
            <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
            <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>

            <%-- js file --%>
            <script src="js/index.js"></script>
            <script src="./js/discovery.js"></script>
            <script src="./js/monitor.js"></script>
            <script src="./js/dashboard.js"></script>

            <%-- project title --%>
            <div class="demo" style="margin: 10px 0 0 -15px">

                <nav>

                    <span style="font-size: 30px; margin-right: 1255px">Light - NMS</span>

                </nav>

            </div>

            <%-- static navigation discovery page content --%>
            <div class="demo" style="margin: 10px 0 0 0">

                <nav>

                    <input type="button" id="discover" value="Discovery" class="lg__cover">

                    <input type="button" id="monitor" value="Monitor" class="lg__monitor">

                    <input type="button" id="dashboard" value="Dashboard" class="lg__dashboard">

                    <button type="button" class="disc__new middle" id="new" onclick="openForm('discoveryForm')" style="margin-left: 885px;cursor: pointer"><i class="bi bi-cloud-plus"></i>&nbsp;add IP</button>

                    <button type="button" class="disc__new middle" onclick="toggleTable()"><i class="bi bi-app-indicator"></i>&nbsp;discoverd_IP</button>

                    <a href="logoutProcess" class="index__logout"><i class="bi bi-box-arrow-left"></i>&nbsp;logout</a>
                </nav>

            </div>

            <hr/>

            <%-- body content --%>

            <div id="tableData">

            </div>

            <div id="dashboardHeader"></div>

            <div id="dashboardBody"></div>

            <%-- discovery form --%>
            <div class="form-popUp" id="discoveryForm" style="display: none">

                <form action="discoveryProcess" method="POST">

                    <div class="demo">

                        <h4 style="margin-top:15px;background-color:grey;border-radius:5px;padding:3px">Discover IP</h4> <br>

                    </div>

                    <div id="type" class="disc__title" style="margin-top:5px">

                        <p class="name">Name</p>

                        <p class="IP">IP/Host</p><br>

                    </div>

                    <div class="disc__entry">

                        <input type="text" name="name" placeholder="Enter Name" id="name" class="disc__name" required size="17">

                        <input type="text" name="IP" placeholder="Enter IP/Host" id="IP" class="disc__address" required size="17"> <br>

                    </div>

                    <div class="demo" style="margin-top:10px">

                        <p class="name" style="margin-top:12px">Device Type</p>

                        <select name="device" id="device" class="list" style="width: 10%;height: 25px" data-drop-down="true" data-required="true" onchange="displayLinuxProfile('profile', this)" required>

                            <option value="0" class="ping">Ping</option>

                            <option value="1" class="linux">Linux</option>

                        </select>

                        <br><br>

                    </div>

                    <div class="event" id="profile">

                        <div class="demo">

                            Username : <input type="text" name="username" id="username" class="linux_user" placeholder="Linux Username" required size="17"> &nbsp;&nbsp;

                            Password : <input type="password" id="password" class="linux_pass" placeholder="Linux Password" required size="17"> <br><br>

                        </div>

                    </div>

                    <div class="demo">

                        <button type="button" class="btn change" onclick="discoverData()" style="cursor: pointer; background-color: #32adcf">Create</button>

                        <button type="button" class="btn cancel" onclick="closeForm('discoveryForm')" style="cursor: pointer; background-color: orange">Cancel</button>

                    </div>

                </form>

            </div>



            <%-- view result form --%>
            <div class="disc__popUp" id="provisionForm">

                <form action="" class="disc__container" method="POST">

                    <div class="demo popUp__header">

                        <h4 class="popUp__title" style="margin-top:20px;background-color:grey;border-radius:4px;padding:2px">Discovery Result</h4>

                        <button type="button" class="popUp__icon" onclick="closeForm('provisionForm')" style="cursor: pointer"><i class="bi bi-x-circle"></i></button>

                    </div>

                    <hr/>

                    <div>

                        <header>

                            <h4 style="margin-left: -66%">Discovered Objects</h4>

                        </header>

                        <div class="demo popUp__body">

                            <i class="bi bi-search" style="margin-left:-4px"></i><input type="text" placeholder="search here" style="height:25px;border-radius:10px;margin-left:6px;">

                            <select name="" id="" class="popUp__list" style="width: 10%;height: 30px; margin-left: 373px">

                                <option value="10">10</option>

                                <option value="20">20</option>

                                <option value="50">50</option>

                                <option value="75">75</option>

                                <option value="100">100</option>

                            </select>

                        </div>


                        <div>

                            <table class="popUp__table" style=" margin-left: 28px; width: 92%" id="newData">

                                <thead>

                                <tr>

                                    <th><input type="hidden">ID</th>

                                    <th scope="col"><input type="checkbox"></th>

                                    <th scope="col">Host</th>

                                    <th scope="col">Discovery Type</th>

                                    <th scope="col">Profile</th>

                                    <th scope="col">Device</th>

                                </tr>

                                </thead>

                                <hr/>

                                <tbody id="resultTable">


                                </tbody>

                            </table>

                        </div>

                    </div>

                    <hr/>

                    <div class="popUp__footer" style="margin: 1px 3px 0 460px;">

                        <button type="button" class="btn footer__provision" id="provision" onclick="monitorData('check')" style="cursor: pointer;background-color: #32adcf">Provision Object</button>

                        <button type="button" class="btn footer__cancel" onclick="closeForm('provisionForm')" style="cursor: pointer; background-color:orange">Cancel</button>

                    </div>

                </form>

            </div>

            <%-- edit feilds modal --%>
            <div id="myModal" class="modal fade" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Edit IP Address</h5>
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                        </div>
                        <div class="modal-body">
                            <form action="">

                                <input type="text" id="editID" hidden>
                                Name : <input type="text" id="editName"><br><br>
                                IP Address : <input type="text" id="editIP"><br><br>
                                <div id="editProfile">
                                    Username : <input type="text" id="editUsername"><br><br>
                                    Password : <input type="password" id="editPassword"><br><br>
                                </div>
                                Device Type : <input type="text" id="editDeviceType" disabled>

                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                            <button type="button" class="btn btn-primary" onclick="updateDiscoveryData()">Save</button>
                        </div>
                    </div>
                </div>
            </div>

            <script>

                $('#discover').click(function () {
                    getDiscoveryDetails();
                });

                $('#monitor').click(function () {
                    getMonitorDetails();
                });

            </script>

        </body>

        </html>

    </div>