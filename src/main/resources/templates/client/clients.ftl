<#ftl encoding="UTF-8">
<#import "/spring.ftl" as spr/>
<script type="text/javascript" src="js/client.js"></script>
<script type="text/javascript" src="js/payment.js"></script>
<h1 align="center" ><@spr.message code="clientsCreditBank" /></h1>
<#if model.errorDataMessage??>
<h3 align="center" style="color: red"><@spr.message (model.errorDataMessage) /></h3>
<#else>

<h3 align="center"><@spr.message "clientCount" />: ${model.clientCount!}</h3>
<button style="margin: auto; display: block" id="goToAddClient"><@spr.message "addNewClient"/></button>
<h2 align="center" ><@spr.message code="searchClientCreditBank" /></h2>

<form id="searchClient">
    <table class="paymentsTable">
    <#if model.message == true>
        <tr class=${(model.messageType)!}><td colspan="2"><@spr.message (model.currentMessage) /></td></tr>
    </#if>
        <tr class="error_message" id="clientError" style="visibility: hidden">
            <td></td>
            <td></td>
            <td><@spr.message "validError"/></td></tr>
        <tr>
        <tr class="tr_search">
            <td class="td_search"><@spr.message code="find_clientBy"/>:</td>
            <td><select id="search_criteria">
                <option value="name"><@spr.message code="clientName"/></option>
                <option value="surname"><@spr.message code="clientSurname"/></option>
                <option value="itn"><@spr.message code="itn"/></option>
                <option value="phone"><@spr.message code="clientPhoneNumber"/></option>
            </select></td>
            <td><input type="text" id="input_data" size="20"/></td>
            <td><input type="button" id="search_cl" value=<@spr.message code="search"/>></td>
        </tr>
    </table>
</form>
<#if model.answer == true>
    <#include "searchCLientResult.ftl"/>
</#if>
</#if>