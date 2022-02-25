let monitorFormId;

let discoverFormId;

// refresh page

function refreshPage()
{
    location.reload(true);
}

// open discovery form

function openForm(idName)
{
    discoverFormId = idName;

    if ($('#' + monitorFormId).css({display: 'block'}))
    {
        $('#' + monitorFormId).css({display: 'none'});
    }

    $('#' + idName).css({display: "block"});

    document.getElementById(idName).style.display = 'block';
}

// close discovery form

function closeForm(idName)
{
    document.getElementById(idName).style.display = 'none';
}

// post request

function getPostCall(request) {

    $.ajax({

        type: "POST",

        cache: false,

        timeout: 180000,

        data: request.data,

        url: request.url,

        success: function (data) {

            let myCallback;

            if (request.callback !== undefined)
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

        timeout : 180000,

        data: request.data,

        url : request.url,

        success : function (data) {

            let myCallback;

            if (request.callback !== undefined)
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

function discoveryTable(request)
{
    let tableData = "";

    let data = request.data;

    $.each(data.beanList, function () {

        tableData += "<tr class='disc__data' id='"+this.id+"'>" +

            "<td>" + this.name + "</td>" +

            "<td>" + this.IP + "</td>" +

            "<td>" +

            "<i class='bi bi-play disc__discovery' title='Discovery' data-value='"+this.id+", "+this.IP+", "+this.device+"'></i> &nbsp;" +

            "<i class='bi bi-eye disc__monitor' title='View Result' data-value='"+this.id+"'></i> &nbsp;" +

            "<i class='bi bi-pencil-square disc__edit' title='Edit'></i> &nbsp;" +

            "<i class='bi bi-trash disc__delete' title='Delete' data-value='"+this.id+"'></i> &nbsp;" +

            "</td>" +

            "</tr>"
    });

    $('#mainTable').html(tableData);

    $('.bi-play').on('click', function () {

        let parameter = event.currentTarget.getAttribute('data-value');

        let array = parameter.split(",");

        reDiscoverData(array[0], array[1].trim(), array[2].trim());

        alert("your ip is successfully rediscovered!");

    });

    $('.bi-eye').on('click', function () {

        let parameter = event.currentTarget.getAttribute('data-value');

        showForm(parameter);

    });

    $('.bi-trash').on('click', function () {

        let parameter = event.currentTarget.getAttribute('data-value');

        deleteRow(parameter);

    });
}

function getDiscoveryDetails()
{
    let provisionForm = "provisionForm";

    getGetCall({ url: "discoveryTable", callback: discoveryTable });
}

// manipulated discovery table data

function displayLinuxProfile(id, elementValue)
{
    let value = elementValue.value;

    document.getElementById(id).style.display = value === "1" ? 'block' : 'none';
}

function verifyDiscovery(request)
{
    let flag = true;

    let data = request.data;

    console.log(data);

    $.each(data.beanList, function () {

        flag = this.flag;

    });

    if (flag)
    {
        alert("device successfully discovered!");
    }
    else
    {
        alert("some thing went wrong on discover device!");
    }

    refreshPage();

}

// show discovery form

function discoverData()
{
    let name = $("#name").val();

    let ip = $("#IP").val();

    let discoveryUsername = $("#username").val();

    let discoveryPassword = $("#password").val();

    let deviceType = $("#device").val();

    getGetCall({ url: "discoveryProcess", data: { name: name, ip: ip, discoveryUsername: discoveryUsername, discoveryPassword: discoveryPassword, deviceType: deviceType }, callback: verifyDiscovery });
}

// for rediscovery

function reDiscoverData(id, ip, deviceType)
{
    getPostCall({ url: "reDiscoveryProcess", data: { id: id, ip: ip, deviceType: deviceType } });

    refreshPage();
}

// manually polling

function reloadPage(result)
{
    if (result.data.deviceType === "Ping")
    {
        window.location.href = "dashboard.jsp";
    }
    else
    {
        window.location.href = "linux_dashboard.jsp";
    }
}

function getPolling(id, ip, deviceType)
{

    getPostCall({ url: "monitorPolling", data: {id: id, ip: ip, deviceType: deviceType }, callback: reloadPage });
}

// toggle discovery table

function toggleTable()
{
    let table = document.getElementById("myTableDiv");

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

function deletedRow(result)
{
    document.getElementById(result.data.id).remove();

    alert("successfully row deleted!");
}

function deleteRow(id)
{
    getPostCall({ url: "discoveryDelete", data: { id: id }, callback: deletedRow });
}

// provision ip

function provisionIP(request)
{
    let flag = true;

    let data = request.data;

    $.each(data.beanList, function () {

        flag = this.flag;

    });

    if (flag)
    {
        alert("device successfully monitored!");
    }
    else
    {
        alert("device already added or ip is down!");
    }

}

function monitorData(id)
{
    if (document.getElementById(id).checked)
    {
        let id = $("input[name=key]").val();

        getGetCall({ url: "monitorProcess", data: { id: id }, callback: provisionIP });
    }
    else
    {
        alert("must be checked checkbox!");
    }
}

// show monitor form

function getMonitorForm(request)
{
    let tableData = "";

    let data = request.data;

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

function showForm(id)
{
    monitorFormId = $('div.disc__popUp').attr('id');

    if ($('#' + discoverFormId).css({display: 'block'}))
    {
        $('#' + discoverFormId).css({display: 'none'});
    }

    $('#' + monitorFormId).css({display: "block"});

    // document.getElementById(monitorFormId).style.display = 'block';

    getGetCall({ url: "monitorForm", data: { id: id }, callback: getMonitorForm });
}

// monitor table data

function getMonitorTable(result)
{
    let tableData = "";

    let data = result.data;

    $.each(data.beanList, function () {

        tableData += "<tr>" +

            "<td><input type='checkbox'></td>" +

            "<td>" + this.id + "</td>" +

            "<td>" + this.IP + "&nbsp;&nbsp;&nbsp;&nbsp;<i class='bi bi-box-arrow-up-right monitor__icon' data-value='"+this.id+", "+this.IP+", "+this.device+"'></i>" + "</td>" +

            "<td>" + this.device + "</td>" +

            "<td>" + this.status + "</td>" +

            "</tr>";

    });

    $('#monitorTable').html(tableData);

    $('.bi-box-arrow-up-right').on('click', function() {

        let parameter = event.currentTarget.getAttribute('data-value');

        let array = parameter.split(",");

        showDashboard(array[0], array[1].trim(), array[2].trim());

    });
}

function getMonitorDetails()
{
    getGetCall({ url: "monitorTable", callback: getMonitorTable });
}

// show dashboard

function getDashboard(result)
{
    if (result.data.deviceType === "Ping")
    {
        window.open('dashboard.jsp', '_blank');
    }
    else
    {
        window.open('linux_dashboard.jsp', '_blank');
    }
}

function showDashboard(id, ip, deviceType)
{
    getPostCall({ url: "dashboardProcess", data: { id: id, ip: ip, deviceType: deviceType }, callback: getDashboard });
}

// ping dashboard header

function getDashboardHeaderData(request)
{
    let tableData = "";

    let tableTitle = "";

    let data = request.data;

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

        let parameter = event.currentTarget.getAttribute('data-value');

        console.log(parameter);

        let array = parameter.split(",");

        getPolling(array[0], array[1].trim(), array[2].trim());

        alert("polling data...");

    });
}

function getDashboardHeader()
{
    getGetCall({ url: "dashboardTable", callback: getDashboardHeaderData });
}

// ping dashboard body

function getDashboardBodyData(request)
{
    let tableData = "";

    let data = request.data;

    $.each(data.beanList, function () {

        tableData += "<tr>" +

            "<td class='dash__firstRow'>" + "<div id='dougnutChart' style='height: 270px'>" + "</div>" + "</td>" +

            "<td class='dash__secondRow'>" + "<div class='dash__widget'>" + "<div>" + "<p>Sent Packet</p>" + "</div>" + "<div>" + "<p class='dash__response'>" + this.response[0] + "</p>" + "</div>" + "</div>" + "</td>" +

            "<td class='dash__thirdRow'>" + "<div class='dash__widget'>" + "<div>" + "<p>Packet Loss (%)</p>" + "</div>" + "<div>" + "<p class='dash__response'>" + this.response[1] + "</p>" + "</div>" + "</div>" + "</td>" +

            "<td class='dash__fourthRow'>" + "<div class='dash__widget'>" + "<div>" + "<p>Received Packet</p>" + "</div>" + "<div>" + "<p class='dash__response'>" + this.response[2] + "</p>" + "</div>" + "</div>" + "</td>" +

            "<td class='dash__fifthRow'>" + "<div class='dash__widget'>" + "<div>" + "<p>RTT (ms)</p>" + "</div>" + "<div>" + "<p class='dash__response'>" + this.response[3] + "</p>" + "</div>" + "</div>" + "</td>" +

            + "</tr>";

    });

    $('#dashboardTableBody').html(tableData);

    getPieChartDetails();

    getColumnChartDetails();
}

function getDashboardBody()
{
    getGetCall({ url: "dashboardTable", callback: getDashboardBodyData });
}

// linux dashboard header

function getLinuxDashboardHeaderData(request)
{
    let data = request.data;

    let tableData = "";

    let tableTitle = "";

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

        let parameter = event.currentTarget.getAttribute('data-value');

        console.log(parameter);

        let array = parameter.split(",");

        getPolling(array[0], array[1].trim(), array[2].trim());

        alert("polling data...");

    });
}

function getLinuxDashboardHeader() {

    getGetCall({ url: "dashboardTable", callback: getLinuxDashboardHeaderData });
}

// linux dashboard body

function getLinuxDashboardBodyData(request)
{
    let data = request.data;

    let tableData = "";

    $.each(data.beanList, function () {

        tableData += "<tr style='height: 300px'>" +

            "<td class='dash__firstRow'>" + "<div id='dougnutChart' style='height: 270px; width: 100%'>" + "</div>" + "</td>" +

            "<td class='linux__Widget'>" + "<div>" + "<table style='margin-left: 12px'>" + "<tbody>" +

            "<tr>" + "<td><b>Monitor</b>" + "<td>" + this.IP + "</td>" + "</td>" + "</tr>" +

            "<tr>" + "<td><b>Type</b>" + "<td>" + this.response[0] + "</td>" + "</td>" + "</tr>" +

            "<tr>" + "<td><b>System Name</b>" + "<td>" + this.response[1] + "</td>" + "</td>" + "</tr>" +

            "<tr>" + "<td><b>CPU Type</b>" + "<td>" + this.response[2] + "</td>" + "</td>" + "</tr>" +

            "<tr>" + "<td><b>OS Version</b>" + "<td>" + this.response[3] + "</td>" + "</td>" + "</tr>" +

            "<tr>" + "<td><b>OS Name</b>" + "<td>" + this.response[4] + "</td>" + "</td>" + "</tr>" + "</tbody>" + "</table>" + "</div>" +

            "</td>" +

            "<td class='linux__Widget'>" + "<div class='dash__widget'>" + "<div><p>Memory Used (%)</p></div>" + "<div><b><p>" + this.response[5] + "</p></b></div>" + "</div>" + "</td>" +

            "<td class='linux__Widget'>" + "<div class='dash__widget'>" + "<div><p>Memory Free (%)</p></div>" + "<div><b><p>" + this.response[6] + "</p></b></div>" + "</div>" + "</td>" +

            "<td class='linux__Widget'>" + "<div class='dash__widget'>" + "<div><p>RTT (ms)</p></div>" + "<div><b><p>" + this.response[7] + "</p></b></div>" + "</div>" + "</td>" +

            + "</tr>" +

            "<tr style='height: 300px'>" +

            "<td class='linux__initial'>" + "<div class='dash__widget'>" + "<div><p>CPU User (%)</p></div>" + "<div><b><p>" + this.response[8] + "</p></b></div>" + "</div>" + "</td>" +

            "<td class='linux__initial'>" + "<div class='dash__widget'>" + "<div><p>CPU System (%)</p></div>" + "<div><b><p>" + this.response[9] + "</p></b></div>" + "</div>" + "</td>" +

            "<td class='linux__initial'>" + "<div class='dash__widget'>" + "<div><p>Swap Used Memory (%)</p></div>" + "<div><b><p>" + this.response[10] + "</p></b></div>" + "</div>" + "</td>" +

            "<td class='linux__initial'>" + "<div class='dash__widget'>" + "<div><p>Swap Free Memory (%)</p></div>" + "<div><b><p>" + this.response[11] + "</p></b></div>" + "</div>" + "</td>" +

            "<td class='linux__initial'>" + "<div class='dash__widget'>" + "<div><p>Disk (%)</p></div>" + "<div><b><p>" + this.response[12] + "</p></b></div>" + "</div>" + "</td>" +

            + "</tr>";
    });

    $('#linuxDashboardTableBody').html(tableData);

    getPieChartDetails();

    getColumnChartDetails();
}

function getLinuxDashboardBody()
{
    getGetCall({ url: "dashboardTable", callback: getLinuxDashboardBodyData });
}

// column chart

function getColumnChartData(request)
{
    let data = request.data;

    $.each(data.beanList, function () {

        let secondChart;

        let status = this.status;

        let deviceType = this.device;

        let receivedPacket = this.packet;

        let memoryPercent = this.memory;

        console.log(receivedPacket);

        console.log(memoryPercent);

        let timeArray = this.currentTime;

        if (status == "Down" && deviceType == "Ping")
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
        else if (status == "Up" && deviceType == "Ping")
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
        else if (status == "Up" && deviceType == "Linux")
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

function getColumnChartDetails()
{
    getGetCall({ url: "dashboardTable", callback: getColumnChartData });
}

// pie chart

function getPieChartData(request)
{
    let data = request.data;

    $.each(data.beanList, function () {

        let firstChart;

        firstChart = new CanvasJS.Chart("dougnutChart",
            {
                width : 340,

                title:{
                    text: this.IP
                },

                data: [
                    {
                        type: "doughnut",

                        innerRadius: "50%",

                        dataPoints: [
                            {  x: 0, y: this.percent[0], indexLabel: "Up" },
                            {  x: 0, y: this.percent[1], indexLabel: "Down" },
                        ]

                    }
                ]

            });

        firstChart.render();

    });

}

function getPieChartDetails()
{
    getGetCall({ url: "dashboardTable", callback: getPieChartData });
}
