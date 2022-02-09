// refresh page

function refreshPage() {

    location.reload(true);
}

function openForm(idName) {

    document.getElementById(idName).style.display = 'block';

}

function closeForm(idName) {

    document.getElementById(idName).style.display = 'none';

    // document.getElementById("discoveryPage").style.opacity = 1;

    if (idName == 'provisionForm')
    {
        $("#resultTable").val("");
    }
}

$(document).ready(function() {

    $('.clickable').click(function () {

        $('#newData').load(window.location.href);

    })
    $('.change').click(function () {

        $('#mainTable').load(window.location.href);

    })
});


function getDiscoveryDetails() {

    var provisionForm = "provisionForm";

    $.ajax({

        type : "POST",

        cache : false,

        timeout : 180000,

        async : true,

        url : "discoveryTable.action",

        success : function (data) {

            var tableData = "";

            $.each(data.beanList, function () {

                var id = this.id;

                var IP = this.IP;

                var device = this.device;

                console.log(id);

                tableData += "<tr class='disc__data' id='"+this.id+"'>" +

                                "<td>" + this.name + "</td>" +

                                "<td>" + this.IP + "</td>" +

                                "<td>" +

                                         "<i class='bi bi-play disc__discovery' title='Discovery' data-value='"+this.IP+", "+this.device+"'></i> &nbsp;" +

                                         "<i class='bi bi-eye disc__monitor' title='View Result' data-value='"+this.id+"'></i> &nbsp;" +

                                         // "<button type='button' class='clickable' data-value='"+this.id+"'><i class='bi bi-eye disc__monitor' title='View Result'></i></button> &nbsp;" +

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
        },
        
        error : function (data) {

            alert("some error occurred!")
        }
    });
}

// show discovery form

function discoverData() {

    var name = $("#name").val();

    var ip = $("#IP").val();

    var discoveryUsername = $("#username").val();

    var discoveryPassword = $("#password").val();

    var deviceType = $("#device").val();

        $.ajax({

            type : "POST",

            cache : false,

            timeout : 180000,

            async : true,

            url : "discoveryProcess.action",

            data : "name=" + name + "&ip=" + ip + "&discoveryUsername=" + discoveryUsername	+ "&discoveryPassword=" + discoveryPassword + "&deviceType=" + deviceType,

            success : function (data) {

                alert("successfully discovered!");
            },
            error : function () {

                alert("something went wrong!");
            }
        });


    refreshPage();
}

function displayLinuxProfile(idName, elementValue) {

    document.getElementById(idName).style.display = elementValue.value == 1 ? 'block' : 'none';
}

// for rediscovery

function reDiscoverData(ip, deviceType) {

    $.ajax({

        type : "POST",

        cache : false,

        timeout : 180000,

        async : true,

        url : "reDiscoveryProcess.action",

        data : "ip=" + ip + "&deviceType=" + deviceType,

        success : function (data) {

            alert("re discovery successfully completed!");
        },
        error : function () {

            alert("discovery not completed!");
        }
    });

    refreshPage();
}

function getPolling(id, ip, deviceType) {

    $.ajax({

        type : "POST",

        cache : false,

        timeout : 180000,

        async : true,

        url : "monitorPolling.action",

        data : "id=" + id + "&ip=" + ip + "&deviceType=" + deviceType,

        success : function (data) {

            if (deviceType == 'Ping')
            {
                window.location.href = "dashboard.jsp";
            }
            else
            {
                window.location.href = "linux_dashboard.jsp";
            }
        },
        error : function () {

            alert("polling not completed!");
        }
    });
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

// delete discovery table data

function deleteRow(idAttribute) {

    $.ajax({

        type : "POST",

        cache : false,

        timeout : 180000,

        async : true,

        url : "discoveryDelete.action",

        data : "idAttribute=" + idAttribute,

        success : function (data) {

            alert("successfully row deleted!");
        },
        error : function () {

            alert("still row not deleted!");
        }
    });

    document.getElementById(idAttribute).remove();
}


// show monitor form

function showForm(id) {

    var idName1 = $('div.disc__popUp').attr('id');

    document.getElementById(idName1).style.display = 'block';

    $.ajax({

        type : "POST",

        cache : false,

        timeout : 180000,

        async : true,

        url : "monitorProcess.action",

        data : "id=" + id,

        success : function (data) {

            alert("loading data...")
        },
        error : function () {

            console.log("not worked properly!");
        }
    });

    refreshPage();
}

// provision ip

function monitorData(idName) {

    if (document.getElementById(idName).checked)
    {
        var key = $("input[name=key]").val();

        $.ajax({

            type : "POST",

            cache : false,

            timeout : 180000,

            async : true,

            url : "monitorData.action",

            data : "id=" + key,

            success : function (data) {

                alert("successfully monitored!")

            },
            error : function () {

                alert("something went wrong!");
            }
        });
    }
    else
    {
        alert("must be checked checkbox!");
    }
}

// show dashboard


function showDashboard(id, ip, deviceType) {

    $.ajax({

        type : "POST",

        cache : false,

        timeout : 180000,

        async : true,

        url : "dashboardProcess.action",

        data : "id=" + id + "&ip=" + ip + "&deviceType=" + deviceType,

        success : function (data) {

            console.log("worked properly!");

            if (deviceType == 'Ping')
            {
                window.location.href = "dashboard.jsp";
            }
            else
            {
                window.location.href = "linux_dashboard.jsp";
            }

            getDashboardDetails(id, ip, deviceType);

        },
        error : function (data) {

            console.log("not worked properly!");
        }
    });
}

function getDashboardBody() {

    $.ajax({

        type : "POST",

        cache : false,

        async : true,

        timeout : 180000,

        url : "dashboardTable.action",

        success : function (data) {

             var tableData = "";

             $.each(data.beanList, function () {

                 var response = this.response;

                 var sentPacket = response.substring(response.indexOf("statistics") + 14, response.indexOf("statistics") + 15);

                 var packetLoss = getPacketLoss(response);

                 var receivedPacket = response.substring(response.indexOf("transmitted") + 13, response.indexOf("transmitted") + 14);

                 var rttTime = getRTTTime(response);

                 console.log(response);

                 tableData += "<tr>" +

                                    // "<td class='dash__firstRow'>" + "<div id='dougnutChart' style='height: 270px'>" + + "</div>" + "</td>" +

                                    "<td class='dash__secondRow'>" + "<div class='dash__widget'>" + "<div>" + "<p>Sent Packet</p>" + "</div>" + "<div>" + "<p class='dash__response'>" + sentPacket + "</p>" + "</div>" + "</div>" + "</td>" +

                                    "<td class='dash__thirdRow'>" + "<div class='dash__widget'>" + "<div>" + "<p>Packet Loss (%)</p>" + "</div>" + "<div>" + "<p class='dash__response'>" + packetLoss + "</p>" + "</div>" + "</div>" + "</td>" +

                                    "<td class='dash__fourthRow'>" + "<div class='dash__widget'>" + "<div>" + "<p>Received Packet</p>" + "</div>" + "<div>" + "<p class='dash__response'>" + receivedPacket + "</p>" + "</div>" + "</div>" + "</td>" +

                                    "<td class='dash__fifthRow'>" + "<div class='dash__widget'>" + "<div>" + "<p>RTT (ms)</p>" + "</div>" + "<div>" + "<p class='dash__response'>" + rttTime + "</p>" + "</div>" + "</div>" + "</td>" +


                            + "</tr>";

             });

             $('#dashboardTableBody').html(tableData);

        },
        error : function (data) {

            alert("error");
        }
    });
}

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

function getDashboardHeader() {

    $.ajax({

        type : "POST",

        cache : false,

        async : true,

        timeout : 180000,

        url : "dashboardTable.action",

        success : function(data) {

            var tableData = "";

            $.each(data.beanList, function () {

                tableData += "<tr>" +

                                 "<td><b>IP/Host</b>:&nbsp;" + this.IP + "&nbsp;&nbsp;<i class='bi bi-activity' style='color: #2a92ff;'></i></td>" +

                                 "<td><b>Profile</b>:&nbsp;" + this.username + "</td>" +

                                 "<td><b>Poll Time</b>:&nbsp;" + this.currentTime + "&nbsp;&nbsp;<i class='bi bi-arrow-repeat' style='cursor:pointer;' title='Poll Now' data-value='"+this.id+", "+this.IP+", "+this.device+"'></i>" +"</td>" +

                           + "</tr>" +

                            "<tr>" +

                                 "<td><b>Id</b>:" + this.id + "</td>" +

                                 "<td><b>Status</b>:" + this.status + "</td>" +

                                 "<td><b>DeviceType</b>:" + this.device + "</td>" +

                          + "</tr>";

            });

            $('#dashboardHeader').html(tableData);

            $('.bi-arrow-repeat').on('click', function () {

                var parameter = event.currentTarget.getAttribute('data-value');

                console.log(parameter);

                var array = parameter.split(",");

                getPolling(array[0], array[1].trim(), array[2].trim());

                alert("polling data...");

            });
        }

    });
}

function getMonitorDetails() {

    $.ajax({

        type : "POST",

        cache : false,

        async : true,

        url : "monitorProcess.action",

        success : function (data) {

            var tableData = "";

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

                // getDashboardDetails(array[0], array[1].trim(), array[2].trim());

                alert("loading dashboard data...");

            });
        }
    })
}
