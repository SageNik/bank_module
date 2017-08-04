<#ftl encoding="UTF-8">
<#import "/spring.ftl" as spr/>
<h2 align="center"><@spr.message code="changeClientClientsBank" /></h2>
<script type="text/javascript" src="js/client.js"></script>
<form id="changeClientForm">
    <table class="clientTable">
        <tr class="error_message" id="clientError" style="visibility: hidden">
            <td></td>
            <td><@spr.message "validError"/></td></tr>
        <tr>
        <tr>
            <td class="labelCl2"><@spr.message code="clientName"/>:</td>
            <td class="labelCl"><input type="text" size="30" id="nameCl" value=${model.clients[0].name}></td>
        </tr>
        <tr>
            <td class="labelCl2"><@spr.message code="clientSurname"/>:</td>
            <td class="labelCl"><input type="text" size="30" id="surnameCl" value=${model.clients[0].surname}></td>
        </tr>
        <tr>
            <td class="labelCl2"><@spr.message code="clientITN"/>:</td>
            <td class="labelCl"><input type="text" readonly size="30" id="itnCl" value=${model.clients[0].itn}></td>
        </tr>
        <tr>
            <td class="labelCl2"><@spr.message code="clientPhoneNumber"/>:</td>
            <td class="labelCl"><input type="text" size="30" id="phoneCl" value=${model.clients[0].phoneNumber}></td>
        </tr>
        <tr>
            <td colspan="2" align="center"><input id="changeCl" type="button" value=<@spr.message code="save_change"/>>
            </td>
        </tr>
    </table>
</form>
