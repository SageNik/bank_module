<table class="clientTable">
<#if model.message == true>
    <tr class=${(model.messageType)!}><td colspan="2"><@spr.message (model.currentMessage) /></td></tr>
</#if>
    <tr class="clientRow1">
        <th class="clientCol1"><@spr.message code="itn"/></th>
        <th class="clientCol2"><@spr.message code="clientName"/></th>
        <th class="clientCol3"><@spr.message code="clientSurname"/></th>
        <th class="clientCol4"><@spr.message code="clientPhoneNumber"/></th>
        <th colspan="2" class="clientCol2"><@spr.message code="actions"/></th>
    </tr>
<#list  model.clients as client >
    <tr class="clientRow2">
        <td class="clientCol1">${client.itn}</td>
        <td class="clientCol2">${client.name}</td>
        <td class="clientCol3">${client.surname}</td>
        <td class="clientCol4">${client.phoneNumber}</td>
        <td class="clientBut"><input type="button" onclick="moveToClient(${client.itn})" value=<@spr.message code="view"/> ></td>
        <td class="clientBut"><input type="button" class="buttonClDel" onclick="deleteClient(${client.itn})" value=<@spr.message code="delete"/>></td>
    </tr>
</#list>
</table>