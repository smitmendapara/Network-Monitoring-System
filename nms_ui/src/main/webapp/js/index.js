// refresh page

function refreshPage() {

    location.reload(true);
}

// open discovery form

function openForm(idName) {

    document.getElementById(idName).style.display = 'block';

}

// close discovery form

function closeForm(idName) {

    document.getElementById(idName).style.display = 'none';
}

// post request

function getPostCall(request) {

    $.ajax({

        type: "POST",

        cache: false,

        async: true,

        timeout: 180000,

        data: request.data,

        url: request.url,

        success: function (data) {

            var myCallback;

            if (request.callback != undefined)
            {
                myCallback = $.Callbacks();

                myCallback.add(request.callback);

                myCallback.fire(request);

                myCallback.remove(request.callback);
            }

        },
        error: function () {

            alert("some error occurred!");
        }

    });
}

// get request

function getGetCall(request) {

    $.ajax({

        type : "GET",

        cache : false,

        async : true,

        timeout : 180000,

        data: request.data,

        url : request.url,

        success : function (data) {

            var myCallback;

            if (request.callback != undefined)
            {
               myCallback = $.Callbacks();

               myCallback.add(request.callback);

               request.data = data;

               myCallback.fire(request);

               myCallback.remove(request.callback);
            }
        },
        error : function () {

            alert("some error occurred!");
        }
    });
}

// fetch discovery data from database

function discoveryTable(result) {

    var tableData = "";

    var data = result.data;

    $.each(data.beanList, function () {

        var id = this.id;

        var IP = this.IP;

        var device = this.device;

        tableData += "<tr class='disc__data' id='"+this.id+"'>" +

            "<td>" + this.name + "</td>" +

            "<td>" + this.IP + "</td>" +

            "<td>" +

            "<i class='bi bi-play disc__discovery' title='Discovery' data-value='"+this.IP+", "+this.device+"'></i> &nbsp;" +

            "<i class='bi bi-eye disc__monitor' title='View Result' data-value='"+this.id+"'></i> &nbsp;" +

            "<i class='bi bi-pencil-square disc__edit' title='Edit'></i> &nbsp;" +

            "<i class='bi bi-trash disc__delete' title='Delete' data-value='"+this.id+"'></i> &nbsp;" +

            "</td>" +

            "</tr>"
    });

    $('#mainTable').html(tableData);

    $('.bi-play').on('click', function () {

        var parameter = event.currentTarget.getAttribute('data-value');

        var array = parameter.split(",");

        reDiscoverData(array[0], array[1].trim());

        alert("your ip is successfully rediscover!");

    });

    $('.bi-eye').on('click', function () {

        var parameter = event.currentTarget.getAttribute('data-value');

        showForm(parameter);

    });

    $('.bi-trash').on('click', function () {

        var parameter = event.currentTarget.getAttribute('data-value');

        deleteRow(parameter);

    });
}

function getDiscoveryDetails() {

    var provisionForm = "provisionForm";

    getGetCall({ url: "discoveryTable.action", callback: discoveryTable });
}

// manipulated discovery table data

function displayLinuxProfile(idName, elementValue) {

    document.getElementById(idName).style.display = elementValue.value == 1 ? 'block' : 'none';
}

// show discovery form

function discoverData() {

    var name = $("#name").val();

    var ip = $("#IP").val();

    var discoveryUsername = $("#username").val();

    var discoveryPassword = $("#password").val();

    var deviceType = $("#device").val();

    getPostCall({ url: "discoveryProcess.action", data: { name: name, ip: ip, discoveryUsername: discoveryUsername, discoveryPassword: discoveryPassword, deviceType: deviceType } });

    refreshPage();
}

// for rediscovery

function reDiscoverData(ip, deviceType) {

    getPostCall({ url: "reDiscoveryProcess.action", data: { ip: ip, deviceType: deviceType } });

    refreshPage();
}

// manually polling

function reloadPage(result) {

    if (result.data.deviceType == "Ping")
    {
        window.location.href = "dashboard.jsp";
    }
    else
    {
        window.location.href = "linux_dashboard.jsp";
    }
}

function getPolling(id, ip, deviceType) {

    getPostCall({ url: "monitorPolling.action", data: {id: id, ip: ip, deviceType: deviceType }, callback: reloadPage });
}

// toggle discovery table

function toggleTable() {

    var table = document.getElementById("myTableDiv");

    if (table.style.display === "none")
    {
        table.style.display = "block";
    }
    else
    {
        table.style.display = "none";
    }
}

