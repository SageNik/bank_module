
<h3 align="center"><@spr.message code="authoriz"/></h3>
<form action="/home" method="post">
    <table>
        <#if login_error.isPresent()>
        <tr class="error_message"><td colspan="2"><@spr.message code="error_login" /></td></tr>
        </#if>
        <tr>
            <td><@spr.message code="login" />:</td>
            <td><input type="text" class="text" name="username" value=""/></td>
        </tr>
        <tr>
            <td><@spr.message code="password" />:</td>
            <td><input type="password" class="text" name="password" /></td>
        </tr>
        <tr align="center">
            <td colspan="2">
                <input type="submit" value=<@spr.message code="loginButton"/> >
            </td>
        </tr>
    </table>
</form>
