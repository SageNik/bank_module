<#ftl encoding="UTF-8">
<#import "/spring.ftl" as spr/>
<h2 align="center" ><@spr.message code="addClientsBank" /></h2>
<script type="text/javascript" src="js/client.js"></script>
<form id="newClient" >
<table>
 <#if model.message == true>
    <tr class=${(model.messageType)!}><td colspan="2"><@spr.message (model.currentMessage) /></td></tr>
 </#if>
     <tr class="error_message" id="clientError" style="visibility: hidden">
         <td></td>
         <td><@spr.message "validError"/></td></tr>
     <tr>
    <tr>
        <td class="labelAddCl"><@spr.message code="clientName"/>:</td>
        <td><input id="cl_name" type="text" size="30"></td>
    </tr>
    <tr>
        <td class="labelAddCl"><@spr.message code="clientSurname"/>:</td>
        <td><input id="cl_surname" type="text" size="30"></td>
    </tr><tr>
    <td class="labelAddCl"><@spr.message code="clientITN"/>:</td>
    <td><input id="cl_itn" type="text" size="15"></td>
</tr><tr>
    <td class="labelAddCl"><@spr.message code="clientPhoneNumber"/>:</td>
    <td><input onkeypress="return onlyNumber(event)" id="cl_phone"  type="text" size="15"></td>
</tr>
    <tr>
        <td colspan="2" align="center"><input type="button" id="addClient"  value=<@spr.message code="add"/>></td>
    </tr>
</table>
</form>