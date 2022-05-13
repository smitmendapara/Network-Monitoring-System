// home dashboard matrix
function getHomeDashboardData(request)
{
    if (request !== undefined && request.data !== undefined)
    {
        let tableData = "";

        $.each(request.data.beanList, function () {

            tableData += "<div class='float-container'>" +
                "<div class='float-child'>" +
                "<div class='green'>" +
                "<span class='content'><b>Total Device</b></span><br><br>" +
                "<span class='content-value'><b>" + this.totalDevice + "</b></span>" +
                "</div>" +
                "</div>" +
                "<div class='float-child'>" +
                "<div class='blue'>" +
                "<span class='content'><b>Up Device</b></span><br><br>" +
                "<span class='content-value'><b>" + this.upDevice + "</b></span>" +
                "</div>" +
                "</div>" +
                "<div class='float-child'>" +
                "<div class='yellow'>" +
                "<span class='content-title'><b>Down Device</b></span><br><br>" +
                "<span class='content-value'><b>" + this.downDevice + "</b></span>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "<div class='float-container'>" +
                "<div class='float-second-child'>" +
                "<div class='green'>" +
                "<span class='content-2'><b>Top 2 CPU Device</b></span><br><hr>" +
                "<table class='content-2-title'><tbody>" +
                "<tr><th>IP Address</th><th>CPU Idle (%)</th></tr><tr><td>" + this.topCPU[1] + "</td><td>" + this.topCPU[0] + "</td></tr>" +
                "<tr><td>" + this.topCPU[3] + "</td><td>" + this.topCPU[2] + "</td></tr>" +
                "</tbody></table>" +

                "</div>" +
                "</div>" +
                "<div class='float-second-child'>" +
                "<div class='blue'>" +
                "<span class='content-2-second'><b>Top 2 Memory Device</b></span><hr>" +
                "<table class='content-2-title'><tbody>" +
                "<tr><th>IP Address</th><th>Free Memory (%)</th></tr><tr><td>" + this.topMemory[1] + "</td><td>" + this.topMemory[0] + "</td></tr>" +
                "<tr><td>" + this.topMemory[3] + "</td><td>" + this.topMemory[2] + "</td></tr>" +
                "</tbody></table>" +
                "</div>" +
                "</div>" +
                "</div>";

            tableData += "<div class='third-container'>" +
                "<div class='thirdRow'>" +
                "<div>" +
                "<span class='content-3'><b>Total Ping Device</b></span><br><br>" +
                "<span class='content-value'><b>" + this.pingDevice + "</b></span>" +
                "</div>" +
                "</div>" +
                "<div class='thirdRow'>" +
                "<div>" +
                "<span class='content-3'><b>Total Linux Device</></span><br><br>" +
                "<span class='content-value'><b>" + this.linuxDevice + "</b></span>" +
                "</div>" +
                "</div>" +
                "<div class='thirdRow'>" +
                "<div>" +
                "<span class='content-3'><b>Top 2 Disk Device</b></span><br><br>" +
                "<table class='content-3-title'><tbody>" +
                "<tr><th>IP Address</th><th>Disk Used (%)</th></tr><tr><td>" + this.topDisk[1] + "</td><td>" + this.topDisk[0] + "</td></tr>" +
                "<tr><td>" + this.topDisk[3] + "</td><td>" + this.topDisk[2] + "</td></tr>" +
                "</tbody></table>" +
                "</div>" +
                "</div>" +
                "</div>";

        });

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
        if (request.data.deviceType === "Ping")
        {
            let tableData = "";

            let data = request.data;

            $.each(data.beanList, function () {

                tableData += "<h3 style='margin-left: 12px'>" + this.IP + "&nbsp;&nbsp;<i class='bi bi-activity' style='color: #2a92ff;'></i></h3>" +

                    "<table style='width: 98%;margin: auto'>" +
                    "<tbody>" +
                    "<tr>" +
                    "<td><b>IP/Host</b>:&nbsp;" + this.IP + "&nbsp;&nbsp;<i class='bi bi-activity' style='color: #2a92ff;'></i></td>" +

                    "<td><b>Last Poll</b>:&nbsp;" + this.currentTime[0] + "&nbsp;&nbsp;<i class='bi bi-arrow-repeat' style='cursor:pointer;' title='Poll Now' data-value='"+this.id+", "+this.IP+", "+this.device+"'></i>" +"</td>" +

                    "<td><b>Id</b>:&nbsp;" + this.id + "</td>" +

                    "<td><b>Status</b>:&nbsp;" + this.status + "</td>" +

                    "<td><b>DeviceType</b>:&nbsp;" + this.device + "</td>" +
                    "</tr>" +
                    "<tr>" +

                    "<td class='dash__firstRow'>" + "<div id='dougnutChart' style='height: 270px; width: 270px'>" + "</div>" + "</td>" +

                    "<td class='dash__secondRow'>" + "<div class='dash__widget'>" + "<div>" + "<p>Sent Packet</p>" + "</div>" + "<div>" + "<p class='dash__response'>" + this.response[0] + "</p>" + "</div>" + "</div>" + "</td>" +

                    "<td class='dash__thirdRow'>" + "<div class='dash__widget'>" + "<div>" + "<p>Packet Loss (%)</p>" + "</div>" + "<div>" + "<p class='dash__response'>" + this.response[1] + "</p>" + "</div>" + "</div>" + "</td>" +

                    "<td class='dash__fourthRow'>" + "<div class='dash__widget'>" + "<div>" + "<p>Received Packet</p>" + "</div>" + "<div>" + "<p class='dash__response'>" + this.response[2] + "</p>" + "</div>" + "</div>" + "</td>" +

                    "<td class='dash__fifthRow'>" + "<div class='dash__widget'>" + "<div>" + "<p>RTT (ms)</p>" + "</div>" + "<div>" + "<p class='dash__response'>" + this.response[3] + "</p>" + "</div>" + "</div>" + "</td>" +

                    "</tr>" +
                    "</tbody>" +
                    "</table>" +

                    "<table><tbody><tr><div id=\"areaChart\" style=\"height: 300px;margin: 20px 0 0 20px\"></div></tr></tbody></table>";
            });

            $('#tableData').html(tableData);
        }

        if (request.data.deviceType === "Linux")
        {
            let tableData = "";

            let data = request.data;

            $.each(data.beanList, function () {

                tableData += "<h3 style='margin-left: 12px'>" + this.IP + "&nbsp;&nbsp;<i class='bi bi-activity' style='color: #2a92ff;'></i></h3>" +

                    "<table style='width: 98%;margin: auto;'>" +
                    "<tbody>" +
                    "<tr>" +
                    "<td><b>IP/Host</b>:&nbsp;" + this.IP + "&nbsp;&nbsp;<i class='bi bi-activity' style='color: #2a92ff;'></i></td>" +

                    "<td><b>Last Poll</b>:&nbsp;" + this.currentTime[0] + "&nbsp;&nbsp;<i class='bi bi-arrow-repeat' style='cursor:pointer;' title='Poll Now' data-value='"+this.id+", "+this.IP+", "+this.device+"'></i>" +"</td>" +

                    "<td><b>Id</b>:&nbsp;" + this.id + "</td>" +

                    "<td><b>Status</b>:&nbsp;" + this.status + "</td>" +

                    "<td><b>DeviceType</b>:&nbsp;" + this.device + "</td>" +
                    "</tr>" +

                    "<tr style='height: 300px'>" +
                    "<td class='dash__firstRow'>" + "<div id='dougnutChart' style='height: 270px; width: 100%'>" + "</div>" + "</td>" +
                    "<td class='linux__Widget'>" +
                    "<div>" +

                    "<table style='margin-left: 12px'>" +

                    "<tbody>" +

                    "<tr>" + "<td><b>Monitor</b>" + "<td>" + this.IP + "</td>" + "</td>" + "</tr>" +

                    "<tr>" + "<td><b>Type</b>" + "<td>" + this.response[0] + "</td>" + "</td>" + "</tr>" +

                    "<tr>" + "<td><b>System Name</b>" + "<td>" + this.response[1] + "</td>" + "</td>" + "</tr>" +

                    "<tr>" + "<td><b>CPU Type</b>" + "<td>" + this.response[2] + "</td>" + "</td>" + "</tr>" +

                    "<tr>" + "<td><b>OS Version</b>" + "<td>" + this.response[3] + "</td>" + "</td>" + "</tr>" +

                    "<tr>" + "<td><b>OS Name</b>" + "<td>" + this.response[4] + "</td>" + "</td>" + "</tr>" +

                    "</tbody>" +

                    "</table>" +

                    "</div>" +
                    "</td>"+
                    "<td class='linux__Widget'>"+ "<div class='dash__widget'>" + "<div><p>Memory Used (%)</p></div>" + "<div><b><p>" + this.response[5] + "</p></b></div>" + "</div>" +"</td>" +
                    "<td class='linux__Widget'>"+ "<div class='dash__widget'>" + "<div><p>Memory Free (%)</p></div>" + "<div><b><p>" + this.response[6] + "</p></b></div>" + "</div>" +"</td>" +
                    "<td class='linux__Widget'>" + "<div class='dash__widget'>" + "<div><p>RTT (ms)</p></div>" + "<div><b><p>" + this.response[7] + "</p></b></div>" + "</div>" + "</td>" +

                    "<tr>" +
                    "<td class='linux__initial'>" + "<div class='dash__widget'>" + "<div><p>CPU User (%)</p></div>" + "<div><b><p>" + this.response[8] + "</p></b></div>" + "</div>" + "</td>" +

                    "<td class='linux__initial'>" + "<div class='dash__widget'>" + "<div><p>CPU System (%)</p></div>" + "<div><b><p>" + this.response[9] + "</p></b></div>" + "</div>" + "</td>" +

                    "<td class='linux__initial'>" + "<div class='dash__widget'>" + "<div><p>Swap Used Memory (%)</p></div>" + "<div><b><p>" + this.response[10] + "</p></b></div>" + "</div>" + "</td>" +

                    "<td class='linux__initial'>" + "<div class='dash__widget'>" + "<div><p>Swap Free Memory (%)</p></div>" + "<div><b><p>" + this.response[11] + "</p></b></div>" + "</div>" + "</td>" +

                    "<td class='linux__initial'>" + "<div class='dash__widget'>" + "<div><p>Disk Used (%)</p></div>" + "<div><b><p>" + this.response[12] + "</p></b></div>" + "</div>" + "</td>" +

                    "</tr>" +
                    "</tr>" +
                    "</tbody>" +
                    "</table>" +

                    "<table style='margin-top: 20px'><tbody><tr><div id=\"areaChart\" style=\"height: 300px;margin: 20px 0 0 20px\"></div></tr></tbody></table>";
            });

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

        $.each(data.beanList, function () {

            let firstChart;

            firstChart = new CanvasJS.Chart("dougnutChart",
                {
                    width : 340,

                    title:{
                        text: "Last 24 Hours "+ this.IP +" Availability",
                        fontSize: 16,
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

        $.each(data.beanList, function () {

            let secondChart;

            secondChart = new CanvasJS.Chart("areaChart",
                {
                    width : 1420,

                    title:{
                        text: "Last 40 Minutes "+ this.IP +" Availability",
                        fontSize: 16,
                    },

                    axisY: {
                        title: this.title,
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

                                { label: this.currentTime[0], y: this.dataPoints[0] },

                                { label: this.currentTime[1], y: this.dataPoints[1] },

                                { label: this.currentTime[2], y: this.dataPoints[2] },

                                { label: this.currentTime[3], y: this.dataPoints[3] },

                                { label: this.currentTime[4], y: this.dataPoints[4] },

                                { label: this.currentTime[5], y: this.dataPoints[5] },

                                { label: this.currentTime[6], y: this.dataPoints[6] },

                                { label: this.currentTime[7], y: this.dataPoints[7] },
                            ]

                        }
                    ]

                });

            secondChart.render();

        });
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