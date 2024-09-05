<#macro messageEditor msg>
    <a class="btn btn-primary" data-toggle="collapse" href="#collapseForm" role="button" aria-expanded="false"
       aria-controls="collapseExample">
        ${msg}
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
                           name="newTag" placeholder="Tag"/>
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

                <button type="submit" class="btn btn-primary mt-3">Save message</button>
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <input type="hidden" name="id" value="<#if message?? >${message.id!''}</#if>"/>
            </form>
        </div>
    </div>
</#macro>