// delete discovery table row

function deletedRow(result) {

    document.getElementById(result.data.id).remove();

    alert("successfully row deleted!");
}

function deleteRow(id) {

    getPostCall({ url: "discoveryDelete.action", data: { id: id }, callback: deletedRow });
}

// provision ip

function provisionIP(request) {

    alert("successfully monitored!");
}

function monitorData(id) {

    if (document.getElementById(id).checked)
    {
        var id = $("input[name=key]").val();

        getPostCall({ url: "monitorData.action", data: { id: id }, callback: provisionIP });
    }
    else
    {
        alert("must be checked checkbox!");
    }
}

// show monitor form

function monitorForm(request) {

    var tableData = "";

    var data = request.data;

    $.each(data.beanList, function () {

        tableData = "<tr class='disc__data'>" +

            "<td><input type='hidden' class='one' name='key' id='key' value='"+this.id+"'>" + this.id + "</td>" +

            "<td><input type='checkbox' id='check' checked></td>" +

            "<td>" + this.IP + "</td>" +

            "<td>" + "0" + "</td>" +

            "<td>" + this.username + "</td>" +

            "<td>" + this.device + "</td>" +

            + "</tr>";

    });

    $('#resultTable').html(tableData);
}

function showForm(id) {

    var monitorFormId = $('div.disc__popUp').attr('id');

    document.getElementById(monitorFormId).style.display = 'block';

    getGetCall({ url: "monitorForm.action", data: { id: id }, callback: monitorForm });
}

// monitor table data

function getMonitorTable(result) {

    var tableData = "";

    var data = result.data;

    $.each(data.beanList, function () {

        tableData += "<tr>" +

            "<td><input type='checkbox'></td>" +

            "<td>" + this.id + "</td>" +

            "<td>" + this.IP + "&nbsp;&nbsp;&nbsp;&nbsp;<i class='bi bi-box-arrow-up-right monitor__icon' data-value='"+this.id+", "+this.IP+", "+this.device+"'></i>" + "</td>" +

            "<td>" + this.device + "</td>" +

            "</tr>";

    });

    $('#monitorTable').html(tableData);

    $('.bi-box-arrow-up-right').on('click', function() {

        var parameter = event.currentTarget.getAttribute('data-value');

        var array = parameter.split(",");

        showDashboard(array[0], array[1].trim(), array[2].trim());

        alert("loading dashboard data...");

    });
}

function getMonitorDetails() {

    getGetCall({ url: "monitorProcess.action", callback: getMonitorTable });
}

// show dashboard

function getDashboard(result) {

    if (result.data.deviceType == "Ping")
    {
        window.open('dashboard.jsp', '_blank');
    }
    else
    {
        window.open('linux_dashboard.jsp', '_blank');
    }
}

function showDashboard(id, ip, deviceType) {

    getPostCall({ url: "dashboardProcess.action", data: { id: id, ip: ip, deviceType: deviceType }, callback: getDashboard });
}

// get all the basic details for ping device

function getRTTTime(response) {

    var rttTime = 0;

    var rtt = response.indexOf("rtt");

    if (rtt != -1)
    {
        rttTime = response.substring(response.indexOf("rtt") + 23, response.indexOf("rtt") + 24);
    }

    return rttTime;
}

function getPacketLoss(response) {

    var packetLoss;

    var receivedPacket = response.substring(response.indexOf("transmitted") + 13, response.indexOf("transmitted") + 14);

    if (receivedPacket == "0")
    {
        packetLoss = "100";
    }
    if (receivedPacket == "1")
    {
        packetLoss = "75";
    }
    if (receivedPacket == "2")
    {
        packetLoss = "50";
    }
    if (receivedPacket == "3")
    {
        packetLoss = "25";
    }
    if (receivedPacket == "4")
    {
        packetLoss = "0";
    }

    return packetLoss;

}

// ping dashboard header

