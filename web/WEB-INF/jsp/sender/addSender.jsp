<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,member-scalable=no" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <!--[if lt IE 9]>
    <script type="text/javascript" src="/script/html5.js"></script>
    <script type="text/javascript" src="/script/respond.min.js"></script>
    <script type="text/javascript" src="/script/PIE_IE678.js"></script>
    <![endif]-->
    <link href="/css/H-ui.min.css" rel="stylesheet" type="text/css" />
    <link href="/css/H-ui.admin.css" rel="stylesheet" type="text/css" />
    <link href="/css/icheck/icheck.css" rel="stylesheet" type="text/css" />
    <link href="/css/iconfont.css" rel="stylesheet" type="text/css" />
    <!--[if IE 6]>
    <script type="text/javascript" src="/script/DD_belatedPNG_0.0.8a-min.js" ></script>
    <script>DD_belatedPNG.fix('*');</script>
    <![endif]-->
    <title>添加送货员</title>
</head>
<body>
<div class="pd-20">
    <form action="/sender/addSender.do" method="post" class="form form-horizontal" id="form-member-add">
        <input type="hidden" id="senderId" name="senderId" value="${senderInfo.id}"/>
        <div class="row cl">
            <label class="form-label col-3"><span class="c-red">*</span>姓名：</label>
            <div class="formControls col-5">
                <input type="text" class="input-text" value="${senderInfo.userName}" id="userName" name="userName" datatype="*2-16" nullmsg="姓名不能为空">
            </div>
            <div class="col-4"> </div>
        </div>
        <div class="row cl">
            <label class="form-label col-3"><span class="c-red">*</span>电话：</label>
            <div class="formControls col-5">
                <input type="text" class="input-text" value="${senderInfo.phone}" id="phone" name="phone" datatype="*2-16" nullmsg="电话不能为空">
            </div>
            <div class="col-4"> </div>
        </div>
        <div class="row cl">
            <div class="col-9 col-offset-3">
                <input class="btn btn-primary radius" type="button" onclick="addSender();" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
                <input class="btn btn-primary radius" type="button" onclick="layer_close();" value="&nbsp;&nbsp;取消&nbsp;&nbsp;">
            </div>
        </div>
    </form>
</div>
</div>
<script type="text/javascript" src="/script/jquery.min.js"></script>
<script type="text/javascript" src="/script/jquery.icheck.min.js"></script>
<script type="text/javascript" src="/script/validform/Validform.min.js"></script>
<script type="text/javascript" src="/script/layer.js"></script>
<script type="text/javascript" src="/script/H-ui.js"></script>
<script type="text/javascript" src="/script/H-ui.admin.js"></script>
<script type="text/javascript" src="/script/jquery.json.js"></script>
<script type="text/javascript">
    $(function(){
        $('.skin-minimal input').iCheck({
            checkboxClass: 'icheckbox-blue',
            radioClass: 'iradio-blue',
            increaseArea: '20%'
        });

        $("#form-member-add").Validform({
            tiptype:2,
            callback:function(form){
                form[0].submit();
                var index = parent.layer.getFrameIndex(window.name);
                parent.$('.btn-refresh').click();
                parent.layer.close(index);
            }
        });
    });

    function addSender()
    {
        var userName = $("#userName").val();
        var phone = $("#phone").val();
        var id = $("#senderId").val();
        $.ajax({
            url : "/sender/saveSender.do",
            contentType: "application/x-www-form-urlencoded; charset=utf-8",
            dataType:"json",
            type:"POST",
            data:$.toJSON({ userName: userName, phone:phone,id:id}),
            success: function(re)
            {
                if(re.ok)
                {
                    refresh_layer_close();
                }
                else
                {
                    alert(re.errorMsg);
                }
            }
        });
    }
</script>
</body>
</html>
