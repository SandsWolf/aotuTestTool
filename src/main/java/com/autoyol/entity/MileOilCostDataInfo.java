package com.autoyol.entity;

public class MileOilCostDataInfo {
    private String rentDate;            //租期
    private String oilVolume;           //邮箱容积
    private String molecule;            //能源价格
    private String totalConsumeAmt;     //租客总消费
    private String codeFlag;            //"费用明细"接口调用标识：0调用接口失败，1调用接口成功
    private String status;
    private String settle;
    private String carNo;
    private String rentTime;
    private String revertTime;
    private String renterNo;
    private String ownerNo;
    private String totalAmt;
    private String ownerGetMileage;         //trans_ext表中车主取车里程数
    private String ownerReturnMileage;      //trans_ext表中车主还车里程数

    public String getOwnerGetMileage() {
        return ownerGetMileage;
    }

    public void setOwnerGetMileage(String ownerGetMileage) {
        this.ownerGetMileage = ownerGetMileage;
    }

    public String getOwnerReturnMileage() {
        return ownerReturnMileage;
    }

    public void setOwnerReturnMileage(String ownerReturnMileage) {
        this.ownerReturnMileage = ownerReturnMileage;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getRenterNo() {
        return renterNo;
    }

    public void setRenterNo(String renterNo) {
        this.renterNo = renterNo;
    }

    public String getOwnerNo() {
        return ownerNo;
    }

    public void setOwnerNo(String ownerNo) {
        this.ownerNo = ownerNo;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSettle() {
        return settle;
    }

    public void setSettle(String settle) {
        this.settle = settle;
    }

    public String getRentDate() {
        return rentDate;
    }

    public void setRentDate(String rentDate) {
        this.rentDate = rentDate;
    }

    public String getOilVolume() {
        return oilVolume;
    }

    public void setOilVolume(String oilVolume) {
        this.oilVolume = oilVolume;
    }

    public String getMolecule() {
        return molecule;
    }

    public void setMolecule(String molecule) {
        this.molecule = molecule;
    }

    public String getTotalConsumeAmt() {
        return totalConsumeAmt;
    }

    public void setTotalConsumeAmt(String totalConsumeAmt) {
        this.totalConsumeAmt = totalConsumeAmt;
    }

    public String getCodeFlag() {
        return codeFlag;
    }

    public void setCodeFlag(String codeFlag) {
        this.codeFlag = codeFlag;
    }
}
