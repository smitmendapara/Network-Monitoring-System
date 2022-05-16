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
    document.getElementById(id).style.display = elementValue.value === "1" ? 'block' : 'none';
}

// device discover or not
function verifyDiscovery(request)
{
    if (request !== undefined && request.data !== undefined)
    {
        if (request.data.flag)
        {
            toastr.success('Device Added in Queue!');
        }
        else
        {
            toastr.error('Device Not Added in Queue!');
        }
    }
    else
    {
        toastr.error("Discovery verification response undefined!");
    }
}

// add device
function addDevice(request)
{
    if (request !== undefined && request.data !== undefined)
    {
        if (request.data.ipValid)
        {
            setTimeout(refreshPage, 1000);

            if (request.data.flag)
            {
                toastr.success("Device Successfully Added!");
            }
            else
            {
                toastr.error("Device Not Added!");
            }
        }
        else
        {
            toastr.error("IP not Valid!");

            $('#IP').val('');
        }
    }
    else
    {
        toastr.error("Add device request data undefined!");
    }

}

function addData()
{
    let serializeParams = $('.form-popUp form').serialize();

    executePOSTRequest({ url: "discoveryProcess", data: serializeParams, callback: addDevice });
}

function discoverData(id, ip, deviceType)
{
    executePOSTRequest({ url: "discoverData", data: { id: id, ip: ip, deviceType: deviceType }, callback: verifyDiscovery });
}

function getWebSocket()
{
    let requestURL = $(location).attr('href');

    let splitURL = requestURL.split("/");

    let webSocket = new WebSocket("wss://" + splitURL[2] + "/serverEndPoint");

    webSocket.onopen = function (message) {
        messageOnOpen("Web Socket Open " + message);
    };

    webSocket.onmessage = function (message) {
        messageOnMessage(message);
    };

    webSocket.onclose = function (message) {
        messageOnClose(message);
    };

    webSocket.onerror = function (message) {
        messageOnError(message);
    };

    function messageOnOpen(message)
    {
        toastr.info("Frontend Web Socket Started...");
    }
    
    function messageOnMessage(message)
    {
        toastr.info(message.data);
    }

    function messageOnClose(message)
    {
        toastr.info("Frontend Web Socket Closed...");

        getWebSocket();
    }

    function messageOnError(message)
    {
        toastr.info("Web Socket Error : " + message.data)
    }
}

// fetch discovery data from database
function discoveryTable(request)
{
    getWebSocket();

    if (request !== undefined && request.data !== undefined)
    {
        let tableData = "";

        let tableHead = "";

        let data = request.data;

        tableHead += "<div class='demo' style='margin-top: 10px' id='myTableDiv' id=\"myTable\">" +
            "<table class='disc__table' style='width: 95%'>" +
            "<thead><tr class='disc__heading' style='text-align: left'>" +
            "<th scope='col'>Name</th><th scope='col'>IP</th><th scope='col'>Device Type</th><th scope='col'>Options</th>" +
            "</tr>" +
            "</thead>" +
            "<tbody id='mainTable'></tbody>" +
            "</table>" +
            "</div>";

        $('#tableData').html(tableHead);

        $.each(data.beanList, function () {

            tableData += "<tr class='disc__data' id='" + this.id + "'>" +

                "<td>" + this.name + "</td>" +

                "<td>" + this.ip + "</td>" +

                "<td>" + this.deviceType + "</td>" +

                "<td>" +

                "<i class='bi bi-play disc__discovery' title='Discovery' data-value='" + this.id + ", "+this.ip +", "+this.deviceType+"'></i> &nbsp;" +

                "<i class='bi bi-eye disc__monitor' title='View Result' data-value='"+this.id+"'></i> &nbsp;" +

                "<i class='bi bi-pencil-square disc__edit' title='Edit' data-value='" + this.id + ", " + this.ip + ", " + this.deviceType + "'></i> &nbsp;" +

                "<i class='bi bi-trash disc__delete' title='Delete' data-value='" + this.id + "'></i> &nbsp;" +

                "</td>" +

                "</tr>"
        });

        $('#mainTable').html(tableData);

        $('.bi-play').on('click', function () {

            let parameter = event.currentTarget.getAttribute('data-value');

            let array = parameter.split(",");

            discoverData(array[0], array[1].trim(), array[2].trim());

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
    else
    {
        toastr.error("Discover data undefined!");
    }
}

function getDiscoveryDetails()
{
    executeGETRequest({url: "discoveryTable", callback: discoveryTable} );
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
    if (request !== undefined && request.data !== undefined)
    {
        let requestData = request.data;

        $('#editID').val(requestData.id);

        $('#editName').val(requestData.name);

        $('#editIP').val(requestData.ip);

        $('#editDeviceType').val(requestData.deviceType);

        if (requestData.deviceType === "Ping")
        {
            $('#editProfile').css({display: 'none'});
        }
        else
        {
            $('#editProfile').css({display: 'block'});

            $('#editUsername').val(requestData.discoveryUsername);

            $('#editPassword').val(requestData.discoveryPassword);
        }

        $("#myModal").modal('show');
    }
}

function editIPAddress(id, ip, deviceType)
{
    executePOSTRequest({ url: "fetchDeviceData", data: { id: id, ip: ip, deviceType: deviceType }, callback: fetchDeviceData });
}

function updateDevice(request)
{
    if (request !== undefined && request.data !== undefined)
    {
        $('#myModal').modal('hide');

        setTimeout(refreshPage, 1500);

        if (request.data.flag)
        {
            toastr.success("Device Successfully Updated!");
        }
        else
        {
            toastr.error("Device Already Exist!");
        }
    }
    else
    {
        toastr.error("Device updated data undefined!");
    }
}

function updateDiscoveryData()
{
    let serializeParams = $('.discoverEditForm form').serialize() + "&deviceType=" + $('#editDeviceType').val();

    executePOSTRequest({ url: "updateDevice", data: serializeParams, callback: updateDevice })
}

// delete discovery table row
function deletedDiscoveryRow(request)
{
    if (request !== undefined && request.data !== undefined)
    {
        $('#' + request.data.id).remove();

        toastr.success("Device Successfully Deleted!");

        setTimeout(refreshPage, 1000);
    }
    else
    {
        toastr.error("Delete response data undefined!");
    }
}

function deleteDiscoveryData(id)
{
    executePOSTRequest({ url: "discoverDelete", data: { id: id }, callback: deletedDiscoveryRow });

    $('#deleteModal').modal('hide');
}

function executeDeleteDiscoveryRow(id)
{
    $('#deleteModal').modal();

    $('#deleteButton').html('<a class="btn btn-danger" onclick="deleteDiscoveryData('+id+')">Delete</a>');
}