function getDashboardHeaderData(request) {

    var tableData = "";

    var tableTitle = "";

    var data = request.data;

    $.each(data.beanList, function () {

        tableTitle += "<p>" + this.IP + "</p>";

        tableData += "<tr>" +

            "<td><b>IP/Host</b>:&nbsp;" + this.IP + "&nbsp;&nbsp;<i class='bi bi-activity' style='color: #2a92ff;'></i></td>" +

            "<td><b>Profile</b>:&nbsp;" + this.username + "</td>" +

            "<td><b>Poll Time</b>:&nbsp;" + this.currentTime[0] + "&nbsp;&nbsp;<i class='bi bi-arrow-repeat' style='cursor:pointer;' title='Poll Now' data-value='"+this.id+", "+this.IP+", "+this.device+"'></i>" +"</td>" +

            + "</tr>" +

            "<tr>" +

            "<td><b>Id</b>:&nbsp;" + this.id + "</td>" +

            "<td><b>Status</b>:&nbsp;" + this.status + "</td>" +

            "<td><b>DeviceType</b>:&nbsp;" + this.device + "</td>" +

            + "</tr>";

    });

    $('#dashboardTitle').html(tableTitle);

    $('#dashboardHeader').html(tableData);

    $('.bi-arrow-repeat').on('click', function () {

        var parameter = event.currentTarget.getAttribute('data-value');

        console.log(parameter);

        var array = parameter.split(",");

        getPolling(array[0], array[1].trim(), array[2].trim());

        alert("polling data...");

    });
}

function getDashboardHeader() {

    getGetCall({ url: "dashboardTable.action", callback: getDashboardHeaderData });
}

// ping dashboard body

function getDashboardBodyData(request) {

    var tableData = "";

    var data = request.data;

    $.each(data.beanList, function () {

        var response = this.response;

        var sentPacket = response.substring(response.indexOf("statistics") + 14, response.indexOf("statistics") + 15);

        var packetLoss = getPacketLoss(response);

        var receivedPacket = response.substring(response.indexOf("transmitted") + 13, response.indexOf("transmitted") + 14);

        var rttTime = getRTTTime(response);

        tableData += "<tr>" +

            "<td class='dash__firstRow'>" + "<div id='dougnutChart' style='height: 270px'>" + "</div>" + "</td>" +

            "<td class='dash__secondRow'>" + "<div class='dash__widget'>" + "<div>" + "<p>Sent Packet</p>" + "</div>" + "<div>" + "<p class='dash__response'>" + sentPacket + "</p>" + "</div>" + "</div>" + "</td>" +

            "<td class='dash__thirdRow'>" + "<div class='dash__widget'>" + "<div>" + "<p>Packet Loss (%)</p>" + "</div>" + "<div>" + "<p class='dash__response'>" + packetLoss + "</p>" + "</div>" + "</div>" + "</td>" +

            "<td class='dash__fourthRow'>" + "<div class='dash__widget'>" + "<div>" + "<p>Received Packet</p>" + "</div>" + "<div>" + "<p class='dash__response'>" + receivedPacket + "</p>" + "</div>" + "</div>" + "</td>" +

            "<td class='dash__fifthRow'>" + "<div class='dash__widget'>" + "<div>" + "<p>RTT (ms)</p>" + "</div>" + "<div>" + "<p class='dash__response'>" + rttTime + "</p>" + "</div>" + "</div>" + "</td>" +


            + "</tr>";

    });

    $('#dashboardTableBody').html(tableData);

    getPieChartDetails();

    getColumnChartDetails();
}

function getDashboardBody() {

    getGetCall({ url: "dashboardTable.action", callback: getDashboardBodyData });
}

// get all the basic details for linux device

function getDiskPercent(response) {

    var diskPercent;

    var array = response.split(",");

    diskPercent = array[11].trim();

    return Math.round(diskPercent);
}

function getSwapFreePercent(response) {

    var swapFree;

    var array = response.split(",");

    swapFree = (array[10] / array[8]) * 100;

    return Math.round(swapFree);
}

function getSwapUsedPercent(response) {

    var swapUsed;

    var array = response.split(",");

    swapUsed = (array[9] / array[8]) * 100;

    return Math.round(swapUsed);
}

function getCPUSystemPercent(response) {

    var cpuSystem;

    var array = response.split(",");

    cpuSystem = array[12].trim();

    return Math.round(cpuSystem);
}

function getCPUUserPercent(response) {

    var cpuUser;

    var array = response.split(",");

    cpuUser = array[12].trim();

    return Math.round(cpuUser);
}

function getRTT(response) {

    var rtt = 0;

    if (response == null)
    {
        rtt = "";
    }

    return rtt;
}

function getFreeMemoryPercent(response) {

    var array = response.split(",");

    var freeMemory = array[3].trim() - array[4].trim() - array[14].trim() - array[15].substring(0, array[15].length - 1).trim();

    var totalMemory = array[3].trim();

    var free = (freeMemory / totalMemory) * 100;

    return Math.round(free);
}

