package com.autoyol.entity;

public class MileCostInfo {
    private String getCarMileage;       //取车里程数
    private String returnCarMileage;    //还车里程数
    private String dayMiles;            //日限里程
    private String dayPrice;            //日均价
    private String mileCost;            //超里程费
//    private String totalConsumeAmt;     //租客总消费
//    private String codeFlag;            //"费用明细"接口调用标识：0调用接口失败，1调用接口成功
    private String msg;

    public String getGetCarMileage() {
        return getCarMileage;
    }

    public void setGetCarMileage(String getCarMileage) {
        this.getCarMileage = getCarMileage;
    }

    public String getReturnCarMileage() {
        return returnCarMileage;
    }

    public void setReturnCarMileage(String returnCarMileage) {
        this.returnCarMileage = returnCarMileage;
    }

    public String getDayMiles() {
        return dayMiles;
    }

    public void setDayMiles(String dayMiles) {
        this.dayMiles = dayMiles;
    }

    public String getDayPrice() {
        return dayPrice;
    }

    public void setDayPrice(String dayPrice) {
        this.dayPrice = dayPrice;
    }

    public String getMileCost() {
        return mileCost;
    }

    public void setMileCost(String mileCost) {
        this.mileCost = mileCost;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
