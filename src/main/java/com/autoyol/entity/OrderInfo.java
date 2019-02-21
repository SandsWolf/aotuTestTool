package com.autoyol.entity;

public class OrderInfo {
    private Integer total_amt;				//租车押金
    private String owner_no;				//车主NO
    private String renter_no;				//租客NO
    private String token;					//租车token
    private String order_no;				//车主订单号
    private String renter_order_no;			//租客订单号
    private String rent_time;				//取车时间
    private String revert_time;				//还车时间
    private Integer status;					//订单状态
    private Integer settle;					//订单结算标记
    private Integer car_owner_type;			//订单表中车辆类型
    private String car_no;


    private Integer get_car_mileage_owner;			//车主取车时里程数
    private Integer return_car_mileage_owner;		//车主还车时里程数
    private Integer get_car_oil_scale_owner;		//车主取车时油量刻度
    private Integer return_car_oil_scale_owner;		//车主还车时油量刻度
    private Integer get_car_mileage_renter;			//租客取车时里程数
    private Integer return_car_mileage_renter;		//租客还车时里程数
    private Integer get_car_oil_scale_renter;		//租客取车时油量刻度
    private Integer return_car_oil_scale_renter;	//租客还车时油量刻度

    private Integer day_miles_owner;			//车主日限里程数：0表示不限
    private Integer day_miles_renter;			//租客日限里程数：0表示不限

    private Integer holiday_average;				//车主日均价
    private Integer renter_dayPrice;				//租客日均价
    private Integer guide_day_price;				//car表"日租金指导价"

    private Integer oil_volume;					//油箱容量
    private Integer molecule;					//油价分子
    private Integer denominator;                //油价分母
    private Integer serviceCost;				//油量服务费

    private Integer mileage_cost_owner;			//车主超里程费用
    private Integer oil_cost_owner;				//车主油费
    private Integer new_owner_oil_subsidy;      //车主取还车补贴
    private Integer new_owner_oil_service_cost; //车主加油服务费

    private Integer mileage_cost_renter;		//租客超里程费用
    private Integer oil_cost_renter;			//租客油费
    private Integer oil_service_cost_owner;		//车主端加油服务费
    private Integer oil_service_cost_renter;	//租客端加油服务费

    public Integer getDenominator() {
        return denominator;
    }

    public void setDenominator(Integer denominator) {
        this.denominator = denominator;
    }

    public Integer getNew_owner_oil_subsidy() {
        return new_owner_oil_subsidy;
    }

    public void setNew_owner_oil_subsidy(Integer new_owner_oil_subsidy) {
        this.new_owner_oil_subsidy = new_owner_oil_subsidy;
    }

    public Integer getNew_owner_oil_service_cost() {
        return new_owner_oil_service_cost;
    }

    public void setNew_owner_oil_service_cost(Integer new_owner_oil_service_cost) {
        this.new_owner_oil_service_cost = new_owner_oil_service_cost;
    }

    public String getCar_no() {
        return car_no;
    }

    public void setCar_no(String car_no) {
        this.car_no = car_no;
    }

    public Integer getTotal_amt() {
        return total_amt;
    }

