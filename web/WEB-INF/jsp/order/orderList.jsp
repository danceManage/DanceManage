<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <LINK rel="Bookmark" href="/favicon.ico" >
    <LINK rel="Shortcut Icon" href="/favicon.ico" />
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
    <title>管理员列表</title>
</head>
<body>
<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 订单
    <span class="c-gray en">&gt;</span> 订单列表
    <a class="btn btn-success radius r mr-20" id="refreshDiv" style="line-height:1.6em;margin-top:3px" onclick="refreshDiv();" href="javascript:void(0);" title="刷新" >
        <i class="Hui-iconfont">&#xe68f;</i></a></nav>
<div class="pd-20">
    <div class="text-c"> 日期范围：

        <input type="text" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endDate\')||\'%y-%M-%d\'}'})" value="${startDate}" id="startDate" class="input-text Wdate" style="width:120px;">
        -
        <input type="text" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\')}',maxDate:'%y-%M-%d'})" value="${endDate}" id="endDate" class="input-text Wdate" style="width:120px;">
        <input type="text" class="input-text" style="width:250px" placeholder="输入订单号" value="${orderNo}" id="orderNo">
        <button type="button" class="btn btn-success" onclick="doSearch();"><i class="Hui-iconfont">&#xe665;</i> 搜索</button>
    </div>
    <div class="cl pd-5 bg-1 bk-gray mt-20">
        <span class="l"><a href="javascript:;" onclick="orderChkDel();" class="btn btn-danger radius"><i class="Hui-iconfont">&#xe6e2;</i> 批量删除</a>
            <a href="javascript:;" onclick="order_add('添加订单','/order/addOrder.do')" class="btn btn-primary radius">
                <i class="Hui-iconfont">&#xe600;</i> 添加订单</a></span> <span class="r">共有数据：<strong>${total}</strong> 条</span> </div>
    <table class="table table-border table-bordered table-bg">
        <thead>
        <tr>
            <th scope="col" colspan="9">员工列表</th>
        </tr>
        <tr class="text-c">
            <th width="25"><input type="checkbox" name="" value=""></th>
            <th width="40">序号</th>
            <th width="40">订单号</th>
            <th width="150">客户</th>
            <th width="90">送货人</th>
            <th width="150">操作人</th>
            <th>订单日期</th>
            <th width="130">送货日期</th>
            <th width="100">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${orderList}" var="order" varStatus="ct">
            <tr class="text-c">
                <td><input type="checkbox" name="checkOrder" value="${order.orderId}"></td>
                <td>${ct.count}</td>
                <td>${order.orderNo}</td>
                <td>${order.customerName}</td>
                <td>${order.sendName}</td>
                <td>${order.userName}</td>
                <td>${order.orderDate}</td>
                <td>${order.senderDate}</td>
                    <%--<td class="td-status"><span class="label label-success radius">已启用</span></td>--%>
                    <%--<td class="td-status"><span class="label radius">已停用</span></td>--%>
                <td class="td-manage">
                    <%--<a style="text-decoration:none" onClick="admin_stop(this,'10001')" href="javascript:;" title="停用"><i class="Hui-iconfont">&#xe631;</i></a>--%>
                    <a title="编辑" href="javascript:;" onclick="order_edit('订单编辑','/order/addOrder.do?id=${order.orderId}')" class="ml-5" style="text-decoration:none">
                        <i class="Hui-iconfont">&#xe6df;</i>
                    </a>
                    <a title="删除" href="javascript:;" onclick="order_del(this,'${order.orderId}')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6e2;</i></a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <br/>
    <div id="tabPage" style="float: right;"></div>
