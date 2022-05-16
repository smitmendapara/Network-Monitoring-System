// home dashboard matrix
function getHomeDashboardData(request)
{
    if (request !== undefined && request.data !== undefined)
    {
        let tableData = "";

        let data = request.data;

        tableData += "<div class='float-container'>" +
            "<div class='float-child'>" +
            "<div class='green'>" +
            "<span class='content'><b>Total Device</b></span><br><br>" +
            "<span class='content-value'><b>" + data.totalDevice + "</b></span>" +
            "</div>" +
            "</div>" +
            "<div class='float-child'>" +
            "<div class='blue'>" +
            "<span class='content'><b>Total Ping Device</b></span><br><br>" +
            "<span class='content-value'><b>" + data.pingDevice + "</b></span>" +
            "</div>" +
            "</div>" +
            "<div class='float-child'>" +
            "<div class='yellow'>" +
            "<span class='content-title'><b>Total Linux Device</b></span><br><br>" +
            "<span class='content-value'><b>" + data.linuxDevice + "</b></span>" +
            "</div>" +
            "</div>" +
            "</div>" +
            "<div class='float-container'>" +
            "<div class='float-second-child'>" +
            "<div class='green'>" +
            "<span class='content-2'><b>Top 2 CPU Device</b></span><br><hr>" +
            "<table class='content-2-title'><tbody>" +
            "<tr><th>IP Address</th><th>CPU Idle (%)</th></tr><tr><td>" + data.topCPU[1] + "</td><td>" + data.topCPU[0] + "</td></tr>" +
            "<tr><td>" + data.topCPU[3] + "</td><td>" + data.topCPU[2] + "</td></tr>" +
            "</tbody></table>" +

            "</div>" +
            "</div>" +
            "<div class='float-second-child'>" +
            "<div class='blue'>" +
            "<span class='content-2-second'><b>Top 2 Memory Device</b></span><hr>" +
            "<table class='content-2-title'><tbody>" +
            "<tr><th>IP Address</th><th>Free Memory (%)</th></tr><tr><td>" + data.topMemory[1] + "</td><td>" + data.topMemory[0] + "</td></tr>" +
            "<tr><td>" + data.topMemory[3] + "</td><td>" + data.topMemory[2] + "</td></tr>" +
            "</tbody></table>" +
            "</div>" +
            "</div>" +

            "<div class='float-second-child'>" +
            "<div>" +
            "<span class='content-2-second'><b>Top 2 Disk Device</b></span><br><hr>" +
            "<table class='content-2-title'><tbody>" +
            "<tr><th>IP Address</th><th>Disk Used (%)</th></tr><tr><td>" + data.topDisk[1] + "</td><td>" + data.topDisk[0] + "</td></tr>" +
            "<tr><td>" + data.topDisk[3] + "</td><td>" + data.topDisk[2] + "</td></tr>" +
            "</tbody></table>" +
            "</div>" +
            "</div>" +
            "</div>";

        tableData += "<div class='third-container'>" +
            "<div class='thirdRow'>" +
            "<div>" +
            "<span class='content-3'><b>Total Up Device</b></span><br><br>" +
            "<span class='content-value'><b>" + data.upDevice + "</b></span>" +
            "</div>" +
            "</div>" +
            "<div class='thirdRow'>" +
            "<div>" +
            "<span class='content-3'><b>Total Down Device</></span><br><br>" +
            "<span class='content-value'><b>" + data.downDevice + "</b></span>" +
            "</div>" +
            "</div>" +
            "<div class='thirdRow'>" +
            "<div>" +
            "<span class='content-3'><b>Total Unknown Device</></span><br><br>" +
            "<span class='content-value'><b>" + data.unknownDevice + "</b></span>" +
            "</div>" +
            "</div>" +
            "</div>";

        $('#tableData').html(tableData);
    }
    else
    {
        toastr.error("Home Dashboard Matrix data undefined!");
    }
}

function getHomeDashboardDetails()
{
    if ($('#' + monitorFormId).css({display: 'block'}))
    {
        $('#' + monitorFormId).css({display: 'none'});
    }

    if ($('#' + discoverFormId).css({display: 'block'}))
    {
        $('#' + discoverFormId).css({display: 'none'});
    }

    executeGETRequest({ url: "homeDashboardData", callback: getHomeDashboardData });
}

