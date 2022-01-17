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

function reDiscoverData(ip) {

    $.ajax({

        type : "POST",

        cache : false,

        timeout : 180000,

        async : true,

        url : "discoveryProcess.action",

        data : "ip=" + ip,

        success : function (data) {

            console.log("worked properly!");
        },
        error : function () {

            console.log("not worked properly!");
        }
    });
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

            console.log(data);

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

function showDashboard(idName) {

    $.ajax({

        type : "POST",

        cache : false,

        timeout : 180000,

        async : true,

        url : "monitorProcess.action",

        data : "idName=" + idName,

        success : function (data) {

            console.log("worked properly!");
        },
        error : function () {
            console.log("not worked properly!");
        }
    });
}


// create pie chart

anychart.onDocumentReady(function () {

    // create custom data for pie chart
    var data = [
        {x: "white", value: 223553265},
    ];

    // create a chart
    var chart = anychart.pie();

    // set title to pie chart and set radius
    chart.title("127.0.0.1").innerRadius('40%');

    // set theme to pie chart
    anychart.theme('lightBlue');

    // set label position
    chart.labels().position('outside');

    // set data to chart
    chart.data(data);

    // initialize div id to chart
    chart.container('pieChart');

    // finally draw chart
    chart.draw();

});

// create time series chart

anychart.onDocumentReady(function () {

    // create custom data for time chart
    var data = [
        {x: "white", value: 223553265},
    ];

    // create a chart
    chart = anychart.column();

    chart.title("127.0.0.1");

    // create a column series and set the data
    var series = chart.column(data);

    // set the container id
    chart.container("timeChart");

    // initiate drawing the chart
    chart.draw();

});




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

function addClassByClick() {

    $('.in').addClass('active');
}