    public void setTotal_amt(Integer total_amt) {
        this.total_amt = total_amt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getCar_owner_type() {
        return car_owner_type;
    }

    public void setCar_owner_type(Integer car_owner_type) {
        this.car_owner_type = car_owner_type;
    }

    public Integer getGuide_day_price() {
        return guide_day_price;
    }

    public void setGuide_day_price(Integer guide_day_price) {
        this.guide_day_price = guide_day_price;
    }

    public String getOwner_no() {
        return owner_no;
    }
    public void setOwner_no(String owner_no) {
        this.owner_no = owner_no;
    }
    public String getRenter_no() {
        return renter_no;
    }
    public void setRenter_no(String renter_no) {
        this.renter_no = renter_no;
    }
    public String getOrder_no() {
        return order_no;
    }
    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }
    public String getRenter_order_no() {
        return renter_order_no;
    }
    public void setRenter_order_no(String renter_order_no) {
        this.renter_order_no = renter_order_no;
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
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getSettle() {
        return settle;
    }
    public void setSettle(Integer settle) {
        this.settle = settle;
    }
    public Integer getGet_car_mileage_owner() {
        return get_car_mileage_owner;
    }
    public void setGet_car_mileage_owner(Integer get_car_mileage_owner) {
        this.get_car_mileage_owner = get_car_mileage_owner;
    }
    public Integer getReturn_car_mileage_owner() {
        return return_car_mileage_owner;
    }
    public void setReturn_car_mileage_owner(Integer return_car_mileage_owner) {
        this.return_car_mileage_owner = return_car_mileage_owner;
    }
    public Integer getGet_car_oil_scale_owner() {
        return get_car_oil_scale_owner;
    }
    public void setGet_car_oil_scale_owner(Integer get_car_oil_scale_owner) {
        this.get_car_oil_scale_owner = get_car_oil_scale_owner;
    }
    public Integer getReturn_car_oil_scale_owner() {
        return return_car_oil_scale_owner;
    }
    public void setReturn_car_oil_scale_owner(Integer return_car_oil_scale_owner) {
        this.return_car_oil_scale_owner = return_car_oil_scale_owner;
    }
    public Integer getGet_car_mileage_renter() {
        return get_car_mileage_renter;
    }
    public void setGet_car_mileage_renter(Integer get_car_mileage_renter) {
        this.get_car_mileage_renter = get_car_mileage_renter;
    }
    public Integer getReturn_car_mileage_renter() {
        return return_car_mileage_renter;
    }
    public void setReturn_car_mileage_renter(Integer return_car_mileage_renter) {
        this.return_car_mileage_renter = return_car_mileage_renter;
    }
    public Integer getGet_car_oil_scale_renter() {
        return get_car_oil_scale_renter;
    }
    public void setGet_car_oil_scale_renter(Integer get_car_oil_scale_renter) {
        this.get_car_oil_scale_renter = get_car_oil_scale_renter;
    }
    public Integer getReturn_car_oil_scale_renter() {
        return return_car_oil_scale_renter;
    }
    public void setReturn_car_oil_scale_renter(Integer return_car_oil_scale_renter) {
        this.return_car_oil_scale_renter = return_car_oil_scale_renter;
    }
    public Integer getDay_miles_owner() {
        return day_miles_owner;
    }
    public void setDay_miles_owner(Integer day_miles_owner) {
        this.day_miles_owner = day_miles_owner;
    }
    public Integer getDay_miles_renter() {
        return day_miles_renter;
    }
    public void setDay_miles_renter(Integer day_miles_renter) {
        this.day_miles_renter = day_miles_renter;
    }
    public Integer getHoliday_average() {
        return holiday_average;
    }
    public void setHoliday_average(Integer holiday_average) {
        this.holiday_average = holiday_average;
    }
    public Integer getRenter_dayPrice() {
        return renter_dayPrice;
    }
    public void setRenter_dayPrice(Integer renter_dayPrice) {
        this.renter_dayPrice = renter_dayPrice;
    }
    public Integer getOil_volume() {
        return oil_volume;
    }
    public void setOil_volume(Integer oil_volume) {
        this.oil_volume = oil_volume;
    }

    public Integer getMolecule() {
        return molecule;
    }
    public void setMolecule(Integer molecule) {
        this.molecule = molecule;
    }
    public Integer getServiceCost() {
        return serviceCost;
    }
    public void setServiceCost(Integer serviceCost) {
        this.serviceCost = serviceCost;
    }
    public Integer getMileage_cost_owner() {
        return mileage_cost_owner;
    }
    public void setMileage_cost_owner(Integer mileage_cost_owner) {
        this.mileage_cost_owner = mileage_cost_owner;
    }
    public Integer getOil_cost_owner() {
        return oil_cost_owner;
    }
    public void setOil_cost_owner(Integer oil_cost_owner) {
        this.oil_cost_owner = oil_cost_owner;
    }
    public Integer getMileage_cost_renter() {
        return mileage_cost_renter;
    }
    public void setMileage_cost_renter(Integer mileage_cost_renter) {
        this.mileage_cost_renter = mileage_cost_renter;
    }
    public Integer getOil_cost_renter() {
        return oil_cost_renter;
    }
    public void setOil_cost_renter(Integer oil_cost_renter) {
        this.oil_cost_renter = oil_cost_renter;
    }
    public Integer getOil_service_cost_owner() {
        return oil_service_cost_owner;
    }
    public void setOil_service_cost_owner(Integer oil_service_cost_owner) {
        this.oil_service_cost_owner = oil_service_cost_owner;
    }
    public Integer getOil_service_cost_renter() {
        return oil_service_cost_renter;
    }
    public void setOil_service_cost_renter(Integer oil_service_cost_renter) {
        this.oil_service_cost_renter = oil_service_cost_renter;
    }

    @Override
    public String toString() {
        return "Order{" +
                "total_amt=" + total_amt +
                ", owner_no='" + owner_no + '\'' +
                ", renter_no='" + renter_no + '\'' +
                ", token='" + token + '\'' +
                ", order_no='" + order_no + '\'' +
                ", renter_order_no='" + renter_order_no + '\'' +
                ", rent_time='" + rent_time + '\'' +
                ", revert_time='" + revert_time + '\'' +
                ", status=" + status +
                ", settle=" + settle +
                ", car_owner_type=" + car_owner_type +
                ", get_car_mileage_owner=" + get_car_mileage_owner +
                ", return_car_mileage_owner=" + return_car_mileage_owner +
                ", get_car_oil_scale_owner=" + get_car_oil_scale_owner +
                ", return_car_oil_scale_owner=" + return_car_oil_scale_owner +
                ", get_car_mileage_renter=" + get_car_mileage_renter +
                ", return_car_mileage_renter=" + return_car_mileage_renter +
                ", get_car_oil_scale_renter=" + get_car_oil_scale_renter +
                ", return_car_oil_scale_renter=" + return_car_oil_scale_renter +
                ", day_miles_owner=" + day_miles_owner +
                ", day_miles_renter=" + day_miles_renter +
                ", holiday_average=" + holiday_average +
                ", renter_dayPrice=" + renter_dayPrice +
                ", guide_day_price=" + guide_day_price +
                ", oil_volume=" + oil_volume +
                ", molecule=" + molecule +
                ", serviceCost=" + serviceCost +
                ", mileage_cost_owner=" + mileage_cost_owner +
                ", oil_cost_owner=" + oil_cost_owner +
                ", mileage_cost_renter=" + mileage_cost_renter +
                ", oil_cost_renter=" + oil_cost_renter +
                ", oil_service_cost_owner=" + oil_service_cost_owner +
                ", oil_service_cost_renter=" + oil_service_cost_renter +
                '}';
    }
}
