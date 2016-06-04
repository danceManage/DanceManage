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
    <!--[if lt IE 9]>
    <script type="text/javascript" src="/script/html5.js"></script>
    <script type="text/javascript" src="/script/respond.min.js"></script>
    <script type="text/javascript" src="/script/PIE_IE678.js"></script>
    <![endif]-->
    <link href="/css/H-ui.min.css" rel="stylesheet" type="text/css" />
    <link href="/css/H-ui.admin.css" rel="stylesheet" type="text/css" />
    <link href="/css/iconfont.css" rel="stylesheet" type="text/css" />
    <link href="/css/iconfont/iconfont.css" rel="stylesheet" type="text/css" />
    <!--[if IE 6]>
    <script type="text/javascript" src="/script/DD_belatedPNG_0.0.8a-min.js" ></script>
    <script>DD_belatedPNG.fix('*');</script>
    <![endif]-->
    <title>新增图片</title>
</head>
<body>
<div class="pd-20">
    <a class="btn btn-success radius r mr-20" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" >
        <i class="Hui-iconfont">&#xe68f;</i></a>
    <form action="" method="post" class="form form-horizontal" id="form-article-add">
        <div class="row cl">
            <label class="form-label col-2"><span class="c-red">*</span>订单编号：</label>
            <div class="formControls col-2">
                <input type="text" class="input-text" value="${orderNo}" id="orderNo" name="orderNo" readonly>
            </div>
            <label class="form-label col-2"><span class="c-red">*</span>操作人：</label>
            <div class="formControls col-2">
                <input type="hidden" class="input-text" value="${_USER_SESSION_ID_.id}" id="operatorId" name="operatorId" readonly>
                <c:choose>
                    <c:when test="${empty operatorName}">
                        <input type="text" class="input-text" value="${_USER_SESSION_ID_.userName}"  readonly>
                    </c:when>
                    <c:otherwise>
                        <input type="text" class="input-text" value="${operatorName}"  readonly>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-2"><span class="c-red">*</span>送货人：</label>
            <div class="formControls col-2"> <span class="select-box">
				<select name="senderId" id="senderId" class="select">
                    <option value="">--请选择--</option>
                    <c:forEach items="${senderInfoList}" var="sendInfo">
                        <option value="${sendInfo.id}">${sendInfo.userName}</option>
                    </c:forEach>
                </select>
				</span>
            </div>
            <label class="form-label col-2"><span class="c-red">*</span>客户：</label>
            <div class="formControls col-2"> <span class="select-box">
				<select name="customerId" id="customerId" class="select" onchange="changeCustomer(this);">
                    <option value="">--请选择--</option>
                    <c:forEach items="${customerInfoList}" var="customer">
                        <option value="${customer.id}" title="${customer.customerName}" tphone="${customer.customerPhone}" taddress="${customer.customerAddress}">${customer.customerName}</option>
                    </c:forEach>
                </select>
				</span>
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-2">客户地址：</label>
            <div class="formControls col-10">
                <input type="text" class="input-text" value="${customerAddress}" id="customerAddress" readonly>
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-2">客户电话：</label>
            <div class="formControls col-2">
                <input type="text" class="input-text" value="${customerPhone}" id="customerPhone" readonly>
            </div>
        </div>

        <div class="row cl">
            <label class="form-label col-2">订单日期：</label>
            <div class="formControls col-4">
                <input type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" id="orderDate"
                       value="${orderDate}" class="input-text Wdate" style="width:180px;">
            </div>
            <label class="form-label col-2">送货日期：</label>
            <div class="formControls col-4">
                <input type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" id="senderDate"
                       value="${sendDate}" class="input-text Wdate" style="width:180px;">
            </div>
        </div>

        <div class="row cl">
            <input type="hidden" id="orderHidden"/>
            <span style="float: right;margin-bottom: 10px;">
                <a href="javascript:void(0);" onclick="order_add('添加品种','/product/productList.do?order=true')" class="btn btn-primary radius">
                <i class="Hui-iconfont">&#xe600;</i> 选择品种</a>
            </span>
            <table class="table table-border table-bordered table-bg">
                <thead>
                <tr>
                    <th scope="col" colspan="7">订单明细</th>
                </tr>
                <tr class="text-c">
                    <th width="40">ID</th>
                    <th width="200">种类</th>
                    <th width="80">数量</th>
                    <th width="80">单位</th>
                    <th>价格</th>
                    <th>总价</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody id="orderTbody">
                    <c:forEach items="${orderDetailList}" var="orderDetail" varStatus="ct">
                        <tr class="text-c">
                            <td>${ct.count}<input type="hidden" name="id" value="${orderDetail.id}"/></td>
                            <td><input type="text" name="productName" value="${orderDetail.productName}" class="input-text"/></td>
                            <td><input type="text" name="amount" value="${orderDetail.amount}" onkeyup="this.value=this.value.replace(/[^0-9.]/g,'')" oninput="computeMoney(this);" class="input-text"/></td>
                            <td><input type="text" name="unit" value="${orderDetail.unit}" class="input-text"/></td>
                            <td><input type="text" name="price" onkeyup="this.value=this.value.replace(/[^0-9.]/g,'')"
                                       oninput="computeMoneyByPrice(this);" value="${orderDetail.price}" class="input-text"/></td>
                            <td><input type="text" name="totalMoney" value="${orderDetail.totalMoney}" class="input-text"/></td>
                            <td class="td-manage">
                                <a title="删除" href="javascript:void(0);" onclick="orderTd_del(this,${orderDetail.id})" class="ml-5" style="text-decoration:none">
                                    <i class="Hui-iconfont">&#xe6e2;</i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
                <tr>
                    <th scope="col" colspan="7" id="orderMoney" title="122.89" style="color: red;">订单总额：122.89</th>
                </tr>
            </table>
        </div>

        <div class="row cl">
            <div class="col-7 col-offset-2" style="text-align: center;">
                <%--<button onClick="article_save_submit();" class="btn btn-primary radius" type="submit"><i class="Hui-iconfont">&#xe632;</i> 保存订单</button>--%>
                <button onClick="saveOrder();" class="btn btn-secondary radius" type="button"><i class="Hui-iconfont">&#xe632;</i>保存订单</button>
                <c:if test="${not empty orderInfo.id}">
                    <button onClick="exportExcel('${orderInfo.id}');" class="btn btn-secondary radius" type="button"><i class="icon iconfont">&#xe602;</i>导出Excel</button>
                </c:if>
                <button onClick="layer_close();" class="btn btn-default radius" type="button">&nbsp;&nbsp;取消&nbsp;&nbsp;</button>
            </div>
        </div>
    </form>
