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
                        <label for="oldPassword" class="form-label">Current Password</label>
                        <input type="password" class="form-control" id="oldPassword" name="oldPassword"
                               placeholder="Enter your current password" required/>
                    </div>

                    <div class="mb-3">
                        <label for="newPassword" class="form-label">New Password</label>
                        <input type="password" class="form-control" id="newPassword" name="newPassword"
                               placeholder="Enter your new password" required/>
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
