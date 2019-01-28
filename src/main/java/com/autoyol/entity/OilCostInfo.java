package com.autoyol.entity;

public class OilCostInfo {
    private String getCarOilScale;          //取车时油量刻度
    private String returnCarOilScale;       //还车时油量刻度
//    private String oilVolume;               //邮箱容积
//    private String molecule;                //能源价格
    private String oilCost;                 //油费
    private String oilServiceCost;          //加油服务费
    private String platformServiceCost;     //平台加油服务费
    private String ownerOilSubsidy;         //取还车油费补贴
    private String ownerOilGetSubsidy;      //取还车油费补贴_取车部分
    private String ownerOilReturnSubsidy;   //取还车油费补贴_还车部分
    private String msg;

    public String getOwnerOilGetSubsidy() {
        return ownerOilGetSubsidy;
    }

    public void setOwnerOilGetSubsidy(String ownerOilGetSubsidy) {
        this.ownerOilGetSubsidy = ownerOilGetSubsidy;
    }

    public String getOwnerOilReturnSubsidy() {
        return ownerOilReturnSubsidy;
    }

    public void setOwnerOilReturnSubsidy(String ownerOilReturnSubsidy) {
        this.ownerOilReturnSubsidy = ownerOilReturnSubsidy;
    }

    public String getPlatformServiceCost() {
        return platformServiceCost;
    }

    public void setPlatformServiceCost(String platformServiceCost) {
        this.platformServiceCost = platformServiceCost;
    }

    public String getOwnerOilSubsidy() {
        return ownerOilSubsidy;
    }

    public void setOwnerOilSubsidy(String ownerOilSubsidy) {
        this.ownerOilSubsidy = ownerOilSubsidy;
    }

    public String getGetCarOilScale() {
        return getCarOilScale;
    }

    public void setGetCarOilScale(String getCarOilScale) {
        this.getCarOilScale = getCarOilScale;
    }

    public String getReturnCarOilScale() {
        return returnCarOilScale;
    }

    public void setReturnCarOilScale(String returnCarOilScale) {
        this.returnCarOilScale = returnCarOilScale;
    }

    public String getOilCost() {
        return oilCost;
    }

    public void setOilCost(String oilCost) {
        this.oilCost = oilCost;
    }

    public String getOilServiceCost() {
        return oilServiceCost;
    }

    public void setOilServiceCost(String oilServiceCost) {
        this.oilServiceCost = oilServiceCost;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
