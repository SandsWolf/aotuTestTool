//关闭对话框
function clearalert(){
	$("#can").empty();
	$(".opacity_bg").hide();
}

//加载工具列表 
function loadToolsList(event){
	//设置显示全部笔记列表，其他列表隐藏
	$("#pc_part_2").show();
	$("#pc_part_6").hide();
	$("#pc_part_5").hide();
	$("#pc_part_3").show();
	$("#pc_part_4").hide();
	
	//给笔记本li设置选中样式
	$("#toolbox_list li a").removeClass("checked");
	$(this).find("a").addClass("checked");
	
	//获取boxId（点哪个获取哪个用this，加$(this)表示转成JQuery对象）
	var toolboxId = $(this).data("toolboxId");
	
	$.ajax({
		url:event.data.ip + "/tool/loadlist",
		type:"post",
		data:{"toolboxId":toolboxId},
		dataType:"json",
		success:function(result){
			//每次刷新笔记列表
			$("#tool_list").empty();	//方法1
			//$("#note_list li").remove();	//方法2
			$("#tool_content").empty();
			
			if(result.status==0){
				var tools = result.data;	
				for(var i=0;i<tools.length;i++){
					var toolId = tools[i].tool_id;
					var toolName = tools[i].tool_name;
					var toolStatus = tools[i].status;
					
					//拼成一个笔记列表的li
					var s_li = 	'<li class="online">';
					s_li +=			'<a>';
					s_li +=				'<i class="fa fa-file-text-o" title="online" rel="tooltip-bottom"></i><b>'+toolName+'</b>';
					s_li +=			'</a>';
					s_li += 	'</li>';

					var $li = $(s_li);
					$li.data("toolId",toolId);//给li绑定笔记ID

					//将笔记li添加到笔记ul中
					$("#tool_list").append($li);
				}
			}
		},
		error:function(){
			alert("error");
		}
	});
}

//加载工具内容
function loadtool(event){
	var toolId = $(this).data("toolId");
	$("#tool_list li a").removeClass("checked");
	$(this).find("a").addClass("checked");	
	
	$.ajax({
		url:event.data.ip + "/tool/loadtool",
		type:"post",
		data:{"toolId":toolId},
		dataType:"json",
		success:function(result){
			if(result.status==0){
                $("#tool_content").empty();

                var tooldesc = result.data;
                var $li = $(tooldesc);
                $("#tool_content").append($li);


                //缓存
                // console.log("orderNo:" + window.publicOrderNo);
                $("#input_orderNo").val(window.publicOrderNo);
                $("#input_mobile").val(window.publicMobile);
                $("#input_carNo").val(window.publicCarNo);
                $("#input_rentTime").val(window.publicRentTime);
                $("#input_revertTime").val(window.publicRevertTime);
                $("#input_lon").val(window.publicLon);
                $("#input_lat").val(window.publicLat);
                $("#input_dayPrice").val(window.publicDayPrice);
                $("#input_holidayPrice").val(window.publicHolidayPrice);

                var rentTime = window.publicRentTime;
                var revertTime = window.publicRevertTime;

                if (rentTime != null) {
                    if (rentTime.length == 14) {
                        var showRentTiem = rentTime.substring(0, 4) + "-" + rentTime.substring(4, 6) + "-" + rentTime.substring(6, 8) + " " + rentTime.substring(8, 10) + ":" + rentTime.substring(10, 12) + ":" + rentTime.substring(12, 14);
                        $("#show_rentTime").html(showRentTiem);
                    } else {
                        $("#show_rentTime").html("时间格式不对，请重新输入");
                    }
                }

                if (revertTime != null) {
                    if (revertTime != null && revertTime.length == 14) {
                        var showRevertTiem = revertTime.substring(0, 4) + "-" + revertTime.substring(4, 6) + "-" + revertTime.substring(6, 8) + " " + revertTime.substring(8, 10) + ":" + revertTime.substring(10, 12) + ":" + revertTime.substring(12, 14);
                        $("#show_revertTime").html(showRevertTiem);
                    } else {
                        $("#show_rentTime").html("时间格式不对，请重新输入");
                    }
                }

                //判断是否存在加载附加驾驶人元素：有则加载附加驾驶人
                var commUseDriver = $("#comm_use_driver");
                if(commUseDriver.length > 0 && $("#input_mobile").val().trim() !="") {
                    var environment = $("#set_environment").val();
                    if(environment == "-请选择-"){
                        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
                        $("#can").load("alert/alert_environment.html");//显示对话框
                        return;
                    }

                    var mobile = $("#input_mobile").val() == undefined ? '' : $("#input_mobile").val().trim();
                    var environment = $("#set_environment").val();

                    if (mobile != "") {
                        $.ajax({
                            url:event.data.ip + "/member/getCommUseDrivers",
                            type:"post",
                            data:{"environment":environment,"mobile":mobile},
                            dataType:"json",
                            success:function(result){
                                if(result.status==0){
                                    $("#comm_use_driver").empty();
                                    var li = "<br><span class='sign_span'><b>----附加驾驶人列表----</b></span><br>" + result.data;
                                    var $li = $(li);
                                    $("#comm_use_driver").append($li);
                                }

                                if(result.status==1){
                                    $("#comm_use_driver").empty();
                                    var li = "<br><span class='sign_span' style='color:red;'>" + result.data + "</span>";
                                    var $li = $(li);
                                    $("#comm_use_driver").append($li);
                                }
                            },
                            error:function(){
                                $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
                                $("#can").load("alert/alert_error.html");//显示对话框
                            }
                        });
                    }
				}
			}
		},
		error:function(){
			alert("error");
		}
	});
	
	setInterval(function(){
		var now = new Date();
		var time = now.toLocaleTimeString();
		if(time.substring(0,2)=="上午"){
			var hour = parseInt(time.substring(2, time.indexOf(":"))) + "";
			time = hour + time.substring(time.indexOf(":"));
		}
		if(time.substring(0,2)=="下午"){
			var hour = (parseInt(time.substring(2, time.indexOf(":"))) + 12) + "";
			time = hour + time.substring(time.indexOf(":"));
		}
		$("#clock").html("<b>"+time+"</b>");
	},1000);
}








//环境测试
function environmentTest(event){
	var mobile = $("#input_note_title").val();
	var environment = $("#set_environment").val();
	
	if(environment == "-请选择-"){
		alert("请选择环境");
		return;
	}
	if(mobile == ""){
		alert("请输入手机号");
		return;
	}
	
	$.ajax({
		url:event.data.ip + "/tool/pathtest",
		type:"post",
		data:{"mobile":mobile,"environment":environment},
		dataType:"json",
		success:function(result){
			if(result.status==0){
				$("#result_div").empty();
				
				var realName = result.data;
				var li = "<br><span class='sign_span'><b>测试数据：<br>" + realName + "</b></span>";
				var $li = $(li);
				$("#result_div").append($li);
			}
		},
		error:function(){
			alert("error");
		}
	});
}





