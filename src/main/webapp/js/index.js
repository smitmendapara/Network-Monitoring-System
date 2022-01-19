// refresh page

function refreshPage() {

    window.location.reload();
}

function openForm(idName) {

    // document.getElementById("discoveryPage").style.opacity = 0.2;

    document.getElementById(idName).style.display = 'block';

    // setTimeout(refreshPage(), 1000);
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
        $('#mainTable').load(window.location.href)
    })
});

// create pie chart

// Load google charts
google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);

// Draw the chart and set the chart values
function drawChart() {
    var data = google.visualization.arrayToDataTable([
        ['Task', 'Hours per Day'],
        ['Work', 8],
        ['Eat', 2],
        ['TV', 4],
        ['Gym', 2],
        ['Sleep', 8]
    ]);

    // Optional; add a title and set the width and height of the chart
    var options = {'title':'My Average Day', 'width':550, 'height':400};

    // Display the chart inside the <div> element with id="piechart"
    var chart = new google.visualization.PieChart(document.getElementById('pieChart'));
    chart.draw(data, options);
}

// create time series chart

google.charts.load('current', {packages: ['corechart', 'line']});
google.charts.setOnLoadCallback(drawBasic);

function drawBasic() {

    var data = new google.visualization.DataTable();
    data.addColumn('number', 'X');
    data.addColumn('number', 'Dogs');

    data.addRows([
        [0, 0],   [1, 10],  [2, 23],  [3, 17],  [4, 18],  [5, 9],
        [6, 11],  [7, 27],  [8, 33],  [9, 40],  [10, 32], [11, 35],
        [12, 30], [13, 40], [14, 42], [15, 47], [16, 44], [17, 48],
        [18, 52], [19, 54], [20, 42], [21, 55], [22, 56], [23, 57],
        [24, 60], [25, 50], [26, 52], [27, 51], [28, 49], [29, 53],
        [30, 55], [31, 60], [32, 61], [33, 59], [34, 62], [35, 65],
        [36, 62], [37, 58], [38, 55], [39, 61], [40, 64], [41, 65],
        [42, 63], [43, 66], [44, 67], [45, 69], [46, 69], [47, 70],
        [48, 72], [49, 68], [50, 66], [51, 65], [52, 67], [53, 70],
        [54, 71], [55, 72], [56, 73], [57, 75], [58, 70], [59, 68],
        [60, 64], [61, 60], [62, 65], [63, 67], [64, 68], [65, 69],
        [66, 70], [67, 72], [68, 75], [69, 80]
    ]);

    var options = {
        hAxis: {
            title: 'Time'
        },
        vAxis: {
            title: 'Popularity'
        }
    };

    var chart = new google.visualization.LineChart(document.getElementById('timeChart'));

    chart.draw(data, options);
}


// show discovery form

function discoverData() {

    var name = $("#name").val();

    var ip = $("#ip").val();

    var discoveryUsername = $("#discoveryUsername").val();

    var discoveryPassword = $("#discoveryPassword").val();

    var deviceType = $("#deviceType").val();

    $.ajax({

        type : "POST",

        cache : false,

        timeout : 180000,

        async : true,

        url : "discoveryProcess.action",

        data : "name=" + name + "&ip=" + ip + "&discoveryUsername=" + discoveryUsername	+ "&discoveryPassword=" + discoveryPassword + "&deviceType=" + deviceType,

        success : function(data) {

            alert("successfully discovered");

        },
        error : function() {

            alert("discovery failed!")
        }
    });

    refreshPage();

    $("#name").val("");

    $("#ip").val("");

    $("#discoveryUsername").val("");

    $("#discoveryPassword").val("");
}

function displayLinuxProfile(idName, elementValue) {

    document.getElementById(idName).style.display = elementValue.value == 1 ? 'block' : 'none';
}

// for re discovery

function reDiscoverData(ip) {

    $.ajax({

        type : "POST",

        cache : false,

        timeout : 180000,

        async : true,

        url : "reDiscoveryProcess.action",

        data : "ip=" + ip,

        success : function (data) {

            alert("discovery successfully completed!");
        },
        error : function () {

            alert("discovery not completed!");
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

            console.log("successfully row deleted!");
        },
        error : function () {

            console.log("still row not deleted!");
        }
    });

    document.getElementById(idAttribute).remove();
}


// show monitor form

function showForm(idName1, id) {

    document.getElementById(idName1).style.display = 'block';

    $.ajax({

        type : "POST",

        cache : false,

        timeout : 180000,

        async : true,

        url : "monitorProcess.action",

        data : "id=" + id,

        success : function (data) {

            console.log("worked properly!");
        },
        error : function () {

            console.log("not worked properly!");
        }
    });

    refreshPage();
}

// show monitor table data

function monitorData(idName) {

    if (document.getElementById(idName).checked)
    {
        // var hideId = (this).closest('tr').attr('td');

        var key = $("input[name=key]").val();

        $.ajax({

            type : "POST",

            cache : false,

            timeout : 180000,

            async : true,

            url : "monitorData.action",

            data : "id=" + key,

            success : function (data) {

                alert("successfully monitored!");
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

function showDashboard(id, ip) {

    $.ajax({

        type : "POST",

        cache : false,

        timeout : 180000,

        async : true,

        url : "dashboardProcess.action",

        data : "id=" + id + "&ip=" + ip,

        success : function (data) {

            console.log("worked properly!");
        },
        error : function () {

            console.log("not worked properly!");
        }
    });
}






// currently unused or not working

function startLoader() {

    var myVar;

    document.getElementById("myForm").style.opacity = 0.2;

    document.getElementById("loader").style.display = 'block';

    myVar = setTimeout(showMessage, 1000);
}

function showMessage() {

    document.getElementById("loader").style.display = 'none';

    document.getElementById("myForm").style.opacity = 1;

    alert("Discovery is successfully done!");
}

$(document).ready(function() {
    $('#new').click(function() {
        $('#discoveryForm').fadeIn(6000);
    });
});

// function addClassByClick() {
//
//     $('.in').addClass('active');
// }