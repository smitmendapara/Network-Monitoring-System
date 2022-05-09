let monitorFormId;

let discoverFormId;

// refresh page
function refreshPage()
{
    location.reload(true);
}

// post request
function executePOSTRequest(request)
{
    $.ajax({

        type: "POST",

        cache: false,

        timeout: 180000,

        data: request.data,

        url: request.url,

        success: function (data) {

            let myCallback;

            if (request.callback !== undefined)
            {
                myCallback = $.Callbacks();

                myCallback.add(request.callback);

                myCallback.fire(request);

                myCallback.remove(request.callback);
            }

        },
        error: function () {

            alert("some error occurred!");
        }

    });
}

// get request
function executeGETRequest(request)
{
    $.ajax({

        type : "GET",

        cache : false,

        timeout : 180000,

        data: request.data,

        url : request.url,

        success : function (data) {

            let myCallback;

            if (request.callback !== undefined)
            {
               myCallback = $.Callbacks();

               myCallback.add(request.callback);

               request.data = data;

               myCallback.fire(request);

               myCallback.remove(request.callback);
            }
        },
        error : function () {

            alert("some error occurred!");
        }
    });
}