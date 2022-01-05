/**
 * Created by smit on 4/1/22.
 */
function displayLinuxProfile(idName, elementValue) {

    document.getElementById(idName).style.display = elementValue.value == 1 ? 'block' : 'none';
}
//
// $(document).ready(function(){
//     $(".new").click(function(){
//         $("div:even").addClass("form-popUp");
//     });
// });

function openForm(idName) {

    document.getElementById(idName).style.display ='block';
}

function closeForm(idName)
{
    document.getElementById(idName).style.display = 'none';
}