<#import "parts/common.ftl" as c>
<#import "parts/messageEdit.ftl" as msgAdd>
<#include "parts/security.ftl">
<@c.page>
    <h2 class="text-center mb-4">${username}</h2>
    <#if isCurrentUser && message??>
        <@msgAdd.messageEditor "Edit message"></@msgAdd.messageEditor>
    </#if>
    <#include "parts/messageList.ftl" />
</@c.page>