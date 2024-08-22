<#import "parts/common.ftl" as c>
<#import "parts/infoMessage.ftl" as m>
<@c.page>
    <h3 class="card-title text-center mb-4">Change Password</h3>
    <@m.msg></@m.msg>
    <div class="container mt-4" style="max-width: 400px;">
        <div class="card shadow-sm">
            <div class="card-body">
                <form method="post" action="/user/change-password">

                    <div class="mb-3">
                        <label for="currentPassword" class="form-label ">Current password</label>
                        <input type="password" name="currentPassword" id="currentPassword"
                               class="form-control ${(currentPasswordError??)?string('is-invalid','')}"
                               placeholder="Current password"/>
                        <#if currentPasswordError??>
                            <div class="invalid-feedback">
                                ${currentPasswordError}
                            </div>
                        </#if>
                    </div>

                    <div class="mb-3">
                        <label for="password" class="form-label ">New password</label>
                        <input type="password" name="password" id="password"
                               class="form-control ${(passwordError??)?string('is-invalid','')}"
                               placeholder="New password"/>
                        <#if passwordError??>
                            <div class="invalid-feedback">
                                ${passwordError}
                            </div>
                        </#if>
                    </div>

                    <div class="mb-3">
                        <label for="passwordConfirmation" class="form-label ">Retype password</label>
                        <input type="password" name="passwordConfirmation" id="passwordConfirmation"
                               class="form-control ${(passwordConfirmationError??)?string('is-invalid','')}"
                               placeholder="Retype password"/>
                        <#if passwordConfirmationError??>
                            <div class="invalid-feedback">
                                ${passwordConfirmationError}
                            </div>
                        </#if>
                    </div>

                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>

                    <div class="d-flex justify-content-between">
                        <button type="submit" class="btn btn-primary">Change Password</button>
                        <a href="/user/profile" class="btn btn-secondary">Back to Profile</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</@c.page>
