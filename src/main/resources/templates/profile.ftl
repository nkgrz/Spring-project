<#import "parts/common.ftl" as c>
<#import "parts/infoMessage.ftl" as m>
<@c.page>
    <h3 class="card-title text-center mb-4">Profile</h3>
    <h4 class="card-title text-center mb-4">${username}</h4>

    <@m.msg></@m.msg>

    <div class="container mt-2" style="max-width: 400px;">
        <div class="card shadow-sm">
            <div class="card-body">
                <h5 class="card-title text-center mb-4">Update Your Profile</h5>
                <form method="post">
                    <div class="mb-3">
                        <label for="username" class="form-label">User Name</label>
                        <input type="text" class="form-control" id="username" name="username"
                               value="${username}" placeholder="User name" required/>
                    </div>

                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" class="form-control" id="email" name="email"
                               value="${email!""}" placeholder="email@company.com"/>
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
