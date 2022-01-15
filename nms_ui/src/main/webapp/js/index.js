/**
 * Created by smit on 4/1/22.
 */

anychart.onDocumentReady(function () {

    // create custom data for pie chart
    var data = [
        {x: "white", value: 223553265},
        {x: "black", value: 38929319},
        {x: "blue", value: 2932248},
        {x: "green", value: 14674252},
        {x: "grey", value: 540013},
        {x: "red", value: 19107368},
        {x: "pink", value: 9009073}
    ];

    // create a chart
    var chart = anychart.pie();

    // set title to pie chart and set radius
    chart.title("my chart!").innerRadius('40%');

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


anychart.onDocumentReady(function () {

    // create custom data for time chart
    var data = [
        {x: "white", value: 223553265},
        {x: "black", value: 38929319},
        {x: "blue", value: 2932248},
        {x: "green", value: 14674252},
        {x: "grey", value: 540013},
        {x: "red", value: 19107368},
        {x: "pink", value: 9009073}
    ];

    // create a chart
    chart = anychart.column();

    // create a column series and set the data
    var series = chart.column(data);

    // set the container id
    chart.container("timeChart");

    // initiate drawing the chart
    chart.draw();

});


$(document).ready(function() {
    $('#new').click(function() {
        $('#discoveryForm').fadeIn(6000);
    });
});

function openForm(idName) {

    // document.getElementById("discoveryPage").style.opacity = 0.2;

    document.getElementById(idName).style.display = 'block';

    // setTimeout(refreshPage(), 1000);
}

function addClassByClick() {

    $('.in').addClass('active');
}

function closeForm(idName) {

    document.getElementById(idName).style.display = 'none';

    // document.getElementById("discoveryPage").style.opacity = 1;

    if (idName == 'provisionForm')
    {
        $("#resultTable").val("");
    }
}


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

    setTimeout(refreshPage(), 1000);
}

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

function refreshPage() {

    window.location.reload();
}

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

function displayLinuxProfile(idName, elementValue) {

    document.getElementById(idName).style.display = elementValue.value == 1 ? 'block' : 'none';
}

// --------------------------- loader ----------------------------

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
