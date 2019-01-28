package com.autoyol.entity;

public class TransModificationConsole {
    private String id;
    private String car_no;
    private String rent_time;
    private String revert_time;
    private String day_unit_price;      //平日价
    private String weekend_price;		//周末价
    private String holiday_price;       //节假日价
//    private String holiday_average;
    private String rent_amt;
    private String operator_time;       //操作时间
//    private String create_time;


    public String getWeekend_price() {
        return weekend_price;
    }

    public void setWeekend_price(String weekend_price) {
        this.weekend_price = weekend_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCar_no() {
        return car_no;
    }

    public void setCar_no(String car_no) {
        this.car_no = car_no;
    }

    public String getRent_time() {
        return rent_time;
    }

    public void setRent_time(String rent_time) {
        this.rent_time = rent_time;
    }

    public String getRevert_time() {
        return revert_time;
    }

    public void setRevert_time(String revert_time) {
        this.revert_time = revert_time;
    }

    public String getDay_unit_price() {
        return day_unit_price;
    }

    public void setDay_unit_price(String day_unit_price) {
        this.day_unit_price = day_unit_price;
    }

    public String getHoliday_price() {
        return holiday_price;
    }

    public void setHoliday_price(String holiday_price) {
        this.holiday_price = holiday_price;
    }

    public String getRent_amt() {
        return rent_amt;
    }

    public void setRent_amt(String rent_amt) {
        this.rent_amt = rent_amt;
    }

    public String getOperator_time() {
        return operator_time;
    }

    public void setOperator_time(String operator_time) {
        this.operator_time = operator_time;
    }

    @Override
    public String toString() {
        return "TransModificationConsole{" +
                "id='" + id + '\'' +
                ", car_no='" + car_no + '\'' +
                ", rent_time='" + rent_time + '\'' +
                ", revert_time='" + revert_time + '\'' +
                ", day_unit_price='" + day_unit_price + '\'' +
                ", weekend_price='" + weekend_price + '\'' +
                ", holiday_price='" + holiday_price + '\'' +
                ", rent_amt='" + rent_amt + '\'' +
                ", operator_time='" + operator_time + '\'' +
                '}';
    }
}
