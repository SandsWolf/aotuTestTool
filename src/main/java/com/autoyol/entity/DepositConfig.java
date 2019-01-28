package com.autoyol.entity;

public class DepositConfig {
    private Integer reg_no;                 //carNo
    private Integer deposit_type;           //押金类型：1:租车押金，2:违章押金,3:租车押金默认(城市未配置取值)，4:违章押金默认(城市未配置取值)
    private Integer deposit_value;          //押金金额
    private Integer city_code;              //城市code
    private Integer city;                   //城市code
    private Integer surplus_price;          //车辆残值
    private Integer purchase_price_begin;   //车辆购置价，开始范围
    private Integer purchase_price_end;     //车辆购置价，结束范围
    private Integer multiple;               //倍数(租车押金使用)

    public Integer getMultiple() {
        return multiple;
    }

    public void setMultiple(Integer multiple) {
        this.multiple = multiple;
    }

    public Integer getReg_no() {
        return reg_no;
    }

    public void setReg_no(Integer reg_no) {
        this.reg_no = reg_no;
    }

    public Integer getDeposit_type() {
        return deposit_type;
    }

    public void setDeposit_type(Integer deposit_type) {
        this.deposit_type = deposit_type;
    }

    public Integer getDeposit_value() {
        return deposit_value;
    }

    public void setDeposit_value(Integer deposit_value) {
        this.deposit_value = deposit_value;
    }

    public Integer getCity_code() {
        return city_code;
    }

    public void setCity_code(Integer city_code) {
        this.city_code = city_code;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public Integer getSurplus_price() {
        return surplus_price;
    }

    public void setSurplus_price(Integer surplus_price) {
        this.surplus_price = surplus_price;
    }

    public Integer getPurchase_price_begin() {
        return purchase_price_begin;
    }

    public void setPurchase_price_begin(Integer purchase_price_begin) {
        this.purchase_price_begin = purchase_price_begin;
    }

    public Integer getPurchase_price_end() {
        return purchase_price_end;
    }

    public void setPurchase_price_end(Integer purchase_price_end) {
        this.purchase_price_end = purchase_price_end;
    }


    @Override
    public String toString() {
        return "DepositConfig{" +
                "reg_no=" + reg_no +
                ", deposit_type=" + deposit_type +
                ", deposit_value=" + deposit_value +
                ", city_code=" + city_code +
                ", city=" + city +
                ", surplus_price=" + surplus_price +
                ", purchase_price_begin=" + purchase_price_begin +
                ", purchase_price_end=" + purchase_price_end +
                ", multiple=" + multiple +
                '}';
    }
}
