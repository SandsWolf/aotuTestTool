package com.autoyol.entity;

import java.util.List;

/**
 * @author LX
 *
 */
public class TimeAxis {
	private String date;						//日期
	private List<String> timeList;				//不可租时间轴
	private String flag;						//用于判断对象是否满足不可阻条件：true:满足（可租）  false:不满足（不可租）
	private List<List<String>> allTimeList;		//用于合并时间轴时候，存放某个日子对应的所有时间轴list
	private String msg;							//不可租时间描述：判断时当flag=false时去掉，用于前段展示不可租时间
	private String timeListString;				//时间轴String

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<String> getTimeList() {
		return timeList;
	}

	public void setTimeList(List<String> timeList) {
		this.timeList = timeList;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public List<List<String>> getAllTimeList() {
		return allTimeList;
	}

	public void setAllTimeList(List<List<String>> allTimeList) {
		this.allTimeList = allTimeList;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTimeListString() {
		return timeListString;
	}

	public void setTimeListString(String timeListString) {
		this.timeListString = timeListString;
	}


	@Override
	public String toString() {
		return "TimeAxis{" +
				"date='" + date + '\'' +
				", timeList=" + timeList +
				", flag='" + flag + '\'' +
				", allTimeList=" + allTimeList +
				", msg='" + msg + '\'' +
				", timeListString='" + timeListString + '\'' +
				'}';
	}
}
