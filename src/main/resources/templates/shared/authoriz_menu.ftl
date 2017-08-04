
    <h3 align="center"><@spr.message code="authoriz"/></h3>
    <table>
        <tr>
            <td colspan="2"><@spr.message code="authMes"/> :</td>
        </tr>
        <tr class="tr_discr">
            <td colspan="2">${authUser.fullName}</td>
        </tr>
        <tr>
            <td><@spr.message code="authMes2" />:</td>
            <td class="tr_discr">${authUser.username}</td>
        </tr>
        <tr>
            <form action="/home-operator" method="post">
                <td colspan="2" align="center"><a href="/logout" class="button" name="log_out"><@spr.message code="log_out"/></a></td>
            </form>
        </tr>
    </table>

    <h3 align="center" ><@spr.message code="menu_operator" /></h3>
    <p class="str_menu"><@spr.message code="view_info"/></p>
    <ul>
        <li><a href="javascript:;" id="clients" ><@spr.message code="allClients"/></a></li>
        <li><a href="javascript:;" id="all_credLin" ><@spr.message code="allCredLin"/></a></li>
    </ul>

    <p class="str_menu"><@spr.message code="work_client"/></p>
    <ul>
        <li><a href="javascript:;" id="addNewClient"><@spr.message code="addNewClient"/></a></li>
    </ul>
