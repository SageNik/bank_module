/**
 * Created by Ник on 21.07.2017.
 */
function paymentValidate(formId) {

    var permit = true;
    var errorMessage = $('#paymentError');
    var elem = $("#"+formId).find(":input");
    var result = validate(permit, errorMessage, elem);
    return result
}

function move_allPayments(itnClient) {
    $.get('/home-operator/allPayments', {itnClient: itnClient}, function (data) {
        $('#clientContent').html(data);
        return false;
    });
}

$(document).ready(
    function() {
        $('#addPaym').click(function(){
            if(paymentValidate("addPaymForm")) {

                var credLin_amount = $("#credLin_amount").val();
                var credLin_id = $("#credLin_id").val();
                var client_itn = $("#client_itn").val();

                $('#addPaym').prop("disabled", true);

                $.post("/home-operator/addPayment", {
                        itnClient: client_itn,
                        creditlineId: credLin_id,
                        amount: credLin_amount
                    }
                ).done(function (data) {
                    $('#clientContent').html(data);
                }).fail(function () {
                    alert("Error: new payment didn`t save!");
                }).always(function () {
                    $('#addPaym').prop("disabled", false);
                });
            }else return false
        });
    });

function deletePayment(idPaym, idCredlin, itnClient) {
    $('.buttonPayDel').prop("disabled", true);

    $.post("/home-operator/deletePayment", {
        paymentId: idPaym,
        creditlineId: idCredlin,
        itnClient: itnClient
    }).done(function (data) {
        $('#clientContent').html(data);
    }).fail(function () {
        alert("Error: payment didn`t delete!");
    }).always(function () {
        $('.buttonPayDel').prop("disabled", false);
    });
}