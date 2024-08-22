<#import "parts/common.ftl" as c>
<@c.page>
    <h3>List of messages</h3>

    <div class="form-row">
        <div class="form-group col-md-6 mb-5">
            <form method="get" action="/main" class="form-inline">
                <label>
                    <input type="text" name="tag" class="form-control" placeholder="Search by tag" value="${tag!""}">
                </label>
                <button type="submit" class="btn btn-primary ms-2">Search</button>
            </form>
        </div>
    </div>

    <a class="btn btn-primary" data-toggle="collapse" href="#collapseForm" role="button" aria-expanded="false"
       aria-controls="collapseExample">
        New message
    </a>

    <div class="collapse <#if message??>show</#if>" id="collapseForm">
        <div class="form-group mt-3" style="max-width: 500px;">
            <form method="post" enctype="multipart/form-data">
                <div class="form-group mt-2">
                    <label for="messageText" class="form-label">Message</label>
                    <input type="text" id="messageText" class="form-control ${(textError??)?string('is-invalid','')}"
                           value="<#if message??>${message.text!''}</#if>" name="text"
                           placeholder="Enter a message"/>
                    <#if textError??>
                        <div class="invalid-feedback">
                            ${textError}
                        </div>
                    </#if>
                </div>

                <div class="form-group mt-2">
                    <label for="messageTag" class="form-label">Tag</label>
                    <input type="text" class="form-control ${(tagError??)?string('is-invalid','')}"
                           value="<#if message??>${message.tag!''}</#if>" id="messageTag"
                           name="tag" placeholder="Tag"/>
                    <#if tagError??>
                        <div class="invalid-feedback">
                            ${tagError}
                        </div>
                    </#if>
                </div>

                <div class="form-group mt-3">
                    <label for="customFile" class="form-label">Choose file</label>
                    <input type="file" class="form-control" name="file" id="customFile"/>
                </div>

                <button type="submit" class="btn btn-primary mt-3">Add message</button>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            </form>
        </div>
    </div>

    <#if messages?has_content>
        <div class="container mt-4">
            <div class="row">
                <#list messages as msg>
                    <div class="col-md-4 mb-4">
                        <div class="card shadow-sm">
                            <div class="card-body">
                                <h5 class="card-title">${msg.authorName}</h5>
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

    <div class="mb3">
        <form method="post" action="delete">
            <button type="submit" class="btn btn-warning">Delete all message</button>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        </form>
    </div>


</@c.page>