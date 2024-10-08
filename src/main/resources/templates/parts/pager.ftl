<#macro pager url page>
    <#if page.getTotalPages() gt 7>
        <#assign
        totalPages = page.getTotalPages()
        pageNumber = page.getNumber() + 1

        head = (pageNumber > 4)?then([1, -1], [1, 2, 3])
        tail = (pageNumber < totalPages - 3)?then([-1, totalPages], [totalPages - 2, totalPages - 1, totalPages])
        bodyBefore = (pageNumber > 4 && pageNumber < totalPages - 1)?then([pageNumber - 2, pageNumber - 1], [])
        bodyAfter = (pageNumber > 2 && pageNumber < totalPages - 3)?then([pageNumber + 1, pageNumber + 2], [])

        body = head + bodyBefore + (pageNumber > 3 && pageNumber < totalPages - 2)?then([pageNumber], []) + bodyAfter + tail
        >
    <#else>
        <#assign body = 1..page.getTotalPages()>
    </#if>
    <div class="container mt-3">
        <div class="row">
            <ul class="pagination col justify-content-center">
                <li class="page-item disabled">
                    <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Pages</a>
                </li>
                <#list body as p>
                    <#if (p - 1) == page.getNumber()>
                        <li class="page-item active" aria-current="page">
                            <a class="page-link" href="#">${p}</a>
                        </li>
                    <#elseif p == -1>
                        <li class="page-item disabled" aria-current="page">
                            <a class="page-link" href="#">...</a>
                        </li>
                    <#else>
                        <li class="page-item" aria-current="page">
                            <a class="page-link" href="${url}?page=${p-1}&amp;size=${page.getSize()}">${p}</a>
                        </li>
                    </#if>
                </#list>
            </ul>

            <ul class="pagination col justify-content-center">
                <li class="page-item disabled">
                    <a class="page-link" href="#" tabindex="-1" aria-disabled="true">Элементов на странице</a>
                </li>
                <#list [5, 10, 25, 50] as c>
                    <#if c == page.getSize()>
                        <li class="page-item active" aria-current="page">
                            <a class="page-link" href="#">${c}</a>
                        </li>
                    <#else>
                        <li class="page-item" aria-current="page">
                            <a class="page-link" href="${url}?page=${page.getNumber()}&amp;size=${c}">${c}</a>
                        </li>
                    </#if>
                </#list>
            </ul>
        </div>
    </div>
</#macro>