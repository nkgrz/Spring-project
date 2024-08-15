<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>
<@c.page>
    <div class="navbar justify-content-end">
        <span><a href="/user" class="btn btn-secondary me-2">User list</a></span>
        <@l.logout></@l.logout>
    </div>
    <br><br>
    <div>
        <form method="post" enctype="multipart/form-data">
            <h3>Добавить сообщение</h3>
            <label><input type="text" class="col" name="text" placeholder="Введите сообщение"/></label>
            <label><input type="text" class="col" name="tag" placeholder="Тег"></label>
            <input type="file" name="file">
            <button type="submit" class="btn btn-primary">Добавить</button>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        </form>
    </div>

    <br><h3>Список сообщений</h3>
    <form method="get" action="/main">
        <label><input type="text" name="tag" value="${tag!""}" placeholder="Поиск по тегу"></label>
        <button type="submit" class="btn btn-primary">Найти</button>
    </form>


    <#if messages?has_content>
        <table class="table">
            <thead>
            <tr>
                <th>ID сообщения</th>
                <th>Текст</th>
                <th>Тег</th>
                <th>Автор</th>
                <th>Img</th>
            </tr>
            </thead>
            <tbody>
            <#list messages as msg>
                <tr>
                    <td>${msg.id}</td>
                    <td>${msg.text}</td>
                    <td>${msg.tag}</td>
                    <td>${msg.authorName}</td>
                    <td><div>
                            <#if msg.filename??>
                                <img src="/img/${msg.filename}" alt="image" style="max-width: 200px; max-height: 100px;">
                            </#if>
                        </div></td>
                </tr>
            </#list>
            </tbody>
        </table>
    <#else>
        No messages available.
    </#if>
    <br><br>
    <form method="post" action="delete">
        <button type="submit" class="btn btn-warning">Удалить все</button>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    </form>

</@c.page>