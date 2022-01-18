<%--
  Created by IntelliJ IDEA.
  User: smit
  Date: 15/1/22
  Time: 1:56 PM
  To change this template use File | Settings | File Templates.
--%>

<jsp:include page="index.jsp"></jsp:include>

<hr/>

<%@ taglib uri="/struts-tags" prefix="s" %>

    <%-- css file --%>

    <link rel="stylesheet" href="css/style.css">

    <%-- icon library --%>

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css">

    <%-- chart library --%>

    <script src="https://cdn.anychart.com/js/8.0.1/anychart-pie.min.js"></script>

    <script src="https://cdn.anychart.com/releases/8.11.0/js/anychart-base.min.js"></script>

    <%-- jQuery library --%>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

    <script type="text/javascript" src="js/jquery-3.1.1.min.js"></script>

    <%-- js file --%>

    <script src="js/index.js"></script>

    <br/>

    <div class="demo" style="float:left;margin-top: 10px">

        <nav>

            <a href="discover" class="lg__cover"><i class="bi bi-disc"></i>&nbsp;Discovery</a>

            <a href="monitor" class="lg__monitor"><i class="bi bi-tv"></i>&nbsp;Monitor</a>

            <a href="dashboard" class="lg__dashboard"><i class="bi bi-grid"></i>&nbsp;Dashboard</a>

        </nav>

    </div>

    <br>

    <div id="pieChart" style="margin-top: 30px"></div>

    <div id="timeChart"></div>

<br><br>
