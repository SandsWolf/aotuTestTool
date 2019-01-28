package com.autoyol.entity;

public class IllegalQueryDayConf {
	private Integer city_code;
	private Integer illegal_query_day;
	private Integer trans_process_day;	//实际还车后X天定时任务自动结算
	public Integer getCity_code() {
		return city_code;
	}
	public void setCity_code(Integer city_code) {
		this.city_code = city_code;
	}
	public Integer getIllegal_query_day() {
		return illegal_query_day;
	}
	public void setIllegal_query_day(Integer illegal_query_day) {
		this.illegal_query_day = illegal_query_day;
	}
	public Integer getTrans_process_day() {
		return trans_process_day;
	}
	public void setTrans_process_day(Integer trans_process_day) {
		this.trans_process_day = trans_process_day;
	}
}
