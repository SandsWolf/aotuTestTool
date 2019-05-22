package com.autoyol.entity;

public class Address {

    // 地址名称
    private String address;
    // 地址经度 (高德坐标)
    private double longitude;
    // 地址纬度(高德坐标)
    private double latitude;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}


