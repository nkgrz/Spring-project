<#import "parts/common.ftl" as c>
<@c.page>
    <h2>User editor</h2>
    <form action="/user" method="post">
        <label><input type="text" name="username" value="${user.username}"></label>
        <#list roles as role>
            <div>
                <label><input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked", "")}>${role}</label>
            </div>
        </#list>
        <label><input type="hidden" value="${user.id}" name="userId"></label>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit" class="btn btn-primary">Save</button>
    </form>
    <br><span><a href="/" class="btn btn-secondary me-2">Main page</a></span>
</@c.page>