<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>
    <h1>Greeting</h1>
    <#if name != "unknown">
        <p>Hello, ${name}!</p>
<#else>
    <p>Hello, guest!</p>
    </#if>
</@c.page>