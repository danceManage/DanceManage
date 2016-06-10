<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <title>演示：jQuery自动完成插件：completer</title>
    <meta name="keywords" content="jquery" />
    <meta name="description" content="jQuery自动完成插件：completer" />
    <link rel="stylesheet" type="text/css" href="/completer/completer.min.css">
    <link rel="stylesheet" href="/css/bootstrap.css">
    <link rel="stylesheet" href="/autocomplete/jquery-ui.min.css">
    <style type="text/css">
        .demo{width:360px;margin:50px auto 10px auto;padding:10px;}
        .demo p{line-height:30px}
    </style>
</head>
<body>
<div id="main">
    <div class="demo">
        <p>1、输入邮箱号：</p>
        <input type="text" id="auto-complete-email" class="form-control" placeholder="E-mail">
        <p>&nbsp;</p>
        <p>2、输入时间：</p>
        <input type="text" id="auto-complete-time" class="form-control">
        <p>&nbsp;</p>
        <p>3、输入域名：</p>
        <div class="input-group">
            <span class="input-group-addon">www.</span>
            <input id="auto-complete-domain" class="form-control" type="text" placeholder="请输入域名" autocomplete="off" style="z-index:0">
            <span class="input-group-btn"><a id="auto-complete-go" class="btn btn-default" href="javascript:void(0);">Go!</a></span>
        </div>
        <p>4、种类1</p>
        <input type="text" class="form-control category1">
        <p>5、种类2</p>
        <table>
            <tr><td><input type="text" name="productName" class="form-control category"></td><td><input type="text" name="price" class="form-control"></td></tr>
            <tr><td><input type="text" name="productName" class="form-control category"></td><td><input type="text" name="price" class="form-control"></td></tr>
        </table>
    </div>
</div>
<script src="/script/jquery.min.js"></script>
<script src="/completer/completer.min.js"></script>
<script src="/autocomplete/jquery-ui.min.js"></script>
<script type="text/javascript">
    $(function(){
        var availableTags = ["dbc:大白菜:2.5", "hlb:胡萝卜:1.5", "jxc:卷心菜:3.5", "ymc:鸭苗菜:4.5", "dy:豆芽:6", "xqc：小青菜:3.6", "tds:土豆丝:2.5"];
        $( ".category" ).autocomplete({
            autoFocus: true,
            select: function( event, ui ){
                var value = ui.item.value;
                if(value != undefined && value.indexOf(":") > -1)
                {
                    var valArr = value.split(":");
                    this.value = valArr[1];
                    var parentTr = $(this).parent().parent()[0];
                    var price = $(parentTr).find("input[name='price']")[0];
                    $(price).val(valArr[2]);
                    var nextTr = $(parentTr).next();
                    var nextProduct = $(nextTr).find("input[name='productName']")[0];
                    nextProduct.focus();
                    return false;
                }
            },
            source: availableTags
        });
        $(".category1").completer({
            filter: function(val) {
                return val;
            },
            source: ["dbc:大白菜", "hlb:胡萝卜", "jxc:卷心菜", "ymc:鸭苗菜", "dy:豆芽", "xqc：小青菜", "tds:土豆丝"]
        });

        $("#auto-complete-email").completer({
            separator: "@",
            source: ["163.com", "qq.com", "126.com", "139.com", "gmail.com", "hotmail.com", "icloud.com"]
        });
        $("#auto-complete-time").completer({
            filter: function(val) {
                val = val.replace(/\D/g, "").substr(0, 2);

                if (val) {
                    val = parseInt(val, 10) || 0;
                    val = val > 23 ? 23 : val < 10 ? "0" + val : val;
                }

                return val;
            },
            separator: ":",
            source: ["00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"]
        });
        var $autoCompleteDomain = $("#auto-complete-domain"),
                $autoCompleteGo = $("#auto-complete-go");

        $autoCompleteDomain.completer({
            complete: function() {
                var url = "http://www." + $autoCompleteDomain.val();

                $autoCompleteGo.attr("href", url);
            },
            separator: ".",
            source: ["com", "net", "org", "co", "io", "me", "cn", "com.cn"]
        });
    });
</script>
</body>
</html>
