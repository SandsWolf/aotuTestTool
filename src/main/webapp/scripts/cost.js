//计算全面保障服务费
function getAbatementInsure(event){
    $("#result_msg").empty();
    var environment = $("#set_environment").val();
    var mobile = $("#input_mobile").val().trim();
    var carPara = $("#input_carNo").val().trim();
    var rentTime = $("#input_rentTime").val().trim();
    var revertTime = $("#input_revertTime").val();

    if(environment == "-请选择-"){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_environment.html");//显示对话框
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
    if(rentTime == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_trans_start_time.html");//显示对话框
        return;
    }
    if(revertTime == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_trans_end_time.html");//显示对话框
        return;
    }

    $.ajax({
        url:event.data.ip + "/cost/abatementInsure",
        type:"post",
        data:{"environment":environment,"mobile":mobile,"carPara":carPara,"rentTime":rentTime,"revertTime":revertTime},
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
                li = li + "</span><br>车辆购置价：<span class='sign_span' style='color:red;'>" + resultMsg.guidePurchasePrice + " 元</span><br>";
                li = li + "</span><br>全面保障服务费/日：<span class='sign_span' style='color:red;'>" + resultMsg.priceList + " 元/天</span><br>";
                li = li + "</span><br>驾驶证初次领证日期：<span class='sign_span' style='color:red;'>" + resultMsg.startYear + "</span><br>";
                li = li + "</span><br>租期：<span class='sign_span' style='color:red;'>" + resultMsg.rentDays + " 天</span><br>";
                li = li + "</span><br>驾龄描述：<span class='sign_span' style='color:red;'>" + resultMsg.msg + "</span><br>";
                li = li + "</span><br>驾龄系数：<span class='sign_span' style='color:red;'>" + resultMsg.index + "</span><br>";
                li = li + "</span><br>全面保障服务费[<span class='sign_span' style='color:red;'>规则：（阶段租期 * 全面保障服务费/天）↑ * 驾龄系数↑</span>]：<span class='sign_span' style='color:red;'>" + resultMsg.abatementInsure + " 元</span><br>";
                li = li + "</b></span><br><br><br>";
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



//平台保障费
function getInsureTotalPrices(event){
    $("#result_msg").empty();
    var environment = $("#set_environment").val();
    var mobile = $("#input_mobile").val().trim();
    var carPara = $("#input_carNo").val().trim();
    var rentTime = $("#input_rentTime").val().trim();
    var revertTime = $("#input_revertTime").val();

    if(environment == "-请选择-"){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_environment.html");//显示对话框
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
    if(rentTime == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_trans_start_time.html");//显示对话框
        return;
    }
    if(revertTime == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_trans_end_time.html");//显示对话框
        return;
    }

    $.ajax({
        url:event.data.ip + "/cost/insureTotalPrices",
        type:"post",
        data:{"environment":environment,"mobile":mobile,"carPara":carPara,"rentTime":rentTime,"revertTime":revertTime},
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
                li = li + "</span><br>车辆购置价：<span class='sign_span' style='color:red;'>" + resultMsg.guidePurchasePrice + " 元</span><br>";
                li = li + "</span><br>平台保障费/日：<span class='sign_span' style='color:red;'>" + resultMsg.insuranceValue + " 元/天</span><br>";
                li = li + "</span><br>驾驶证初次领证日期：<span class='sign_span' style='color:red;'>" + resultMsg.startYear + "</span><br>";
                li = li + "</span><br>租期：<span class='sign_span' style='color:red;'>" + resultMsg.rentDays + " 天</span><br>";
                li = li + "</span><br>驾龄描述：<span class='sign_span' style='color:red;'>" + resultMsg.msg + "</span><br>";
                li = li + "</span><br>驾龄系数：<span class='sign_span' style='color:red;'>" + resultMsg.index + "</span><br>";
                li = li + "</span><br>平台保障费[<span class='sign_span' style='color:red;'>规则：（平台保障费/日 * 驾龄系数）↑ * 租期↓</span>]：<span class='sign_span' style='color:red;'>" + resultMsg.insureTotalPrices + " 元</span><br>";
                li = li + "</b></span><br><br><br>";
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




//取还车服务费
function getReturnCost(event){
    $("#result_msg").empty();
    var environment = $("#set_environment").val();
    var cityCode = $("#set_cityCode").val();
    var rentTime = $("#input_rentTime").val().trim();
    var revertTime = $("#input_revertTime").val().trim();
    var carNo = $("#input_carNo").val().trim();
    /*
    * <option value="1">使用取换车服务(1,1)</option>
    * <option value="2">使用取车服务(1,0)</option>
    * <option value="0">不使用服务(0,0)</option>
    */
    var srvFlag = $("#set_srvFlag").val();					                    //取还车开关
    var getCoordinate = $("#select_getAddressList").val() == undefined ? '' : $("#select_getAddressList").val().trim();
    var getAddress = $("#select_getAddressList option:selected").text();        //取车select地址名
    var returnCoordinate = $("#select_returnAddressList").val() == undefined ? '' : $("#select_returnAddressList").val().trim();
    var returnAddress = $("#select_returnAddressList option:selected").text();  //还车select地址名
    // console.log("cityCode:" + cityCode);
    // console.log("carNo:" + carNo);
    // console.log("srvFlag:" + srvFlag);
    // console.log("getCoordinate:" + getCoordinate);
    // console.log("getAddress:" + getAddress);
    // console.log("returnCoordinate:" + returnCoordinate);
    // console.log("returnAddress:" + returnAddress);

    if(environment == "-请选择-"){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_environment.html");//显示对话框
        return;
    }

    if(carNo == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_car_no.html");//显示对话框
        return;
    }
    if(rentTime == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_trans_start_time.html");//显示对话框
        return;
    }
    if(revertTime == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_trans_end_time.html");//显示对话框
        return;
    }
    if(getCoordinate == ""){
        alert("请确认取车经纬度");
        return;
    }
    if(returnCoordinate == ""){
        alert("请确认还车经纬度");
        return;
    }

    $.ajax({
        url:event.data.ip + "/cost/getReturnCost",
        type:"post",
        data:{"environment":environment,"cityCode":cityCode,"carNo":carNo,"srvFlag":srvFlag,"getCoordinate":getCoordinate,"returnCoordinate":returnCoordinate,"rentTime":rentTime,"revertTime":revertTime},
        dataType:"json",
        success:function(result){
            if(result.status == 0){
                $("#result_msg").empty();

                var resultMsg = result.data;
                var li = "<br><span class='sign_span'><b>结果：<br></span><br><span class='sign_span' style='color:red;'>" + resultMsg + "</span></b></span><br><br><br>";
                var $li = $(li);
                $("#result_msg").append($li);
            }

            if(result.status == 1){
                $("#result_msg").empty();
                var getResultMsg = result.data.getResultMap;            //取车服务费结果
                var returnResultMsg = result.data.returnResultMap;      //还车服务费结果

                var li = "<br><span class='sign_span'><b>结果：";
                li = li + "</span><br><span class='sign_span' style='color:red;'>====== 取车服务费 ======</span>";
                li = li + "</span><br>基础费用：<span class='sign_span' style='color:red;'>" + getResultMsg.deliveryServiceCost + " 元</span>";
                li = li + "</span><br>球面距离：<span class='sign_span' style='color:red;'>" + getResultMsg.distance1 + " km</span>";
                li = li + "</span><br>展示距离：<span class='sign_span' style='color:red;'>" + getResultMsg.distance2 + " km</span>";
                li = li + "</span><br>距离系数：<span class='sign_span' style='color:red;'>" + getResultMsg.distanceRate + "</span>";
                li = li + "</span><br>特殊时间段系数：<span class='sign_span' style='color:red;'>" + getResultMsg.overRate + "</span><br>";
                li = li + "</span><br>取车服务费（<span class='sign_span' style='color:red;'>规则：基础费用 * 特殊时间段系数 * 距离系数</span>）：<span class='sign_span' style='color:red;'>" + getResultMsg.cost + " 元</span><br>";

                li = li + "<hr style='height:1px;border:none;border-top:1px dashed #0066CC;' />";

                li = li + "</span><br><span class='sign_span' style='color:red;'>====== 还车服务费 ======</span>";
                li = li + "</span><br>基础费用：<span class='sign_span' style='color:red;'>" + returnResultMsg.deliveryServiceCost + " 元</span>";
                li = li + "</span><br>球面距离：<span class='sign_span' style='color:red;'>" + returnResultMsg.distance1 + " km</span>";
                li = li + "</span><br>展示距离：<span class='sign_span' style='color:red;'>" + returnResultMsg.distance2 + " km</span>";
                li = li + "</span><br>距离系数：<span class='sign_span' style='color:red;'>" + returnResultMsg.distanceRate + "</span>";
                li = li + "</span><br>特殊时间段系数：<span class='sign_span' style='color:red;'>" + returnResultMsg.overRate + "</span><br>";
                li = li + "</span><br>还车服务费（<span class='sign_span' style='color:red;'>规则：基础费用 * 特殊时间段系数 * 距离系数</span>）：<span class='sign_span' style='color:red;'>" + returnResultMsg.cost + " 元</span><br>";
                li = li + "</b></span><br><br><br>";
                var $li = $(li);
                $("#result_msg").append($li);
            }

            if(result.status == 2){
                $("#result_msg").empty();
                var resultMsg = result.data;

                var li = "<br><span class='sign_span'><b>结果：";
                li = li + "</span><br><span class='sign_span' style='color:red;'>====== 取车服务费 ======</span>";
                li = li + "</span><br>基础费用：<span class='sign_span' style='color:red;'>" + resultMsg.deliveryServiceCost + " 元</span>";
                li = li + "</span><br>球面距离：<span class='sign_span' style='color:red;'>" + resultMsg.distance1 + " km</span>";
                li = li + "</span><br>展示距离：<span class='sign_span' style='color:red;'>" + resultMsg.distance2 + " km</span>";
                li = li + "</span><br>距离系数：<span class='sign_span' style='color:red;'>" + resultMsg.distanceRate + "</span>";
                li = li + "</span><br>特殊时间段系数：<span class='sign_span' style='color:red;'>" + resultMsg.overRate + "</span><br>";
                li = li + "</span><br>取车服务费（<span class='sign_span' style='color:red;'>规则：基础费用 * 特殊时间段系数 * 距离系数</span>）：<span class='sign_span' style='color:red;'>" + resultMsg.cost + " 元</span><br>";

                li = li + "<hr style='height:1px;border:none;border-top:1px dashed #0066CC;' />";

                li = li + "</span><span class='sign_span' style='color:red;'>====== 还车服务费 ======</span>";
                li = li + "</span><br>还车服务费（<span class='sign_span' style='color:red;'>规则：基础费用 * 特殊时间段系数 * 距离系数</span>）：<span class='sign_span' style='color:red;'>0 元</span><br>";
                li = li + "</b></span><br><br><br>";
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


//取还车服务费
function getReturnCostNew(event){
    $("#result_msg").empty();
    var environment = $("#set_environment").val();
    var cityCode = $("#set_cityCode").val();
    var rentTime = $("#input_rentTime").val().trim();
    var revertTime = $("#input_revertTime").val().trim();
    var carNo = $("#input_carNo").val().trim();
    // var distance = $("#input_distance").val().trim();
    var distance = $("#input_distance").val() == undefined ? '' : $("#input_distance").val().trim();
    /*
    * <option value="1">使用取换车服务(1,1)</option>
    * <option value="2">使用取车服务(1,0)</option>
    * <option value="0">不使用服务(0,0)</option>
    */
    var srvFlag = $("#set_srvFlag").val();					                    //取还车开关
    var getCoordinate = $("#select_getAddressList").val() == undefined ? '' : $("#select_getAddressList").val().trim();
    var getAddress = $("#select_getAddressList option:selected").text();        //取车select地址名
    var returnCoordinate = $("#select_returnAddressList").val() == undefined ? '' : $("#select_returnAddressList").val().trim();
    var returnAddress = $("#select_returnAddressList option:selected").text();  //还车select地址名

    if(environment == "-请选择-"){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_environment.html");//显示对话框
        return;
    }

    if(carNo == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_car_no.html");//显示对话框
        return;
    }
    if(rentTime == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_trans_start_time.html");//显示对话框
        return;
    }
    if(revertTime == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_trans_end_time.html");//显示对话框
        return;
    }
    if(getCoordinate == ""){
        alert("请确认取车经纬度");
        return;
    }
    if(returnCoordinate == ""){
        alert("请确认还车经纬度");
        return;
    }

    $.ajax({
        url:event.data.ip + "/cost/getReturnCostNew",
        type:"post",
        data:{"environment":environment,"cityCode":cityCode,"carNo":carNo,"srvFlag":srvFlag,"getCoordinate":getCoordinate,"returnCoordinate":returnCoordinate,"rentTime":rentTime,"revertTime":revertTime,"distance":distance},
        dataType:"json",
        success:function(result){
            if(result.status == 0){
                $("#result_msg").empty();

                var resultMsg = result.data;
                var li = "<br><span class='sign_span'><b>结果：<br></span><br><span class='sign_span' style='color:red;'>" + resultMsg + "</span></b></span><br><br><br>";
                var $li = $(li);
                $("#result_msg").append($li);
            }

            if(result.status == 1){
                $("#result_msg").empty();
                var getResultMsg = result.data.getResultMap;            //取车服务费结果
                var returnResultMsg = result.data.returnResultMap;      //还车服务费结果

                var li = "<br><span class='sign_span'><b>结果：";
                li = li + "</span><br><span class='sign_span' style='color:red;'>====== 取车服务费 ======</span>";
                li = li + "</span><br>普通基础费用：<span class='sign_span' style='color:red;'>" + getResultMsg.deliveryServiceCost + " 元</span>";
                li = li + "</span><br>套餐基础费用：<span class='sign_span' style='color:red;'>" + getResultMsg.packageDeliveryServiceCost + " 元</span>";
                li = li + "</span><br>球面距离：<span class='sign_span' style='color:red;'>" + getResultMsg.distance1 + " km</span>";
                li = li + "</span><br>球面距离系数：<span class='sign_span' style='color:red;'>" + getResultMsg.ballRatio + "</span>";
                li = li + "</span><br>展示距离：<span class='sign_span' style='color:red;'>" + getResultMsg.distance2 + " km</span><br>";

                li = li + "</span><br>特殊时间段系数：<span class='sign_span' style='color:red;'>" + getResultMsg.overRate + "</span>";
                li = li + "</span><br>距离系数：<span class='sign_span' style='color:red;'>" + getResultMsg.distanceRate + "</span>";
                li = li + "</span><br>取还车圈层系数：<span class='sign_span' style='color:red;'>" + getResultMsg.rangeRate + "</span><br>";
                li = li + "</span><br>普通取车服务费（<span class='sign_span' style='color:red;'>规则：基础费用 * 特殊时间段系数 * 距离系数 * 取还车圈层系数</span>）：<span class='sign_span' style='color:red;'>" + getResultMsg.cost + " 元</span><br>";
                li = li + "</span><br>套餐取车服务费（<span class='sign_span' style='color:red;'>规则：基础费用 * 特殊时间段系数 * 取还车圈层系数</span>）：<span class='sign_span' style='color:red;'>" + getResultMsg.packageCost + " 元</span><br>";

                li = li + "<hr style='height:1px;border:none;border-top:1px dashed #0066CC;' />";

                li = li + "</span><br><span class='sign_span' style='color:red;'>====== 还车服务费 ======</span>";
                li = li + "</span><br>普通基础费用：<span class='sign_span' style='color:red;'>" + returnResultMsg.deliveryServiceCost + " 元</span>";
                li = li + "</span><br>套餐基础费用：<span class='sign_span' style='color:red;'>" + returnResultMsg.packageDeliveryServiceCost + " 元</span>";
                li = li + "</span><br>球面距离：<span class='sign_span' style='color:red;'>" + returnResultMsg.distance1 + " km</span>";
                li = li + "</span><br>球面距离系数：<span class='sign_span' style='color:red;'>" + returnResultMsg.ballRatio + "</span>";
                li = li + "</span><br>展示距离：<span class='sign_span' style='color:red;'>" + returnResultMsg.distance2 + " km</span><br>";

                li = li + "</span><br>特殊时间段系数：<span class='sign_span' style='color:red;'>" + returnResultMsg.overRate + "</span>";
                li = li + "</span><br>距离系数：<span class='sign_span' style='color:red;'>" + returnResultMsg.distanceRate + "</span>";
                li = li + "</span><br>取还车圈层系数：<span class='sign_span' style='color:red;'>" + returnResultMsg.rangeRate + "</span><br>";
                li = li + "</span><br>普通还车服务费（<span class='sign_span' style='color:red;'>规则：基础费用 * 特殊时间段系数 * 距离系数 * 取还车圈层系数</span>）：<span class='sign_span' style='color:red;'>" + returnResultMsg.cost + " 元</span><br>";
                li = li + "</span><br>套餐取车服务费（<span class='sign_span' style='color:red;'>规则：基础费用 * 特殊时间段系数 * 取还车圈层系数</span>）：<span class='sign_span' style='color:red;'>" + returnResultMsg.packageCost + " 元</span><br>";
                li = li + "</b></span><br><br><br>";
                var $li = $(li);
                $("#result_msg").append($li);
            }

            if(result.status == 2){
                $("#result_msg").empty();
                var resultMsg = result.data;

                var li = "<br><span class='sign_span'><b>结果：";
                li = li + "</span><br><span class='sign_span' style='color:red;'>====== 取车服务费 ======</span>";
                li = li + "</span><br>普通基础费用：<span class='sign_span' style='color:red;'>" + resultMsg.deliveryServiceCost + " 元</span>";
                li = li + "</span><br>套餐基础费用：<span class='sign_span' style='color:red;'>" + resultMsg.packageDeliveryServiceCost + " 元</span>";
                li = li + "</span><br>球面距离：<span class='sign_span' style='color:red;'>" + resultMsg.distance1 + " km</span>";
                li = li + "</span><br>球面距离系数：<span class='sign_span' style='color:red;'>" + resultMsg.ballRatio + "</span>";
                li = li + "</span><br>展示距离：<span class='sign_span' style='color:red;'>" + resultMsg.distance2 + " km</span><br>";

                li = li + "</span><br>特殊时间段系数：<span class='sign_span' style='color:red;'>" + resultMsg.overRate + "</span>";
                li = li + "</span><br>距离系数：<span class='sign_span' style='color:red;'>" + resultMsg.distanceRate + "</span>";
                li = li + "</span><br>取还车圈层系数：<span class='sign_span' style='color:red;'>" + resultMsg.rangeRate + "</span><br>";
                li = li + "</span><br>普通取车服务费（<span class='sign_span' style='color:red;'>规则：基础费用 * 特殊时间段系数 * 距离系数 * 取还车圈层系数</span>）：<span class='sign_span' style='color:red;'>" + resultMsg.cost + " 元</span><br>";
                li = li + "</span><br>套餐取车服务费（<span class='sign_span' style='color:red;'>规则：基础费用 * 特殊时间段系数 * 取还车圈层系数</span>）：<span class='sign_span' style='color:red;'>" + resultMsg.packageCost + " 元</span><br>";

                li = li + "<hr style='height:1px;border:none;border-top:1px dashed #0066CC;' />";

                li = li + "</span><span class='sign_span' style='color:red;'>====== 还车服务费 ======</span>";
                li = li + "</span><br>还车服务费（<span class='sign_span' style='color:red;'>规则：基础费用 * 特殊时间段系数 * 距离系数 * 取还车圈层系数</span>）：<span class='sign_span' style='color:red;'>0 元</span><br>";
                li = li + "</b></span><br><br><br>";
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



//附加驾驶人费用
function extraDriverInsure(event){
    $("#result_msg").empty();
    var environment = $("#set_environment").val();
    var rentTime = $("#input_rentTime").val().trim();
    var revertTime = $("#input_revertTime").val().trim();
    var num = $("input[type='checkbox']:checked").length;   //选中checkbox数量

    if(environment == "-请选择-"){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_environment.html");//显示对话框
        return;
    }

    if(rentTime == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_trans_start_time.html");//显示对话框
        return;
    }
    if(revertTime == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_trans_end_time.html");//显示对话框
        return;
    }
    if (num == 0) {
        // alert("手机号：" + $("#input_revertTime").val().trim() + "名下不存在附加驾驶人");
        alert("附加驾驶人数量=0，不会产生【附加驾驶人】费用");
        return;
    }

    $.ajax({
        url:event.data.ip + "/cost/extraDriverInsure",
        type:"post",
        data:{"environment":environment,"num":num,"rentTime":rentTime,"revertTime":revertTime},
        dataType:"json",
        success:function(result){
            if(result.status == 1){
                $("#result_msg").empty();

                var resultMsg = result.data;
                var li = "<br><span class='sign_span'><b>结果：<br></span><br><span class='sign_span' style='color:red;'>" + resultMsg + "</span></b></span><br><br><br>";
                var $li = $(li);
                $("#result_msg").append($li);
            }

            if(result.status == 0){
                $("#result_msg").empty();

                var resultMsg = result.data;
                var li = "<br><span class='sign_span'><b>结果：";
                li = li + "</span><br>租期：<span class='sign_span' style='color:red;'>" + resultMsg.rentDate + " 天</span>";
                li = li + "</span><br>附加驾驶人数：<span class='sign_span' style='color:red;'>" + num + " 人</span>";
                li = li + "</span><br>计费天数：<span class='sign_span' style='color:red;'>" + resultMsg.days + " 天</span><br>";
                li = li + "</span><br>附加驾驶人费用（<span class='sign_span' style='color:red;'>规则：20 * 附加驾驶人数 * 计费天数</span>）：<span class='sign_span' style='color:red;'>" + resultMsg.fee + " 元</span><br>";
                li = li + "</b></span><br><br><br>";
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


//计算车辆押金
function carDepositAmt(event){
    $("#result_msg").empty();
    var environment = $("#set_environment").val();
    var carNo = $("#input_carNo").val().trim();
    var mobile = $("#input_mobile").val().trim();
    // var testAlreadyReliefPercentage = $("#input_testAlreadyReliefPercentage").val().trim();

    if(environment == "-请选择-"){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_environment.html");//显示对话框
        return;
    }
    if(carNo == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_car_no.html");//显示对话框
        return;
    }
    if(mobile == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_phone.html");//显示对话框
        return;
    }

    $.ajax({
        url:event.data.ip + "/cost/carDepositAmt",
        type:"post",
        data:{"environment":environment,"mobile":mobile,"carNo":carNo},
        dataType:"json",
        success:function(result){
            if(result.status == 1){
                $("#result_msg").empty();

                var resultMsg = result.data;
                var li = "<br><span class='sign_span'><b>结果：<br></span><br><span class='sign_span' style='color:red;'>" + resultMsg + "</span></b></span><br><br><br>";
                var $li = $(li);
                $("#result_msg").append($li);
            }

            if(result.status == 0){
                $("#result_msg").empty();

                var li = "<br><span class='sign_span'><b>结果：";
                var resultMsg = result.data;
                if (resultMsg.InternalStaff == 1) {     //内部员工
                    li = li + "</span><br>是否内部员工：<span class='sign_span' style='color:red;'>是（内部会员车辆押金固定300元）</span><br>";
                    li = li + "</span><br>车辆押金：<span class='sign_span' style='color:red;'>" + resultMsg.CarDepositAmt + " 元</span><br>";
                } else {    //非内部员工
                    li = li + "</span><br>车辆残值：<span class='sign_span' style='color:red;'>" + resultMsg.surplusPrice + " 元</span>";
                    li = li + "</span><br>车辆cityCode：<span class='sign_span' style='color:red;'>" + resultMsg.cityCode + "</span>";
                    li = li + "</span><br>原始车辆押金：<span class='sign_span' style='color:red;'>" + resultMsg.depositValue + " 元</span>";
                    li = li + "</span><br>会员减免比例：<span class='sign_span' style='color:red;'>" + resultMsg.alreadyReliefPercentage + "%</span>";
                    li = li + "</span><br>年份：<span class='sign_span' style='color:red;'>" + resultMsg.year + " 年</span>";
                    li = li + "</span><br>---->年份系数：<span class='sign_span' style='color:red;'>" + resultMsg.newCarCoefficient + "</span>";
                    li = li + "</span><br>---->车辆品牌系数：<span class='sign_span' style='color:red;'>" + resultMsg.carParamRatio + "</span>";
                    li = li + "</span><br>-------->新算法_总系数：<span class='sign_span' style='color:red;'>" + resultMsg.newCarParamRatio + "</span>";
                    li = li + "</span><br>是否内部员工：<span class='sign_span' style='color:red;'>否</span><br>";
                    li = li + "</span><br>车辆押金 [<span class='sign_span' style='color:red;'>规则：【原始车辆押金 * （100% - 会员减免比例）】 * （1 + 车辆品牌系数）</span>]：<span class='sign_span' style='color:red;'>" + resultMsg.CarDepositAmt + " 元</span>";
                    if (resultMsg.newCarDepositAmtflag == 1) {
                        li = li + "</span><br>新_车辆押金 [<span class='sign_span' style='color:red;'>规则：车辆押金 = 车辆押金基数（车辆残值 > 150万时）</span>]：<span class='sign_span' style='color:red;'>" + resultMsg.newCarDepositAmt + " 元</span><br>";
                    }  else {
                        li = li + "</span><br>新_车辆押金 [<span class='sign_span' style='color:red;'>规则：【原始车辆押金 * （100% - 会员减免比例）】 * 总系数）</span>]：<span class='sign_span' style='color:red;'>" + resultMsg.newCarDepositAmt + " 元</span><br>";
                    }
                }
                li = li + "</b></span><br><br><br>";
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


//计算违章押金
function depositAmt(event){
    $("#result_msg").empty();
    var environment = $("#set_environment").val();
    var carNo = $("#input_carNo").val().trim();
    var mobile = $("#input_mobile").val().trim();

    if(environment == "-请选择-"){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_environment.html");//显示对话框
        return;
    }
    if(carNo == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_car_no.html");//显示对话框
        return;
    }
    if(mobile == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_phone.html");//显示对话框
        return;
    }

    $.ajax({
        url:event.data.ip + "/cost/depositAmt",
        type:"post",
        data:{"environment":environment,"mobile":mobile,"carNo":carNo},
        dataType:"json",
        success:function(result){
            if(result.status == 1){
                $("#result_msg").empty();

                var resultMsg = result.data;
                var li = "<br><span class='sign_span'><b>结果：<br></span><br><span class='sign_span' style='color:red;'>" + resultMsg + "</span></b></span><br><br><br>";
                var $li = $(li);
                $("#result_msg").append($li);
            }

            if(result.status == 0){
                $("#result_msg").empty();

                var resultMsg = result.data;

                var li = "<br><span class='sign_span'><b>结果：";
                li = li + "</span><br>违章押金计费信息：<span class='sign_span' style='color:red;'>" + resultMsg.msg + "</span>";
                li = li + "</span><br>车辆cityCode：<span class='sign_span' style='color:red;'>" + resultMsg.cityCode + "</span><br>";
                li = li + "</span><br>违章押金：<span class='sign_span' style='color:red;'>" + resultMsg.depositAmt + " 元</span>";
                li = li + "</b></span><br><br><br>";
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


//计算超里程&油费
function mileOilCost(event){
    $("#result_msg").empty();
    var environment = $("#set_environment").val();
    var orderNo = $("#input_orderNo").val().trim();
    // var testTotalAmt = $("#input_testTotalAmt").val().trim();
    // var testCodeFlag = $("#input_testCodeFlag").val().trim();

    if(environment == "-请选择-"){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_environment.html");//显示对话框
        return;
    }
    if(orderNo == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_order_no.html");//显示对话框
        return;
    }

    $.ajax({
        url:event.data.ip + "/cost/mileOilCost",
        type:"post",
        data:{"environment":environment,"orderNo":orderNo}, //,"testTotalAmt":testTotalAmt,"testCodeFlag":testCodeFlag
        dataType:"json",
        success:function(result){
            if(result.status == 1){
                $("#result_msg").empty();

                var resultMsg = result.data;
                var li = "<br><span class='sign_span'><b>结果：<br></span><br><span class='sign_span' style='color:red;'>" + resultMsg + "</span></b></span><br><br><br>";
                var $li = $(li);
                $("#result_msg").append($li);
            }

            if(result.status == 0){
                $("#result_msg").empty();

                var resultMsg = result.data;
                console.log("resultMsg:" + resultMsg);

                var isPackage = resultMsg.isPackage;
                var mileOilCostDataInfo = resultMsg.mileOilCostDataInfo;        //基础数据
                var ownerMileCostInfo = resultMsg.ownerMileCostInfo;            //车主"超里程费"
                var ownerOilCostInfo = resultMsg.ownerOilCostInfo;              //车主"油费"
                var renterMileCostInfo = resultMsg.renterMileCostInfo;          //租客"超里程费"
                var renterOilCostInfo = resultMsg.renterOilCostInfo;            //租客"油费"
                var settleMileOilCostInfo = resultMsg.settleMileOilCostInfo;    //结算后表中数据
                // console.log("isPackage:" + isPackage==null);
                // console.log("isPackage:" + isPackage);
                // console.log("mileOilCostDataInfo:" + mileOilCostDataInfo==null);
                // console.log("mileOilCostDataInfo:" + mileOilCostDataInfo.carNo);
                // console.log("ownerMileCostInfo:" + ownerMileCostInfo==null);
                // console.log("ownerOilCostInfo:" + ownerOilCostInfo==null);
                // console.log("renterMileCostInfo:" + renterMileCostInfo==null);
                // console.log("renterOilCostInfo:" + renterOilCostInfo==null);
                // console.log("settleMileOilCostInfo:" + settleMileOilCostInfo==null);
                // console.log("---");

                // if (mileOilCostDataInfo.settle == 1) {
                //     console.log("settleMileOilCostInfo.ownerMileageCost:" + settleMileOilCostInfo.ownerMileageCost);
                // } else {
                //     console.log("订单没结算");
                // }
                //
                // console.log("车主总里程数：" + (ownerMileCostInfo.returnCarMileage - ownerMileCostInfo.getCarMileage));
                // console.log("车主总限里程数：" + (mileOilCostDataInfo.rentDate * ownerMileCostInfo.dayMiles));
                // console.log("---");

                // console.log("总油费：" + (parseInt(ownerOilCostInfo.oilCost) + parseInt(ownerOilCostInfo.oilServiceCost)));
                // console.log("---");

                var li = "<br><span class='sign_span'><b>结果：";
                li = li + "</span><br>==========基本信息==========</span>";
                if (isPackage == 1) {
                    li = li + "</span><br>-->isPackage：<span class='sign_span' style='color:red;'>是</span>";
                } else {
                    li = li + "</span><br>-->isPackage：<span class='sign_span' style='color:red;'>否</span>";
                }

                li = li + "</span><br>-->status：<span class='sign_span' style='color:red;'>" + mileOilCostDataInfo.status + "</span>";
                li = li + "</span><br>-->settle：<span class='sign_span' style='color:red;'>" + mileOilCostDataInfo.settle + "</span>";
                li = li + "</span><br>-->ownerNo：<span class='sign_span' style='color:red;'>" + mileOilCostDataInfo.ownerNo + "</span>";
                li = li + "</span><br>-->renterNo：<span class='sign_span' style='color:red;'>" + mileOilCostDataInfo.renterNo + "</span>";
                li = li + "</span><br>-->租期：<span class='sign_span' style='color:red;'>" + mileOilCostDataInfo.rentTime + " — " + mileOilCostDataInfo.revertTime + "（" + mileOilCostDataInfo.rentDate + "天）" + "</span>";
                li = li + "</span><br>-->carNo：<span class='sign_span' style='color:red;'>" + mileOilCostDataInfo.carNo + "</span>";
                if (mileOilCostDataInfo.settle == 1) {
                    li = li + "</span><br>-->租车押金：<span class='sign_span' style='color:red;'>" + mileOilCostDataInfo.totalAmt + " 元</span>";
                    li = li + "</span><br>-->总消费：<span class='sign_span' style='color:red;'>" + mileOilCostDataInfo.totalConsumeAmt + " 元</span>";
                    li = li + "</span><br>-->codeFlag：<span class='sign_span' style='color:red;'>" + mileOilCostDataInfo.codeFlag + "</span><br>";
                } else {
                    li = li + "</span><br>-->租车押金：<span class='sign_span' style='color:red;'>" + mileOilCostDataInfo.totalAmt + " 元</span><br>";
                }
                li = li + "</span><br>-->能源价格：<span class='sign_span' style='color:red;'>" + mileOilCostDataInfo.molecule + " 元/升</span>";
                li = li + "</span><br>-->油箱容积：<span class='sign_span' style='color:red;'>" + mileOilCostDataInfo.oilVolume + " 升</span>";
                li = li + "</span><br>-->油表刻度分母：<span class='sign_span' style='color:red;'>" + mileOilCostDataInfo.oilScaleDenominator + " 格</span>";


                li = li + "<hr style='height:1px;border:none;border-top:1px dashed #0066CC;' />";

                li = li + "</span><br>==========车主费用==========</span>";
                li = li + "</span><br>------超里程费------</span>";
                // li = li + "</span><br>---->取车时里程数：<span class='sign_span' style='color:red;'>" + ownerMileCostInfo.getCarMileage + "（" + mileOilCostDataInfo.ownerGetMileage + "）</span>";
                // li = li + "</span><br>---->还车时里程数：<span class='sign_span' style='color:red;'>" + ownerMileCostInfo.returnCarMileage + "（" + mileOilCostDataInfo.ownerReturnMileage + "）</span>";
                li = li + "</span><br>---->取车时里程数：<span class='sign_span' style='color:red;'>" + mileOilCostDataInfo.ownerGetMileage + " km</span>";
                li = li + "</span><br>---->还车时里程数：<span class='sign_span' style='color:red;'>" + mileOilCostDataInfo.ownerReturnMileage + " km</span>";
                li = li + "</span><br>---->日限里程：<span class='sign_span' style='color:red;'>" + ownerMileCostInfo.dayMiles + " km</span>";
                li = li + "</span><br>---->日均价：<span class='sign_span' style='color:red;'>" + ownerMileCostInfo.dayPrice + " 元/日</span>";
                if (mileOilCostDataInfo.settle == 1) {
                    li = li + "</span><br>------>【超里程费】（totalMile=<span class='sign_span' style='color:red;'>" + (ownerMileCostInfo.returnCarMileage - ownerMileCostInfo.getCarMileage) + " km</span>,rentMile=<span class='sign_span' style='color:red;'>" + (mileOilCostDataInfo.rentDate * ownerMileCostInfo.dayMiles) + " km</span>）：<span class='sign_span' style='color:red;'>" + ownerMileCostInfo.mileCost + " 元（" + settleMileOilCostInfo.ownerMileageCost + " 元）</span>";
                } else {
                    li = li + "</span><br>------>【超里程费】（totalMile=<span class='sign_span' style='color:red;'>" + (ownerMileCostInfo.returnCarMileage - ownerMileCostInfo.getCarMileage) + " km</span>,rentMile=<span class='sign_span' style='color:red;'>" + (mileOilCostDataInfo.rentDate * ownerMileCostInfo.dayMiles) + " km</span>）：<span class='sign_span' style='color:red;'>" + ownerMileCostInfo.mileCost + " 元</span>";
                }
                li = li + "</span><br>---->msg：<span class='sign_span' style='color:red;'>" + ownerMileCostInfo.msg + "</span><br>";

                li = li + "</span><br>------油费------</span>";
                li = li + "</span><br>---->取车时油量：<span class='sign_span' style='color:red;'>" + ownerOilCostInfo.getCarOilScale + "</span>";
                li = li + "</span><br>---->还车时油量：<span class='sign_span' style='color:red;'>" + ownerOilCostInfo.returnCarOilScale + "</span>";

                if (mileOilCostDataInfo.settle == 1) {
                    li = li + "</span><br>------>【油费】（totalScale=<span class='sign_span' style='color:red;'>" + (renterOilCostInfo.getCarOilScale - renterOilCostInfo.returnCarOilScale) + "</span>）：<span class='sign_span' style='color:red;'>" + ownerOilCostInfo.oilCost + " 元（" + settleMileOilCostInfo.ownerOilCost + " 元）</span>";
                    li = li + "</span><br>------>【加油服务费】：<span class='sign_span' style='color:red;'>" + ownerOilCostInfo.oilServiceCost + " 元（" + settleMileOilCostInfo.ownerOilServiceCost + " 元）</span>";
                    li = li + "</span><br>------>【平台加油服务费】：<span class='sign_span' style='color:red;'>" + ownerOilCostInfo.platformServiceCost + " 元（" + settleMileOilCostInfo.newOwnerOilServiceCost + " 元）</span>";
                    li = li + "</span><br>------>【取还车油费补贴】：<span class='sign_span' style='color:red;'>" + ownerOilCostInfo.ownerOilSubsidy + " 元（" + settleMileOilCostInfo.newOwnerOilSubsidy +" 元）</span>";
                    li = li + "</span><br>-------->【取还车油费补贴_取车部分】：<span class='sign_span' style='color:red;'>" + ownerOilCostInfo.ownerOilGetSubsidy + " 元</span>";
                    li = li + "</span><br>-------->【取还车油费补贴_还车部分】：<span class='sign_span' style='color:red;'>" + ownerOilCostInfo.ownerOilReturnSubsidy + " 元</span>";

                    li = li + "</span><br>------>【总油费】：<span class='sign_span' style='color:red;'>" + (parseInt(ownerOilCostInfo.oilCost) + parseInt(ownerOilCostInfo.oilServiceCost) + parseInt(ownerOilCostInfo.platformServiceCost) + parseInt(ownerOilCostInfo.ownerOilSubsidy)) + " 元（" + (parseInt(settleMileOilCostInfo.ownerOilCost) + parseInt(settleMileOilCostInfo.ownerOilServiceCost) + parseInt(settleMileOilCostInfo.newOwnerOilSubsidy) + parseInt(settleMileOilCostInfo.newOwnerOilServiceCost)) + " 元）</span>";
                } else {
                    li = li + "</span><br>------>【油费】（totalScale=<span class='sign_span' style='color:red;'>" + (renterOilCostInfo.getCarOilScale - renterOilCostInfo.returnCarOilScale) + "</span>）：<span class='sign_span' style='color:red;'>" + ownerOilCostInfo.oilCost + " 元</span>";
                    li = li + "</span><br>------>【加油服务费】：<span class='sign_span' style='color:red;'>" + ownerOilCostInfo.oilServiceCost + " 元</span>";
                    li = li + "</span><br>------>【平台加油服务费】：<span class='sign_span' style='color:red;'>" + ownerOilCostInfo.platformServiceCost + " 元</span>";
                    li = li + "</span><br>------>【取还车油费补贴】：<span class='sign_span' style='color:red;'>" + ownerOilCostInfo.ownerOilSubsidy + " 元</span>";
                    li = li + "</span><br>-------->【取还车油费补贴_取车部分】：<span class='sign_span' style='color:red;'>" + ownerOilCostInfo.ownerOilGetSubsidy + " 元</span>";
                    li = li + "</span><br>-------->【取还车油费补贴_还车部分】：<span class='sign_span' style='color:red;'>" + ownerOilCostInfo.ownerOilReturnSubsidy + " 元</span>";

                    // li = li + "</span><br>------>【总油费】：<span class='sign_span' style='color:red;'>" + (parseInt(ownerOilCostInfo.oilCost) + parseInt(ownerOilCostInfo.oilServiceCost)) + "</span>";
                    li = li + "</span><br>------>【总油费】：<span class='sign_span' style='color:red;'>" + (parseInt(ownerOilCostInfo.oilCost) + parseInt(ownerOilCostInfo.oilServiceCost) + parseInt(ownerOilCostInfo.platformServiceCost) + parseInt(ownerOilCostInfo.ownerOilSubsidy)) + " 元</span>";
                }
                li = li + "</span><br>---->msg：<span class='sign_span' style='color:red;'>" + ownerOilCostInfo.msg + "</span><br>";

                li = li + "<hr style='height:1px;border:none;border-top:1px dashed #0066CC;' />";

                li = li + "</span><br>==========租客费用==========</span>";
                li = li + "</span><br>------超里程费------</span>";
                li = li + "</span><br>---->取车时里程数：<span class='sign_span' style='color:red;'>" + renterMileCostInfo.getCarMileage + " km</span>";
                li = li + "</span><br>---->还车时里程数：<span class='sign_span' style='color:red;'>" + renterMileCostInfo.returnCarMileage + " km</span>";
                li = li + "</span><br>---->日限里程：<span class='sign_span' style='color:red;'>" + renterMileCostInfo.dayMiles + " km</span>";
                li = li + "</span><br>---->日均价：<span class='sign_span' style='color:red;'>" + renterMileCostInfo.dayPrice + " 元/日</span>";
                if (mileOilCostDataInfo.settle == 1) {
                    li = li + "</span><br>------>【超里程费】（totalMile=<span class='sign_span' style='color:red;'>" + (renterMileCostInfo.returnCarMileage - renterMileCostInfo.getCarMileage) + " km</span>,rentMile=<span class='sign_span' style='color:red;'>" + (mileOilCostDataInfo.rentDate * renterMileCostInfo.dayMiles) + " km</span>）：<span class='sign_span' style='color:red;'>" + renterMileCostInfo.mileCost + " 元（" + settleMileOilCostInfo.renterMileageCost + " 元）</span>";
                } else {
                    li = li + "</span><br>------>【超里程费】（totalMile=<span class='sign_span' style='color:red;'>" + (renterMileCostInfo.returnCarMileage - renterMileCostInfo.getCarMileage) + " km</span>,rentMile=<span class='sign_span' style='color:red;'>" + (mileOilCostDataInfo.rentDate * renterMileCostInfo.dayMiles) + " km</span>）：<span class='sign_span' style='color:red;'>" + renterMileCostInfo.mileCost + " 元</span>";
                }
                li = li + "</span><br>---->msg：<span class='sign_span' style='color:red;'>" + renterMileCostInfo.msg + "</span><br>";

                li = li + "</span><br>------油费------</span>";
                li = li + "</span><br>---->取车时油量：<span class='sign_span' style='color:red;'>" + renterOilCostInfo.getCarOilScale + "</span>";
                li = li + "</span><br>---->还车时油量：<span class='sign_span' style='color:red;'>" + renterOilCostInfo.returnCarOilScale + "</span>";
                if (mileOilCostDataInfo.settle == 1) {
                    li = li + "</span><br>------>【油费】（totalScale=<span class='sign_span' style='color:red;'>" + (renterOilCostInfo.getCarOilScale - renterOilCostInfo.returnCarOilScale) + "</span>）：<span class='sign_span' style='color:red;'>" + renterOilCostInfo.oilCost + " 元（" + settleMileOilCostInfo.renterOilCost + " 元）</span>";
                    li = li + "</span><br>------>【加油服务费】：<span class='sign_span' style='color:red;'>" + renterOilCostInfo.oilServiceCost + " 元（" + settleMileOilCostInfo.renterOilServiceCost + " 元）</span>";
                    li = li + "</span><br>------>【总油费】：<span class='sign_span' style='color:red;'>" + (parseInt(renterOilCostInfo.oilCost) + parseInt(renterOilCostInfo.oilServiceCost)) + " 元（" + (parseInt(settleMileOilCostInfo.renterOilCost) + parseInt(settleMileOilCostInfo.renterOilServiceCost)) + " 元）</span>";
                } else {
                    li = li + "</span><br>------>【油费】（totalScale=<span class='sign_span' style='color:red;'>" + (renterOilCostInfo.getCarOilScale - renterOilCostInfo.returnCarOilScale) + "</span>）：<span class='sign_span' style='color:red;'>" + renterOilCostInfo.oilCost + " 元</span>";
                    li = li + "</span><br>------>【加油服务费】：<span class='sign_span' style='color:red;'>" + renterOilCostInfo.oilServiceCost + " 元</span>";
                    li = li + "</span><br>------>【总油费】：<span class='sign_span' style='color:red;'>" + (parseInt(renterOilCostInfo.oilCost) + parseInt(renterOilCostInfo.oilServiceCost)) + " 元</span>";
                }
                li = li + "</span><br>---->msg：<span class='sign_span' style='color:red;'>" + renterOilCostInfo.msg + "</span><br>";
                li = li + "</b></span><br><br><br>";


                // li = li + "</span><br>违章押金计费信息：<span class='sign_span' style='color:red;'>" + resultMsg.msg + "</span>";
                // li = li + "</span><br>车辆cityCode：<span class='sign_span' style='color:red;'>" + resultMsg.cityCode + "</span><br>";
                // li = li + "</span><br>违章押金：<span class='sign_span' style='color:red;'>" + resultMsg.depositAmt + " 元</span>";
                // li = li + "</b></span><br><br><br>";
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