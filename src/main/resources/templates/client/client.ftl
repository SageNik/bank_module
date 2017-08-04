<#ftl encoding="UTF-8">
<#import "/spring.ftl" as spr/>
<script type="text/javascript" src="js/creditline.js"></script>
<h2 align="center" ><@spr.message code="clientClientsBank" /></h2>
<#if model.errorDataMessage??>
<h3 align="center" style="color: red"><@spr.message (model.errorDataMessage) /></h3>
<#else>
    <table class="clientTable">
    <#if model.message == true>
        <tr class=${(model.messageType)!}><td colspan="2"><@spr.message (model.currentMessage) /></td></tr>
    </#if>
        <tr>
            <td class="labelCl2"><@spr.message code="clientName"/>:</td>
            <td class="labelCl">${model.clients[0].name}</td>
        </tr>
        <tr>
            <td class="labelCl2"><@spr.message code="clientSurname"/>:</td>
            <td class="labelCl">${model.clients[0].surname}</td>
        </tr>
        <tr>
        <td class="labelCl2"><@spr.message code="clientITN"/>:</td>
        <td class="labelCl">${model.clients[0].itn}</td>
    </tr>
        <tr>
        <td class="labelCl2"><@spr.message code="clientPhoneNumber"/>:</td>
        <td class="labelCl">${model.clients[0].phoneNumber}</td>
    </tr>
    </table>

<table class="butTable">
    <tr>
        <td class="buttCol2"><button id="addCredLin" onclick="move_addNewCreditline(${model.clients[0].itn})"><@spr.message code="add_credLin"/></button></td>
        <td class="buttCol2"><button id="showAllCredLin" onclick="move_allCreditlines(${model.clients[0].itn})"><@spr.message code="showAllCredLin"/></button></td>
        <td class="buttCol2"><button id="showCurCredLin" onclick="move_allCurCreditlines(${model.clients[0].itn})" ><@spr.message code="show_curCredLin"/></button></td>
        <td class="buttCol"><button id="showAllPayments" onclick="move_allPayments(${model.clients[0].itn})"><@spr.message code="show_allPayments"/></button></td>
        <td class="buttCol"><button id="changeClient" onclick="move_change(${model.clients[0].itn})"><@spr.message code="change_client"/></button></td>
    </tr>
</table>
<div id="clientContent" style="margin-top: 40px">

</div>
</#if>