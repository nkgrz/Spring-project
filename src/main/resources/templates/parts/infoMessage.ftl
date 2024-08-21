<#macro msg>
    <#if message?has_content>
        <div class="alert alert-success text-center" style="max-width: 370px; margin: 0 auto;">
            ${message}
        </div>
    </#if>
    <#if errorMessage?has_content>
        <div class="alert alert-danger text-center" style="max-width: 370px; margin: 0 auto;">
            ${errorMessage}
        </div>
    </#if>
</#macro>