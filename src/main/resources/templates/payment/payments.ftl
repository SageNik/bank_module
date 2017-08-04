<#ftl encoding="UTF-8">
<#import "/spring.ftl" as spr/>
<h2 align="center"><@spr.message code="clientPaymentsBank" /></h2>
<#if model.errorDataMessage??>
<h3 align="center" style="color: red"><@spr.message (model.errorDataMessage) /></h3>
<#else>

<table class="clientTable">
    <tr class="clientRow1">
        <th class="credLinCol3"><@spr.message code="date"/></th>
        <th class="credLinCol2"><@spr.message code="creditLine"/></th>
        <th class="credLinCol4"><@spr.message code="amount"/></th>
    </tr>
<#list  model.payments as payment >
    <tr class="clientRow2">
        <#--<td class="credLinCol1">${payment.id}</td>-->
        <td class="credLinCol3">${payment.date?string["dd.MM.yyyy"]}</td>
        <td class="credLinCol2"><a href="javascript:;" onclick="move_showCreditline(${payment.creditlineId},${model.itnClient})"
                                   title=<@spr.message code="showCreditline"/>>${payment.creditlineId}</a></td>
        <td class="credLinCol4"> ${(payment.value / 100)?double}</td>
    </tr>
</#list>
</table>
</#if>