</div>
</div>
<script type="text/javascript" src="/script/jquery.min.js"></script>
<script type="text/javascript" src="/script/layer.js"></script>
<script type="text/javascript" src="/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/script/validform/Validform.min.js"></script>
<script type="text/javascript" src="/script/H-ui.js"></script>
<script type="text/javascript" src="/script/H-ui.admin.js"></script>
<script type="text/javascript" src="/icheck/jquery.icheck.min.js"></script>
<script type="text/javascript" src="/script/jquery.json.js"></script>
<script type="text/javascript">
    $(document).ready(function(){
        countMoney();
    });
    $(function(){
        $('.skin-minimal input').iCheck({
            checkboxClass: 'icheckbox-blue',
            radioClass: 'iradio-blue',
            increaseArea: '20%'
        });
    });

    $(document).ready(function(){
        <c:if test="${not empty orderInfo.senderId}">
            $("#senderId").val(${orderInfo.senderId});
        </c:if>
        <c:if test="${not empty orderInfo.customerId}">
            $("#customerId").val(${orderInfo.customerId});
        </c:if>
    });

    function order_add(title,url,w,h)
    {
        layer_show(title,url,w,h);
    }

    function countMoney()
    {
        var orderMoney = 0;
        $("#orderTbody").find("input[name='totalMoney']").each(function(){
            orderMoney = orderMoney + parseFloat($(this).val());
        });
        $("#orderMoney").attr("title",orderMoney.toFixed(2));
        $("#orderMoney").text("订单总额：" + orderMoney.toFixed(2));
    }

    function changeCustomer(obj)
    {
        var customerPhone = $($(obj).find("option:selected")[0]).attr("tphone");
        var customerAddress = $($(obj).find("option:selected")[0]).attr("taddress");
        $("#customerAddress").val(customerAddress);
        $("#customerPhone").val(customerPhone);
    }

    function exportExcel(orderId)
    {
        location.href = '/order/excel/export.do?id=' + orderId;
    }

    function saveOrder()
    {
        var orderNo= "${orderNo}";
        var orderId = "${orderInfo.id}";
        var orderDate = $("#orderDate").val();
        var operatorId = $("#operatorId").val();
        var customerId = $("#customerId").val();
        var senderId = $("#senderId").val();
        var senderDate = $("#senderDate").val();
        var totalMoney = $("#totalMoney").val();
        if(senderId.length == 0)
        {
            layer.alert("请选择送货人");
            return;
        }
        if(customerId.length == 0)
        {
            layer.alert("请选择客户");
            return;
        }
        $.ajax({
            url : "/order/saveOrder.do",
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            dataType:"json",
            type:"POST",
            async:false,
            data: $.toJSON({id:orderId,orderNo:orderNo,orderDate:orderDate,operatorId:operatorId,customerId:customerId,senderId:senderId,senderDate:senderDate,totalMoney:totalMoney}),
            success: function(re)
            {
                if(re.ok)
                {
                    if(orderId.length == 0)
                    {
                        orderId = re.id;
                    }
                    saveOrderDetail(orderId);
                }
            }
        });
    }


    function orderTd_del(obj,ids)
    {
        if(confirm("确认删除？"))
        {
            if(ids.length == 0)
            {
                $(obj).parent().parent().remove();
            }
            else
            {
                $.ajax({
                    url : "/order/delOrderDetail.do",
                    contentType: "application/x-www-form-urlencoded; charset=utf-8",
                    dataType:"json",
                    type:"POST",
                    async:false,
                    data: $.toJSON({id:ids}),
                    success: function(re)
                    {
                        if(re.ok)
                        {
                            $(obj).parent().parent().remove();
                            layer.msg('已删除!',{icon:1,time:1000});
                        }
                    }
                });
            }
        }
    }

    function saveOrderDetail(orderId)
    {
        var isSuccess = true;
        //保存订单明细
        $("#orderTbody").find("tr").each(function(){
            var id = $($(this).find("input[name='id']")[0]).val()
            var productName = $($(this).find("input[name='productName']")[0]).val()
            var price = $($(this).find("input[name='price']")[0]).val()
            var amount = $($(this).find("input[name='amount']")[0]).val()
            var unit = $($(this).find("input[name='unit']")[0]).val()
            var totalMoney = $($(this).find("input[name='totalMoney']")[0]).val()
            var detail = {id:id,orderId:orderId,productName:productName,price:price,amount:amount,unit:unit,totalMoney:totalMoney};
            $.ajax({
                url : "/order/saveOrderDetail.do",
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                dataType:"json",
                type:"POST",
                async:false,
                data: $.toJSON(detail),
                success: function(re)
                {
                    if(!re.ok)
                    {
                        isSuccess = false;
                    }
                }
            });
        });

        if(isSuccess)
        {
            refresh_layer_close();
        }
    }
</script>
</body>
</html>
