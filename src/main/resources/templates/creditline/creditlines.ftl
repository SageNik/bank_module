<#ftl encoding="UTF-8">
<#import "/spring.ftl" as spr/>
<script type="text/javascript" src="js/creditline.js"></script>
<script type="text/javascript" src="js/datepicker.js"></script>

<h1 align="center" ><@spr.message code="creditlinesBank" /></h1>
<h3 align="center"><@spr.message "creditlineCount" />: ${model.creditlineCount!}</h3>

<h2 align="center" ><@spr.message code="searchCreditlineCreditBank" /></h2>
<form id="searchCreditline">
<table align="center">
<#if model.message == true>
    <tr class=${(model.messageType)!}><td colspan="2"><@spr.message (model.currentMessage) /></td></tr>
</#if>
    <tr class="error_message" id="creditlineError" style="visibility: hidden">
        <td></td>
        <td><@spr.message "validError"/></td></tr>
    <tr>
    <tr class="tr_search">
        <td class="td_search"><@spr.message code="findCreditlineBy"/>:</td>
        <td><select id="search_criteria">
            <option value="openDate"><@spr.message code="openingDate"/></option>
            <option value="closeDate"><@spr.message code="closingDate"/></option>
        </select></td>
        <td><input type="text" id="input_date" size="20"/></td>
    <td><input type="button"  id="searchCred" value=<@spr.message code="search"/>></td>
</tr>
</table>
</form>

<#if model.answer == true>
    <#include "creditlineList.ftl" />
</#if>