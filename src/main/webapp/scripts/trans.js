//违章结算
function depositAmtSettle(event){
    $("#result_msg").empty();
    var environment = $("#set_environment").val();
    var order_no = $("#input_orderNo").val().trim();

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
        url:event.data.ip + "/trans/depositamtsettle",
        type:"post",
        data:{"environment":environment,"order_no":order_no},
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


//订单结算
function settle(event){
    $("#result_msg").empty();
    var environment = $("#set_environment").val();
    // var order_no = $("#input_orderNo").val().trim();
    var order_no = "";

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
        url:event.data.ip + "/trans/settle",
        type:"post",
        data:{"environment":environment,"order_no":order_no},
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

//支付'租车押金'
function payTotalAmt(event){
    $("#result_msg").empty();
    var environment = $("#set_environment").val();
    var order_no = $("#input_orderNo").val().trim();
    var trans_type = $("#set_transType").val();
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

    if(order_no == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_trans_order_no.html");//显示对话框
        return;
    }

    if(trans_type == "-请选择-"){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_trans_type.html");//显示对话框
        return;
    }
    if(trans_type == "消费"){
        trans_type = "01";
    }
    if(trans_type == "预授权"){
        trans_type = "02";
    }

    $.ajax({
        url:event.data.ip + "/trans/paytotalamt",
        type:"post",
        data:{"environment":environment,"order_no":order_no,"trans_type":trans_type},
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

//支付'违章押金'
function payDepositAmt(event){
    $("#result_msg").empty();
    var environment = $("#set_environment").val();
    var order_no = $("#input_orderNo").val().trim();
    var trans_type = $("#set_transType").val();

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

    if(order_no == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_trans_order_no.html");//显示对话框
        return;
    }

    if(trans_type == "-请选择-"){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_trans_type.html");//显示对话框
        return;
    }
    if(trans_type == "消费"){
        trans_type = "01";
    }
    if(trans_type == "预授权"){
        trans_type = "02";
    }

    $.ajax({
        url:event.data.ip + "/trans/paydepositamt",
        type:"post",
        data:{"environment":environment,"order_no":order_no,"trans_type":trans_type},
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


//检查订单租金
function checkRentAmt(event){
    //测试数据：419431117081  test2      574739507081  线上

    $("#result_msg").empty();
    var environment = $("#set_environment").val();
    var order_no = $("#input_orderNo").val().trim();

    var rentAmtType = $('input:radio[name=rentAmt]:checked').val();

    if(environment == "-请选择-"){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_environment.html");//显示对话框
        return;
    }

    if(order_no == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_trans_order_no.html");//显示对话框
        return;
    }

    $.ajax({
        url:event.data.ip + "/trans/newCheckrentAmt",
        // url:event.data.ip + "/trans/checkrentAmt",
        type:"post",
        data:{"environment":environment,"order_no":order_no,"rentAmtType":rentAmtType},
        dataType:"json",
        success:function(result){
            if(result.status==0){
                $("#result_msg").empty();

                var data = result.data;

                var rentAmt = null;             //trans表租金
                var updateDataList = null;      //计算出的修改订单租金List
                var updateRentAmt = null;       //计算出的修改订单租金
                var flag = null;

                $.each(data,function (dataKey,dataValue){
                    if(dataKey=="rentAmt"){
                        rentAmt = dataValue;
                    }
                    if(dataKey=="updateRentAmt"){
                        updateRentAmt = dataValue;
                    }
                    if(dataKey=="updateDataList"){
                        updateDataList = dataValue;
                    }
                })

                if(rentAmt == updateRentAmt){
                    flag = "PASS";
                }else{
                    flag = "FAIL";
                }


                var li = "";
                li = li + '<span class="dsign_span" style="font-size:20px"><b>结果：</b></span><br><br>' +
                    // '<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />' +
                    '<b>标准租金：<span class="sign_span" style="color:red;">' + updateRentAmt + '</span>元</b><br>' +
                    '<b>"trans"表中租金：<span class="sign_span" style="color:red;">' + rentAmt + '</span>元</b><br><br>';

                if(rentAmt == updateRentAmt){
                    li = li + '<b>订单租金校验结果：<span class="sign_span" style="color:green;">PASS</span></b><br>';
                }else{
                    li = li + '<b>订单租金校验结果：<span class="sign_span" style="color:red;">FAIL</span></b><br>';
                }

                li = li + '<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" /><br><br>';

                for (var i = 0; i < updateDataList.length; i++) {
                    if(i == 0){
                        var useSpecialPriceFlag = updateDataList[i].useSpecialPriceFlag == "1" ? "是" : "否";
                        li = li + '<span class="dsign_span" style="font-size:20px"><b>修改订单详情：</b></span>';
                        li = li + '<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />';
                        li = li + '<span class="dsign_span" style="color:green;">==========  原始订单  ==========</span><br>';
                        li = li + '订单开始时间：<span class="sign_span" style="color:red;">' + updateDataList[i].rentTime + '</span>\n' +
                            '<br>\n' +
                            '订单结束时间：<span class="sign_span" style="color:red;">' + updateDataList[i].revertTime + '</span>\n' +
                            '<br>\n' +
                            'carNo：<span class="sign_span" style="color:red;">' + updateDataList[i].carNo + '</span>\n' +
                            '<br>\n' +
                            '是否使用特供价：<span class="sign_span" style="color:red;">' + useSpecialPriceFlag + '</span>\n' +
                            '<br>\n' +
                            'car平日价：<span class="sign_span" style="color:red;">' + updateDataList[i].carDayPrice + '</span>\n' +
                            '<br>\n';

                        if (rentAmtType == "0") {
                            li = li + 'car节日价：<span class="sign_span" style="color:red;">' + updateDataList[i].carHolidayPrice + '</span>\n' +
                                '<br>\n';
                        } else if (rentAmtType == "1") {
                            li = li + 'car周末价：<span class="sign_span" style="color:red;">' + updateDataList[i].carWeekendPrice + '</span>\n' +
                                '<br>\n' +
                                'car节日价：<span class="sign_span" style="color:red;">' + updateDataList[i].carHolidayPrice + '</span>\n' +
                                '<br>\n';
                        } else if (rentAmtType == "2") {
                            li = li + 'car周末价：<span class="sign_span" style="color:red;">' + updateDataList[i].carWeekendPrice + '</span>\n' +
                                '<br>\n' +
                                'car节日价：<span class="sign_span" style="color:red;">' + updateDataList[i].carHolidayPrice + '</span>\n' +
                                '<br>\n';
                            if (updateDataList[i].useSpecialPriceFlag == "1") {
                                li = li + 'car春节价：<span class="sign_span" style="color:red;">' + updateDataList[i].carSpringPrice + '</span>\n' +
                                    '<br>\n';
                            }
                        }

                        li = li + '总小时数：<span class="sign_span" style="color:red;">' + updateDataList[i].totalH + '</span>小时\n' +
                            '<br>\n' +
                            '平日价所占租期：<span class="sign_span" style="color:red;">' + updateDataList[i].totalDayRentDate + '</span>小时\n' +
                            '<br>\n';

                        if (rentAmtType == "0") {
                            li = li + '节日价所占租期：<span class="sign_span" style="color:red;">' + updateDataList[i].totalHolidayRentDate + '</span>小时\n' +
                                '<br>\n';
                        } else if (rentAmtType == "1") {
                            li = li + '周末价所占租期：<span class="sign_span" style="color:red;">' + updateDataList[i].totalWeekendRentDate + '</span>小时\n' +
                                '<br>\n' +
                                '节日价所占租期：<span class="sign_span" style="color:red;">' + updateDataList[i].totalHolidayRentDate + '</span>小时\n' +
                                '<br>\n';
                        } else if (rentAmtType == "2") {
                            li = li + '周末价所占租期：<span class="sign_span" style="color:red;">' + updateDataList[i].totalWeekendRentDate + '</span>小时\n' +
                                '<br>\n' +
                                '节日价所占租期：<span class="sign_span" style="color:red;">' + updateDataList[i].totalHolidayRentDate + '</span>小时\n' +
                                '<br>\n';
                            if (updateDataList[i].useSpecialPriceFlag == "1") {
                                li = li + '春节价所占租期：<span class="sign_span" style="color:red;">' + updateDataList[i].totalSpringRentDate + '</span>小时\n' +
                                    '<br>\n';
                            }
                        }

                        li = li + '租期：<span class="sign_span" style="color:red;">' + updateDataList[i].rentDate + '</span>天\n' +
                            '<br>\n' +
                            '<br>\n' +
                            '订单日均价：<span class="sign_span" style="color:red;">' + updateDataList[i].avgDayPrice + '</span>元\n' +
                            '<br>\n' +
                            '订单时均价：<span class="sign_span" style="color:red;">' + updateDataList[i].avgHourPrice + '</span>元\n' +
                            '<br>\n' +
                            '租期天数：<span class="sign_span" style="color:red;">' + updateDataList[i].t + '</span>天\n' +
                            '<br>\n' +
                            '剩余小时数：<span class="sign_span" style="color:red;">' + updateDataList[i].h + '</span>小时\n' +
                            '<br>\n' +
                            '租金（<span class="sign_span" style="color:red;">规则：日均价 * 租期天数 + 时均价 * 剩余小时数</span>）为：<span class="sign_span" style="color:red;">' + updateDataList[i].rentAmt + '</span>元\n' +
                            '<br>\n' +
                            '<br>\n' +
                            '<br>';
                    }else{
                        var useSpecialPriceFlag = updateDataList[i].useSpecialPriceFlag == "1" ? "是" : "否";
                        if(updateDataList[i].updateFlag == "1"){        //租期或carNo有变化，租金要重新计算
                            li = li + '<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />';
                            li = li + '<span class="dsign_span" style="color:green;">==========  第' + i + '次修改订单  ==========</span><br>';
                            li = li + '订单开始时间：<span class="sign_span" style="color:red;">' + updateDataList[i].rentTime + '</span>\n' +
                                '<br>\n' +
                                '订单结束时间：<span class="sign_span" style="color:red;">' + updateDataList[i].revertTime + '</span>\n' +
                                '<br>\n' +
                                'carNo：<span class="sign_span" style="color:red;">' + updateDataList[i].carNo + '</span>\n' +
                                '<br>\n' +
                                '是否使用特供价：<span class="sign_span" style="color:red;">' + useSpecialPriceFlag + '</span>\n' +
                                '<br>\n' +
                                'car平日价：<span class="sign_span" style="color:red;">' + updateDataList[i].carDayPrice + '</span>\n' +
                                '<br>\n';

                            if (rentAmtType == "0") {
                                li = li + 'car节日价：<span class="sign_span" style="color:red;">' + updateDataList[i].carHolidayPrice + '</span>\n' +
                                    '<br>\n';
                            } else if (rentAmtType == "1") {
                                li = li + 'car周末价：<span class="sign_span" style="color:red;">' + updateDataList[i].carWeekendPrice + '</span>\n' +
                                    '<br>\n' +
                                    'car节日价：<span class="sign_span" style="color:red;">' + updateDataList[i].carHolidayPrice + '</span>\n' +
                                    '<br>\n';
                            } else if (rentAmtType == "2") {
                                li = li + 'car周末价：<span class="sign_span" style="color:red;">' + updateDataList[i].carWeekendPrice + '</span>\n' +
                                    '<br>\n' +
                                    'car节日价：<span class="sign_span" style="color:red;">' + updateDataList[i].carHolidayPrice + '</span>\n' +
                                    '<br>\n';
                                if (updateDataList[i].useSpecialPriceFlag == "1") {
                                    li = li + 'car春节价：<span class="sign_span" style="color:red;">' + updateDataList[i].carSpringPrice + '</span>\n' +
                                        '<br>\n';
                                }
                            }

                            li = li + '总租期：<span class="sign_span" style="color:red;">' + updateDataList[i].totalRentDate + '</span>天\n' +
                                '<br>\n' +
                                '<b>总租金</b>（<span class="sign_span" style="color:red;">规则：各阶段租金之和</span>）为：<span class="sign_span" style="color:red;">' + updateDataList[i].rentAmt + '</span>元\n' +
                                '<br>\n' +
                                '<br>\n<br>\n<span class="dsign_span"><b>租金阶段详情：</b></span><br>\n' +
                                '描述：<br>&nbsp;&nbsp;&nbsp;<span class="sign_span" style="color:dodgerblue;">1.数据源：';
                            if(updateDataList[i].type == "1"){
                                li = li + '"trans_modification_application"表中"id"=' + updateDataList[i].id;
                            }
                            if(updateDataList[i].type == "2"){
                                li = li + '"trans_modification_console"表中"id"=' + updateDataList[i].id;
                            }
                            li = li + '</span>' +
                                '<br>\n' +
                                '&nbsp;&nbsp;&nbsp;<span class="sign_span" style="color:dodgerblue;">2.触发原因：' + updateDataList[i].msg + '</span>' +
                                '<br>\n' +
                                '&nbsp;&nbsp;&nbsp;<span class="sign_span" style="color:dodgerblue;">3.所处阶段：第' + updateDataList[i].batch + '批，第' + updateDataList[i].part + '段租金轴</span>' +
                                '<br>\n' +

                                '阶段开始时间：<span class="sign_span" style="color:red;">' + updateDataList[i].continueTime + '</span>\n' +
                                '<br>\n' +
                                '阶段结束时间：<span class="sign_span" style="color:red;">' + updateDataList[i].revertTime + '</span>\n' +
                                '<br>\n' +
                                '总小时数：<span class="sign_span" style="color:red;">' + updateDataList[i].totalH + '</span>小时\n' +
                                '<br>\n' +
                                '平日价所占租期：<span class="sign_span" style="color:red;">' + updateDataList[i].totalDayRentDate + '</span>小时\n' +
                                '<br>\n';

                            if (rentAmtType == "0") {
                                li = li + '节日价所占租期：<span class="sign_span" style="color:red;">' + updateDataList[i].totalHolidayRentDate + '</span>小时\n' +
                                    '<br>\n';
                            } else if (rentAmtType == "1") {
                                li = li + '周末价所占租期：<span class="sign_span" style="color:red;">' + updateDataList[i].totalWeekendRentDate + '</span>小时\n' +
                                    '<br>\n' +
                                    '节日价所占租期：<span class="sign_span" style="color:red;">' + updateDataList[i].totalHolidayRentDate + '</span>小时\n' +
                                    '<br>\n';
                            } else if (rentAmtType == "2") {
                                li = li + '周末价所占租期：<span class="sign_span" style="color:red;">' + updateDataList[i].totalWeekendRentDate + '</span>小时\n' +
                                    '<br>\n' +
                                    '节日价所占租期：<span class="sign_span" style="color:red;">' + updateDataList[i].totalHolidayRentDate + '</span>小时\n' +
                                    '<br>\n';
                                if (updateDataList[i].useSpecialPriceFlag == "1") {
                                    li = li + '春节价所占租期：<span class="sign_span" style="color:red;">' + updateDataList[i].totalSpringRentDate + '</span>小时\n' +
                                        '<br>\n';
                                }
                            }

                            li = li + '租期：<span class="sign_span" style="color:red;">' + updateDataList[i].rentDate + '</span>天\n' +
                                '<br>\n' +
                                '<br>\n' +
                                '订单日均价：<span class="sign_span" style="color:red;">' + updateDataList[i].avgDayPrice + '</span>元\n' +
                                '<br>\n' +
                                '订单时均价：<span class="sign_span" style="color:red;">' + updateDataList[i].avgHourPrice + '</span>元\n' +
                                '<br>\n' +
                                '租期天数：<span class="sign_span" style="color:red;">' + updateDataList[i].t + '</span>天\n' +
                                '<br>\n' +
                                '剩余小时数：<span class="sign_span" style="color:red;">' + updateDataList[i].h + '</span>小时\n' +
                                '<br>\n' +
                                '租金（<span class="sign_span" style="color:red;">规则：日均价 * 租期天数 + 时均价 * 剩余小时数</span>）为：<span class="sign_span" style="color:red;">' + updateDataList[i].stageRentAmt + '</span>元\n' +
                                '<br>\n<br>\n' +
                                '<br>';

                        }
                        if(updateDataList[i].updateFlag == "0"){        //租期没变化，租金不用重新计算
                            li = li + '<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />';
                            li = li + '<span class="dsign_span" style="color:green;">==========  第' + i + '次修改订单  ==========</span><br>';
                            li = li + '<span class="dsign_span" style="color:red;"><b>租金无变化：</b></span><br>\n' +
                                '描述：<br>&nbsp;&nbsp;&nbsp;<span class="sign_span" style="color:dodgerblue;">1.数据源：';
                            if(updateDataList[i].type == "1"){
                                li = li + '"trans_modification_application"表中"id"=' + updateDataList[i].id;
                            }
                            if(updateDataList[i].type == "2"){
                                li = li + '"trans_modification_console"表中"id"=' + updateDataList[i].id;
                            }
                            li = li + '</span>' +
                                '<br>\n' +
                                '&nbsp;&nbsp;&nbsp;<span class="sign_span" style="color:dodgerblue;">2.触发原因：' + updateDataList[i].msg + '</span>' +
                                '<br>\n' +
                                '&nbsp;&nbsp;&nbsp;<span class="sign_span" style="color:dodgerblue;">3.所处阶段：第' + updateDataList[i].batch + '批，第' + updateDataList[i].part + '段租金轴</span>' +
                                '<br>\n<br>\n<br>\n' +
                                '<br>';
                        }

                    }
                }

                // var li = "<br><span class='sign_span'><b>结果：<br>" + data + "</b></span>";
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


//计算订单租金
function computeRentAmt(event){
    $("#result_msg").empty();
    var environment = $("#set_environment").val();
    var startTime = $("#input_rentTime").val().trim();
    var endTime = $("#input_revertTime").val().trim();
    var dayPrice = $("#input_dayPrice").val().trim();
    var weekendPrice = $("#input_weekendPrice").val().trim();
    var holiydaPrice = $("#input_holidayPrice").val().trim();
    var springPrice = $("#input_springPrice").val().trim();
    var rentAmtType = $('input:radio[name=rentAmt]:checked').val();


    // console.log("rentAmtType:" + rentAmtType);

    if (environment == "-请选择-") {
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_environment.html");//显示对话框
        return;
    }

    if (startTime == "") {
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_trans_start_time.html");//显示对话框
        return;
    }
    if (endTime == "") {
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_trans_end_time.html");//显示对话框
        return;
    }
    if (dayPrice == "") {
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_trans_day_price.html");//显示对话框
        return;
    }
    if (holiydaPrice == "") {
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_trans_holiday_price.html");//显示对话框
        return;
    }
    if (weekendPrice == "" && (rentAmtType == "1" || rentAmtType == "2")) {
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_trans_weekend_price.html");//显示对话框
        return;
    }
    if (springPrice == "" && rentAmtType == "2") {
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_trans_spring_price.html");//显示对话框
        return;
    }

    if (rentAmtType == "2") {
        $.ajax({
            url:event.data.ip + "/trans/computerentAmtv3",
            type:"post",
            data:{"environment":environment,"startTime":startTime,"endTime":endTime,"dayPrice":dayPrice,"weekendPrice":weekendPrice, "holiydaPrice":holiydaPrice, "springPrice":springPrice},
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
    } else if (rentAmtType == "1") {
        $.ajax({
            url:event.data.ip + "/trans/computerentAmtv2",
            type:"post",
            data:{"environment":environment,"startTime":startTime,"endTime":endTime,"dayPrice":dayPrice,"weekendPrice":weekendPrice, "holiydaPrice":holiydaPrice},
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
    } else {
        $.ajax({
            url:event.data.ip + "/trans/computerentAmt",
            type:"post",
            data:{"environment":environment,"startTime":startTime,"endTime":endTime,"dayPrice":dayPrice,"holiydaPrice":holiydaPrice},
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

}



//校验时间轴
function checktimeaxis(event){
    $("#result_msg").empty();

    //获取客户端ip地址
    // var url = "http://localhost:8080/test/trans/checktimeaxis";
    // $.getJSON(url, function(data){
    //     alert(data.ip);
    // });

    var environment = $("#set_environment").val();
    var carNo = $("#input_carNo").val().trim();
    var rentTime = $("#input_rentTime").val().trim();
    var revertTime = $("#input_revertTime").val().trim();
    var srvFlag = $("#set_srvFlag").val();					//取还车开关

    //调试
    // environment = "baseDB";
    // carNo = "4439552671";
    // rentTime = "20180620031500";
    // revertTime = "20180623080000";
    // srvFlag = "0";
    // if(carNo=="1"){
    // carNo = "443955267";
    // }else{
    // carNo = "4439552671";
    // }

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

    if(srvFlag == "-请选择-"){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_srv_flag.html");//显示对话框
        return;
    }
    if(srvFlag == "不使用取换车"){
        srvFlag = "0";
    }
    if(srvFlag == "使用取换车"){
        srvFlag = "1";
        alert("需求不明，暂无");
        return;
    }

    $.ajax({
        url:event.data.ip + "/trans/checktimeaxis",
        type:"post",
        data:{"environment":environment,"carNo":carNo,"rentTime":rentTime,"revertTime":revertTime,"srvFlag":srvFlag},
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

                var data = result.data;
                var li = '';

                //调试字体颜色
                // li = li + '<span class="dsign_span" style="color:orange;">==============测试颜色逻辑==============</span><br>';
                // li = li + '<span class="dsign_span" style="color:darkturquoise;">==============测试颜色逻辑==============</span><br>';
                // li = li + '<span class="dsign_span" style="color:darkorange;">==============测试颜色逻辑==============</span><br>';
                // li = li + '<span class="dsign_span" style="color:greenyellow;">==============测试颜色逻辑==============</span><br>';
                // li = li + '<span class="dsign_span" style="color:green;">==============测试颜色逻辑==============</span><br>';
                // li = li + '<span class="dsign_span" style="color:lightseagreen;">==============测试颜色逻辑==============</span><br>';
                // li = li + '<span class="dsign_span" style="color:lawngreen;">==============测试颜色逻辑==============</span><br>';
                // li = li + '<span class="dsign_span" style="color:dodgerblue;">==============测试颜色逻辑==============</span><br>';


                li = li + '<br><hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />';

                if(srvFlag=="0"){		//不使用取还车服务
                    var resultFlag = null;			//请求时间是否可租标识
                    var timeAxisList = null;		//对应每张表提示
                    var rentData = null;			//租期
                    var rentDay = null;				//租期天数

                    $.each(data,function (dataKey,dataValue) {
                        if(dataKey=="resultFlag"){
                            resultFlag = dataValue;
                        }
                        if(dataKey=="timeAxisList"){
                            timeAxisList = dataValue;
                        }
                        if(dataKey=="rentData"){
                            rentData = dataValue;
                        }
                        if(dataKey=="rentDay"){
                            rentDay = dataValue;
                        }
                    })

                    li = li + '订单开始时间：<span class="dsign_span" style="color:red;">'+rentData+'</span><br>';
                    li = li + '订单天数：<span class="dsign_span" style="color:red;">'+rentDay+'天</span><br>';
                    li = li + '服务：<span class="dsign_span" style="color:red;">不使用取还车服务</span><br><br>';

                    if(resultFlag=="1"){		//请求时间可租
                        li = li + '时间轴校验结果：<span class="dsign_span" style="color:green;"><b>可租</b><br></span><br><span class="dsign_span" style="font-size:20px"><b>校验详情：</b></span><br>';
                        li = li + '<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />';
                    }else{						//请求时间不可租
                        li = li + '时间轴校验结果：<span class="dsign_span" style="color:red;"><b>不可租</b><br></span><br><span class="dsign_span" style="font-size:20px"><b>校验详情：</b></span><br>';
                        li = li + '<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />';
                    }


                    $.each(timeAxisList,function (i,val) {
                        var name = null;
                        var timeAxisFlag = null;
                        var msg = null;
                        var resultMsg = null;
                        $.each(val,function (key,value) {
                            if(key=="name"){
                                name = value;
                            }
                            if(key=="timeAxisFlag"){
                                timeAxisFlag = value;
                            }
                            if(key=="msg"){
                                msg = value;
                            }
                            if(key=="resultMsg"){
                                resultMsg = value;
                            }
                        })

                        if(timeAxisFlag=="1"){	//租期符合该表时间轴逻辑
                            li = li + '<span class="dsign_span" style="color:green;">==========  ' + name + '逻辑  ==========</span><br>';
                            li = li + '该表校验结果：<span class="dsign_span" style="color:green;">	通过</span><br><br>';
                            // li = li + '<span class="dsign_span style="color:red;"">	'+name+'不可租时间轴：'+msg+'</span><br><br>';
                            li = li + '该表不可租时间轴：' + msg + '<br><br>';
                            li = li + '<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />';
                        }else{					//租期不符合该表时间轴逻辑
                            li = li + '<span class="dsign_span" style="color:red;">==========  ' + name + '逻辑  ==========</span><br>';
                            li = li + '该表校验结果：<span class="dsign_span" style="color:red;">	不通过</span><br><br>';
                            // li = li + '<span class="dsign_span style="color:red;"">	'+name+'不可租时间轴：'+msg+'</span><br>';
                            // li = li + '<span class="dsign_span style="color:red;"">	校验结果不可租时间轴：'+resultMsg+'</span><br><br>';
                            li = li + '该表不可租时间轴：' + msg + '<br><br>';
                            li = li + '请求租期冲突时间轴：<br>' + resultMsg + '<br><br>';
                            li = li + '<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />';
                        }
                    })

                }else{			//使用取还车服务
                    console.log("使用取还车服务");
                }

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



//修改订单计算租金
function updateTrans(event){
    $("#result_msg").empty();
    var environment = $("#set_environment").val();
    var startTime1 = $("#input_startTime1").val() == undefined ? '' : $("#input_startTime1").val().trim();
    var endTime1 = $("#input_endTime1").val() == undefined ? '' : $("#input_endTime1").val().trim();
    var dayPrice1 = $("#input_dayPrice1").val() == undefined ? '' : $("#input_dayPrice1").val().trim();
    var weekendPrice1 = $("#input_weekendPrice1").val() == undefined ? '' : $("#input_weekendPrice1").val().trim();
    var holiydaPrice1 = $("#input_holidayPrice1").val() == undefined ? '' : $("#input_holidayPrice1").val().trim();
    var springdaPrice1 = $("#input_springPrice1").val() == undefined ? '' : $("#input_springPrice1").val().trim();

    var rentAmtType = $('input:radio[name=rentAmt]:checked').val();

    if(environment == "-请选择-"){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_environment.html");//显示对话框
        return;
    }

    if(startTime1 == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_trans_start_time.html");//显示对话框
        return;
    }
    if(endTime1 == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_trans_end_time.html");//显示对话框
        return;
    }
    if(dayPrice1 == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_trans_day_price.html");//显示对话框
        return;
    }
    if(holiydaPrice1 == ""){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_trans_holiday_price.html");//显示对话框
        return;
    }

    var startTime2 = $("#input_startTime2").val().trim();
    var endTime2 = $("#input_endTime2").val().trim();
    var dayPrice2 = $("#input_dayPrice2").val().trim();
    var weekendPrice2 = $("#input_weekendPrice2").val() == undefined ? '' : $("#input_weekendPrice2").val().trim();
    var holiydaPrice2 = $("#input_holidayPrice2").val().trim();
    var springdaPrice2 = $("#input_springPrice2").val() == undefined ? '' : $("#input_springPrice2").val().trim();

    var startTime3 = $("#input_startTime3").val().trim();
    var endTime3 = $("#input_endTime3").val().trim();
    var dayPrice3 = $("#input_dayPrice3").val().trim();
    var weekendPrice3 = $("#input_weekendPrice3").val() == undefined ? '' : $("#input_weekendPrice3").val().trim();
    var holiydaPrice3 = $("#input_holidayPrice3").val().trim();
    var springdaPrice3 = $("#input_springPrice3").val() == undefined ? '' : $("#input_springPrice3").val().trim();

    var startTime4 = $("#input_startTime4").val() == undefined ? '' : $("#input_startTime4").val().trim();
    var endTime4 = $("#input_endTime4").val() == undefined ? '' : $("#input_endTime4").val().trim();
    var dayPrice4 = $("#input_dayPrice4").val() == undefined ? '' : $("#input_dayPrice4").val().trim();
    var weekendPrice4 = $("#input_weekendPrice4").val() == undefined ? '' : $("#input_weekendPrice4").val().trim();
    var holiydaPrice4 = $("#input_holidayPrice4").val() == undefined ? '' : $("#input_holidayPrice4").val().trim();
    var springdaPrice4 = $("#input_springPrice4").val() == undefined ? '' : $("#input_springPrice4").val().trim();

    var map = {};
    var trans1 = new Object();
    trans1.rentTime = startTime1;
    trans1.revertTime = endTime1;
    trans1.dayPrice = dayPrice1;
    trans1.weekendPrice = weekendPrice1;
    trans1.holiydaPrice = holiydaPrice1;
    trans1.springPrice = springdaPrice1;

    var trans2 = new Object();
    trans2.rentTime = startTime2;
    trans2.revertTime = endTime2;
    trans2.dayPrice = dayPrice2;
    trans2.weekendPrice = weekendPrice2;
    trans2.holiydaPrice = holiydaPrice2;
    trans2.springPrice = springdaPrice2;

    var trans3 = new Object();
    trans3.rentTime = startTime3;
    trans3.revertTime = endTime3;
    trans3.dayPrice = dayPrice3;
    trans3.weekendPrice = weekendPrice3;
    trans3.holiydaPrice = holiydaPrice3;
    trans3.springPrice = springdaPrice3;

    var trans4 = new Object();
    trans4.rentTime = startTime4;
    trans4.revertTime = endTime4;
    trans4.dayPrice = dayPrice4;
    trans4.weekendPrice = weekendPrice4;
    trans4.holiydaPrice = holiydaPrice4;
    trans4.springPrice = springdaPrice4;

    map["trans1"] = trans1;
    map["trans2"] = trans2;
    map["trans3"] = trans3;
    map["trans4"] = trans4;
    map["rentAmtType"] = rentAmtType;

    var mapJson = JSON.stringify(map);

    $.ajax({
        url:event.data.ip + "/trans/updateTrans",
        type:"post",
        // data:{"environment":environment,"startTime1":startTime1,"endTime1":endTime1,"dayPrice1":dayPrice1,"holiydaPrice1":holiydaPrice1,"startTime2":startTime2,"endTime2":endTime2,"dayPrice2":dayPrice2,"holiydaPrice2":holiydaPrice2,"startTime3":startTime3,"endTime3":endTime3,"dayPrice3":dayPrice3,"holiydaPrice3":holiydaPrice3,"startTime4":startTime4,"endTime4":endTime4,"dayPrice4":dayPrice4,"holiydaPrice4":holiydaPrice4,"mapJson":mapJson,"arrJson":arrJson},
        data:{"environment":environment,"mapJson":mapJson},
        dataType:"json",
        // contentType:"application/json",
        success:function(result){
            if(result.status==1){
                $("#result_msg").empty();

                var resultMsg = result.data;
                var li = "<br><span class='sign_span'><b>结果：<br></span><br><span class='sign_span' style='color:red;'>" + resultMsg + "</span></b></span>";
                var $li = $(li);
                $("#result_msg").append($li);
            }

            if(result.status == 0){
                $("#result_msg").empty();

                var resultMsg = result.data;
                var li = "";
                for (var i = 0; i < resultMsg.length; i++) {
                    if(i == 0){
                        li = li + '<span class="dsign_span" style="font-size:20px"><b>结果：</b></span>';
                        li = li + '<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />';
                        li = li + '<span class="dsign_span" style="color:green;">==========  原始订单  ==========</span><br>';
                        li = li + '订单开始时间：<span class="sign_span" style="color:red;">' + resultMsg[i].rentTime + '</span>\n' +
                            '<br>\n' +
                            '订单结束时间：<span class="sign_span" style="color:red;">' + resultMsg[i].revertTime + '</span>\n' +
                            '<br>\n' +
                            'car平日价：<span class="sign_span" style="color:red;">' + resultMsg[i].carDayPrice + '</span>\n' +
                            '<br>\n';

                        if (rentAmtType == "0") {
                            li = li + 'car节日价：<span class="sign_span" style="color:red;">' + resultMsg[i].carHolidayPrice + '</span>\n' +
                                '<br>\n';
                        } else if (rentAmtType == "1") {
                            li = li + 'car周末价：<span class="sign_span" style="color:red;">' + resultMsg[i].carWeekendPrice + '</span>\n' +
                                '<br>\n' +
                                'car节日价：<span class="sign_span" style="color:red;">' + resultMsg[i].carHolidayPrice + '</span>\n' +
                                '<br>\n';
                        } else if (rentAmtType == "2") {
                            li = li + 'car周末价：<span class="sign_span" style="color:red;">' + resultMsg[i].carWeekendPrice + '</span>\n' +
                                '<br>\n' +
                                'car节日价：<span class="sign_span" style="color:red;">' + resultMsg[i].carHolidayPrice + '</span>\n' +
                                '<br>\n';
                            if (resultMsg[i].useSpecialPriceFlag == "1") {
                                li = li + 'car春节价：<span class="sign_span" style="color:red;">' + resultMsg[i].carSpringPrice + '</span>\n' +
                                    '<br>\n';
                            }
                        }

                        li = li + '总小时数：<span class="sign_span" style="color:red;">' + resultMsg[i].totalH + '</span>小时\n' +
                            '<br>\n' +
                            '平日价所占租期：<span class="sign_span" style="color:red;">' + resultMsg[i].totalDayRentDate + '</span>小时\n' +
                            '<br>\n';

                        if (rentAmtType == "0") {
                            li = li + '节日价所占租期：<span class="sign_span" style="color:red;">' + resultMsg[i].totalHolidayRentDate + '</span>小时\n' +
                                '<br>\n';
                        } else if (rentAmtType == "1") {
                            li = li + '周末价所占租期：<span class="sign_span" style="color:red;">' + resultMsg[i].totalWeekendRentDate + '</span>小时\n' +
                                '<br>\n' +
                                '节日价所占租期：<span class="sign_span" style="color:red;">' + resultMsg[i].totalHolidayRentDate + '</span>小时\n' +
                                '<br>\n';
                        } else if (rentAmtType == "2") {
                            li = li + '周末价所占租期：<span class="sign_span" style="color:red;">' + resultMsg[i].totalWeekendRentDate + '</span>小时\n' +
                                '<br>\n' +
                                '节日价所占租期：<span class="sign_span" style="color:red;">' + resultMsg[i].totalHolidayRentDate + '</span>小时\n' +
                                '<br>\n';
                            if (resultMsg[i].useSpecialPriceFlag == "1") {
                                li = li + '春节价所占租期：<span class="sign_span" style="color:red;">' + resultMsg[i].totalSpringRentDate + '</span>小时\n' +
                                    '<br>\n';
                            }
                        }

                        li = li + '租期：<span class="sign_span" style="color:red;">' + resultMsg[i].rentDate + '</span>天\n' +
                            '<br>\n' +
                            '<br>\n' +
                            '订单日均价：<span class="sign_span" style="color:red;">' + resultMsg[i].avgDayPrice + '</span>元\n' +
                            '<br>\n' +
                            '订单时均价：<span class="sign_span" style="color:red;">' + resultMsg[i].avgHourPrice + '</span>元\n' +
                            '<br>\n' +
                            '租期天数：<span class="sign_span" style="color:red;">' + resultMsg[i].t + '</span>天\n' +
                            '<br>\n' +
                            '剩余小时数：<span class="sign_span" style="color:red;">' + resultMsg[i].h + '</span>小时\n' +
                            '<br>\n' +
                            '租金（<span class="sign_span" style="color:red;">规则：日均价 * 租期天数 + 时均价 * 剩余小时数</span>）为：<span class="sign_span" style="color:red;">' + resultMsg[i].rentAmt + '</span>元\n' +
                            '<br>\n' +
                            '<br>\n' +
                            '<br>';
                    }else{
                        li = li + '<hr style="height:1px;border:none;border-top:1px dashed #0066CC;" />';
                        li = li + '<span class="dsign_span" style="color:green;">==========  第' + i + '次修改订单  ==========</span><br>';
                        li = li + '订单开始时间：<span class="sign_span" style="color:red;">' + resultMsg[i].rentTime + '</span>\n' +
                            '<br>\n' +
                            '订单结束时间：<span class="sign_span" style="color:red;">' + resultMsg[i].revertTime + '</span>\n' +
                            '<br>\n' +
                            'car平日价：<span class="sign_span" style="color:red;">' + resultMsg[i].carDayPrice + '</span>\n' +
                            '<br>\n';

                        if (rentAmtType == "0") {
                            li = li + 'car节日价：<span class="sign_span" style="color:red;">' + resultMsg[i].carHolidayPrice + '</span>\n' +
                                '<br>\n';
                        } else if (rentAmtType == "1") {
                            li = li + 'car周末价：<span class="sign_span" style="color:red;">' + resultMsg[i].carWeekendPrice + '</span>\n' +
                                '<br>\n' +
                                'car节日价：<span class="sign_span" style="color:red;">' + resultMsg[i].carHolidayPrice + '</span>\n' +
                                '<br>\n';
                        } else if (rentAmtType == "2") {
                            li = li + 'car周末价：<span class="sign_span" style="color:red;">' + resultMsg[i].carWeekendPrice + '</span>\n' +
                                '<br>\n' +
                                'car节日价：<span class="sign_span" style="color:red;">' + resultMsg[i].carHolidayPrice + '</span>\n' +
                                '<br>\n';
                            if (resultMsg[i].useSpecialPriceFlag == "1") {
                                li = li + 'car春节价：<span class="sign_span" style="color:red;">' + resultMsg[i].carSpringPrice + '</span>\n' +
                                    '<br>\n';
                            }
                        }

                        li = li + '总租期：<span class="sign_span" style="color:red;">' + resultMsg[i].totalRentDate + '</span>天\n' +
                            '<br>\n' +
                            '<b>总租金</b>（<span class="sign_span" style="color:red;">规则：各阶段租金之和</span>）为：<span class="sign_span" style="color:red;">' + resultMsg[i].rentAmt + '</span>元\n' +
                            '<br>\n' +
                            '<br>\n<br>\n<span class="dsign_span"><b>租金阶段详情：</b></span><br>\n' +
                            '描述：<span class="sign_span" style="color:dodgerblue;">阶段所处第' + resultMsg[i].batch + '批，第' + resultMsg[i].part + '段租金轴</span>' +
                            '<br>\n' +

                            '阶段开始时间：<span class="sign_span" style="color:red;">' + resultMsg[i].continueTime + '</span>\n' +
                            '<br>\n' +
                            '阶段结束时间：<span class="sign_span" style="color:red;">' + resultMsg[i].revertTime + '</span>\n' +
                            '<br>\n' +
                            '总小时数：<span class="sign_span" style="color:red;">' + resultMsg[i].totalH + '</span>小时\n' +
                            '<br>\n' +
                            '平日价所占租期：<span class="sign_span" style="color:red;">' + resultMsg[i].totalDayRentDate + '</span>小时\n' +
                            '<br>\n';

                        if (rentAmtType == "0") {
                            li = li + '节日价所占租期：<span class="sign_span" style="color:red;">' + resultMsg[i].totalHolidayRentDate + '</span>小时\n' +
                                '<br>\n';
                        } else if (rentAmtType == "1") {
                            li = li + '周末价所占租期：<span class="sign_span" style="color:red;">' + resultMsg[i].totalWeekendRentDate + '</span>小时\n' +
                                '<br>\n' +
                                '节日价所占租期：<span class="sign_span" style="color:red;">' + resultMsg[i].totalHolidayRentDate + '</span>小时\n' +
                                '<br>\n';
                        } else if (rentAmtType == "2") {
                            li = li + '周末价所占租期：<span class="sign_span" style="color:red;">' + resultMsg[i].totalWeekendRentDate + '</span>小时\n' +
                                '<br>\n' +
                                '节日价所占租期：<span class="sign_span" style="color:red;">' + resultMsg[i].totalHolidayRentDate + '</span>小时\n' +
                                '<br>\n';
                            if (resultMsg[i].useSpecialPriceFlag == "1") {
                                li = li + '春节价所占租期：<span class="sign_span" style="color:red;">' + resultMsg[i].totalSpringRentDate + '</span>小时\n' +
                                    '<br>\n';
                            }
                        }

                        li = li + '租期：<span class="sign_span" style="color:red;">' + resultMsg[i].rentDate + '</span>天\n' +
                            '<br>\n' +
                            '<br>\n' +
                            '订单日均价：<span class="sign_span" style="color:red;">' + resultMsg[i].avgDayPrice + '</span>元\n' +
                            '<br>\n' +
                            '订单时均价：<span class="sign_span" style="color:red;">' + resultMsg[i].avgHourPrice + '</span>元\n' +
                            '<br>\n' +
                            '租期天数：<span class="sign_span" style="color:red;">' + resultMsg[i].t + '</span>天\n' +
                            '<br>\n' +
                            '剩余小时数：<span class="sign_span" style="color:red;">' + resultMsg[i].h + '</span>小时\n' +
                            '<br>\n' +
                            '租金（<span class="sign_span" style="color:red;">规则：日均价 * 租期天数 + 时均价 * 剩余小时数</span>）为：<span class="sign_span" style="color:red;">' + resultMsg[i].stageRentAmt + '</span>元\n' +
                            '<br>\n<br>\n' +
                            '<br>';
                    }
                }
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

//修改订单计算租金_输入测试数据
function inputTestData(event){
    $("#result_msg").empty();
    $("#input_startTime1").val("20180820090000");
    $("#input_endTime1").val("20180822200000");
    $("#input_dayPrice1").val("500");
    $("#input_weekendPrice1").val("700");
    $("#input_holidayPrice1").val("1000");
    $("#input_springPrice1").val("1500");

    $("#input_startTime2").val("20180820090000");
    $("#input_endTime2").val("20180823071500");
    $("#input_dayPrice2").val("600");
    $("#input_weekendPrice2").val("800");
    $("#input_holidayPrice2").val("1100");
    $("#input_springPrice2").val("1600");

    $("#input_startTime3").val("20180820090000");
    $("#input_endTime3").val("20180821071500");
    $("#input_dayPrice3").val("500");
    $("#input_weekendPrice3").val("700");
    $("#input_holidayPrice3").val("1000");
    $("#input_springPrice3").val("1700");

    $("#input_startTime4").val("20180820090000");
    $("#input_endTime4").val("20180822081500");
    $("#input_dayPrice4").val("700");
    $("#input_weekendPrice4").val("900");
    $("#input_holidayPrice4").val("1200");
    $("#input_springPrice4").val("1800");
}


//时间轴测试数据
function inputTimeTestData(event){
    $("#result_msg").empty();
    $("#input_carNo").val("443955267");
    $("#input_rentTime").val("20180620031500");
    $("#input_revertTime").val("20180623130000");
}



//计算取还车费用距离系数
function getdistance(event){
    $("#result_msg").empty();
    var environment = $("#set_environment").val();
    var carNo = $("#input_carNo").val().trim();
    var coordinate = $("#select_addressList").val();                //经纬度坐标
    var address = $("#select_addressList option:selected").text();  //select地址名
    // console.log("coordinate:" + coordinate);
    // console.log("address:" + address);

    if(environment == "-请选择-"){
        $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
        $("#can").load("alert/alert_environment.html");//显示对话框
        return;
    }

    if(carNo == ""){
        alert("请输入carNo");
        return;
    }
    if(coordinate == null || coordinate == ""){
        alert("请选择地址");
        return;
    }

    $.ajax({
        url:event.data.ip + "/trans/getDistance",
        type:"post",
        data:{"environment":environment,"carNo":carNo,"coordinate":coordinate},
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


//设置订单状态流程
function statusflow(event){
    $("#result_msg").empty();
    var environment = $("#set_environment").val();
    var orderNo = $("#input_orderNo").val().trim();
    var statusType = $("#set_statusType").val();
    var stepFlag = "";

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

    if(orderNo == ""){
        alert("请输入orderNo");
        return;
    }
    if(statusType == ""){
        alert("请选择订单状态");
        return;
    }


    stepFlag = "" + statusType;
    // console.log("statusType:" + statusType)
    // console.log("stepFlag:" + stepFlag);;
    // if(statusType == "租客取车（status=12）"){
    //     stepFlag = "1";
    // }
    // if(statusType == "确认还车（status=13）"){
    //     stepFlag = "2";
    // }
    // if(statusType == "双方评价（status=20）"){
    //     stepFlag = "3";
    // }

    $("#status_flow").attr({"disabled":"true"});    //提交按钮置灰
    var li = "<br><br><span class='sign_span' style='color:red;'><b>正在等待【定时任务】执行完毕，请稍等...<br><br>";
    var $li = $(li);
    $("#result_msg").append($li);

    $.ajax({
        url:event.data.ip + "/trans/statusFlow",
        timeout : 120000,   //等待2分钟
        type:"post",
        data:{"environment":environment,"orderNo":orderNo,"stepFlag":stepFlag},
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
                $("#status_flow").removeAttr("disabled");   //提交按钮恢复

                var resultMsg = result.data;
                // var li = "<br><br><span class='sign_span'><b>结果：<br><br>" + resultMsg + "</b></span>";
                var li = "<br><span class='sign_span'><b>结果：<br></span><br><span class='sign_span' style='color:red;'>" + resultMsg + "</span></b></span>";
                var $li = $(li);
                $("#result_msg").append($li);
            }
        },
        error:function(){
            $("#result_msg").empty();
            $("#status_flow").removeAttr("disabled");   //提交按钮恢复
            $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
            $("#can").load("alert/alert_error.html");//显示对话框
        }
    });
}



//携程下单
function createCtripTrans(event){
    $("#result_msg").empty();
    var environment = $("#set_environment").val();
    var pickupDate = $("#input_rentTime").val().trim();
    var returnDate = $("#input_revertTime").val().trim();
    var cityCode = $("#input_cityCode").val().trim();
    var vehicleCode = $("#input_value").val().trim();




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

    if(pickupDate == ""){
        alert("请输入取车时间");
        return;
    }

    if(returnDate == ""){
        alert("请输入还车时间");
        return;
    }


    $.ajax({
        url:event.data.ip + "/ctrip/createTrans",
        type:"post",
        data:{"environment":environment,"pickupDate":pickupDate,"returnDate":returnDate,"cityCode":cityCode,"vehicleCode":vehicleCode},
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
                $("#status_flow").removeAttr("disabled");   //提交按钮恢复

                var resultMsg = result.data;
                // var li = "<br><br><span class='sign_span'><b>结果：<br><br>" + resultMsg + "</b></span>";
                var li = "<br><span class='sign_span'><b>结果：<br></span><br><pre class='sign_span' style='color:red;'>" + resultMsg + "</pre></b></pre>";
                var $li = $(li);
                $("#result_msg").append($li);
            }
        },
        error:function(){
            $("#result_msg").empty();
            $("#status_flow").removeAttr("disabled");   //提交按钮恢复
            $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
            $("#can").load("alert/alert_error.html");//显示对话框
        }
    });
}





//携程库存查询
function selectCtripInventory(event){
    $("#result_msg").empty();
    var environment = $("#set_environment").val();
    var pickupDate = $("#input_rentTime").val().trim();
    var returnDate = $("#input_revertTime").val().trim();
    var cityCode = $("#input_cityCode").val().trim();



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

    if(pickupDate == ""){
        alert("请输入取车时间");
        return;
    }

    if(returnDate == ""){
        alert("请输入还车时间");
        return;
    }


    $.ajax({
        url:event.data.ip + "/ctrip/selectInventory",
        type:"post",
        data:{"environment":environment,"pickupDate":pickupDate,"returnDate":returnDate,"cityCode":cityCode},
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
                $("#status_flow").removeAttr("disabled");   //提交按钮恢复

                var resultMsg = result.data;
                // var li = "<br><br><span class='sign_span'><b>结果：<br><br>" + resultMsg + "</b></span>";
                var li = "<br><span class='sign_span'><b>结果：<br></span><br><pre class='sign_span' style='color:red;'>" + resultMsg + "</pre></b></pre>";
                var $li = $(li);
                $("#result_msg").append($li);
            }
        },
        error:function(){
            $("#result_msg").empty();
            $("#status_flow").removeAttr("disabled");   //提交按钮恢复
            $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
            $("#can").load("alert/alert_error.html");//显示对话框
        }
    });
}