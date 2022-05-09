// home dashboard matrix
function getHomeDashboardData(request) {

    let tableData = "";

    tableData += "<div class='float-container'><div class='float-child'><div class='green'>Div 1</div></div><div class='float-child'><div class='blue'>Div 2</div></div><div class='float-child'><div class='yellow'>Div 3</div></div></div>" +
                 "<div class='float-container'><div class='float-second-child'><div class='green'>Div 1</div></div><div class='float-second-child'><div class='blue'>Div 2</div></div></div>";

    $('#tableData').html(tableData);

}

function getHomeDashboardDetails()
{
    executeGETRequest({ url: "homeDashboardData", callback: getHomeDashboardData });
}

// dashboard data
function getDashboardData(request)
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

                "<td><b>Poll Time</b>:&nbsp;" + this.currentTime[0] + "&nbsp;&nbsp;<i class='bi bi-arrow-repeat' style='cursor:pointer;' title='Poll Now' data-value='"+this.id+", "+this.IP+", "+this.device+"'></i>" +"</td>" +

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

                "<td><b>Poll Time</b>:&nbsp;" + this.currentTime[0] + "&nbsp;&nbsp;<i class='bi bi-arrow-repeat' style='cursor:pointer;' title='Poll Now' data-value='"+this.id+", "+this.IP+", "+this.device+"'></i>" +"</td>" +

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

                "<td class='linux__initial'>" + "<div class='dash__widget'>" + "<div><p>Disk (%)</p></div>" + "<div><b><p>" + this.response[12] + "</p></b></div>" + "</div>" + "</td>" +

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

function showDashboard(id, ip, deviceType)
{
    executeGETRequest({ url: "dashboardTable", data: { id: id, ip: ip, deviceType: deviceType }, callback: getDashboardData });
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

function getPieChartDetails(id, ip, deviceType)
{
    executeGETRequest({ url: "dashboardTable", data: {id: id, ip: ip, deviceType: deviceType }, callback: getPieChartData });
}

// column chart
function getColumnChartData(request)
{
    let data = request.data;

    $.each(data.beanList, function () {

        let secondChart;

        let deviceType = this.device;

        let receivedPacket = this.packet;

        let memoryPercent = this.memory;

        let timeArray = this.currentTime;

        if (deviceType === "Ping")
        {
            secondChart = new CanvasJS.Chart("areaChart",
                {
                    width : 1420,

                    title:{
                        text: "Last 40 Minutes "+ this.IP +" Availability",
                        fontSize: 16,
                    },

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

                                { label: timeArray[0], y: receivedPacket[0] },

                                { label: timeArray[1], y: receivedPacket[1] },

                                { label: timeArray[2], y: receivedPacket[2] },

                                { label: timeArray[3], y: receivedPacket[3] },

                                { label: timeArray[4], y: receivedPacket[4] },

                                { label: timeArray[5], y: receivedPacket[5] },

                                { label: timeArray[6], y: receivedPacket[6] },

                                { label: timeArray[7], y: receivedPacket[7] },
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

                    title:{
                        text: "Last 40 Minutes "+ this.IP +" Availability",
                        fontSize: 16,
                    },

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

                                { label: timeArray[0], y: memoryPercent[0] },

                                { label: timeArray[1], y: memoryPercent[1] },

                                { label: timeArray[2], y: memoryPercent[2] },

                                { label: timeArray[3], y: memoryPercent[3] },

                                { label: timeArray[4], y: memoryPercent[4] },

                                { label: timeArray[5], y: memoryPercent[5] },

                                { label: timeArray[6], y: memoryPercent[6] },

                                { label: timeArray[7], y: memoryPercent[7] },
                            ]

                        }
                    ]

                });
        }

        secondChart.render();

    });

}

function getColumnChartDetails(id, ip, deviceType)
{
    executeGETRequest({ url: "dashboardTable", data: {id: id, ip: ip, deviceType: deviceType }, callback: getColumnChartData });
}