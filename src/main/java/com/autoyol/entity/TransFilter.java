package com.autoyol.entity;

public class TransFilter {
	private String rent_time;			//起租时间
	private String revert_time;			//结束时间
	private Integer get_return_flag;		//是否是兼容取还车时间，0：否，1：是
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
	
	public Integer getGet_return_flag() {
		return get_return_flag;
	}
	public void setGet_return_flag(Integer get_return_flag) {
		this.get_return_flag = get_return_flag;
	}
	@Override
	public String toString() {
		return "TransFilter [rent_time=" + rent_time + ", revert_time="
				+ revert_time + ", get_return_flag=" + get_return_flag + "]";
	}
	
	
}
