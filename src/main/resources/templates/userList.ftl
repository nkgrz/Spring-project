<#import "parts/common.ftl" as c>
<@c.page>
    <h2>List of users</h2>
    <table>
        <thead>
        <tr>
            <th>Name</th>
            <th>Role</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <#list users as user>
            <tr>
                <td>${user.username}</td>
                <td><#list user.roles as role>${role}<#sep>, </#list></td>
                <td><a href="/user/${user.id}" class="btn btn-secondary">edit</a>
                    <form action="/user/${user.id}/delete" method="post" style="display:inline;">
                        <input type="hidden" name="_csrf" value="${_csrf.token}">
                        <button type="submit" class="btn btn-danger btn-sm">delete</button>
                    </form>
                </td>
            </tr>
        </#list>
        </tbody>

    </table>
    <a href="/main" class="btn btn-primary">Main page</a>
</@c.page>