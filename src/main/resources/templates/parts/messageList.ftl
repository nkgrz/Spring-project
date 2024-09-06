<#import 'pager.ftl' as p>
<#if page.content?has_content>
    <@p.pager url page/>
    <div class="container mt-4 d-flex justify-content-center">
        <div id="messagesContainer" class="row"
             style="max-height: 900px; overflow-y: auto; width: 100%; max-width: 700px; border: 2px solid #ccc; padding: 10px; border-radius: 8px;">
            <#list page.content?reverse as msg>
                <div class="col-12 mb-4"
                     style="display: flex;
                             justify-content: <#if msg.author.id == currentUserId>flex-end<#else>flex-start</#if>;">
                    <div class="card shadow-sm" style="max-width: 60%; width: 100%;">
                        <div class="card-body" data-id="${msg.id}">
                            <a href="/user-messages/${msg.author.id}" style="text-decoration: none; color: inherit;">
                                <h5 class="card-title">${msg.authorName}</h5>
                            </a>
                            <p class="card-text">${msg.text}</p>
                            <#if msg.filename??>
                                <div class="text-center mb-3">
                                    <img src="/img/${msg.filename}" alt="image" class="img-fluid rounded"
                                         style="max-width: 100%; height: auto;"/>
                                </div>
                            </#if>

                            <div class="d-flex justify-content-between align-items-center">
                                <div class="d-flex align-items-center">
                                    <#if msg.tag??>
                                        <a class="badge bg-secondary me-3" id="msg_tag" style="text-decoration: none;"
                                           href="/main?tag=${msg.tag}">${msg.tag}</a>
                                    </#if>

                                    <div class="like-container d-flex align-items-center">
                                        <a href="/messages/${msg.id}/like">
                                            <#if msg.meLiked>
                                                <i class="fa-solid fa-heart me-2"></i>
                                            <#else >
                                                <i class="fa-regular fa-heart me-2"></i>
                                            </#if>
                                        </a>
                                        <span>${msg.likes}</span>
                                    </div>
                                </div>
                                <#if msg.author.id == currentUserId>
                                    <div>
                                        <a class="btn btn-sm fa-solid fa-pencil"
                                           href="/user-messages/${msg.author.id}?message=${msg.id}"> Edit</a>
                                        <form action="/user-messages/delete-msg/${msg.id}" method="post"
                                              style="display:inline;">
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                            <button class="btn fa-solid fa-trash-can-arrow-up" type="submit"></button>
                                        </form>
                                    </div>
                                </#if>
                            </div>
                        </div>
                    </div>
                </div>
            </#list>
        </div>
    </div>

    <!-- Скрипт для прокрутки вниз после полной загрузки контента -->
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            const messagesContainer = document.getElementById('messagesContainer');
            setTimeout(function () {
                messagesContainer.scrollTop = messagesContainer.scrollHeight;
            }, 1);
        });
    </script>
<#else>
    <div class="container mt-4 text-center">
        <div class="alert alert-info" role="alert">
            No messages available.
        </div>
    </div>
</#if>
