<#import "parts/common.ftl" as c>
<@c.page>
    <h2>User editor</h2>
    <form action="/user" method="post">
        <label><input type="text" name="username" value="${user.username}"/></label>
        <#list roles as role>
            <div>
                <label><input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked", "")}/>${role}</label>
            </div>
        </#list>
        <label><input type="hidden" value="${user.id}" name="userId"></label>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-primary">Save</button>
    </form>
    <div>
        <br><a href="/user" class="btn btn-primary me-3">User list</a>
        <a href="/main" class="btn btn-primary me-2">Main page</a>
    </div>
</@c.page>