</div>
<script type="text/javascript" src="/script/jquery.min.js"></script>
<script type="text/javascript" src="/script/layer.js"></script>
<script type="text/javascript" src="/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/laypage/1.2/laypage.js"></script>
<script type="text/javascript" src="/script/H-ui.js"></script>
<script type="text/javascript" src="/script/H-ui.admin.js"></script>
<script type="text/javascript" src="/script/jquery.json.js"></script>
<script type="text/javascript">
    var currentPage = "${page}";
    if(currentPage.length == 0 )
    {
        currentPage = 0;
    }
    else
    {
        currentPage = parseInt(currentPage);
    }
    var totalCnt = "${total}";
    if(totalCnt.length == 0 )
    {
        totalCnt = 0;
    }
    else
    {
        totalCnt = parseInt(totalCnt);
    }
    var totalPage = 0;
    if(totalCnt %10 == 0 )
    {
        totalPage = totalCnt/10;
    }
    else
    {
        totalPage = Math.ceil(totalCnt/10);
    }
    laypage({
        cont: 'tabPage', //容器。值支持id名、原生dom对象，jquery对象,
        pages: totalPage, //总页数
        skip: true, //是否开启跳页
        skin: '#AF0000',
        groups: 3, //连续显示分页数
        curr:currentPage,
        jump: function(e, first){ //触发分页后的回调
            if(!first){ //一定要加此判断，否则初始时会无限刷新
                location.href = '/order/orderList.do?page='+e.curr;
            }
        }
    });
    /*管理员-增加*/
    function order_add(title,url){
        var index = layer.open({
            type: 2,
            title: title,
            content: url
        });
        layer.full(index);
    }

    function doSearch()
    {
        var url = "/order/orderList.do?page=1";
        var startDate = $("#startDate").val();
        var endDate = $("#endDate").val();
        var orderNo = $("#orderNo").val();
        if(startDate.length > 0)
        {
            url += "&startDate=" + startDate;
        }
        if(endDate.length > 0)
        {
            url += "&endDate=" + endDate;
        }
        if(orderNo.length > 0)
        {
            url += "&orderNo=" + orderNo;
        }
        location.href = url;
    }

    function orderChkDel()
    {
        var chkLength = $("input[name='checkOrder']:checked").length;
        if(chkLength == 0)
        {
            layer.alert("请选择需要删除的订单");
        }
        else
        {
            var isSuccess = false;
            $("input[name='checkOrder']:checked").each(function(){
                $.ajax({
                    url : "/order/delOrder.do",
                    contentType: "application/x-www-form-urlencoded; charset=utf-8",
                    dataType:"json",
                    type:"POST",
                    async:false,
                    data: $.toJSON({id:$(this).val()}),
                    success: function(re)
                    {
                        if(re.ok)
                        {
                            isSuccess = true;
                            $(this).parent().parent().remove();
                        }
                    }
                });
            });
            if(isSuccess)
            {
                layer.msg('已删除!',{icon:1,time:1000});
                refreshDiv();
            }
        }
    }

    $("input[name='checkOrder']:checked").each();

    function refreshDiv()
    {
        location.replace(location.href);
    }

    function order_del(obj,orderId)
    {
        if(confirm("确认删除该订单？"))
        {
            $.ajax({
                url : "/order/delOrder.do",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                dataType:"json",
                type:"POST",
                async:false,
                data: $.toJSON({id:orderId}),
                success: function(re)
                {
                    if(re.ok)
                    {
                        $(obj).parent().parent().remove();
                        layer.msg('已删除!',{icon:1,time:1000});
                        refreshDiv();
                    }
                }
            });
        }
    }

    /*管理员-删除*/
    function admin_del(obj,id){
        layer.confirm('确认要删除吗？',function(index){
            //此处请求后台程序，下方是成功后的前台处理……

            $(obj).parents("tr").remove();
            layer.msg('已删除!',{icon:1,time:1000});
        });
    }
    /*管理员-编辑*/
    function order_edit(title,url,id,w,h){
        var index = layer.open({
            type: 2,
            title: title,
            content: url
        });
        layer.full(index);
    }
    /*管理员-停用*/
    function admin_stop(obj,id){
        layer.confirm('确认要停用吗？',function(index){
            //此处请求后台程序，下方是成功后的前台处理……

            $(obj).parents("tr").find(".td-manage").prepend('<a onClick="admin_start(this,id)" href="javascript:;" title="启用" style="text-decoration:none"><i class="Hui-iconfont">&#xe615;</i></a>');
            $(obj).parents("tr").find(".td-status").html('<span class="label label-default radius">已禁用</span>');
            $(obj).remove();
            layer.msg('已停用!',{icon: 5,time:1000});
        });
    }

    /*管理员-启用*/
    function admin_start(obj,id){
        layer.confirm('确认要启用吗？',function(index){
            //此处请求后台程序，下方是成功后的前台处理……


            $(obj).parents("tr").find(".td-manage").prepend('<a onClick="admin_stop(this,id)" href="javascript:;" title="停用" style="text-decoration:none"><i class="Hui-iconfont">&#xe631;</i></a>');
            $(obj).parents("tr").find(".td-status").html('<span class="label label-success radius">已启用</span>');
            $(obj).remove();
            layer.msg('已启用!', {icon: 6,time:1000});
        });
    }
</script>
</body>
</html>
