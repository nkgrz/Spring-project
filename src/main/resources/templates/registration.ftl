<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>
<@c.page>
    Add new user
    <#if message?has_content>
        <br>${message}
    </#if>
    <@l.login "/registration"></@l.login>
    <a href="/login">Login page</a>
</@c.page>