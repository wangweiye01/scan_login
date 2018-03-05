<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="scripts/jquery-1.11.0.min.js"></script>
<style type="text/css">
body {
	background-color: gray;
	text-align: center;
}

.main {
	width: 370px;
	height: 460px;
	background-color: white;
	top: 50%;
	left: 50%;
	text-align: center;
	position: absolute;
	border-radius: 4px;
	margin-left: -185px;
	margin-top: -230px;
}

.title {
	padding: 20px;
	font-size: 20px;
}
</style>
<script type="text/javascript">
	$(function() {
		// 文档就绪
		$("#qrcode").attr("src", "/qrcode/${uuid}");
	    $("#result").html("使用手机扫描二维码");
		keepPool();
	});

	function keepPool(){
		$.post("/pool", {
            uuid : "${uuid}",
        }, function(data) {
            if(data=='success'){
              $("#result").html("登录成功");
            }else if(data=='timeout'){
            	$("#result").html("登录超时，请刷新重试");
            }else{
                keepPool();
            }
        });
	}
</script>
</head>
<body>
	<div class="main">
		<div class="title">
			<img id="qrcode" alt="" src="">
		</div>
		<div id="result" class="title"></div>
	</div>

</body>
</html>