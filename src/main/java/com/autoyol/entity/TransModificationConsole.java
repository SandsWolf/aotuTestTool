package com.autoyol.entity;

public class TransModificationConsole {
    private String id;
    private String car_no;
    private String rent_time;
    private String revert_time;

    private String day_unit_price;                  //平日价
    private String weekend_price;		            //周末价
    private String holiday_price;                   //节假日价
    private String use_special_price;				//是否使用特供价		1:用 0：不用
    private String ordinary_days_special_price;		//平日特供价格
    private String weekend_special_price;			//周末特供价
    private String holiday_special_price;			//节日特供价格
    private String spring_festival_special_price;	//春节特供价格

//    private String holiday_average;
    private String rent_amt;
    private String operator_time;       //操作时间
//    private String create_time;


    public String getUse_special_price() {
        return use_special_price;
    }

    public void setUse_special_price(String use_special_price) {
        this.use_special_price = use_special_price;
    }

    public String getOrdinary_days_special_price() {
        return ordinary_days_special_price;
    }

    public void setOrdinary_days_special_price(String ordinary_days_special_price) {
        this.ordinary_days_special_price = ordinary_days_special_price;
    }

    public String getWeekend_special_price() {
        return weekend_special_price;
    }

    public void setWeekend_special_price(String weekend_special_price) {
        this.weekend_special_price = weekend_special_price;
    }

    public String getHoliday_special_price() {
        return holiday_special_price;
    }

    public void setHoliday_special_price(String holiday_special_price) {
        this.holiday_special_price = holiday_special_price;
    }

    public String getSpring_festival_special_price() {
        return spring_festival_special_price;
    }

    public void setSpring_festival_special_price(String spring_festival_special_price) {
        this.spring_festival_special_price = spring_festival_special_price;
    }

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
