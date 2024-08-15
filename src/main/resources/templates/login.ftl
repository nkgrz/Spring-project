<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>
<@c.page>
    Login page
    <br>
    <#if message?has_content>
        <br>${message}<br>
    </#if>
    <@l.login "/login"></@l.login>
    <a href="/registration" class="btn btn-secondary">Add new user</a>
</@c.page>
