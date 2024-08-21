<#import "parts/common.ftl" as c>
<@c.page>
    <h2 class="text-center mb-4">List of Users</h2>
    <div class="container">
        <div class="table-responsive">
            <table class="table table-striped" style="max-width: 600px; margin: 0 auto;">
                <thead class="thead-light">
                <tr>
                    <th>Name</th>
                    <th>Role</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <#list users as user>
                    <tr>
                        <td>${user.username}</td>
                        <td>
                            <#list user.roles as role>${role}<#sep>, </#list>
                        </td>
                        <td>
                            <a href="/user/${user.id?c}" class="btn btn-secondary btn-sm mx-2">Edit</a>
                            <form action="/user/${user.id?c}/delete" method="post" style="display:inline;">
                                <input type="hidden" name="_csrf" value="${_csrf.token}">
                                <button type="submit" class="btn btn-danger btn-sm mx-2">Delete</button>
                            </form>
                        </td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
        <div class="text-center">
            <a href="/main" class="btn btn-primary my-4">Main Page</a>
        </div>
    </div>
</@c.page>
