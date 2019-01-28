package com.autoyol.entity;

public class City {
    private Integer code;   //城市编码
    private String name;    //城市名
    private Integer delivery_service_cost;          //普通订单基础费用
    private Integer package_delivery_service_cost;  //套餐订单基础费用
    private String address_range;                   //取还车范围
    private String free_address_range;              //免费取还车范围

    public String getAddress_range() {
        return address_range;
    }

    public void setAddress_range(String address_range) {
        this.address_range = address_range;
    }

    public String getFree_address_range() {
        return free_address_range;
    }

    public void setFree_address_range(String free_address_range) {
        this.free_address_range = free_address_range;
    }

    public Integer getDelivery_service_cost() {
        return delivery_service_cost;
    }

    public void setDelivery_service_cost(Integer delivery_service_cost) {
        this.delivery_service_cost = delivery_service_cost;
    }

    public Integer getPackage_delivery_service_cost() {
        return package_delivery_service_cost;
    }

    public void setPackage_delivery_service_cost(Integer package_delivery_service_cost) {
        this.package_delivery_service_cost = package_delivery_service_cost;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
