package com.autoyol.entity;

public class TransLog {
	private String id;
	private String status;
	private String rent_time;			//起租时间
	private String revert_time;			//结束时间
	private String day_unit_price;		//车辆平日价
	private String holiday_price;		//车辆节假日价
	private String rent_amt;			//租金
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "TransLog [id=" + id + ", status=" + status + ", rent_time="
				+ rent_time + ", revert_time=" + revert_time
				+ ", day_unit_price=" + day_unit_price + ", holiday_price="
				+ holiday_price + ", rent_amt=" + rent_amt + "]";
	}
	
	
	
}
