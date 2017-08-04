<#ftl encoding="UTF-8">
<#import "/spring.ftl" as spr/>
<h2 align="center"><@spr.message code="creditlineCreditBank" /></h2>
<script type="text/javascript" src="js/payment.js"></script>
<#if model.errorDataMessage??>
<h3 align="center" style="color: red"><@spr.message (model.errorDataMessage) /></h3>
<#else>
<table class="clientTable">
    <tr>
        <td class="labelCl2"><@spr.message code="amount"/>:</td>
        <td class="labelCl">${model.creditlines[0].amount}</td>
    </tr>
    <tr>
        <td class="labelCl2"><@spr.message code="creditPercent"/>:</td>
        <td class="labelCl">${model.creditlines[0].percent} %</td>
    </tr>
    <tr>
        <td class="labelCl2"><@spr.message code="duration"/>, <@spr.message code="month"/>:</td>
        <td class="labelCl">${model.creditlines[0].duration}</td>
    </tr>
    <tr>
        <td class="labelCl2"><@spr.message code="currency"/>:</td>
        <td class="labelCl">${model.creditlines[0].currency}</td>
    </tr>
    <tr>
        <td class="labelCl2"><@spr.message code="creditType"/>:</td>
        <td class="labelCl">
        <#if model.creditlines[0].type == "annuit"><@spr.message code="selectAnnuity"/></#if>
        <#if model.creditlines[0].type == "diff"><@spr.message code="selectDiff"/></#if></td>
    </tr>
    <tr>
        <td class="labelCl2"><@spr.message code="openingDate"/>:</td>
        <td class="labelCl">${model.creditlines[0].openDate?string["dd.MM.yyyy"]}</td>
    </tr>
    <tr>
        <td class="labelCl2"><@spr.message code="closingDate"/>:</td>
        <td class="labelCl">
        <#if (creditline.closeDate)??>${model.creditlines[0].closeDate?string["dd.MM.yyyy"]}
        <#else> -- </#if></td>
    </tr>
    <tr>
        <td class="labelCl2"><@spr.message code="closed"/>:</td>
        <td class="labelCl">
        <#if model.creditlines[0].state == "CLOSED" ><@spr.message code="yes"/>
        <#elseif model.creditlines[0].state == "OPENED" ><@spr.message code="no"/></#if>
        </td>
    </tr>
    <tr>
        <td class="labelCl4" colspan="2">
            <button id="deleteCreditline"
                    onclick="deleteCreditline(${model.creditlines[0].id},${model.itnClient})"><@spr.message code="delete_credLin"/></button>
        </td>
    </tr>
</table>


<#if model.creditlines[0].state == "OPENED">
<h3 align="center"><@spr.message code="addPaymentBank" /></h3>
<form id="addPaymForm">
    <table class="resumTableB">
        <#setting number_format = "0.00"/>
        <#if model.messageAddPayment == true>
            <tr class=${(model.messageType)!}>
                <td colspan="2"><@spr.message (model.currentMessage) /></td>
            </tr>
        </#if>
        <tr class="error_message" id="paymentError" style="visibility: hidden">
            <td></td>
            <td><@spr.message "validError"/></td>
        </tr>
    <tr>
        <#if (model.recommendPay)??>
            <tr>
                <td class="labelAddPay2"><@spr.message code="recomPayAmount"/>:</td>
                <td class="labelAddPay">${(model.recommendPay)!}</td>
            </tr>
            <tr>
                <td class="labelAddPay2"><@spr.message code="amount"/>:</td>
                <td class="labelAddPay">
                    <input id="credLin_amount" type="text" size="12" value=${(model.recommendPay)!}>
                    <#setting number_format = "0"/>
                    <input id="credLin_id" type="hidden" value=${model.creditlines[0].id}>
                    <input id="client_itn" type="hidden" value=${model.itnClient}>
                </td>
                <td align="center"><input type="button" id="addPaym" value=<@spr.message code="add"/>></td>
            </tr>
        <#else >
            <tr>
                <td align="center" colspan="2">
                    <button type="button" onclick="closeCreditLine(${model.creditlines[0].id}, ${model.itnClient})"
                            id="closeCredLine"><@spr.message code="closeCredline"/></button>
                </td>
            </tr>
        </#if>
    </table>
