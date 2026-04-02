<!DOCTYPE html>
<html>
<head>
    <title>Catalog Report</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #dddddd; text-align: left; padding: 8px; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>
<h1>Raportul Catalogului Meu</h1>
<table>
    <tr>
        <th>ID</th>
        <th>Titlu</th>
        <th>An</th>
        <th>Autor</th>
        <th>Locație</th>
    </tr>
    <#list resurse as res>
        <tr>
            <td>${res.id}</td>
            <td>${res.title}</td>
            <td>${res.year}</td>
            <td>${res.author}</td>
            <td><a href="${res.location}" target="_blank">Deschide Resursa</a></td>
        </tr>
    </#list>
</table>
</body>
</html>