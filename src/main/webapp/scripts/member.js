//会员注册
function createmember(event){
	$("#result_msg").empty();
	var environment = $("#set_environment").val();
	var mobile = $("#input_mobile").val().trim();
	
	if(environment == "-请选择-"){
		$(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
		$("#can").load("alert/alert_environment.html");//显示对话框
		return;
	}
	
	if(environment == "线上"){
		$(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
		$("#can").load("alert/alert_online.html");//显示对话框
		return;
	}
	
	if(mobile == ""){
		$(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
		$("#can").load("alert/alert_phone.html");//显示对话框
		return;
	}
	
	$.ajax({
		url:event.data.ip + "/member/createmember",
		type:"post",
		data:{"environment":environment,"mobile":mobile},
		dataType:"json",
		success:function(result){
            if(result.status==1){
                $("#result_msg").empty();

                var resultMsg = result.data;
                var li = "<br><span class='sign_span'><b>结果：<br></span><br><span class='sign_span' style='color:red;'>" + resultMsg + "</span></b></span>";
                var $li = $(li);
                $("#result_msg").append($li);
            }

			if(result.status==0){
				$("#result_msg").empty();
				
				var resultMsg = result.data;
				var li = "<br><span class='sign_span'><b>结果：<br>" + resultMsg + "</b></span>";
				var $li = $(li);
				$("#result_msg").append($li);
			}
		},
		error:function(){
			$(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
			$("#can").load("alert/alert_error.html");//显示对话框
		}
	});
}


//会员认证
function updateMemberInfo(event){
	$("#result_msg").empty();
	var environment = $("#set_environment").val();
	var mobile = $("#input_mobile").val().trim();
	
	if(environment == "-请选择-"){
		$(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
		$("#can").load("alert/alert_environment.html");//显示对话框
		return;
	}
	
	if(environment == "线上"){
		$(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
		$("#can").load("alert/alert_online.html");//显示对话框
		return;
	}
	
	if(mobile == ""){
		$(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
		$("#can").load("alert/alert_phone.html");//显示对话框
		return;
	}
	
	$.ajax({
		url:event.data.ip + "/member/updateinfo",
		type:"post",
		data:{"environment":environment,"mobile":mobile},
		dataType:"json",
		success:function(result){
            if(result.status==1){
                $("#result_msg").empty();

                var resultMsg = result.data;
                var li = "<br><span class='sign_span'><b>结果：<br></span><br><span class='sign_span' style='color:red;'>" + resultMsg + "</span></b></span>";
                var $li = $(li);
                $("#result_msg").append($li);
            }

			if(result.status==0){
				$("#result_msg").empty();
				
				var resultMsg = result.data;
				var li = "<br><span class='sign_span'><b>结果：<br>" + resultMsg + "</b></span>";
				var $li = $(li);
				$("#result_msg").append($li);
			}
		},
		error:function(){
			$(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
			$("#can").load("alert/alert_error.html");//显示对话框
		}
	});
}


//会员手机号查询或token查询
function getMobileOrToken(event){
    $("#result_msg").empty();
    var environment = $("#set_environment").val();
    var value = $("#input_value").val().trim();

    if(environment == "-请选择-"){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_environment.html");//显示对话框
        return;
    }

    if(environment == "线上"){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_online.html");//显示对话框
        return;
    }

    if(value == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_phone.html");//显示对话框
        return;
    }

    $.ajax({
        url:event.data.ip + "/member/getMobileOrToken",
        type:"post",
        data:{"environment":environment,"value":value},
        dataType:"json",
        success:function(result){
            if(result.status==1){
                $("#result_msg").empty();

                var resultMsg = result.data;
                var li = "<br><pre class='sign_span'><b>结果：<br></pre><br><pre class='sign_span' style='color:red;'>" + resultMsg + "</pre></b></pre>";
                var $li = $(li);
                $("#result_msg").append($li);
            }

            if(result.status==0){
                $("#result_msg").empty();

                var resultMsg = result.data;
                var li = "<br><pre class='sign_span'><b>结果：<br>" + resultMsg + "</b></pre>";
                var $li = $(li);
                $("#result_msg").append($li);
            }
        },
        error:function(){
            $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
            $("#can").load("alert/alert_error.html");//显示对话框
        }
    });
}


//会员插入优惠券
function insertCoupon(event){
    $("#result_msg").empty();
    var environment = $("#set_environment").val();
    var value = $("#input_value").val().trim();

    if(environment == "-请选择-"){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_environment.html");//显示对话框
        return;
    }

    if(environment == "线上"){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_online.html");//显示对话框
        return;
    }

    if(value == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_phone.html");//显示对话框
        return;
    }

    $.ajax({
        url:event.data.ip + "/member/insertCoupon",
        type:"post",
        data:{"environment":environment,"value":value},
        dataType:"json",
        success:function(result){
            if(result.status==1){
                $("#result_msg").empty();

                var resultMsg = result.data;
                var li = "<br><pre class='sign_span'><b>结果：<br></pre><br><pre class='sign_span' style='color:red;'>" + resultMsg + "</pre></b></pre>";
                var $li = $(li);
                $("#result_msg").append($li);
            }

            if(result.status==0){
                $("#result_msg").empty();

                var resultMsg = result.data;
                var li = "<br><pre class='sign_span'><b>结果：<br>" + resultMsg + "</b></pre>";
                var $li = $(li);
                $("#result_msg").append($li);
            }
        },
        error:function(){
            $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
            $("#can").load("alert/alert_error.html");//显示对话框
        }
    });
}