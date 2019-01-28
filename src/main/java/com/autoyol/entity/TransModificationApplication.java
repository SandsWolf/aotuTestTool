package com.autoyol.entity;

public class TransModificationApplication {
	private String id;
	private String order_no;
	private String rent_time;			//订单开始时间；如：20180329151500
	private String revert_time;			//订单结束时间；如：20180331151500
	private String day_unit_price;		//修改订单时，车辆平日价
	private String weekend_price;		//修改订单时，车辆周末价
	private String holiday_price;		//修改订单时，车辆节假日价
	private String rent_amt;			//修改订单时，订单租金
	private String is_original;			//是否原始订单	1.是  2.不是
	private String car_no;
	private String create_time;			//数据请求时间；如：2018-03-29 11:07:08
	private String confirm_flag; 		//拒绝类型  0:未处理，1:同意，2:自动拒绝，3:主动拒绝
	private String supamt_flag;			//该请求状态  0无效 1有效
	private String delay_request_flag;	//是否为延时申请
	private String holiday_average;		//日均价
	private String operator_time;		//操作时间


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

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
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

	public String getIs_original() {
		return is_original;
	}

	public void setIs_original(String is_original) {
		this.is_original = is_original;
	}

	public String getCar_no() {
		return car_no;
	}

	public void setCar_no(String car_no) {
		this.car_no = car_no;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getConfirm_flag() {
		return confirm_flag;
	}

	public void setConfirm_flag(String confirm_flag) {
		this.confirm_flag = confirm_flag;
	}

	public String getSupamt_flag() {
		return supamt_flag;
	}

	public void setSupamt_flag(String supamt_flag) {
		this.supamt_flag = supamt_flag;
	}

	public String getDelay_request_flag() {
		return delay_request_flag;
	}

	public void setDelay_request_flag(String delay_request_flag) {
		this.delay_request_flag = delay_request_flag;
	}

	public String getHoliday_average() {
		return holiday_average;
	}

	public void setHoliday_average(String holiday_average) {
		this.holiday_average = holiday_average;
	}

	public String getOperator_time() {
		return operator_time;
	}

	public void setOperator_time(String operator_time) {
		this.operator_time = operator_time;
	}

	@Override
	public String toString() {
		return "TransModificationApplication{" +
				"id='" + id + '\'' +
				", order_no='" + order_no + '\'' +
				", rent_time='" + rent_time + '\'' +
				", revert_time='" + revert_time + '\'' +
				", day_unit_price='" + day_unit_price + '\'' +
				", weekend_price='" + weekend_price + '\'' +
				", holiday_price='" + holiday_price + '\'' +
				", rent_amt='" + rent_amt + '\'' +
				", is_original='" + is_original + '\'' +
				", car_no='" + car_no + '\'' +
				", create_time='" + create_time + '\'' +
				", confirm_flag='" + confirm_flag + '\'' +
				", supamt_flag='" + supamt_flag + '\'' +
				", delay_request_flag='" + delay_request_flag + '\'' +
				", holiday_average='" + holiday_average + '\'' +
				", operator_time='" + operator_time + '\'' +
				'}';
	}
}
