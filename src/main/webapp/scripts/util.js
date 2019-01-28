// 设置公共orderNo
function setPublicOrderNo(){
    window.publicOrderNo = $("#input_orderNo").val().trim();
}


// 设置公共手机号
function setPublicMobile(){
    window.publicMobile = $("#input_mobile").val().trim();
}

// 设置公共carNo
function setPublicCarNo(){
    window.publicCarNo = $("#input_carNo").val().trim();
}

// 设置公共起租时间
function setPublicRentTime(){
    window.publicRentTime = $("#input_rentTime").val().trim();
    var rentTime = $("#input_rentTime").val().trim();
    if (rentTime.length != 0) {
        if (rentTime.length == 14) {
            var showRentTiem = rentTime.substring(0, 4) + "-" + rentTime.substring(4, 6) + "-" + rentTime.substring(6, 8) + " " + rentTime.substring(8, 10) + ":" + rentTime.substring(10, 12) + ":" + rentTime.substring(12, 14);
            $("#show_rentTime").html(showRentTiem);
        } else {
            $("#show_rentTime").html("时间格式不对，请重新输入");
        }
    }
}

// 设置公共结束时间
function setPublicRevertTime(){
    window.publicRevertTime = $("#input_revertTime").val().trim();
    var revertTime = $("#input_revertTime").val().trim();
    if (revertTime.length != 0) {
        if (revertTime.length == 14) {
            var showRevertTiem = revertTime.substring(0, 4) + "-" + revertTime.substring(4, 6) + "-" + revertTime.substring(6, 8) + " " + revertTime.substring(8, 10) + ":" + revertTime.substring(10, 12) + ":" + revertTime.substring(12, 14);
            $("#show_revertTime").html(showRevertTiem);
        } else {
            $("#show_rentTime").html("时间格式不对，请重新输入");
        }
    }
}

// 设置公共经度
function setPublicLon(){
    window.publicLon = $("#input_lon").val().trim();
}

// 设置公共维度
function setPublicLat(){
    window.publicLat = $("#input_lat").val().trim();
}

// 设置公共日均价
function setPublicDayPrice(){
    window.publicDayPrice = $("#input_dayPrice").val().trim();
}

// 设置公共节日均价
function setPublicHolidayPrice(){
    window.publicHolidayPrice = $("#input_holidayPrice").val().trim();
}

// 加载【高德地址select控件】
function addressList(event){
    var address = $("#input_address").val().trim();

    if (address != "") {
        $.ajax({
            url:event.data.ip + "/map/searchAddress",
            type:"post",
            data:{"address":address},
            dataType:"json",
            success:function(result){
                if(result.status==0){
                    $("#show_msg").html("");
                    $("#selectAddress").empty();
                    var li = "<br><select id='select_addressList'>";
                    li = li + result.data;
                    li = li + "</select><br>";
                    var $li = $(li);
                    $("#selectAddress").append($li);

                    var coordinate = $("#select_addressList option:selected").val();
                    if (coordinate != "") {
                        $("#addressCoordinate").html("");
                        $("#addressCoordinate").html('&nbsp;&nbsp;lon："' + coordinate.split(",")[0] + '"，' + 'lat："' + coordinate.split(",")[1] + '"');
                    }
                }

                if(result.status==1){
                    $("#selectAddress").empty();
                    $("#show_msg").html(result.data);
                }
            },
            error:function(){
                $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
                $("#can").load("alert/alert_error.html");//显示对话框
            }
        });
    }
}

// 监控【高德地址select控件】：显示经纬度
function showSelectCoordinate(){
    var address = $("#select_addressList").val();
    var addressList = address.split(",");
    if (address != "") {
        $("#addressCoordinate").html("");
        $("#addressCoordinate").html('&nbsp;&nbsp;lon："' + addressList[0] + '"，' + 'lat："' + addressList[1] + '"');
    }
}



