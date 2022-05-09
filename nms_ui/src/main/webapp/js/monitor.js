// show monitor form
function getMonitorForm(request)
{
    let tableData = "";

    let data = request.data;

    $.each(data.beanList, function () {

        tableData = "<tr class='disc__data'>" +

            "<td><input type='hidden' class='one' name='key' id='key' value='"+this.id+"'>" + this.id + "</td>" +

            "<td><input type='checkbox' id='check' checked></td>" +

            "<td>" + this.IP + "</td>" +

            "<td>" + "0" + "</td>" +

            "<td>" + this.username + "</td>" +

            "<td>" + this.device + "</td>" +

            + "</tr>";

    });

    $('#resultTable').html(tableData);
}

function showForm(id)
{
    monitorFormId = $('div.disc__popUp').attr('id');

    if ($('#' + discoverFormId).css({display: 'block'}))
    {
        $('#' + discoverFormId).css({display: 'none'});
    }

    $('#' + monitorFormId).css({display: "block"});

    executeGETRequest({ url: "monitorForm", data: { id: id }, callback: getMonitorForm });
}

// provision ip
function provisionIP(request)
{
    let flag = true;

    let data = request.data;

    $.each(data.beanList, function () {

        flag = this.flag;

    });

    if (flag)
    {
        toastr.success("Device Successfully Monitored!");
    }
    else
    {
        toastr.warning("Device Already Added!");
    }

    $('#' + monitorFormId).css({display: "none"});

}

function monitorData(id)
{
    if (document.getElementById(id).checked)
    {
        let id = $("input[name=key]").val();

        executeGETRequest({ url: "monitorProcess", data: { id: id }, callback: provisionIP });
    }
    else
    {
        toastr.warning("Checkbox must be Checked!");
    }
}

// show monitor table data
function getMonitorTable(result)
{
    let tableData = "";

    let tableHead = "";

    let data = result.data;

    tableHead += "<div class='demo' style='margin-top: 10px'>" +
        "<table class='disc__table' style='width: 95%'>" +
        "<thead><tr class='disc__heading' style='text-align: left'>" +
        "<th scope='col'><input type='checkbox'></th><th scope='col'>ID</th><th scope='col'>IP/Host</th><th scope='col'>Type</th><th scope='col'>Status</th><th scope='col'>Options</th>" +
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

            "<td>" + this.IP + "</td>" +

            "<td>" + this.device + "</td>" +

            "<td>" + this.status + "</td>" +

            "<td>" + "&nbsp;&nbsp;&nbsp;&nbsp;<i class='bi bi-box-arrow-up-right monitor__icon' data-value='"+this.id+", "+this.IP+", "+this.device+"' style='cursor: pointer'></i>" + "&nbsp;<i class='bi bi-trash disc__delete' title='Delete' data-value='"+this.id+"' style='cursor: pointer'></i> &nbsp;" + "</td>" +

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

function getMonitorDetails()
{
    executeGETRequest({ url: "monitorTable", callback: getMonitorTable });
}

function deleteMonitorRow(request)
{
    $('#' + request.data.id).remove();

    refreshPage();
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