// dashboard data
function getDashboardData(request)
{
    if (request !== undefined && request.data !== undefined)
    {
        let tableData = "";

        let data = request.data;

        tableData += "<h3 style='margin-left: 12px'>" + data.ip + "&nbsp;&nbsp;<i class='bi bi-activity' style='color: #2a92ff;'></i></h3>" +

                        "<table style='width: 98%;margin: auto'>" +
                            "<tbody>" +
                                "<tr>" +
                                    "<td><b>IP</b>:&nbsp;" + data.ip + "&nbsp;&nbsp;<i class='bi bi-activity' style='color: #2a92ff;'></i></td>" +

                                    "<td><b>Last Poll</b>:&nbsp;" + data.currentTime[0] + "&nbsp;&nbsp;<i class='bi bi-arrow-repeat' style='cursor:pointer;' title='Poll Now' data-value='"+data.id+", "+data.ip+", "+data.deviceType+"'></i>" +"</td>" +

                                    "<td><b>Id</b>:&nbsp;" + data.id + "</td>" +

                                    "<td><b>Status</b>:&nbsp;" + data.status + "</td>" +

                                    "<td><b>DeviceType</b>:&nbsp;" + data.deviceType + "</td>" +
                                "</tr>";

        if (data.deviceType === "Ping")
        {
            tableData += "<tr>" +

                            "<td class='dash__firstRow'>" + "<div id='dougnutChart' style='height: 270px; width: 270px'>" + "</div>" + "</td>" +

                            "<td class='dash__secondRow'>" + "<div class='dash__widget'>" + "<div>" + "<p>Sent Packet</p>" + "</div>" + "<div>" + "<p class='dash__response'>" + data.response[0] + "</p>" + "</div>" + "</div>" + "</td>" +

                            "<td class='dash__thirdRow'>" + "<div class='dash__widget'>" + "<div>" + "<p>Packet Loss (%)</p>" + "</div>" + "<div>" + "<p class='dash__response'>" + data.response[1] + "</p>" + "</div>" + "</div>" + "</td>" +

                            "<td class='dash__fourthRow'>" + "<div class='dash__widget'>" + "<div>" + "<p>Received Packet</p>" + "</div>" + "<div>" + "<p class='dash__response'>" + data.response[2] + "</p>" + "</div>" + "</div>" + "</td>" +

                            "<td class='dash__fifthRow'>" + "<div class='dash__widget'>" + "<div>" + "<p>RTT (ms)</p>" + "</div>" + "<div>" + "<p class='dash__response'>" + data.response[3] + "</p>" + "</div>" + "</div>" + "</td>" +

                         "</tr>" +
                    "</tbody>" +
                "</table>" +

                "<table><tbody><tr><div id=\"areaChart\" style=\"height: 300px;margin: 20px 0 0 20px\"></div></tr></tbody></table>";

            $('#tableData').html(tableData);
        }

        if (data.deviceType === "Linux")
        {
            tableData +=

                "<tr style='height: 300px'>" +
                    "<td class='dash__firstRow'>" + "<div id='dougnutChart' style='height: 270px; width: 100%'>" + "</div>" + "</td>" +
                    "<td class='linux__Widget'>" +
                    "<div>" +

                        "<table style='margin-left: 12px'>" +

                            "<tbody>" +

                                "<tr>" + "<td><b>Monitor</b>" + "<td>" + data.ip + "</td>" + "</td>" + "</tr>" +

                                "<tr>" + "<td><b>Type</b>" + "<td>" + data.response[0] + "</td>" + "</td>" + "</tr>" +

                                "<tr>" + "<td><b>System Name</b>" + "<td>" + data.response[1] + "</td>" + "</td>" + "</tr>" +

                                "<tr>" + "<td><b>CPU Type</b>" + "<td>" + data.response[2] + "</td>" + "</td>" + "</tr>" +

                                "<tr>" + "<td><b>OS Version</b>" + "<td>" + data.response[3] + "</td>" + "</td>" + "</tr>" +

                                "<tr>" + "<td><b>OS Name</b>" + "<td>" + data.response[4] + "</td>" + "</td>" + "</tr>" +

                            "</tbody>" +

                        "</table>" +

                    "</div>" +

                "</td>"+

                        "<td class='linux__Widget'>"+ "<div class='dash__widget'>" + "<div><p>Memory Used (%)</p></div>" + "<div><b><p>" + data.response[5] + "</p></b></div>" + "</div>" +"</td>" +
                        "<td class='linux__Widget'>"+ "<div class='dash__widget'>" + "<div><p>Memory Free (%)</p></div>" + "<div><b><p>" + data.response[6] + "</p></b></div>" + "</div>" +"</td>" +
                        "<td class='linux__Widget'>" + "<div class='dash__widget'>" + "<div><p>RTT (ms)</p></div>" + "<div><b><p>" + data.response[7] + "</p></b></div>" + "</div>" + "</td>" +

                        "<tr>" +
                            "<td class='linux__initial'>" + "<div class='dash__widget'>" + "<div><p>CPU User (%)</p></div>" + "<div><b><p>" + data.response[8] + "</p></b></div>" + "</div>" + "</td>" +

                            "<td class='linux__initial'>" + "<div class='dash__widget'>" + "<div><p>CPU System (%)</p></div>" + "<div><b><p>" + data.response[9] + "</p></b></div>" + "</div>" + "</td>" +

                            "<td class='linux__initial'>" + "<div class='dash__widget'>" + "<div><p>Swap Used Memory (%)</p></div>" + "<div><b><p>" + data.response[10] + "</p></b></div>" + "</div>" + "</td>" +

                            "<td class='linux__initial'>" + "<div class='dash__widget'>" + "<div><p>Swap Free Memory (%)</p></div>" + "<div><b><p>" + data.response[11] + "</p></b></div>" + "</div>" + "</td>" +

                            "<td class='linux__initial'>" + "<div class='dash__widget'>" + "<div><p>Disk Used (%)</p></div>" + "<div><b><p>" + data.response[12] + "</p></b></div>" + "</div>" + "</td>" +
                        "</tr>" +
                        "</tr>" +
                    "</tbody>" +
                "</table>" +

                "<table style='margin-top: 20px'><tbody><tr><div id=\"areaChart\" style=\"height: 300px;margin: 20px 0 0 20px\"></div></tr></tbody></table>";

            $('#tableData').html(tableData);
        }

        getPieChartDetails(request.data.id, request.data.ip, request.data.deviceType);

        getColumnChartDetails(request.data.id, request.data.ip, request.data.deviceType);
    }
    else
    {
        toastr.error("Dashboard data undefined!")
    }

}

