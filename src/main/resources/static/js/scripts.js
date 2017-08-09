/**
 * Created by Ник on 18.01.2017.
 */

$(document).ready(
    function () {
        $('#clients').click(function () {
            $('#content1').load("home-operator/clients.ftl");
            return false;
        });
    });
$(document).ready(
    function () {
        $('#all_credLin').click(function () {
            $('#content1').load("home-operator/creditlines.ftl");
            return false;
        });
    });
$(document).ready(
    function () {
        $('#addNewClient').click(function () {
            $('#content1').load("home-operator/addNewClient.ftl");
            return false;
        });
    });

function calcValidate(){

    var permit = true;
    var errorMessage = $('#calcError');
    var elem = $("#calcs").find(":input");

   var result = validate(permit, errorMessage, elem);
    return result
}

function validate(permit, errorMessage, elem) {
    elem.each(function(){
        if(!$(this).val()){
            $(this).css('border', 'red 1px solid');
            permit = false;
        }else{
            $(this).css('border', 'gray 1px solid');
        }
    });
    if(permit){
        errorMessage.css('visibility','hidden');
        return true
    }else{
        errorMessage.css('visibility','visible');
        return false
    }
}

function onlyNumber(obj) {
    obj = (obj) ? obj : window.obj;
    var charCode = (obj.which) ? obj.which : obj.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
}

function onlyFloatNumber(obj) {
    obj.value = obj.value.replace(/[^\d,.]*/g, '')
        .replace(/([,.])[,.]+/g, '$1')
        .replace(/^[^\d]*(\d+([.,]\d{0,5})?).*$/g, '$1');
}

$(document).ready(
    function () {
        $('#search_client').click(function () {
            $('#content1').load("home-operator/clients.ftl");
            return false;
        });
    });