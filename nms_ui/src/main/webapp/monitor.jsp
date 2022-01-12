<%--
  Created by IntelliJ IDEA.
  User: smit
  Date: 10/1/22
  Time: 5:53 PM
  To change this template use File | Settings | File Templates.
--%>
<jsp:include page="index.jsp"></jsp:include>

<hr/>

<%@ taglib uri="/struts-tags" prefix="s" %>

    <link rel="stylesheet" href="css/style.css">

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

    <script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>

    <script src="js/index.js"></script>

    <br/>

        <div class="demo" style="float:left;margin-top: 10px">

            <nav>

                <a href="discover" class="lg__cover"><i class="bi bi-disc"></i>&nbsp;Discovery</a>

                <a href="monitor" class="lg__monitor"><i class="bi bi-tv"></i>&nbsp;Monitor</a>

                <i class="bi bi-search" style="margin-left:1040px;"></i><input type="text" class="monitor__search" placeholder="search here" style="height:25px;border-radius:10px;margin-left:6px;">

            </nav>

        </div>

        <br><br>

        <div class="demo" id="" style="margin-top: 10px;">

            <table class="disc__table" style="width: 100%">

                <thead>

                <tr class="disc__heading">

                    <th scope="col"><input type="checkbox"></th>

                    <th scope="col">ID</th>

                    <th scope="col">Name</th>

                    <th scope="col">IP/Host</th>

                    <th scope="col">Type</th>

                </tr>

                </thead>

                <hr/>

                <tbody>

                    <tr>

                        <td><input type="checkbox"></td>

                        <td>1</td>

                        <td>Test</td>

                        <td>127.0.0.1</td>

                        <td>ping</td>

                    </tr>

                    <tr>

                        <td><input type="checkbox"></td>

                        <td>5</td>

                        <td>Test</td>

                        <td>127.0.0.5</td>

                        <td>ping</td>

                    </tr>

                </tbody>

            </table>

        </div>

