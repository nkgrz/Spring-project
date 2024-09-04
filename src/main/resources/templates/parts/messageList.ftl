<#if messages?has_content>
    <div class="container mt-4 d-flex justify-content-center">
        <div id="messagesContainer" class="row"
             style="max-height: 900px; overflow-y: auto; width: 100%; max-width: 700px; border: 2px solid #ccc; padding: 10px; border-radius: 8px;">
            <#list messages as msg>
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
                            <a class="badge bg-secondary" id="msg_tag" style="text-decoration: none;"
                               href="/main?tag=${msg.tag}">${msg.tag}</a>
                            <#if msg.author.id == currentUserId>
                                <a class="btn btn-light btn-sm"
                                   href="/user-messages/${msg.author.id}?message=${msg.id}">Edit</a>
                            </#if>
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
            }, 100);
        });
    </script>
<#else>
    <div class="container mt-4 text-center">
        <div class="alert alert-info" role="alert">
            No messages available.
        </div>
    </div>
</#if>
