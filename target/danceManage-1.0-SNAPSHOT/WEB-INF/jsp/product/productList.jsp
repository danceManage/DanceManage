<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<!--[if lt IE 9]>
<script type="text/javascript" src="/script/html5.js"></script>
<script type="text/javascript" src="/script/respond.min.js"></script>
<script type="text/javascript" src="/script/PIE_IE678.js"></script>
<![endif]-->
<link href="/css/H-ui.min.css" rel="stylesheet" type="text/css" />
<link href="/css/H-ui.admin.css" rel="stylesheet" type="text/css" />
<link href="/css/style.css" rel="stylesheet" type="text/css" />
<link href="/css/iconfont.css" rel="stylesheet" type="text/css" />
<!--[if IE 6]>
<script type="text/javascript" src="/script/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->
<title>建材列表</title>
</head>
<body class="pos-r">
<div>
	<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 品种 <span class="c-gray en">&gt;</span>
        品种列表 <a class="btn btn-success radius r mr-20" id="refreshDiv" style="line-height:1.6em;margin-top:3px" href="javascript:void(0);"onclick="refreshDiv();" title="刷新" >
            <i class="Hui-iconfont">&#xe68f;</i></a></nav>
	<div class="pd-20">
		<div class="cl pd-5 bg-1 bk-gray mt-20">
            <span class="l">
            <%--<a href="javascript:;" onclick="datadel()" class="btn btn-danger radius"><i class="Hui-iconfont">&#xe6e2;</i> 批量删除</a>--%>
                <a class="btn btn-primary radius" onclick="add_Product('添加产品','/product/addProduct.do','600','310')" href="javascript:void(0);">
                <i class="Hui-iconfont">&#xe600;</i> 添加产品</a>
                <c:if test="${order}">
                    <a class="btn btn-primary radius" onclick="checkProduct();" href="javascript:void(0);">确定</a>
                </c:if>
            </span>
            <span class="r">共有数据：<strong>${total}</strong> 条</span>
        </div>
		<div class="mt-20">
			<table class="table table-border table-bordered table-bg table-hover table-sort">
				<thead>
					<tr class="text-c">
                        <c:if test="${order}">
                            <th width="25"><input type="checkbox" name="" value=""></th>
                        </c:if>
						<th width="40">ID</th>
						<th width="60">名称</th>
						<th width="100">快捷键</th>
						<th width="100">价格</th>
						<th width="100">单位</th>
						<th width="60">创建日期</th>
						<th width="100">操作</th>
					</tr>
				</thead>
				<tbody>
                    <c:forEach items="${productList}" varStatus="ct" var="product">
                        <tr class="text-c va-m">
                            <c:if test="${order}">
                                <td><input type="checkbox" value="${product.id}:${product.productName}:${product.price}:${product.unit}" name="checkProduct"></td>
                            </c:if>
                            <td>${ct.count}</td>
                            <td>${product.productName}</td>
                            <td>${product.productShort}</td>
                            <td>${product.price}</td>
                            <td>${product.unit}</td>
                            <td><fmt:formatDate value="${product.createTime}" pattern="yyyy-MM-dd"/></td>
                            <td class="td-manage">
                                <a style="text-decoration:none" class="ml-5" onClick="add_Product('产品编辑','/product/addProduct.do?id=${product.id}','600','310')" href="javascript:;" title="编辑"><i class="Hui-iconfont">&#xe6df;</i></a>
                                <a style="text-decoration:none" class="ml-5" onClick="product_del(this,'${product.id}')" href="javascript:;" title="删除"><i class="Hui-iconfont">&#xe6e2;</i></a>
                            </td>
                        </tr>
                    </c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
<script type="text/javascript" src="/script/jquery.min.js"></script>
<script type="text/javascript" src="/script/layer.js"></script>
<script type="text/javascript" src="/script/H-ui.js"></script>
<script type="text/javascript" src="/script/H-ui.admin.js"></script>
<script type="text/javascript" src="/script/jquery.json.js"></script>
<script type="text/javascript">
    /*管理员-角色-添加*/
    function add_Product(title,url,w,h){
        layer_show(title,url,w,h);
    }
    function refreshDiv()
    {
        location.replace(location.href);
    }

    function product_del(obj,id){
        layer.confirm('确认要删除该品种吗？',function(index){
            $.ajax({
                url : "/product/delProduct.do",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                dataType:"json",
                type:"POST",
                data:$.toJSON({ id: id}),
                success: function(re)
                {
                    if(re.ok)
                    {
                        $(obj).parents("tr").remove();
                        layer.msg('已删除!',{icon:1,time:1000});
                    }
                }
            });
        });
    }

    function checkProduct()
    {
        var productArr = [];
        $("input[name='checkProduct']:checked").each(function(){
            productArr.push($(this).val());
        });
        $(parent.document.getElementById("orderHidden")).val(productArr);

        check_layer_close(productArr);
    }
</script>
</body>
</html>