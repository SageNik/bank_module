<#ftl encoding="UTF-8">
<html lang="en">

<#import "/spring.ftl" as spr/>

    <#include "shared/head.ftl" />
    <#include "shared/header.ftl" />

<body>

    <div class="authoriz" >
        <#include "shared/login_block.ftl"/>
    </div>

    <h2 align="center" ><@spr.message "creditCalc" /></h2>

    <div class="body_content" >
        <h3 align="center"><@spr.message code="creditParam" /></h3>
        <form  action="/calculate" id="calcs" method="post" onsubmit="return calcValidate()">
            <table align="center">
                <tr class="error_message" id="calcError" style="visibility: hidden">
                    <td></td>
                    <td><@spr.message "validError"/></td></tr>
                <tr>
                    <td><@spr.message code="creditAmount"/>:</td>
                    <td><input onkeyup="onlyFloatNumber(this)" type="text" maxlength="16" name="creditAmount" id="amount" class="text" value=""/>
                </tr>
                <tr>
                    <td><@spr.message code="creditPercent"/>:</td>
                    <td><input type="text" onkeyup="onlyFloatNumber(this)" maxlength="5" name="creditPercent" id="percent"class="text" value="" /> %
                </tr>
                <tr>
                    <td><@spr.message code="creditTime"/>:</td>
                    <td><input type="text" onkeypress="return onlyNumber(event)" maxlength="3" name="creditTime" id="duration" class="text" value=""/><@spr.message code="month"/>
                </tr>
                <tr>
                    <td><@spr.message code="creditMethod"/>:</td>
                    <td><select name="calcMethod">
                        <option value="annuit"><@spr.message code="annuit" /></option>
                        <option value="diff"><@spr.message code="diff"/></option>
                    </select>
                    </td>
                </tr>
                <tr>
                    <td colspan="2"><input id="calc_button" type="submit" name="calculate" value=<@spr.message code="calcButton"/>></td>
                </tr>
            </table>
        </form>

        <#if (model.calcTable)!false >
<div id="calcPaym">
    <#if model.calcMethod == "annuit">
        <h3 align="center" ><@spr.message code="annMethod" /></h3>
    </#if>
    <#if model.calcMethod == "diff">
        <h3 align="center" ><@spr.message code="difMethod" /></h3>
    </#if>
<#include "shared/calcPayments.ftl"/>
    </div>
        </#if>
    </div>

</body>

</html>

