/**
 * Created by Ник on 19.07.2017.
 */
function creditlineValidate(formId) {

    var permit = true;
    var errorMessage = $('#creditlineError');
    var elem = $("#"+formId).find(":input");
    var result = validate(permit, errorMessage, elem);
    return result
}

function move_addNewCreditline(clientItn) {
    $.get('/addNewCreditline', {itnClient: clientItn}, function (data) {
        $('#clientContent').html(data);
        return false;
    });
}

$(document).ready(
    function(){
        $("#searchCred").click(function(){
            if(creditlineValidate("searchCreditline")) {
                var criteria = $("#search_criteria").val();
                var input = $("#input_date").val();
                $("#searchCreditline").prop("disabled", true);

                $.post("/searchCreditline", {
                    searchCriteria: criteria,
                    inputDate: input
                }).done(function (data) {
                    $("#content1").html(data)
                }).fail(function () {
                    alert("Search failed")
                }).always(function () {
                    $("#searchCreditline").prop("disabled", false);
                });
            }else return false
        });
    });

$(document).ready(
    function() {
        $('#calcBut').click(function(){
            if(creditlineValidate("addCredLine")) {
                var percent = $("#credlin_percent").val();
                var amount = $("#credlin_amount").val();
                var duration = $("#credlin_duration").val();
                var type = $("#credlin_type").val();

                $('#calcBut').prop("disabled", true);

                $.post("/calcPaymentCreditline", {
                    creditPercent: percent,
                    creditAmount: amount,
                    creditTime: duration,
                    calcMethod: type
                    }
                ).done(function (data) {
                    $('#paymentsTable').html(data);
                }).fail(function () {
                    alert("Error: new creditline didn`t save!");
                }).always(function () {
                    $('#calcBut').prop("disabled", false);
                });
            }else return false
        });
    });

$(document).ready(
    function() {
        $('#openCredlin').click(function(){
            if(creditlineValidate("addCredLine")) {
                var itnClient = $("#itn_client").val();
                var percent = $("#credlin_percent").val();
                var amount = $("#credlin_amount").val();
                var duration = $("#credlin_duration").val();
                var currency = $("#credlin_currency").val();
                var type = $("#credlin_type").val();

                $('#openCredlin').prop("disabled", true);

                $.post("/addCreditline", {
                        itnClient: itnClient,
                        percent: percent,
                        amount: amount,
                        duration: duration,
                        currency: currency,
                        type: type
                    }
                ).done(function (data) {
                    $('#content1').html(data);
                }).fail(function () {
                    alert("Error: new creditline didn`t save!");
                }).always(function () {
                    $('#openCredlin').prop("disabled", false);
                });
            }else return false
        });
    });

function move_allCreditlines(itnClient) {
    $.get('/allClientCreditlines', {itnClient: itnClient}, function (data) {
        $('#clientContent').html(data);
        return false;
    });
}

function move_allCurCreditlines(itnClient) {
    $.get('/allCurCreditlines', {itnClient: itnClient}, function (data) {
        $('#clientContent').html(data);
        return false;
    });
}

function move_showCreditline(id_credLine, itnClient) {

    $.get('/showCreditline', {
        idCredline: id_credLine,
        itnClient: itnClient
    }, function (data) {
        $('#clientContent').html(data);
        return false;
    });
}

function deleteCreditline(idCredlin,itnClient) {
    $('#deleteCreditline').prop("disabled", true);

    $.post("/deleteCreditline", {
        creditlineId: idCredlin,
        itnClient: itnClient
    }).done(function (data) {
        $('#content1').html(data);
    }).fail(function () {
        alert("Error: creditline didn`t delete!");
    }).always(function () {
        $('#deleteCreditline').prop("disabled", false);
    });
}

function closeCreditLine(idCredlin, itnClient) {
    $('#closeCredLine').prop("disable", true);

    $.post("/closeCreditline",{
        creditlineId: idCredlin,
        itnClient: itnClient
    }).done(function (data) {
        $('#content1').html(data);
    }).fail(function () {
        alert("Error: creditline didn`t close");
    }).always(function () {
        $('#closeCredLine').prop("disable", false);
    });
}

function showCreditline(id_credLine, itnClient) {

    $.get('/client', {itnClient: itnClient}, function (data) {
        $('#content1').html(data);
        return false;
    });
    $.get('/showCreditline', {
        idCredline: id_credLine,
        itnClient: itnClient
    }, function (data) {
        $('#clientContent').html(data);
        return false;
    });
}