function getUsedMemoryPercent(response) {

    var array = response.split(",");

    var usedMemory = array[3].trim() - array[5].trim() - array[14].trim() - array[15].substring(0, array[15].length - 1).trim();

    var totalMemory = array[3].trim();

    var used = (usedMemory / totalMemory) * 100;

    return Math.round(used);
}

function getOSName(response) {

    var OSName;

    var array = response.split(",");

    OSName = array[7].trim();

    return OSName;
}

function getOSVersion(response) {

    var OSVersion;

    var array = response.split(",");

    OSVersion = array[6].trim();

    return OSVersion;
}

function getCPUType(response) {

    var CPUType;

    var array = response.split(",");

    CPUType = array[2].trim();

    return CPUType;
}

function getSystemName(response) {

    var systemName;

    var array = response.split(",");

    systemName = array[1].trim();

    return systemName;
}

function getDeviceName(response) {

    var deviceType;

    var array = response.substring(1).split(",");

    deviceType = array[0].trim();

    return deviceType;
}

// linux dashboard header

function getLinuxDashboardHeaderData(request) {

    var data = request.data;

    var tableData = "";

    var tableTitle = "";

    $.each(data.beanList, function () {

        tableTitle += "<p>" + this.IP + "</p>";

        tableData += "<tr>" +

            "<td><b>IP/Host</b>:&nbsp;" + this.IP + "&nbsp;&nbsp;<i class='bi bi-activity' style='color: #2a92ff;'></i></td>" +

            "<td><b>Profile</b>:&nbsp;" + this.username + "</td>" +

            "<td><b>Poll Time</b>:&nbsp;" + this.currentTime[0] + "&nbsp;&nbsp;<i class='bi bi-arrow-repeat' style='cursor:pointer;' title='Poll Now' data-value='"+this.id+", "+this.IP+", "+this.device+"'></i>" +"</td>" +

            + "</tr>" +

            "<tr>" +

            "<td><b>Id</b>:&nbsp;" + this.id + "</td>" +

            "<td><b>Status</b>:&nbsp;" + this.status + "</td>" +

            "<td><b>DeviceType</b>:&nbsp;" + this.device + "</td>" +

            + "</tr>";

    });

    $('#dashboardTitle').html(tableTitle);

    $('#linuxDashboardHeader').html(tableData);

    $('.bi-arrow-repeat').on('click', function () {

        var parameter = event.currentTarget.getAttribute('data-value');

        console.log(parameter);

        var array = parameter.split(",");

        getPolling(array[0], array[1].trim(), array[2].trim());

        alert("polling data...");

    });
}

function getLinuxDashboardHeader() {

    getGetCall({ url: "dashboardTable.action", callback: getLinuxDashboardHeaderData });
}

// linux dashboard body

