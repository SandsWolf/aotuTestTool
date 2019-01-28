package com.autoyol.entity;

public class CarOrderSetting {
    private Integer is_acceptance;  //'是否接受订单，0:否,1:是',
    private Integer rental_days;    //'几天起租，如，3'

    public Integer getIs_acceptance() {
        return is_acceptance;
    }

    public void setIs_acceptance(Integer is_acceptance) {
        this.is_acceptance = is_acceptance;
    }

    public Integer getRental_days() {
        return rental_days;
    }

    public void setRental_days(Integer rental_days) {
        this.rental_days = rental_days;
    }

    @Override
    public String toString() {
        return "CarOrderSetting{" +
                "is_acceptance=" + is_acceptance +
                ", rental_days=" + rental_days +
                '}';
    }
}
