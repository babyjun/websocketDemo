<!DOCTYPE html>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <base href="<%=basePath%>">

  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <title>弹窗</title>
  <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
</head>

<body>
<h2>${cid}</h2>
<input id="start">
<input id="message">
<iframe id="my_iframe" style="display:none;"></iframe>
<script type="application/javascript">
  var socket;
  if(typeof(WebSocket) == "undefined") {
    console.log("您的浏览器不支持WebSocket");
  }else{
    console.log("您的浏览器支持WebSocket");
    socket = new WebSocket("ws://localhost:8081/websocket/${cid}");
//打开事件
    socket.onopen = function() {
      console.log("Socket 已打开");
      $("#start").val("Socket 已打开")
//socket.send("这是来自客户端的消息" + location.href + new Date());
    };
//获得消息事件
    socket.onmessage = function(msg) {
      console.log(msg.data);
      $("#message").val(msg.data)
//发现消息进入 开始处理前端触发逻辑
    };
//关闭事件
    socket.onclose = function() {
      console.log("Socket已关闭");
      $("#close").val("Socket已关闭")
    };
//发生了错误事件
    socket.onerror = function() {
      alert("Socket发生了错误");
//此时可以尝试刷新页面
    }

  }
</script>
</body>
</html>
