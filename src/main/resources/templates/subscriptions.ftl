<#import "parts/common.ftl" as c>
<@c.page>
    <div class="container" style="max-width: 600px">
        <h2 class="text-center mb-4">${user.username}'s ${type}</h2>
        <ul class="list-group">
            <#list users as user>
                <li class="list-group-item">
                    <a href="/user-messages/${user.id}" class="text-decoration-none">
                        ${user.getUsername()}
                    </a>
                </li>
            </#list>
        </ul>
    </div>
</@c.page>