// 加载【高德取车地址select控件】
function getAddressList(event){
    var address = $("#input_getAddress").val() == undefined ? '' : $("#input_getAddress").val().trim();

    if (address != "") {
        $.ajax({
            url:event.data.ip + "/map/searchAddress",
            type:"post",
            data:{"address":address},
            dataType:"json",
            success:function(result){
                console.log("status:" + result.status);
                if(result.status==0){
                    $("#show_getMsg").html("");
                    $("#selectGetAddress").empty();
                    var li = "<br><select id='select_getAddressList'>";
                    li = li + result.data;
                    li = li + "</select>";
                    var $li = $(li);
                    $("#selectGetAddress").append($li);

                    var coordinate = $("#select_getAddressList option:selected").val();
                    if (coordinate != "") {
                        $("#getAddressCoordinate").html("");
                        $("#getAddressCoordinate").html('&nbsp;&nbsp;lon："' + coordinate.split(",")[0] + '"，' + 'lat："' + coordinate.split(",")[1] + '"');
                    }
                }

                if(result.status==1){
                    $("#selectGetAddress").empty();
                    $("#show_getMsg").html(result.data);
                }
            },
            error:function(){
                $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
                $("#can").load("alert/alert_error.html");//显示对话框
            }
        });
    }
}

// 监控【高德取车地址select控件】：显示经纬度
function showGetSelectCoordinate(){
    var address = $("#select_getAddressList").val();
    var addressList = address.split(",");
    if (address != "") {
        $("#getAddressCoordinate").html("");
        $("#getAddressCoordinate").html('&nbsp;&nbsp;lon："' + addressList[0] + '"，' + 'lat："' + addressList[1] + '"');
    }
}


// 加载【高德还车地址select控件】
function returnAddressList(event){
    var address = $("#input_returnAddress").val() == undefined ? '' : $("#input_returnAddress").val().trim();
    console.log("address:" + address);

    if (address != "") {
        $.ajax({
            url:event.data.ip + "/map/searchAddress",
            type:"post",
            data:{"address":address},
            dataType:"json",
            success:function(result){
                if(result.status==0){
                    $("#show_returnMsg").html("");
                    $("#selectReturnAddress").empty();
                    var li = "<br><select id='select_returnAddressList'>";
                    li = li + result.data;
                    li = li + "</select>";
                    var $li = $(li);
                    $("#selectReturnAddress").append($li);

                    var coordinate = $("#select_returnAddressList option:selected").val();
                    if (coordinate != "") {
                        $("#returnAddressCoordinate").html("");
                        $("#returnAddressCoordinate").html('&nbsp;&nbsp;lon："' + coordinate.split(",")[0] + '"，' + 'lat："' + coordinate.split(",")[1] + '"');
                    }
                }

                if(result.status==1){
                    $("#selectReturnAddress").empty();
                    $("#show_returnMsg").html(result.data);
                }
            },
            error:function(){
                $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
                $("#can").load("alert/alert_error.html");//显示对话框
            }
        });
    }
}

// 监控【高德还车地址select控件】：显示经纬度
function showReturnSelectCoordinate(){
    var address = $("#select_returnAddressList").val();
    var addressList = address.split(",");
    if (address != "") {
        $("#returnAddressCoordinate").html("");
        $("#returnAddressCoordinate").html('&nbsp;&nbsp;lon："' + addressList[0] + '"，' + 'lat："' + addressList[1] + '"');
    }
}



// 光标离开——加载附加驾驶人checkbox控件
function blurLoadcommUseDriverCheckbox(event){
    var commUseDriver = $("#comm_use_driver");
    var mobile = $("#input_mobile").val() == undefined ? '' : $("#input_mobile").val().trim();
    if (commUseDriver.length > 0 && mobile != "") {
        var environment = $("#set_environment").val();
        if(environment == "-请选择-"){
            $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
            $("#can").load("alert/alert_environment.html");//显示对话框
            return;
        }
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


// 点击按钮——加载附加驾驶人checkbox控件
// function clickLoadCommUseDriverCheckbox(event){
//     var mobile = $("#input_mobile").val() == undefined ? '' : $("#input_mobile").val().trim();
//
//     if (mobile != "") {
//         $.ajax({
//             url:event.data.ip + "/member/getCommUseDrivers",
//             type:"post",
//             data:{"mobile":mobile},
//             dataType:"json",
//             success:function(result){
//                 if(result.status==0){
//                     $("#comm_use_driver").empty();
//                     var li = "<br>" + result.data;
//                     var $li = $(li);
//                     $("#comm_use_driver").append($li);
//                 }
//
//                 if(result.status==1){
//                     $("#comm_use_driver").empty();
//                     var li = "<br><span class='sign_span' style='color:red;'>" + result.data + "</span>";
//                     var $li = $(li);
//                     $("#comm_use_driver").append($li);
//                 }
//             },
//             error:function(){
//                 $(".opacity_bg").show();//弹出对话框后背景置灰，防止误操作
//                 $("#can").load("alert/alert_error.html");//显示对话框
//             }
//         });
//     }
// }
