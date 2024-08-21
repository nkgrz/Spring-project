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
                        <input type="text" class="form-control" id="username" name="username" placeholder="User name"/>
<#--                               required/>-->
                    </div>

                    <#if path == "/registration">
                        <div class="mb-3">
                            <label for="email" class="form-label">Email</label>
                            <input type="email" class="form-control" id="email" name="email" placeholder="email@company.com"/>
                        </div>
                    </#if>

                    <div class="mb-3">
                        <label for="password" class="form-label">Password</label>
                        <input type="password" class="form-control" id="password" name="password" placeholder="Password"/>
<#--                               required/>-->
                    </div>

                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>

                    <div class="d-flex justify-content-between mt-4">
                        <#if path == "/login">
                            <input type="submit" value="Sign In" class="btn btn-primary"/>
                            <a href="/registration" class="btn btn-secondary">Registration</a>
                        <#elseif path == "/registration">
                            <input type="submit" value="Register" class="btn btn-primary"/>
                            <a href="/login" class="btn btn-secondary">Login page</a>
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