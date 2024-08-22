<#import "parts/common.ftl" as c>
<#import "parts/infoMessage.ftl" as m>
<@c.page>
    <h3 class="card-title text-center mb-4">Profile</h3>
    <h4 class="card-title text-center mb-4">${user.username}</h4>

    <@m.msg></@m.msg>

    <div class="container mt-2" style="max-width: 400px;">
        <div class="card shadow-sm">
            <div class="card-body">
                <h5 class="card-title text-center mb-4">Update Your Profile</h5>
                <form method="post">
                    <div class="mb-3">
                        <label for="username" class="form-label">User Name</label>
                        <input type="text" id="username" name="username"
                               class="form-control ${(usernameError??)?string('is-invalid','')}"
                               value="${username}" placeholder="User name" required/>
                        <#if usernameError??>
                            <div class="invalid-feedback">
                                ${usernameError}
                            </div>
                        </#if>
                    </div>

                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" id="email" name="email"
                               class="form-control ${(emailError??)?string('is-invalid','')}"
                               value="${email!""}" placeholder="email@company.com"/>
                        <#if emailError??>
                            <div class="invalid-feedback">
                                ${emailError}
                            </div>
                        </#if>
                    </div>

                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>

                    <div class="d-flex justify-content-between">
                        <button type="submit" class="btn btn-primary my-2">Save Changes</button>
                        <a href="/user/profile/change-password" class="btn btn-secondary my-2">Change Password</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</@c.page>