function getLinuxDashboardBodyData(request) {

    var data = request.data;

    var tableData = "";

    var deviceName, systemName, CPUType, OSVersion, OSName, UsedMemory, FreeMemory, RTT, CPUUserPercent, CPUSystemPercent, SwapUsed, SwapFree, Disk;

    $.each(data.beanList, function () {

        var response = this.response;

        if (response == "")
        {
            deviceName = systemName = CPUType = OSVersion = OSName = UsedMemory = FreeMemory = RTT = CPUUserPercent = CPUSystemPercent = SwapFree = SwapUsed = Disk = "";
        }
        else
        {
            deviceName = getDeviceName(response);

            systemName = getSystemName(response);

            CPUType = getCPUType(response);

            OSVersion = getOSVersion(response);

            OSName = getOSName(response);

            UsedMemory = getUsedMemoryPercent(response) + '%';

            FreeMemory = getFreeMemoryPercent(response) + '%';

            RTT = getRTT(response);

            CPUUserPercent = getCPUUserPercent(response) + '%';

            CPUSystemPercent = getCPUSystemPercent(response) + '%';

            SwapUsed = getSwapUsedPercent(response) + '%';

            SwapFree = getSwapFreePercent(response) + '%';

            Disk = getDiskPercent(response) + '%';
        }

        tableData += "<tr style='height: 300px'>" +

            "<td class='dash__firstRow'>" + "<div id='dougnutChart' style='height: 270px; width: 100%'>" + "</div>" + "</td>" +

            "<td class='linux__Widget'>" + "<div>" + "<table style='margin-left: 12px'>" + "<tbody>" +

            "<tr>" + "<td><b>Monitor</b>" + "<td>" + this.IP + "</td>" + "</td>" + "</tr>" +

            "<tr>" + "<td><b>Type</b>" + "<td>" + deviceName + "</td>" + "</td>" + "</tr>" +

            "<tr>" + "<td><b>System Name</b>" + "<td>" + systemName + "</td>" + "</td>" + "</tr>" +

            "<tr>" + "<td><b>CPU Type</b>" + "<td>" + CPUType + "</td>" + "</td>" + "</tr>" +

            "<tr>" + "<td><b>OS Version</b>" + "<td>" + OSVersion + "</td>" + "</td>" + "</tr>" +

            "<tr>" + "<td><b>OS Name</b>" + "<td>" + OSName + "</td>" + "</td>" + "</tr>" + "</tbody>" + "</table>" + "</div>" +

            "</td>" +

            "<td class='linux__Widget'>" + "<div class='dash__widget'>" + "<div><p>Memory Used (%)</p></div>" + "<div><b><p>" + UsedMemory + "</p></b></div>" + "</div>" + "</td>" +

            "<td class='linux__Widget'>" + "<div class='dash__widget'>" + "<div><p>Memory Free (%)</p></div>" + "<div><b><p>" + FreeMemory + "</p></b></div>" + "</div>" + "</td>" +

            "<td class='linux__Widget'>" + "<div class='dash__widget'>" + "<div><p>RTT (ms)</p></div>" + "<div><b><p>" + RTT + "</p></b></div>" + "</div>" + "</td>" +

            + "</tr>" +

            "<tr style='height: 300px'>" +

            "<td class='linux__initial'>" + "<div class='dash__widget'>" + "<div><p>CPU User (%)</p></div>" + "<div><b><p>" + CPUUserPercent + "</p></b></div>" + "</div>" + "</td>" +

            "<td class='linux__initial'>" + "<div class='dash__widget'>" + "<div><p>CPU System (%)</p></div>" + "<div><b><p>" + CPUSystemPercent + "</p></b></div>" + "</div>" + "</td>" +

            "<td class='linux__initial'>" + "<div class='dash__widget'>" + "<div><p>Swap Used Memory (%)</p></div>" + "<div><b><p>" + SwapUsed + "</p></b></div>" + "</div>" + "</td>" +

            "<td class='linux__initial'>" + "<div class='dash__widget'>" + "<div><p>Swap Free Memory (%)</p></div>" + "<div><b><p>" + SwapFree + "</p></b></div>" + "</div>" + "</td>" +

            "<td class='linux__initial'>" + "<div class='dash__widget'>" + "<div><p>Disk (%)</p></div>" + "<div><b><p>" + Disk + "</p></b></div>" + "</div>" + "</td>" +

            + "</tr>";
    });

    $('#linuxDashboardTableBody').html(tableData);

    getPieChartDetails();

    getColumnChartDetails();
}

function getLinuxDashboardBody() {

    getGetCall({ url: "dashboardTable.action", callback: getLinuxDashboardBodyData });
}

// column chart

