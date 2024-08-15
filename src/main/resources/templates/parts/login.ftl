<#macro login path>
    <form action="${path}" method="post">
        <div><label> User Name : <input type="text" name="username"/> </label></div>
        <br>
        <div><label> Password: <input type="password" name="password"/> </label></div>
        <br>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <br>
        <div><input type="submit" value="Sign In" class="btn btn-primary"/></div>
    </form>
</#macro>

<#macro logout>
    <form action="/logout" method="post">
        <input type="submit" value="Sign Out" class="btn btn-secondary"/>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    </form>
</#macro>