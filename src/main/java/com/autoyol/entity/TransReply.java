package com.autoyol.entity;

public class TransReply {
	public String car_no;
	public int reply_min;	//最短租用时间（小时）
	public int reply_max;	//最长租用时间（小时）

	public String getCar_no() {
		return car_no;
	}

	public void setCar_no(String car_no) {
		this.car_no = car_no;
	}

	public int getReply_min() {
		return reply_min;
	}

	public void setReply_min(int reply_min) {
		this.reply_min = reply_min;
	}

	public int getReply_max() {
		return reply_max;
	}

	public void setReply_max(int reply_max) {
		this.reply_max = reply_max;
	}

	@Override
	public String toString() {
		return "TransReply{" +
				"car_no='" + car_no + '\'' +
				", reply_min=" + reply_min +
				", reply_max=" + reply_max +
				'}';
	}
}
