//清缓存
function clearMemory(event){
	$("#result_msg").empty();
	var environment = $("#set_environment").val();
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
	
	$.ajax({
		url:event.data.ip + "/other/clearmemory",
		type:"post",
		data:{"environment":environment},
		dataType:"json",
		success:function(result){
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

//查看当前订单定时任务时间
function getTransTimedTask(event){
	$("#result_msg").empty();
	var type = "0";
	var time = "";
	var environment = $("#set_environment").val();
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
	
	$.ajax({
		url:event.data.ip + "/other/transtimedtask",
		type:"post",
		data:{"environment":environment,"time":time,"type":type},
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


//设置订单定时任务：xx分钟后
function setTransTimedTaskMinute(event){
	$("#result_msg").empty();
	var environment = $("#set_environment").val();
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
	
	var time = $("#input_minute").val().trim();
	if(time == ""){
		$(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
		$("#can").load("alert/alert_minute.html");//显示对话框
		return;
	}
	var type = "1";
	
	$.ajax({
		url:event.data.ip + "/other/transtimedtask",
		type:"post",
		data:{"environment":environment,"time":time,"type":type},
		dataType:"json",
		success:function(result){
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

//设置订单定时任务：yyyyMMddHHmmss
function setTransTimedTaskTime(event){
	$("#result_msg").empty();
	var environment = $("#set_environment").val();
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
	
	var time = $("#input_time").val().trim();
	if(time == ""){
		$(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
		$("#can").load("alert/alert_time.html");//显示对话框
		return;
	}
	if(time.length!=14){
		$(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
		$("#can").load("alert/alert_time.html");//显示对话框
		return;
	}
	var type = "2";
	
	$.ajax({
		url:event.data.ip + "/other/transtimedtask",
		type:"post",
		data:{"environment":environment,"time":time,"type":type},
		dataType:"json",
		success:function(result){
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



//查看当前违章定时任务时间
function getIllegalTimedTask(event){
	$("#result_msg").empty();
	var type = "0";
	var time = "";
	var environment = $("#set_environment").val();
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
	
	$.ajax({
		url:event.data.ip + "/other/Illegaltimedtask",
		type:"post",
		data:{"environment":environment,"time":time,"type":type},
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


//设置违章定时任务：xx分钟后
function setIllegalTimedTaskMinute(event){
	$("#result_msg").empty();
	var environment = $("#set_environment").val();
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
	
	var time = $("#input_minute").val().trim();
	if(time == ""){
		$(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
		$("#can").load("alert/alert_minute.html");//显示对话框
		return;
	}
	var type = "1";
	
	$.ajax({
		url:event.data.ip + "/other/Illegaltimedtask",
		type:"post",
		data:{"environment":environment,"time":time,"type":type},
		dataType:"json",
		success:function(result){
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

//设置违章定时任务：yyyyMMddHHmmss
function setIllegalTimedTaskTime(event){
	$("#result_msg").empty();
	var environment = $("#set_environment").val();
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
	
	var time = $("#input_time").val().trim();
	if(time == ""){
		$(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
		$("#can").load("alert/alert_time.html");//显示对话框
		return;
	}
	if(time.length!=14){
		$(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
		$("#can").load("alert/alert_time.html");//显示对话框
		return;
	}
	var type = "2";
	
	$.ajax({
		url:event.data.ip + "/other/Illegaltimedtask",
		type:"post",
		data:{"environment":environment,"time":time,"type":type},
		dataType:"json",
		success:function(result){
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

//设置登录验证码
function setLoginCode(event){
	$("#result_msg").empty();
	var environment = $("#set_environment").val();
	var mobile = $("#input_mobile").val().trim();
	var code = $("#input_code").val().trim();
	
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
	// if(mobile.length != 11){
	// 	$(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
	// 	$("#can").load("alert/alert_phone.html");//显示对话框
	// 	return;
	// }
	// if(code == ""){
	// 	$(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
	// 	$("#can").load("alert/alert_login_code.html");//显示对话框
	// 	return;
	// }
	if(code.length != 6 && code != ""){
		$(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
		$("#can").load("alert/alert_login_code.html");//显示对话框
		return;
	}
	$.ajax({
		url:event.data.ip + "/other/setlogincode",
		type:"post",
		data:{"environment":environment,"mobile":mobile,"code":code},
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



//计算提前延后时间
function beforeAfterTime(event){
    $("#result_msg").empty();
    var environment = $("#set_environment").val();
    // var carParaA = $("#input_carNoA").val().trim();
    // var carParaB = $("#input_carNoB").val().trim();
    var cityCode = $("#set_cityCode").val();
    var lonA = $("#input_lonA").val().trim();
    var latA = $("#input_latA").val().trim();
    var lonB = $("#input_lonB").val().trim();
    var latB = $("#input_latB").val().trim();
    var carPara = $("#input_carNo").val().trim();

    if(environment == "-请选择-"){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_environment.html");//显示对话框
        return;
    }
    if(lonA == ""){
        alert("A地经度不能为空")
        return;
    }
    if(latA == ""){
        alert("A地维度不能为空")
        return;
    }
    if(lonB == ""){
        alert("B地经度不能为空")
        return;
    }
    if(latB == ""){
        alert("B地维度不能为空")
        return;
    }

    if(carPara == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_car_no.html");//显示对话框
        return;
    }
    // if(carParaB == ""){
    //     $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
    //     $("#can").load("alert/alert_car_no.html");//显示对话框
    //     return;
    // }

    $.ajax({
        url:event.data.ip + "/other/getBeforeAfterTimeByDistance",
        type:"post",
        data:{"environment":environment,"cityCode":cityCode,"lonA":lonA,"latA":latA,"lonB":lonB,"latB":latB,"carPara":carPara},
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
                var li = "<br><span class='sign_span'><b>结果：<br>";
                li = li + "</span><br>球面距离：<span class='sign_span' style='color:red;'>\"" + resultMsg.distance1 + " km\"</span><br>";
                li = li + "</span><br>展示距离：<span class='sign_span' style='color:red;'>\"" + resultMsg.distance2 + " km\"</span><br>";
                li = li + "</span><br>是否限行：<span class='sign_span' style='color:red;'>" + resultMsg.isLimit + "</span><br>";
                li = li + "</span><br>是否本地：<span class='sign_span' style='color:red;'>" + resultMsg.isLocal + "</span><br>";
                li = li + "</span><br>提前延后时间：<span class='sign_span' style='color:red;'>" + resultMsg.beforeBackTime + "</span><br>";
                li = li + "</b></span>";
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







// // 设置公共orderNo
// function setPublicOrderNo(){
//     window.publicOrderNo = $("#input_orderNo").val().trim();
// }
//
//
// // 设置公共手机号
// function setPublicMobile(){
//     window.publicMobile = $("#input_mobile").val().trim();
// }
//
// // 设置公共carNo
// function setPublicCarNo(){
//     window.publicCarNo = $("#input_carNo").val().trim();
// }
//
// // 设置公共起租时间
// function setPublicRentTime(){
//     window.publicRentTime = $("#input_rentTime").val().trim();
//     var rentTime = $("#input_rentTime").val().trim();
//     if (rentTime.length != 0) {
//         if (rentTime.length == 14) {
//             var showRentTiem = rentTime.substring(0, 4) + "-" + rentTime.substring(4, 6) + "-" + rentTime.substring(6, 8) + " " + rentTime.substring(8, 10) + ":" + rentTime.substring(10, 12) + ":" + rentTime.substring(12, 14);
//             $("#show_rentTime").html(showRentTiem);
//         } else {
//             $("#show_rentTime").html("时间格式不对，请重新输入");
//         }
//     }
// }
//
// // 设置公共结束时间
// function setPublicRevertTime(){
//     window.publicRevertTime = $("#input_revertTime").val().trim();
//     var revertTime = $("#input_revertTime").val().trim();
//     if (revertTime.length != 0) {
//         if (revertTime.length == 14) {
//             var showRevertTiem = revertTime.substring(0, 4) + "-" + revertTime.substring(4, 6) + "-" + revertTime.substring(6, 8) + " " + revertTime.substring(8, 10) + ":" + revertTime.substring(10, 12) + ":" + revertTime.substring(12, 14);
//             $("#show_revertTime").html(showRevertTiem);
//         } else {
//             $("#show_rentTime").html("时间格式不对，请重新输入");
//         }
//     }
// }
//
// // 设置公共经度
// function setPublicLon(){
//     window.publicLon = $("#input_lon").val().trim();
// }
//
// // 设置公共维度
// function setPublicLat(){
//     window.publicLat = $("#input_lat").val().trim();
// }
//
// // 设置公共日均价
// function setPublicDayPrice(){
//     window.publicDayPrice = $("#input_dayPrice").val().trim();
// }
//
// // 设置公共节日均价
// function setPublicHolidayPrice(){
//     window.publicHolidayPrice = $("#input_holidayPrice").val().trim();
// }
//
// // 加载高德地址select控件
// function addressList(event){
//     var address = $("#input_address").val().trim();
//
//     if (address != "") {
//         $.ajax({
//             url:event.data.ip + "/map/searchAddress",
//             type:"post",
//             data:{"address":address},
//             dataType:"json",
//             success:function(result){
//                 if(result.status==0){
//                     $("#show_msg").html("");
//                     var li = "<br><select id='select_addressList'>";
//                     li = li + result.data;
//                     li = li + "</select>";
//                     var $li = $(li);
//                     $("#selectAddress").append($li);
//                 }
//
//                 if(result.status==1){
//                     $("#selectAddress").empty();
//                     $("#show_msg").html(result.data);
//                 }
//             },
//             error:function(){
//                 $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
//                 $("#can").load("alert/alert_error.html");//显示对话框
//             }
//         });
//     }
// }
//
// // 加载高德取车地址select控件
// function getAddressList(event){
//     var address = $("#input_getAddress").val() == undefined ? '' : $("#input_getAddress").val().trim();
//
//     if (address != "") {
//         $.ajax({
//             url:event.data.ip + "/map/searchAddress",
//             type:"post",
//             data:{"address":address},
//             dataType:"json",
//             success:function(result){
//                 console.log("status:" + result.status);
//                 if(result.status==0){
//                     $("#show_getMsg").html("");
//                     var li = "<br><select id='select_getAddressList'>";
//                     li = li + result.data;
//                     li = li + "</select>";
//                     var $li = $(li);
//                     $("#selectGetAddress").append($li);
//                 }
//
//                 if(result.status==1){
//                     $("#selectGetAddress").empty();
//                     $("#show_getMsg").html(result.data);
//                 }
//             },
//             error:function(){
//                 $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
//                 $("#can").load("alert/alert_error.html");//显示对话框
//             }
//         });
//     }
// }
//
//
// // 加载高德还车地址select控件
// function returnAddressList(event){
//     var address = $("#input_returnAddress").val() == undefined ? '' : $("#input_returnAddress").val().trim();
//     console.log("address:" + address);
//
//     if (address != "") {
//         $.ajax({
//             url:event.data.ip + "/map/searchAddress",
//             type:"post",
//             data:{"address":address},
//             dataType:"json",
//             success:function(result){
//                 if(result.status==0){
//                     $("#show_returnMsg").html("");
//                     var li = "<br><select id='select_returnAddressList'>";
//                     li = li + result.data;
//                     li = li + "</select>";
//                     var $li = $(li);
//                     $("#selectReturnAddress").append($li);
//                 }
//
//                 if(result.status==1){
//                     $("#selectReturnAddress").empty();
//                     $("#show_returnMsg").html(result.data);
//                 }
//             },
//             error:function(){
//                 $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
//                 $("#can").load("alert/alert_error.html");//显示对话框
//             }
//         });
//     }
// }
