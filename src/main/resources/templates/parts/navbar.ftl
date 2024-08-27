<#import "login.ftl" as l>
<#include "security.ftl">
<nav class="navbar navbar-expand-lg bg-body-tertiary px-5">
    <div class="container-fluid d-flex justify-content-between">
        <a class="navbar-brand" href="/">Messenger</a>
        <div class="d-flex justify-content-center mx-auto">
            <ul class="navbar-nav mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" aria-current="page" href="/">Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" aria-current="page" href="/main">Messages</a>
                </li>
                <#if isAdmin>
                    <li class="nav-item">
                        <a class="nav-link" href="/user">User list</a>
                    </li>
                </#if>
                <#if user??>
                    <li class="nav-item">
                        <a class="nav-link" href="/user/profile">Profile</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/user-messages/${currentUserId}">My messages</a>
                    </li>
                </#if>
            </ul>
        </div>
        <div class="d-flex">
            <div class="navbar-text me-3">${name}</div>
            <#if name != "unknown">
                <@l.logout_btn/>
            <#else>
                <@l.login_btn/>
            </#if>
        </div>
    </div>
</nav>