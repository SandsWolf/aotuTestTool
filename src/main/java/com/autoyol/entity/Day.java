package com.autoyol.entity;

public class Day {
	private String dayDate;		//当天日期：精确到"天"
	private int is_special;		//节假日标识：0-平日 1-节日 2-周末 3-春节
	private String timeDate;	//订单时间（只有起始时间和结束时间才有值）：精确到rentTime、revertTime
	
	
	public String getDayDate() {
		return dayDate;
	}
	public void setDayDate(String dayDate) {
		this.dayDate = dayDate;
	}

	public int getIs_special() {
		return is_special;
	}

	public void setIs_special(int is_special) {
		this.is_special = is_special;
	}

	public String getTimeDate() {
		return timeDate;
	}
	public void setTimeDate(String timeDate) {
		this.timeDate = timeDate;
	}

	@Override
	public String toString() {
		return "Day{" +
				"dayDate='" + dayDate + '\'' +
				", is_special=" + is_special +
				", timeDate='" + timeDate + '\'' +
				'}';
	}
}
