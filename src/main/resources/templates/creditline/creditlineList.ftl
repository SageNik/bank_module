<#import "/spring.ftl" as spr/>
<script type="text/javascript" src="js/creditline.js"></script>
<table class="clientTable">
<#if model.message == true>
    <tr class=${(model.messageType)!}><td colspan="2"><@spr.message (model.currentMessage) /></td></tr>
<#else >
    <tr class="clientRow1">
        <th class="credLinCol2"><@spr.message code="creditType"/></th>
        <th class="credLinCol3"><@spr.message code="openingDate"/></th>
        <th class="credLinCol3"><@spr.message code="closingDate"/></th>
        <th class="credLinCol4"><@spr.message code="creditAmount"/></th>
        <th class="credLinCol2"><@spr.message code="creditPercent"/>, %</th>
        <th class="credLinCol2"><@spr.message code="duration"/>, <@spr.message code="month"/></th>
        <th class="credLinCol1"><@spr.message code="currency"/></th>
        <th class="credLinCol2"><@spr.message code="closed"/></th>
        <th class="credLinCol2"><@spr.message code="actions"/></th>

    </tr>
<#list  model.creditlines as creditline >
    <tr class="clientRow2">
        <td class="credLinCol2">
            <#if creditline.type == "annuit"><@spr.message code="selectAnnuity"/></#if>
        <#if creditline.type == "diff"><@spr.message code="selectDiff"/></#if>
        </td>
        <td class="credLinCol3">${creditline.openDate?string["dd.MM.yyyy"]}</td>
        <td class="credLinCol3">
        <#if (creditline.closeDate)??>${creditline.closeDate?string["dd.MM.yyyy"]}
        <#else> -- </#if>
        </td>
        <td class="credLinCol4">${creditline.amount}</td>
        <td class="credLinCol2">${creditline.percent}</td>
        <td class="credLinCol2">${creditline.duration}</td>
        <td class="credLinCol1">${creditline.currency}</td>
        <td class="credLinCol2">
            <#if creditline.state == "CLOSED" ><@spr.message code="yes"/>
        <#elseif creditline.state == "OPENED" ><@spr.message code="no"/></#if>
        </td>
        <td>
            <button id="showCredlin"
                    onclick="showCreditline(${creditline.id},${creditline.itnClient})"><@spr.message code="showPayments"/></button>
        </td>
    </tr>
</#list>
</#if>
</table>