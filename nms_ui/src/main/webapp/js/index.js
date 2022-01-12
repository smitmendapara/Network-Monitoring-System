/**
 * Created by smit on 4/1/22.
 */

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

        asnc : true,

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

function openForm(idName) {

    document.getElementById(idName).style.display = 'block';
}

function closeForm(idName) {

    document.getElementById(idName).style.display = 'none';

    if (idName == 'provisionForm')
    {
        $("#resultTable").val("");
    }
}

function monitorData() {

    if ('input[type="checkbox"]')
    {
        
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
