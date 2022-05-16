// show monitor form
function getMonitorForm(request)
{
    if (request !== undefined && request.data !== undefined)
    {
        let tableData = "";

        let data = request.data;

        tableData = "<tr class='disc__data'>" +

            "<td><input type='hidden' class='one' name='key' id='key' value='"+data.id+"'>" + data.id + "</td>" +

            "<td><input type='checkbox' id='check' checked></td>" +

            "<td>" + data.ip + "</td>" +

            "<td>" + "0" + "</td>" +

            "<td>" + data.discoveryUsername + "</td>" +

            "<td>" + data.deviceType + "</td>" +

            + "</tr>";

        $('#resultTable').html(tableData);
    }
    else
    {
        toastr.error("Monitor form data undefined!");
    }
}

function showForm(id)
{
    monitorFormId = $('div.disc__popUp').attr('id');

    if ($('#' + discoverFormId).css({display: 'block'}))
    {
        $('#' + discoverFormId).css({display: 'none'});
    }

    $('#' + monitorFormId).css({display: "block"});

    executePOSTRequest({ url: "monitorForm", data: { id: id }, callback: getMonitorForm });
}

// provision ip
function provisionIP(request)
{
    if (request !== undefined && request.data !== undefined)
    {
        if (request.data.flag)
        {
            toastr.success("Device Successfully Monitored!");
        }
        else
        {
            toastr.error("Device Already Added!");
        }
    }
    else
    {
        toastr.error("Provision data response undefined!");
    }

    $('#' + monitorFormId).css({display: "none"});

}

function monitorData(id)
{
    if (document.getElementById(id).checked)
    {
        let id = $("input[name=key]").val();

        executePOSTRequest({ url: "monitorProcess", data: { id: id }, callback: provisionIP });
    }
    else
    {
        toastr.warning("Checkbox must be Checked!");
    }
}

// show monitor table data
function getMonitorTable(request)
{
    if (request !== undefined && request.data !== undefined)
    {
        let tableData = "";

        let tableHead = "";

        let data = request.data;

        tableHead += "<div class='demo' style='margin-top: 10px'>" +
            "<table class='disc__table' style='width: 95%'>" +
            "<thead><tr class='disc__heading' style='text-align: left'>" +
            "<th scope='col'><input type='checkbox'></th><th scope='col'>ID</th><th scope='col'>IP</th><th scope='col'>Device Type</th><th scope='col'>Status</th><th scope='col'>Options</th>" +
            "</tr>" +
            "</thead>" +
            "<tbody id='monitorTable'></tbody>" +
            "</table>" +
            "</div>";

        $('#tableData').html(tableHead);

        $.each(data.beanList, function () {

            tableData += "<tr>" +

                "<td><input type='checkbox'></td>" +

                "<td>" + this.id + "</td>" +

                "<td>" + this.ip + "</td>" +

                "<td>" + this.deviceType + "</td>" +

                "<td>" + this.status + "</td>" +

                "<td>" + "&nbsp;&nbsp;&nbsp;&nbsp;<i class='bi bi-box-arrow-up-right monitor__icon' data-value='"+this.id+", "+this.ip+", "+this.deviceType+"' style='cursor: pointer'></i>" + "&nbsp;<i class='bi bi-trash disc__delete' title='Delete' data-value='"+this.id+"' style='cursor: pointer'></i> &nbsp;" + "</td>" +

                "</tr>";

        });

        $('#monitorTable').html(tableData);

        $('.bi-box-arrow-up-right').on('click', function() {

            let parameter = event.currentTarget.getAttribute('data-value');

            let array = parameter.split(",");

            showDashboard(array[0], array[1].trim(), array[2].trim());

        });

        $('.bi-trash').on('click', function () {

            let parameter = event.currentTarget.getAttribute('data-value');

            executeDeleteMonitorRow(parameter);

        });
    }
    else
    {
        toastr.error("Monitor data undefined!");
    }
}

function getMonitorDetails()
{
    if ($('#' + monitorFormId).css({display: 'block'}))
    {
        $('#' + monitorFormId).css({display: 'none'});
    }

    if ($('#' + discoverFormId).css({display: 'block'}))
    {
        $('#' + discoverFormId).css({display: 'none'});
    }

    executeGETRequest({ url: "monitorTable", callback: getMonitorTable });
}

function deleteMonitorRow(request)
{
    if (request !== undefined && request.data !== undefined)
    {
        if (request.data.flag)
        {
            $('#' + request.data.id).remove();

            toastr.success("Device Successfully Deleted!");
        }
        else
        {
            toastr.error("Device not Deleted!");
        }

        setTimeout(refreshPage, 1000);
    }
    else
    {
        toastr.error("Delete request not executed!");
    }
}

function deleteMonitorData(id)
{
    executePOSTRequest({ url: "monitorDelete", data: { id: id }, callback: deleteMonitorRow });

    $('#deleteModal').modal('hide');
}

function executeDeleteMonitorRow(id)
{
    $('#deleteModal').modal();

    $('#deleteButton').html('<a class="btn btn-danger" onclick="deleteMonitorData('+id+')">Delete</a>');
}
