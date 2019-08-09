<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    hello,${username}
    <hr/>
    id:${product.id}
    name:${product.name}
    price:${product.price}
    createTime:${product.createTime?date}
    createTime:${product.createTime?time}
    createTime:${product.createTime?datetime}
    <hr/>
    <#list list as product>
        index:${product_index}
        id:${product.id}
        name:${product.name}
        price:${product.price}
        createTime:${product.createTime?date}
    </#list>
    <hr/>
    <#if (age>50)>
        中年
        <#elseif (age>40)>
        青年
        <#else>
        少年
    </#if>
    <hr/>
    ----> ${nullObject!}
    ----> ${nullObject!'ok'}

</body>
</html>