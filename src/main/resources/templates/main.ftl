<#include "parts/security.ftl">
<#import "parts/common.ftl" as c>
<#import "parts/messageEdit.ftl" as msgAdd>
<@c.page>
    <h3>List of messages</h3>

    <div class="form-row">
        <div class="form-group col-md-6 mb-5">
            <form method="get" action="/main" class="form-inline">
                <label>
                    <input type="text" name="tag" class="form-control" placeholder="Search by tag" value="${tag!""}"/>
                </label>
                <button type="submit" class="btn btn-primary ms-2">Search</button>
            </form>
        </div>
    </div>

    <@msgAdd.messageEditor "New message"></@msgAdd.messageEditor>

    <#include "parts/messageList.ftl" />

    <#if isAdmin>
        <div class="my-3">
            <form method="post" action="delete">
                <button type="submit" class="btn btn-warning">Delete all message</button>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            </form>
        </div>
    </#if>


</@c.page>