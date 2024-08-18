<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>
<@c.page>
<#--    <h3>Add new user</h3><br>-->
    <h3 class="card-title text-center mb-4">Add new user</h3>
    <@l.login "/registration"></@l.login>
</@c.page>