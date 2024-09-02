<#import "parts/common.ftl" as c>
<#import "parts/messageEdit.ftl" as msgAdd>
<#include "parts/security.ftl">
<@c.page>
    <h2 class="text-center mb-4">${requestedUser.username}</h2>

    <div class="text-center mb-4">
        <#if !isCurrentUser>
            <#if isSubscriber>
                <a class="btn btn-outline-warning" href="/user/unsubscribe/${requestedUser.id}">Unsubscribe</a>
            <#else>
                <a class="btn btn-outline-warning" href="/user/subscribe/${requestedUser.id}">Subscribe</a>
            </#if>
        </#if>
    </div>

    <div class="container my-3" style="max-width: 600px">
        <div class="row">
            <div class="col">
                <div class="card text-center">
                    <div class="card-body">
                        <div class="card-title">Subscriptions</div>
                        <h3 class="card-text">
                            <a href="/user/subscriptions/${requestedUser.id}/list">${subscriptionsCount}</a>
                        </h3>
                    </div>
                </div>
            </div>

            <div class="col">
                <div class="card text-center">
                    <div class="card-body">
                        <div class="card-title">Subscribers</div>
                        <h3 class="card-text">
                            <a href="/user/subscribers/${requestedUser.id}/list">${subscribersCount}</a>
                        </h3>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <#if isCurrentUser && message??>
        <@msgAdd.messageEditor "Edit message"></@msgAdd.messageEditor>
    </#if>
    <#include "parts/messageList.ftl" />
</@c.page>