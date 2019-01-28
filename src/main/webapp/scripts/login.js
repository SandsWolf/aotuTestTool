$(function(){
		$("#count").val("");
		$("#password").val("");
		
		//给单击按钮追加单击处理
		$("#login").click(function(){
			//清除原有的提示信息
			$("#count_msg").html("");
			$("#password_msg").html("");
			
			//获取请求数据
			var name = $("#count").val().trim();
			var pwd = $("#password").val().trim();

			//检查数据格式
			if(name==""){
				$("#count_msg").html("用户名不能为空");
			}
			if(pwd==""){
				$("#password_msg").html("密码不能为空");
			}

			//发送Ajax请求
			if(name!=""&&pwd!=""){
				$.ajax({
					url:"http://localhost:8080/autoutil/user/login.do",
					type:"post",
					data:{"name":name,"pwd":pwd},
					dataType:"json",
					success:function(result){
						//result是服务器返回的json结果
						if(result.status == 0){//成功
							//获取用户ID，写入Cookie(cookie_util.js包中封装对Cookie的操作)
							var userId = result.data;
							addCookie("uid",userId,2);
							
							//将input中的数据清除，防止下次跳转到该页面时展现上次登入的数据
							$("#count").val("");
							$("#password").val("");
							
							window.location.href="edit.html";	//成功登陆跳转
						}else if(result.status == 1){//用户不存在
							$("#count_msg").html(result.msg);
						}else if(result.status == 2){//密码错误
							$("#password_msg").html(result.msg);
						}
					},
					error:function(){
						alert("登录异常");
					}
				});
			}
		});
	});