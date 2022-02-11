<%--
  Created by IntelliJ IDEA.
  User: smit
  Date: 3/1/22
  Time: 7:01 PM
  To change this template use File | Settings | File Templates.
--%>

    <div id="discoveryPage">

        <jsp:include page="index.jsp"></jsp:include>

        <hr/>

            <%@ taglib uri="/struts-tags" prefix="s" %>

            <%-- css file --%>

            <link rel="stylesheet" href="css/style.css">

            <%-- icon library --%>

            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css">

            <%-- chart library --%>

            <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

            <%-- jQuery library --%>

            <script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>

            <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>

            <%-- js file --%>

            <script src="js/index.js"></script>

            <%
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

                if (session.getAttribute("username") == null)
                {
                    response.sendRedirect("login.jsp");
                }

            %>

            <h3 class="middle" style="margin:15px 0 0 1330px;">Create IP</h3>

            <%-- static navigation discovery page content --%>

            <div class="demo" style="margin: 10px 0 0 -15px">

                <nav>

                    <a href="discover" class="lg__cover"><i class="bi bi-disc"></i>&nbsp;Discovery</a>

                    <a href="monitor" class="lg__monitor"><i class="bi bi-tv"></i>&nbsp;Monitor</a>

                    <a href="" class="lg__dashboard"><i class="bi bi-grid"></i></i>&nbsp;Dashboard</a>

                    <button type="button" class="disc__new middle" id="new" onclick="openForm('discoveryForm')" style="margin-left: 965px;cursor: pointer"><i class="bi bi-cloud-plus"></i>&nbsp;new</button>

                    <button type="button" class="disc__new middle" onclick="toggleTable()"><i class="bi bi-app-indicator"></i>&nbsp;discoverd_IP</button>

                </nav>

            </div>

            <hr/>

            <%-- discovered IP table --%>

            <div class="demo" id="myTableDiv">

                <table class="disc__table" style="width: 100%"  id="myTable">

                    <thead>

                    <tr class="disc__heading">

                        <th scope="col">Name</th>

                        <th scope="col">IP/Host</th>

                        <th scope="col">Options</th>

                    </tr>

                    </thead>

                    <hr/>

                    <tbody id="mainTable">

                    </tbody>

                </table>

            </div>

            <%-- discovery form --%>

            <div class="form-popUp" id="discoveryForm" style="display: none">

                <form action="discoveryProcess" method="POST">

                    <div class="demo">

                        <h3>Discover IP</h3> <br>

                    </div>

                    <div id="type" class="disc__title">

                        <p class="name">Name</p>

                        <p class="IP">IP/Host</p><br>

                    </div>

                    <div class="disc__entry">

                        <input type="text" name="name" placeholder="Enter Name" id="name" class="disc__name" required>

                        <input type="text" name="IP" placeholder="Enter IP/Host" id="IP" class="disc__address" required> <br>

                    </div>

                    <div class="demo">

                        <p class="name">Device Type</p>

                        <select name="device" id="device" class="list" style="width: 10%;height: 25px" data-drop-down="true" data-required="true" onchange="displayLinuxProfile('profile', this)" required>

                            <option value="0" class="ping">Ping</option>

                            <option value="1" class="linux">Linux</option>

                        </select>

                        <br><br>

                    </div>

                    <div class="event" id="profile">

                        <div class="demo">

                            Username : <input type="text" name="username" id="username" class="linux_user" placeholder="Linux Username" required> &nbsp;&nbsp;

                            Password : <input type="password" id="password" class="linux_pass" placeholder="Linux Password" required> <br><br>

                        </div>

                    </div>

                    <div class="demo">

                        <button type="button" class="btn change" onclick="discoverData()" style="cursor: pointer">Create</button>

                        <button type="button" class="btn cancel" onclick="closeForm('discoveryForm')" style="cursor: pointer">Cancel</button>

                    </div>

                </form>

            </div>

            <%-- view result form --%>

            <div class="disc__popUp" id="provisionForm">

                <form action="" class="disc__container" method="POST">

                    <div class="demo popUp__header">

                        <h3 class="popUp__title">Discovery Result</h3>

                        <button type="button" class="popUp__icon" onclick="closeForm('provisionForm')" style="cursor: pointer"><i class="bi bi-x-circle"></i></button>

                    </div>

                    <hr/>

                    <div>

                        <header>

                            <h4 style="margin-left: -74%">Discovered Objects</h4>

                        </header>

                        <div class="demo popUp__body">

                            <i class="bi bi-search"></i><input type="text" placeholder="search here" style="height:25px;border-radius:10px;margin-left:6px;">

                            <select name="" id="" class="popUp__list" style="width: 10%;height: 30px; margin-left: 400px">

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

                    <div class="popUp__footer" style="margin: 1px 3px 0 530px;">

                        <button type="button" class="btn footer__provision" onclick="monitorData('check')" style="cursor: pointer">Provision Object</button>

                        <button type="button" class="btn footer__cancel" onclick="closeForm('provisionForm')" style="cursor: pointer">Cancel</button>

                    </div>

                </form>

            </div>

    </div>

    <script>

        window.onload = function () {

            getDiscoveryDetails();
        }

    </script>
