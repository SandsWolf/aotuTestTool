package com.autoyol.entity;

public class Car {
    private String reg_no;                  //carNo
    private String plate_num;               //车牌号
    private Integer guide_purchase_price;   //购买指导价
    private Integer guide_price;            //指导价
    private String surplus_price;           //车辆残值
    private String city;                    //城市编码
    private String brand;                   //品牌ID
    private String type;                    //车型ID
    private String licenseDay;              //年份：如 6.8000年

    private Integer guid_price_begin;       //平台保障费开始范围费用：非car表
    private Integer guid_price_end;         //平台保障费结束范围费用：非car表
    private Integer insurance_value;        //平台保障费/日：非car表
    private String get_car_lon;            //取车位置经度
    private String get_car_lat;            //取车位置纬度
    private Integer is_local;               //是否本地牌照    1:本地

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }

    public String getPlate_num() {
        return plate_num;
    }

    public void setPlate_num(String plate_num) {
        this.plate_num = plate_num;
    }

    public Integer getGuide_purchase_price() {
        return guide_purchase_price;
    }

    public void setGuide_purchase_price(Integer guide_purchase_price) {
        this.guide_purchase_price = guide_purchase_price;
    }

    public Integer getGuide_price() {
        return guide_price;
    }

    public void setGuide_price(Integer guide_price) {
        this.guide_price = guide_price;
    }

    public String getSurplus_price() {
        return surplus_price;
    }

    public void setSurplus_price(String surplus_price) {
        this.surplus_price = surplus_price;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLicenseDay() {
        return licenseDay;
    }

    public void setLicenseDay(String licenseDay) {
        this.licenseDay = licenseDay;
    }

    public Integer getGuid_price_begin() {
        return guid_price_begin;
    }

    public void setGuid_price_begin(Integer guid_price_begin) {
        this.guid_price_begin = guid_price_begin;
    }

    public Integer getGuid_price_end() {
        return guid_price_end;
    }

    public void setGuid_price_end(Integer guid_price_end) {
        this.guid_price_end = guid_price_end;
    }

    public Integer getInsurance_value() {
        return insurance_value;
    }

    public void setInsurance_value(Integer insurance_value) {
        this.insurance_value = insurance_value;
    }

    public String getGet_car_lon() {
        return get_car_lon;
    }

    public void setGet_car_lon(String get_car_lon) {
        this.get_car_lon = get_car_lon;
    }

    public String getGet_car_lat() {
        return get_car_lat;
    }

    public void setGet_car_lat(String get_car_lat) {
        this.get_car_lat = get_car_lat;
    }

    public Integer getIs_local() {
        return is_local;
    }

    public void setIs_local(Integer is_local) {
        this.is_local = is_local;
    }
}
