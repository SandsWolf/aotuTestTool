package com.autoyol.entity;

/**
 * 记录每次修改订单的数据
 */
public class UpdateData {
    private String totalH;                  //总小时数（小时）
    private String totalDayRentDate;        //平日价所占租期（小时）
    private String totalWeekendRentDate;    //周末价所占租期（小时）
    private String totalHolidayRentDate;    //节假日价所占租期（小时）
    private String rentDate;                //所处阶段租期（天）
    private String avgDayPrice;             //订单日均价
    private String avgHourPrice;            //订单时均价
    private String t;                       //租期天数
    private String h;                       //剩余小时数
    private String rentAmt;                 //租金（整体）
    private String stageRentAmt;            //租金（阶段）
    private String part;                    //部分：根据租期分段
    private String batch;                   //批次：起租时间改变、还车，这2种情况后计算该动作时的整段租期，并重新定义为新批次的第1段
    private String rentTime;                //整段开始时间
    private String revertTime;              //整段结束时间
    private String continueTime;            //修改租期，上一段的结束时间
    private String totalRentDate;           //总租期
    private String carDayPrice;             //所处阶段时车辆平日价
    private String carWeekendPrice;         //所处阶段时车辆周末价
    private String carHolidayPrice;         //所处阶段时车辆节假日价
    private String carNo;
    private Integer type;                   //修改订单用：数据类型：1.trans_modification_application表数据  2.trans_modification_console表数据
    private String id;                      //对应表中数据的id
    private String updateFlag;              //修复重新计算flag    1：有重新计算  0：无重新计算
    private String msg;                     //前端提示信息

    public String getCarWeekendPrice() {
        return carWeekendPrice;
    }

    public void setCarWeekendPrice(String carWeekendPrice) {
        this.carWeekendPrice = carWeekendPrice;
    }

    public String getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(String updateFlag) {
        this.updateFlag = updateFlag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCarDayPrice() {
        return carDayPrice;
    }

    public void setCarDayPrice(String carDayPrice) {
        this.carDayPrice = carDayPrice;
    }

    public String getCarHolidayPrice() {
        return carHolidayPrice;
    }

    public void setCarHolidayPrice(String carHolidayPrice) {
        this.carHolidayPrice = carHolidayPrice;
    }

    public String getTotalRentDate() {
        return totalRentDate;
    }

    public void setTotalRentDate(String totalRentDate) {
        this.totalRentDate = totalRentDate;
    }

    public String getStageRentAmt() {
        return stageRentAmt;
    }

    public void setStageRentAmt(String stageRentAmt) {
        this.stageRentAmt = stageRentAmt;
    }

    public String getRentTime() {
        return rentTime;
    }

    public void setRentTime(String rentTime) {
        this.rentTime = rentTime;
    }

    public String getRevertTime() {
        return revertTime;
    }

    public void setRevertTime(String revertTime) {
        this.revertTime = revertTime;
    }

    public String getContinueTime() {
        return continueTime;
    }

    public void setContinueTime(String continueTime) {
        this.continueTime = continueTime;
    }

    public String getTotalH() {
        return totalH;
    }

    public void setTotalH(String totalH) {
        this.totalH = totalH;
    }

    public String getTotalDayRentDate() {
        return totalDayRentDate;
    }

    public void setTotalDayRentDate(String totalDayRentDate) {
        this.totalDayRentDate = totalDayRentDate;
    }

    public String getTotalHolidayRentDate() {
        return totalHolidayRentDate;
    }

    public void setTotalHolidayRentDate(String totalHolidayRentDate) {
        this.totalHolidayRentDate = totalHolidayRentDate;
    }

    public String getRentDate() {
        return rentDate;
    }

    public void setRentDate(String rentDate) {
        this.rentDate = rentDate;
    }

    public String getAvgDayPrice() {
        return avgDayPrice;
    }

    public void setAvgDayPrice(String avgDayPrice) {
        this.avgDayPrice = avgDayPrice;
    }

    public String getAvgHourPrice() {
        return avgHourPrice;
    }

    public void setAvgHourPrice(String avgHourPrice) {
        this.avgHourPrice = avgHourPrice;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }

    public String getRentAmt() {
        return rentAmt;
    }

    public void setRentAmt(String rentAmt) {
        this.rentAmt = rentAmt;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getTotalWeekendRentDate() {
        return totalWeekendRentDate;
    }

    public void setTotalWeekendRentDate(String totalWeekendRentDate) {
        this.totalWeekendRentDate = totalWeekendRentDate;
    }

    @Override
    public String toString() {
        return "UpdateData{" +
                "totalH='" + totalH + '\'' +
                ", totalDayRentDate='" + totalDayRentDate + '\'' +
                ", totalWeekendRentDate='" + totalWeekendRentDate + '\'' +
                ", totalHolidayRentDate='" + totalHolidayRentDate + '\'' +
                ", rentDate='" + rentDate + '\'' +
                ", avgDayPrice='" + avgDayPrice + '\'' +
                ", avgHourPrice='" + avgHourPrice + '\'' +
                ", t='" + t + '\'' +
                ", h='" + h + '\'' +
                ", rentAmt='" + rentAmt + '\'' +
                ", stageRentAmt='" + stageRentAmt + '\'' +
                ", part='" + part + '\'' +
                ", batch='" + batch + '\'' +
                ", rentTime='" + rentTime + '\'' +
                ", revertTime='" + revertTime + '\'' +
                ", continueTime='" + continueTime + '\'' +
                ", totalRentDate='" + totalRentDate + '\'' +
                ", carDayPrice='" + carDayPrice + '\'' +
                ", carWeekendPrice='" + carWeekendPrice + '\'' +
                ", carHolidayPrice='" + carHolidayPrice + '\'' +
                ", carNo='" + carNo + '\'' +
                ", type=" + type +
                ", id='" + id + '\'' +
                ", updateFlag='" + updateFlag + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
