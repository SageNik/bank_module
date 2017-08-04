<#ftl encoding="UTF-8">
<#import "/spring.ftl" as spr/>
<table class="paymentsTable">
    <tr class="paymRow1">
        <th class="paymCol"> № </th>
        <th class="paymCol"> <@spr.message code="residualCredit"/> </th>
        <th class="paymCol"> <@spr.message code="amortization"/> </th>
        <th class="paymCol"> <@spr.message code="percents"/> </th>
        <th class="paymCol"> <@spr.message code="monthPayment"/> </th>
    </tr>
<#list (model.calculatedPayments) as payList>
    <tr class="paymRow2">
        <td class="paymCol">${payList.number}
        <td class="paymCol">${payList.residualCredit}
        <td class="paymCol">${payList.amortization}
        <td class="paymCol">${payList.percents}
        <td class="paymCol">${payList.monthPayment}
    </tr>
</#list>
</table>
<table  class="resumTable">
    <tr class="tr_discr">
        <td class="disCredit1"><@spr.message code="creditAmount"/>:</td>
        <td class="disCredit2">${(model.creditAmount)!}</td>
    </tr>
    <tr class="tr_discr">
        <td class="disCredit1"><@spr.message code="creditPercent"/>:</td>
        <td class="disCredit2">${(model.creditPercent)!}</td>
        <td>%</td>
    </tr>
    <tr class="tr_discr">
        <td class="disCredit1"><@spr.message code="creditTime"/>:</td>
        <td class="disCredit2">${(model.creditTime)!}</td>
        <td><@spr.message code="month"/></td>
    </tr>
    <tr class="tr_discr">
        <td class="disCredit1"><@spr.message code="fullCost"/>:</td>
        <td class="disCredit2">${(model.fullCost)!}</td>
    </tr>
    <tr class="tr_discr">
        <td class="disCredit1"><@spr.message code="overpayment"/>:</td>
        <td class="disCredit2">${(model.overpayment)!}</td>
    </tr>
</table>