</form>
</#if>
<#setting number_format = "0.00"/>
<h3 align="center"><@spr.message code="paymentsCredLinBank" /></h3>
<table class="paymentsTable">

<#if model.messageDelPayment == true>
    <tr class=${(model.messageType)!}>
        <td colspan="2"><@spr.message (model.currentMessage) /></td>
    </tr>
</#if>

    <tr class="paymRow1">
        <th class="paymCol"> №</th>
        <th class="paymCol"> <@spr.message code="residualCredit"/></th>
        <th class="paymCol"> <@spr.message code="amortization"/></th>
        <th class="paymCol"> <@spr.message code="percents"/></th>
        <th class="paymCol"> <@spr.message code="monthPayment"/></th>
        <th class="paymCol"> <@spr.message code="dateOfPayment"/></th>
        <th class="paymCol"> <@spr.message code="paymentAmount"/></th>
<#if model.creditlines[0].state == "OPENED">
        <th class="paymCol"><@spr.message code="actions"/></th>
</#if>
    </tr>
<#list model.fullPayments as fullPayList>
    <tr class="paymRow2">
        <#setting number_format = "0"/>
        <td class="paymCol">${fullPayList.calculatedPayment.number}</td>
        <#setting number_format = "0.00"/>
        <td class="paymCol">${fullPayList.calculatedPayment.residualCredit}</td>
        <td class="paymCol">${fullPayList.calculatedPayment.amortization}</td>
        <td class="paymCol">${fullPayList.calculatedPayment.percents}</td>
        <td class="paymCol">${fullPayList.calculatedPayment.monthPayment}</td>
        <#if fullPayList.payment??>
            <td class="paymCol">${fullPayList.payment.date?string["dd.MM.yyyy"]}</td>
            <td class="paymCol">${(fullPayList.payment.value/100)?double}</td>
            <#if model.creditlines[0].state == "OPENED">
                <td class="clientBut"><input type="button" class="buttonPayDel"
                                             onclick="deletePayment(${fullPayList.payment.id},
                                             ${model.creditlines[0].id}, ${model.itnClient})"
                                             value=<@spr.message code="delete"/>>
                </td>
            </#if>
        <#else >
            <td class="paymCol">--</td>
            <td class="paymCol">--</td>
            <td class="paymCol">--</td>
        </#if>
    </tr>
</#list>
</table>

<table class="resumTable">
    <tr class="tr_discr">
        <td class="disCredit1"><@spr.message code="creditAmount"/>:</td>
        <td class="disCredit2">${model.creditlines[0].amount}</td>
    </tr>
<#setting number_format = "0"/>
    <tr class="tr_discr">
        <td class="disCredit1"><@spr.message code="creditPercent"/>:</td>
        <td class="disCredit2">${model.creditlines[0].percent}</td>
        <td>%</td>
    </tr>
    <tr class="tr_discr">
        <td class="disCredit1"><@spr.message code="creditTime"/>:</td>
        <td class="disCredit2">${model.creditlines[0].duration}</td>
        <td><@spr.message code="month"/></td>
    </tr>
<#setting number_format = "0.00"/>
    <tr class="tr_discr">
        <td class="disCredit1"><@spr.message code="fullCost"/>:</td>
        <td class="disCredit2">${model.fullCost}</td>
    </tr>
    <tr class="tr_discr">
        <td class="disCredit1"><@spr.message code="overpayment"/>:</td>
        <td class="disCredit2">${model.overpayment}</td>
    </tr>
</table>
</#if>