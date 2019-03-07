package com.autoyol.entity;

public class RentAmtData {
	private String day_price;		//平时日均价
	private String weekend_price;	//周末日均价
	private String holiday_price;	//节假日均价
	private String spring_price;	//春节日均价
	private String use_special_price_flag;	//是否使用特供价  1:使用  0：不用

	private String rent_time;		//订单开始时间
	private String revert_time;		//订单结束时间

	private String continueTime;	//修改订单用：修改租期，上一段的结束时间
	private String rentAmt;			//修改订单用：记录每段租金
	private String part;			//修改订单用：属于第几段
	private String carNo;			//修改订单用：车辆号
	private Long operatorTime;		//修改订单用：操作时间：时间戳格式
	private Integer type;			//修改订单用：数据类型：1.trans_modification_application表数据  2.trans_modification_console表数据
	private String id;				//修改订单用：对应表中的id号

	public String getUse_special_price_flag() {
		return use_special_price_flag;
	}

	public void setUse_special_price_flag(String use_special_price_flag) {
		this.use_special_price_flag = use_special_price_flag;
	}

	public String getSpring_price() {
		return spring_price;
	}

	public void setSpring_price(String spring_price) {
		this.spring_price = spring_price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getOperatorTime() {
		return operatorTime;
	}

	public void setOperatorTime(Long operatorTime) {
		this.operatorTime = operatorTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}

	public String getRentAmt() {
		return rentAmt;
	}

	public void setRentAmt(String rentAmt) {
		this.rentAmt = rentAmt;
	}

	public String getContinueTime() {
		return continueTime;
	}

	public void setContinueTime(String continueTime) {
		this.continueTime = continueTime;
	}

	public String getDay_price() {
		return day_price;
	}
	public void setDay_price(String day_price) {
		this.day_price = day_price;
	}
	public String getHoliday_price() {
		return holiday_price;
	}
	public void setHoliday_price(String holiday_price) {
		this.holiday_price = holiday_price;
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

	public String getWeekend_price() {
		return weekend_price;
	}

	public void setWeekend_price(String weekend_price) {
		this.weekend_price = weekend_price;
	}

	@Override
	public String toString() {
		return "RentAmtData{" +
				"day_price='" + day_price + '\'' +
				", weekend_price='" + weekend_price + '\'' +
				", holiday_price='" + holiday_price + '\'' +
				", rent_time='" + rent_time + '\'' +
				", revert_time='" + revert_time + '\'' +
				", continueTime='" + continueTime + '\'' +
				", rentAmt='" + rentAmt + '\'' +
				", part='" + part + '\'' +
				", carNo='" + carNo + '\'' +
				", operatorTime=" + operatorTime +
				", type=" + type +
				", id='" + id + '\'' +
				'}';
	}
}
