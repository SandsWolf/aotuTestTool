$(function(){
	$("#regist_button").click(function(){
		//TODO清除原有提示信息
		$("#warning_1 span").html("");
		$("#warning_2 span").html("");
		$("#warning_3 span").html("");
		$("#warning_4 span").html("");
		
		//获取表单信息
		var name = $("#regist_username").val().trim();
		var nickname = $("#nickname").val().trim();
		var password = $("#regist_password").val().trim();
		var final_password = $("#final_password").val().trim();
		
		//TODO检测表单信息格式
		var flag = true;
		if(name==""){
			$("#warning_1 span").html("用户名不能为空");
			$("#warning_1").show();
			flag = false;
		}
		if(nickname==""){
			$("#warning_4 span").html("昵称不能为空");
			$("#warning_4").show();
			flag = false;
		}
		if(password==""){
			$("#warning_2 span").html("密码不能为空")
			$("#warning_2").show();
			flag = false;
		}
		if(final_password==""){
			$("#warning_3 span").html("确认密码不能为空")
			$("#warning_3").show();
			flag = false;
		}
		if(final_password!=password){
			$("#warning_3 span").html("确认密码不一致")
			$("#warning_3").show();
			flag = false;
		}
		
		
		//发送Ajax请求
		if(flag){
			$.ajax({
				url:"http://localhost:8080/autoutil/user/regist.do",
				type:"post",
				data:{"name":name,"nickname":nickname,"password":password},
				dataType:"json",
				success:function(result){
					if(result.status==0){
						alert("注册成功");

						//将input中的数据清除，防止下次跳转到该页面时展现上次注册的数据
						$("#regist_username").val("");
						$("#nickname").val("");
						$("#regist_password").val("");
						$("#final_password").val("");
						
						$("#back").click();	//无参数时触发返回按钮的单击
					}else if(result.status==1){	//用户名被占用
						$("#warning_1 span").html(result.msg);
						$("#warning_1").show();	//显示div信息区
					}
				},
				error:function(){
					alert("注册发生异常");
				}
			});
		}
	});
});