﻿<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<title>角色管理</title>
</head>
<body>
<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 送货员 <span class="c-gray en">&gt;</span> 送货员管理 <a class="btn btn-success radius r mr-20" style="line-height:1.6em;margin-top:3px" id="refreshDiv" href="javascript:void(0);" onclick="refreshDiv();" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
<div class="pd-20">
	<div class="cl pd-5 bg-1 bk-gray"> <span class="l"> <%--<a href="javascript:;" onclick="datadel()" class="btn btn-danger radius"><i class="Hui-iconfont">&#xe6e2;</i> 批量删除</a>--%> <a class="btn btn-primary radius" href="javascript:void(0);" onclick="admin_role_add('添加送货员','/sender/addSender.do','500','310')"><i class="Hui-iconfont">&#xe600;</i> 添加送货员</a> </span> <span class="r">共有数据：<strong>${senderInfoList.size()}</strong> 条</span> </div>
	<table class="table table-border table-bordered table-hover table-bg">
		<thead>
			<tr>
				<th scope="col" colspan="6">送货员管理</th>
			</tr>
			<tr class="text-c">
				<%--<th width="25"><input type="checkbox" value="" name=""></th>--%>
				<th width="40">ID</th>
				<th width="200">姓名</th>
				<th>电话</th>
				<th width="300">创建时间</th>
				<th width="70">操作</th>
			</tr>
		</thead>
		<tbody>
            <c:forEach items="${senderInfoList}" var="sender" varStatus="ct">
                <tr class="text-c">
                    <%--<td><input type="checkbox" value="" name=""></td>--%>
                    <td>${ct.count}</td>
                    <td>${sender.userName}</td>
                    <td>${sender.phone}</td>
                    <td><fmt:formatDate value="${sender.createTime}" pattern="yyyy-MM-dd"/></td>
                    <td class="f-14">
                        <a style="text-decoration:none" class="ml-5" onClick="admin_role_add('送货员编辑','/sender/addSender.do?id=${sender.id}','500','310')" href="javascript:;" title="编辑"><i class="Hui-iconfont">&#xe6df;</i></a>
                        <a title="删除" href="javascript:;" onclick="admin_role_del(this,'${sender.id}')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6e2;</i></a>
                    </td>
                </tr>
            </c:forEach>
		</tbody>
	</table>
</div>
<script type="text/javascript" src="/script/jquery.min.js"></script>
<script type="text/javascript" src="/script/layer.js"></script>
<script type="text/javascript" src="/script/H-ui.js"></script>
<script type="text/javascript" src="/script/H-ui.admin.js"></script>
<script type="text/javascript" src="/script/jquery.json.js"></script>
<script type="text/javascript">
function refreshDiv()
{
    location.replace(location.href);
}
/*管理员-角色-添加*/
function admin_role_add(title,url,w,h){
	layer_show(title,url,w,h);
}
/*管理员-角色-编辑*/
function admin_role_edit(title,url,id,w,h){
	layer_show(title,url,w,h);
}
/*管理员-角色-删除*/
function admin_role_del(obj,id){
	layer.confirm('角色删除须谨慎，确认要删除吗？',function(index){
        $.ajax({
            url : "/sender/delSender.do",
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
                    refreshDiv();
                }
            }
        });
	});
}
</script>
</body>
</html>