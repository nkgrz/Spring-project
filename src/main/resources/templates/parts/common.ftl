<#macro page>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Sweater</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
              rel="stylesheet"
              integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
              crossorigin="anonymous">
    </head>
    <style>
        .full-height {
            height: 100vh;
        }
    </style>
    <body>
    <div class="d-flex align-items-center justify-content-center full-height">
        <div class="container my-5">
            <#nested>
        </div>
    </div>
    </body>
    </html>
</#macro>