function showDashboard(id, ip, deviceType)
{
    executePOSTRequest({ url: "dashboardTable", data: { id: id, ip: ip, deviceType: deviceType }, callback: getDashboardData });
}

// pie chart
function getPieChartData(request)
{
    if (request !== undefined && request.data !== undefined)
    {
        let data = request.data;

        let firstChart;

        firstChart = new CanvasJS.Chart("dougnutChart",
            {
                width : 340,

                title:{
                    text: "Last 24 Hours "+ data.ip +" Availability",
                    fontSize: 16,
                },

                data: [
                    {
                        type: "doughnut",

                        innerRadius: "50%",

                        dataPoints: [
                            {  x: 0, y: data.percent[0], indexLabel: "Up" },
                            {  x: 0, y: data.percent[1], indexLabel: "Down" },
                        ]

                    }
                ]

            });

        firstChart.render();
    }
    else
    {
        toastr.error("Pie chart data undefined!");
    }
}

function getPieChartDetails(id, ip, deviceType)
{
    executePOSTRequest({ url: "dashboardTable", data: {id: id, ip: ip, deviceType: deviceType }, callback: getPieChartData });
}

// column chart
function getColumnChartData(request)
{
    if (request !== undefined && request.data !== undefined)
    {
        let data = request.data;

        let secondChart;

        secondChart = new CanvasJS.Chart("areaChart",
            {
                width : 1420,

                title:{
                    text: "Last 40 Minutes "+ data.ip +" Availability",
                    fontSize: 16,
                },

                axisY: {
                    title: data.title,
                    minimum: 0,
                },

                axisX: {
                    title: "Minutes Interval",
                    reversed: true
                },

                data: [
                    {
                        type: "column",

                        indexLabel: "{y}",

                        dataPoints: [

                            { label: data.currentTime[0], y: data.dataPoints[0] },

                            { label: data.currentTime[1], y: data.dataPoints[1] },

                            { label: data.currentTime[2], y: data.dataPoints[2] },

                            { label: data.currentTime[3], y: data.dataPoints[3] },

                            { label: data.currentTime[4], y: data.dataPoints[4] },

                            { label: data.currentTime[5], y: data.dataPoints[5] },

                            { label: data.currentTime[6], y: data.dataPoints[6] },

                            { label: data.currentTime[7], y: data.dataPoints[7] },
                        ]

                    }
                ]

            });

        secondChart.render();

    }
    else
    {
        toastr.error("Column chart data undefined!");
    }
}

function getColumnChartDetails(id, ip, deviceType)
{
    executePOSTRequest({ url: "dashboardTable", data: {id: id, ip: ip, deviceType: deviceType }, callback: getColumnChartData });
}