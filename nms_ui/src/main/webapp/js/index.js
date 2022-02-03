// refresh page

function refreshPage() {

    location.reload(true);
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

        $('#mainTable').load(window.location.href);

    })
});

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

    refreshPage();
}

function getPolling(id, ip, device) {

    $.ajax({

        type : "POST",

        cache : false,

        timeout : 180000,

        async : true,

        url : "monitorPolling.action",

        data : "id=" + id + "&ip=" + ip,

        success : function (data) {

            if (device == 'Ping')
            {
                window.location.href = "dashboard.jsp";
            }
            else
            {
                window.location.href = "linux_dashboard.jsp";
            }
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

            alert("successfully row deleted!");
        },
        error : function () {

            alert("still row not deleted!");
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

// show dashboard

function showDashboard(id, ip, deviceType) {

    $.ajax({

        type : "POST",

        cache : false,

        timeout : 180000,

        async : true,

        url : "dashboardProcess.action",

        data : "id=" + id + "&ip=" + ip,

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

        },
        error : function (data) {

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
