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

// display linux profile fields
function displayLinuxProfile(id, elementValue)
{
    let value = elementValue.value;

    document.getElementById(id).style.display = value === "1" ? 'block' : 'none';
}

// device discover or not
function verifyDiscovery(request)
{
    let flag = true;

    let data = request.data;

    $.each(data.beanList, function () {

        flag = this.flag;

    });

    if (flag)
    {
        // alert("device successfully discovered!");
        $('.toast').toast('show');
    }
    else
    {
        alert("device already added!");
    }

    refreshPage();
}

function discoverData()
{
    let name = $("#name").val();

    let ip = $("#IP").val();

    let discoveryUsername = $("#username").val();

    let discoveryPassword = $("#password").val();

    let deviceType = $("#device").val();

    getGetCall({ url: "discoveryProcess", data: { name: name, ip: ip, discoveryUsername: discoveryUsername, discoveryPassword: discoveryPassword, deviceType: deviceType }, callback: verifyDiscovery });
}

// fetch discovery data from database
function discoveryTable(request)
{
    let tableData = "";

    let tableHead = "";

    let data = request.data;

    tableHead += "<div class='demo' style='margin-top: 10px' id='myTableDiv' id=\"myTable\">" +
        "<table class='disc__table' style='width: 95%'>" +
        "<thead><tr class='disc__heading' style='text-align: left'>" +
        "<th scope='col'>Name</th><th scope='col'>IP/Host</th><th scope='col'>Options</th>" +
        "</tr>" +
        "</thead>" +
        "<tbody id='mainTable'></tbody>" +
        "</table>" +
        "</div>";

    $('#tableData').html(tableHead);

    $.each(data.beanList, function () {

        tableData += "<tr class='disc__data' id='"+this.id+"'>" +

            "<td>" + this.name + "</td>" +

            "<td>" + this.IP + "</td>" +

            "<td>" +

            "<i class='bi bi-play disc__discovery' title='Discovery' data-value='"+this.id+", "+this.IP+", "+this.device+"'></i> &nbsp;" +

            "<i class='bi bi-eye disc__monitor' title='View Result' data-value='"+this.id+"'></i> &nbsp;" +

            "<i class='bi bi-pencil-square disc__edit' title='Edit' data-value='"+this.id+", "+this.IP+", "+this.device+"'></i> &nbsp;" +

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

    $('.bi-pencil-square').on('click', function () {

        let parameter = event.currentTarget.getAttribute('data-value');

        let array = parameter.split(",");

        editIPAddress(array[0], array[1].trim(), array[2].trim());
    });

    $('.bi-trash').on('click', function () {

        let parameter = event.currentTarget.getAttribute('data-value');

        executeDeleteDiscoveryRow(parameter);

    });
}

function getDiscoveryDetails()
{
    let provisionForm = "provisionForm";

    getGetCall({url: "discoveryTable", callback: discoveryTable});
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

// edit discover data fields
function fetchDeviceData(request)
{
    if (request != undefined && request.data != undefined)
    {
        let requestData = request.data;

        $('#editID').val(requestData.id);

        $('#editName').val(requestData.beanList[0].name);

        $('#editIP').val(requestData.ip);

        $('#editDeviceType').val(requestData.deviceType);

        if (requestData.deviceType == "Ping")
        {
            $('#editProfile').css({display: 'none'});
        }
        else
        {
            $('#editUsername').val(requestData.beanList[0].username);

            $('#editPassword').val(requestData.beanList[0].password);
        }

        $("#myModal").modal('show');
    }
}

function editIPAddress(id, ip, deviceType)
{
    getGetCall({ url: "fetchDeviceData", data: { id: id, ip: ip, deviceType: deviceType }, callback: fetchDeviceData });
}

function updateDevice(request)
{
    if (request != undefined && request.data != undefined)
    {
        $('#myModal').modal('hide');

        if (request.data.beanList[0].flag)
        {
            alert("successfully updated!");
        }
        else
        {
            alert("successfully not updated!");
        }
    }

    refreshPage();
}

function updateDiscoveryData()
{
    let id = $('#editID').val();

    let name = $('#editName').val();

    let ip = $('#editIP').val();

    let deviceType = $('#editDeviceType').val();

    let discoveryUsername = $('#editUsername').val();

    let discoveryPassword = $('#editPassword').val();

    getGetCall({ url: "updateDevice", data: { id: id, name: name, ip: ip, deviceType: deviceType, discoveryUsername: discoveryUsername, discoveryPassword: discoveryPassword }, callback: updateDevice })
}

// delete discovery table row
function deletedDiscoveryRow(result)
{
    document.getElementById(result.data.id).remove();
}

function executeDeleteDiscoveryRow(id)
{
    getPostCall({ url: "discoverDelete", data: { id: id }, callback: deletedDiscoveryRow });
}