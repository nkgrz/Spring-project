<#macro login path>
    <#if message?has_content>
        <div class="fw-lighter" style="color: red">
            ${message}
        </div>
    </#if>
    <form action="${path}" method="post">
        <div><label> User Name : <input type="text" name="username"/> </label></div>
        <br>
        <div><label> Password: <input type="password" name="password"/> </label></div>
        <br>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <br>
        <#if path == "/login">
            <div><input type="submit" value="Sign In" class="btn btn-primary me-3"/>
                <a href="/registration" class="btn btn-secondary">Registration</a>
            </div>

        <#elseif path == "/registration">
            <div><input type="submit" value="Register" class="btn btn-primary me-3"/>
                <a href="/login" class="btn btn-secondary">Login page</a>
            </div>
        <#else>
            <div><input type="submit" value="Submit" class="btn btn-primary"/></div><br>
        </#if>
    </form>
</#macro>

<#macro logout>
    <form action="/logout" method="post">
        <input type="submit" value="Sign Out" class="btn btn-secondary"/>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    </form>
</#macro>