function getColumnChartData(request) {

    var data = request.data;

    alert("loading charts...");

    $.each(data.beanList, function () {

        var secondChart;

        var Status = this.status;

        var DeviceType = this.device;

        var response = this.response;

        var receivedPacket = this.packet;

        var memoryPercent = this.memory;

        var timeArray = this.currentTime;

        if (Status == "Down" && DeviceType == "Ping")
        {
            secondChart = new CanvasJS.Chart("areaChart",
                {
                    width : 1420,

                    axisY: {
                        title: "Received Packet",
                        minimum: 0
                    },

                    axisX: {
                        title: "Minutes Interval"
                    },

                    data: [
                        {
                            type: "column",

                            dataPoints: [

                                { label: timeArray[0], y: parseInt(receivedPacket[0]) },

                                { label: timeArray[1], y: parseInt(receivedPacket[1]) },

                                { label: timeArray[2], y: parseInt(receivedPacket[2]) },

                                { label: timeArray[3], y: parseInt(receivedPacket[3]) },

                                { label: timeArray[4], y: parseInt(receivedPacket[4]) },

                                { label: timeArray[5], y: parseInt(receivedPacket[5]) },

                                { label: timeArray[6], y: parseInt(receivedPacket[6]) },

                                { label: timeArray[7], y: parseInt(receivedPacket[7]) },
                            ]

                        }
                    ]

                });
        }
        else if (Status == "Up" && DeviceType == "Ping")
        {
            secondChart = new CanvasJS.Chart("areaChart",
                {
                    width : 1420,

                    axisY: {
                        title: "Received Packet",
                        minimum: 0
                    },

                    axisX: {
                        title: "Minutes Interval"
                    },

                    data: [
                        {
                            type: "column",

                            dataPoints: [

                                { label: timeArray[0], y: parseInt(receivedPacket[0]) },

                                { label: timeArray[1], y: parseInt(receivedPacket[1]) },

                                { label: timeArray[2], y: parseInt(receivedPacket[2]) },

                                { label: timeArray[3], y: parseInt(receivedPacket[3]) },

                                { label: timeArray[4], y: parseInt(receivedPacket[4]) },

                                { label: timeArray[5], y: parseInt(receivedPacket[5]) },

                                { label: timeArray[6], y: parseInt(receivedPacket[6]) },

                                { label: timeArray[7], y: parseInt(receivedPacket[7]) },
                            ]

                        }
                    ]
                });
        }
        else if (Status == "Up" && DeviceType == "Linux")
        {
            secondChart = new CanvasJS.Chart("areaChart",
                {
                    width : 1420,

                    axisY: {
                        title: "Memory (%)",
                        minimum: 0
                    },

                    axisX: {
                        title: "Minutes Interval"
                    },

                    data: [
                        {
                            type: "column",

                            dataPoints: [

                                { label: timeArray[0], y: parseFloat(memoryPercent[0]) },

                                { label: timeArray[1], y: parseFloat(memoryPercent[1]) },

                                { label: timeArray[2], y: parseFloat(memoryPercent[2]) },

                                { label: timeArray[3], y: parseFloat(memoryPercent[3]) },

                                { label: timeArray[4], y: parseFloat(memoryPercent[4]) },

                                { label: timeArray[5], y: parseFloat(memoryPercent[5]) },

                                { label: timeArray[6], y: parseFloat(memoryPercent[6]) },

                                { label: timeArray[7], y: parseFloat(memoryPercent[7]) },
                            ]

                        }
                    ]
                });
        }
        else
        {
            secondChart = new CanvasJS.Chart("areaChart",
                {
                    width : 1420,

                    axisY: {
                        title: "Memory (%)",
                        minimum: 0
                    },

                    axisX: {
                        title: "Minutes Interval"
                    },

                    data: [
                        {
                            type: "column",

                            dataPoints: [

                                { label: timeArray[0], y: parseFloat(memoryPercent[0]) },

                                { label: timeArray[1], y: parseFloat(memoryPercent[1]) },

                                { label: timeArray[2], y: parseFloat(memoryPercent[2]) },

                                { label: timeArray[3], y: parseFloat(memoryPercent[3]) },

                                { label: timeArray[4], y: parseFloat(memoryPercent[4]) },

                                { label: timeArray[5], y: parseFloat(memoryPercent[5]) },

                                { label: timeArray[6], y: parseFloat(memoryPercent[6]) },

                                { label: timeArray[7], y: parseFloat(memoryPercent[7]) },
                            ]

                        }
                    ]

                });
        }

        secondChart.render();

    });

}

function getColumnChartDetails() {

    getGetCall({ url: "dashboardTable.action", callback: getColumnChartData });
}

// pie chart

function getPieChartData(request) {

    var data = request.data;

    $.each(data.beanList, function () {

        var firstChart;

        var IP = this.IP;

        var Status = this.status;

        if (Status == "Down")
        {
            firstChart = new CanvasJS.Chart("dougnutChart",
                {
                    width : 340,

                    title:{
                        text: IP
                    },

                    data: [
                        {
                            type: "doughnut",

                            innerRadius: "50%",

                            xValueFormatString:"Up # %",

                            yValueFormatString:"Down # %",

                            color: "#A21919",

                            dataPoints: [
                                {  x: 0, y: 1.0, indexLabel: IP },
                            ]

                        }
                    ]


                });
        }
        else
        {
            firstChart = new CanvasJS.Chart("dougnutChart",
                {
                    width : 340,

                    title:{
                        text: IP
                    },

                    data: [
                        {
                            type: "doughnut",

                            innerRadius: "50%",

                            xValueFormatString:"Down # %",

                            yValueFormatString:"Up # %",

                            color: "#008000",

                            dataPoints: [
                                {  x: 0, y: 1.0, indexLabel: IP },
                            ]

                        }
                    ]

                });
        }

        firstChart.render();

    });

}

function getPieChartDetails() {

    getGetCall({ url: "dashboardTable.action", callback: getPieChartData });
}
