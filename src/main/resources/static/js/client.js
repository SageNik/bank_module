/**
 * Created by Ник on 26.01.2017.
 */

function clientValidate(formId) {

    var permit = true;
    var errorMessage = $('#clientError');
    var elem = $("#"+formId).find(":input");
    var result = validate(permit, errorMessage, elem);
    return result
}

$(document).ready(
    function() {
            $('#addClient').click(function () {
                if(clientValidate("newClient")) {
                    var name = $("#cl_name").val();
                    var surname = $("#cl_surname").val();
                    var itn = $("#cl_itn").val();
                    var phoneNumber = $("#cl_phone").val();

                    $('#addClient').prop("disabled", true);

                    $.post("/addClient", {
                            name: name,
                            surname: surname,
                            itn: itn,
                            phoneNumber: phoneNumber
                        }
                    ).done(function (data) {
                        $('#content1').html(data);
                    }).fail(function () {
                        alert("Error: new client didn`t save!");
                    }).always(function () {
                        $('#addClient').prop("disabled", false);
                    });
                }else return false
            });
    });

$(document).ready(
    function () {
        $('#goToAddClient').click(function () {
            $('#content1').load("addNewClient.ftl");
            return false;
        });
    });

$(document).ready(
    function(){
        $("#search_cl").click(function(){
            if(clientValidate("searchClient")) {
                var criteria = $("#search_criteria").val();
                var input = $("#input_data").val();
                $("#search_cl").prop("disabled", true);

                $.post("/searchClient", {
                    searchCriteria: criteria,
                    inputData: input
                }).done(function (data) {
                    $("#content1").html(data)
                }).fail(function () {
                    alert("Search failed")
                }).always(function () {
                    $("#search_cl").prop("disabled", false);
                });
            }else return false
        });
    });

function deleteClient(itnCl) {
    $('.buttonClDel').prop("disabled", true);

    $.post("/deleteClient", {
        itnClient: itnCl
    }).done(function (data) {
        $('#content1').html(data);
    }).fail(function () {
        alert("Error: client didn`t delete!");
    }).always(function () {
        $('.buttonClDel').prop("disabled", false);
    });
}

function moveToClient(itnCl) {
    $.get('/client', {itnClient: itnCl}, function (data) {
        $('#content1').html(data);
        return false;
    });
}

function move_change(itnCl) {
    $.get('/changeClient', {itnClient: itnCl}, function (data) {
        $('#clientContent').html(data);
        return false;
    });
}

$(document).ready(
    function() {
        $('#changeCl').click(function(){
            if(clientValidate("changeClientForm")) {
                var name = $("#nameCl").val();
                var surname = $("#surnameCl").val();
                var itn = $("#itnCl").val();
                var phoneNumber = $("#phoneCl").val();

                $('#changeCl').prop("disabled", true);
                $.post("/updateClient", {
                    name: name,
                    surname: surname,
                    itn: itn,
                    phoneNumber: phoneNumber
                    }
                ).done(function (data) {
                    $('#content1').html(data);
                }).fail(function () {
                    alert("Error: client didn`t change!");
                }).always(function () {
                    $('#changeCl').prop("disabled", false);
                });
            }else return false
        });
    });