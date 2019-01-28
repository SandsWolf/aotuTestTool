package com.autoyol.entity;

public class CarBusyTime {
	private String id;
	private String begin_time;	//开始时间 HHmm
	private String end_time;	//结束时间 HHmm
	private String WEEK;		//星期1-7，(2,3,4,5,6,7,1表示每天),由于历史原因，1表示周日,2表示周一,以此类推
	private Integer is_open;	//数据有效开关
	public String getBegin_time() {
		return begin_time;
	}
	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getWEEK() {
		return WEEK;
	}
	public void setWEEK(String wEEK) {
		WEEK = wEEK;
	}
	public Integer getIs_open() {
		return is_open;
	}
	public void setIs_open(Integer is_open) {
		this.is_open = is_open;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "CarBusyTime [id=" + id + ", begin_time=" + begin_time
				+ ", end_time=" + end_time + ", WEEK=" + WEEK + ", is_open="
				+ is_open + "]";
	}
	
	
	
}
