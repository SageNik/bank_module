<#ftl encoding="UTF-8">
<#import "/spring.ftl" as spr/>
<script type="text/javascript" src="js/creditline.js"></script>

<h2 align="center"><@spr.message code="makingClientCreditlinesBank" /></h2>
<form id="addCredLine">
    <table>
        <tr class="error_message" id="creditlineError" style="visibility: hidden">
            <td></td>
            <td><@spr.message "validError"/></td></tr>
        <tr>
        <tr>
            <td class="labelAddCl"><@spr.message code="amount"/>:</td>
            <td><input id="credlin_amount" onkeyup="onlyFloatNumber(this)" type="text" maxlength="16">
                <input type="hidden" id="itn_client" value=${itnClient}>
            </td>
        </tr>
        <tr>
            <td class="labelAddCl"><@spr.message code="creditPercent"/>,%:</td>
            <td><input id="credlin_percent" onkeyup="onlyFloatNumber(this)" maxlength="5" type="text" size="8"></td>
        </tr>
        <tr>
            <td class="labelAddCl"><@spr.message code="duration"/>:</td>
            <td><input id="credlin_duration" type="text" onkeypress="return onlyNumber(event)" maxlength="3"></td>
        </tr>
        <tr>
            <td class="labelAddCl"><@spr.message code="currency"/>:</td>
            <td><select id="credlin_currency" name="currency">
                <#list currencies as currency>
                    <option value=${currency}>${currency}</option>
                </#list>
            </td>
        </tr>
        <tr>
            <td class="labelAddCl"><@spr.message code="creditType"/>:</td>
            <td><select id="credlin_type" name="credit_type">
                <option value="annuit"><@spr.message code="annuit"/></option>
                <option value="diff"><@spr.message code="diff"/></option>
            </select>
            </td>
        </tr>
        <tr>
            <td align="right"><input type="button" id="calcBut" value=<@spr.message code="calcButton"/>></td>
            <td align="center"><input type="button" id="openCredlin" value=<@spr.message code="openCredLin"/>></td>
        </tr>
    </table>
</form>
<div id="paymentsTable">

</div>