
//订单结算
function resetCarIsRent(event){
    $("#result_msg").empty();
    var environment = $("#set_environment").val();
    var car_no = "";

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
        url:event.data.ip + "/car/resetRent",
        type:"post",
        data:{"environment":environment,"car_no":car_no},
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
//借车
function updateCarMemno(event){
    $("#result_msg").empty();
    var environment = $("#set_environment").val();
    var mobile = $("#input_mobile").val().trim();
    var carPara = $("#input_carPara").val().trim();

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
    if(carPara == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_car_no.html");//显示对话框
        return;
    }

    $.ajax({
        url:event.data.ip + "/car/updatecarmemno",
        type:"post",
        data:{"environment":environment,"mobile":mobile,"carPara":carPara},
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

//统计账户下车的数量
function selectMemCarCount(event){
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
        url:event.data.ip + "/car/memcarcount",
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
