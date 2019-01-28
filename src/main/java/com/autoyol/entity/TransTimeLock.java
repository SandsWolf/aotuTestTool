package com.autoyol.entity;

public class TransTimeLock {
	private String order_no;
    private String car_no;
	private int rent_time_lock;		//锁定租车时间，0：否，1：是
	private String rent_time;		//订单开始时间
	private String revert_time;		//订单结束时间
	private int status;

	public String getCar_no() {
		return car_no;
	}

	public void setCar_no(String car_no) {
		this.car_no = car_no;
	}

	public int getRent_time_lock() {
		return rent_time_lock;
	}

	public void setRent_time_lock(int rent_time_lock) {
		this.rent_time_lock = rent_time_lock;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	@Override
	public String toString() {
		return "TransTimeLock{" +
				"order_no='" + order_no + '\'' +
				", car_no='" + car_no + '\'' +
				", rent_time_lock=" + rent_time_lock +
				", rent_time='" + rent_time + '\'' +
				", revert_time='" + revert_time + '\'' +
				", status=" + status +
				'}';
	}
}
