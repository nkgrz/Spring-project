<#import "infoMessage.ftl" as m>
<#macro login path>
    <@m.msg></@m.msg>
    <div class="container mt-2" style="max-width: 400px;">
        <div class="card shadow-sm">
            <div class="card-body">
                <h3 class="card-title text-center mb-4">Welcome</h3>
                <form action="${path}" method="post">
                    <div class="mb-3">
                        <label for="username" class="form-label">User Name</label>
                        <input type="text" id="username" name="username"
                               class="form-control ${(usernameError??)?string('is-invalid','')}"
                               value="<#if user??>${user.username!''}</#if>"
                               placeholder="User name"/>
                        <#if usernameError??>
                            <div class="invalid-feedback">
                                ${usernameError}
                            </div>
                        </#if>
                    </div>

                    <#if path == "/registration">
                        <div class="mb-3">
                            <label for="email" class="form-label">Email</label>
                            <input type="email" id="email" name="email"
                                   class="form-control ${(emailError??)?string('is-invalid','')}"
                                   value="<#if user??>${user.email!''}</#if>"
                                   placeholder="email@company.com"/>
                            <#if emailError??>
                                <div class="invalid-feedback">
                                    ${emailError}
                                </div>
                            </#if>
                        </div>
                    </#if>

                    <div class="mb-3">
                        <label for="password" class="form-label ">Password</label>
                        <input type="password" name="password" id="password"
                               class="form-control ${(passwordError??)?string('is-invalid','')}"
                               placeholder="Password"/>
                        <#if passwordError??>
                            <div class="invalid-feedback">
                                ${passwordError}
                            </div>
                        </#if>
                    </div>

                    <#if path == '/registration'>
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
                    </#if>

                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>

                    <div class="d-flex justify-content-between align-items-center mt-4">
                        <#if path == "/login">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" id="rememberMe" name="remember-me">
                                <label class="form-check-label" for="rememberMe">Remember Me</label>
                            </div>
                            <div>
                                <input type="submit" value="Sign In" class="btn btn-primary"/>
                                <a href="/registration" class="btn btn-secondary ms-2">Registration</a>
                            </div>
                        <#elseif path == "/registration">
                            <input type="submit" value="Register" class="btn btn-primary"/>
                            <a href="/login" class="btn btn-secondary ms-2">Login page</a>
                        <#else>
                            <input type="submit" value="Submit" class="btn btn-primary"/>
                        </#if>
                    </div>
                </form>
            </div>
        </div>
    </div>
</#macro>


<#macro logout_btn>
    <form action="/logout" method="post">
        <button type="submit" class="btn btn-secondary ms-3">Sign Out</button>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    </form>
</#macro>

<#macro login_btn>
    <a href="/login" class="btn btn-primary ms-3" role="button" aria-pressed="true">Login</a>
</#macro>