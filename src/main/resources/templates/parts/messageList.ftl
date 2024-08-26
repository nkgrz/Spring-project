<#if messages?has_content>
    <div class="container mt-4">
        <div class="row">
            <#list messages as msg>
                <div class="col-md-4 mb-4">
                    <div class="card shadow-sm">
                        <div class="card-body">
                            <a href="/user-messages/${msg.author.id}" style="text-decoration: none; color: inherit;">
                                <h5 class="card-title">
                                    ${msg.authorName}</h5>
                            </a>
                            <p class="card-text">${msg.text}</p>
                            <#if msg.filename??>
                                <div class="text-center mb-3">
                                    <img src="/img/${msg.filename}" alt="image" class="img-fluid rounded"
                                         style="max-width: 100%; height: auto;">
                                </div>
                            </#if>
                            <span class="badge bg-secondary">${msg.tag}</span>
                        </div>
                    </div>
                </div>
            </#list>
        </div>
    </div>
<#else>
    <div class="container mt-4 text-center">
        <div class="alert alert-info" role="alert">
            No messages available.
        </div>
    </div>
</#if>