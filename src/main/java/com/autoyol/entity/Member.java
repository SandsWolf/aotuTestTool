package com.autoyol.entity;

public class Member {
	private String reg_no;
	private String mobile;
	private Integer id_card_auth;		//身份证正面照片状态：0：未上传，1：已上传，2：已认证，3：认证不通过, 4:无效数据
	private Integer dri_lic_auth;		//驾照照片状态：0：未上传，1：已上传，2：已认证，3：认证不通过, 4:无效数据
	private Integer dri_vice_lic_auth;	//驾驶证副页照片:0：未上传，1：已上传，2：已认证，3：认证不通过, 4:无效数据, 5:未上传（已认证）
	private Integer rent_flag;			//是否可以租车：1：可租，0：不可租
	private String token;				//token
	private String dri_lic_first_time;	//驾驶证领证时间
	private Integer internal_staff;		//内部员工:0否、1是

	public String getReg_no() {
		return reg_no;
	}

	public void setReg_no(String reg_no) {
		this.reg_no = reg_no;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getId_card_auth() {
		return id_card_auth;
	}

	public void setId_card_auth(Integer id_card_auth) {
		this.id_card_auth = id_card_auth;
	}

	public Integer getDri_lic_auth() {
		return dri_lic_auth;
	}

	public void setDri_lic_auth(Integer dri_lic_auth) {
		this.dri_lic_auth = dri_lic_auth;
	}

	public Integer getDri_vice_lic_auth() {
		return dri_vice_lic_auth;
	}

	public void setDri_vice_lic_auth(Integer dri_vice_lic_auth) {
		this.dri_vice_lic_auth = dri_vice_lic_auth;
	}

	public Integer getRent_flag() {
		return rent_flag;
	}

	public void setRent_flag(Integer rent_flag) {
		this.rent_flag = rent_flag;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDri_lic_first_time() {
		return dri_lic_first_time;
	}

	public void setDri_lic_first_time(String dri_lic_first_time) {
		this.dri_lic_first_time = dri_lic_first_time;
	}

	public Integer getInternal_staff() {
		return internal_staff;
	}

	public void setInternal_staff(Integer internal_staff) {
		this.internal_staff = internal_staff;
	}

	@Override
	public String toString() {
		return "Member{" +
				"reg_no='" + reg_no + '\'' +
				", mobile='" + mobile + '\'' +
				", id_card_auth=" + id_card_auth +
				", dri_lic_auth=" + dri_lic_auth +
				", dri_vice_lic_auth=" + dri_vice_lic_auth +
				", rent_flag=" + rent_flag +
				", token='" + token + '\'' +
				", dri_lic_first_time='" + dri_lic_first_time + '\'' +
				", internal_staff=" + internal_staff +
				'}';
	}
}
