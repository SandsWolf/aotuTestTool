package com.autoyol.entity;

public class Trans {
	private Integer id;
	private String order_no;
	private Integer status;
	private String renter_no;				//租客No
	private String renter_phone;			//租客电话
	private Integer total_amt;				//租金
	private Integer pay_flag;				//租车押金支付状态
	private Integer deposit_amt;			//违章押金
	private Integer deposit_pay_flag;		//违章押金支付状态
	private String real_revert_time;		//实际还车时间
	private Integer settle;					//结算标识
	private Integer is_dispatching;			//是否支持拒单调度0:否，1是调度未结果, 2:是调度有结果
	private Integer dispatching_renter_status;		//租客订单状态（1,2,3,4,12），（16）
	private Integer prepAuth_result;		//租车预授权是否支付成功，0未支付，1(生成订单成功)成功返回银联参数，2(生成订单成功)失败返回银联参数， 3已支付
	private String pay_time;				//订单支付时间(yyyyMMddHHmmss)
	private String operator_event;			//管理后台操作人操作事件，调用APP服务端接口
	private String operate_time;			//管理后台操作人操作时间
	private String owner_no;				//车主NO
	private String owner_phone;				//车主电话
	private Integer depositAuth_result;		//违章押金是否支付成功，0未支付，1(生成订单成功)成功返回银联参数，2(生成订单成功)失败返回银联参数，3已支付
	private String deposit_pay_time;		//支付时间(yyyyMMddHHmmss)
	private String city;
	private String rent_amt;				//租金
	private String renter_order_no;			//套餐订单号
	private String rent_time;				//订单起租时间
	private String revert_time;				//订单结束时间
	private Integer source;
	private String car_no;

	public String getCar_no() {
		return car_no;
	}

	public void setCar_no(String car_no) {
		this.car_no = car_no;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
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
	public String getRenter_order_no() {
		return renter_order_no;
	}
	public void setRenter_order_no(String renter_order_no) {
		this.renter_order_no = renter_order_no;
	}
	public String getRent_amt() {
		return rent_amt;
	}
	public void setRent_amt(String rent_amt) {
		this.rent_amt = rent_amt;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDeposit_pay_time() {
		return deposit_pay_time;
	}
	public void setDeposit_pay_time(String deposit_pay_time) {
		this.deposit_pay_time = deposit_pay_time;
	}
	public Integer getDepositAuth_result() {
		return depositAuth_result;
	}
	public void setDepositAuth_result(Integer depositAuth_result) {
		this.depositAuth_result = depositAuth_result;
	}
	public String getOwner_phone() {
		return owner_phone;
	}
	public void setOwner_phone(String owner_phone) {
		this.owner_phone = owner_phone;
	}
	public String getOwner_no() {
		return owner_no;
	}
	public void setOwner_no(String owner_no) {
		this.owner_no = owner_no;
	}
	public Integer getPrepAuth_result() {
		return prepAuth_result;
	}
	public void setPrepAuth_result(Integer prepAuth_result) {
		this.prepAuth_result = prepAuth_result;
	}
	public String getPay_time() {
		return pay_time;
	}
	public void setPay_time(String pay_time) {
		this.pay_time = pay_time;
	}
	public String getOperator_event() {
		return operator_event;
	}
	public void setOperator_event(String operator_event) {
		this.operator_event = operator_event;
	}
	public String getOperate_time() {
		return operate_time;
	}
	public void setOperate_time(String operate_time) {
		this.operate_time = operate_time;
	}
	public Integer getDispatching_renter_status() {
		return dispatching_renter_status;
	}
	public void setDispatching_renter_status(Integer dispatching_renter_status) {
		this.dispatching_renter_status = dispatching_renter_status;
	}
	public Integer getIs_dispatching() {
		return is_dispatching;
	}
	public void setIs_dispatching(Integer is_dispatching) {
		this.is_dispatching = is_dispatching;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getRenter_no() {
		return renter_no;
	}
	public void setRenter_no(String renter_no) {
		this.renter_no = renter_no;
	}
	public String getRenter_phone() {
		return renter_phone;
	}
	public void setRenter_phone(String renter_phone) {
		this.renter_phone = renter_phone;
	}
	public Integer getTotal_amt() {
		return total_amt;
	}
	public void setTotal_amt(Integer total_amt) {
		this.total_amt = total_amt;
	}
	public Integer getPay_flag() {
		return pay_flag;
	}
	public void setPay_flag(Integer pay_flag) {
		this.pay_flag = pay_flag;
	}
	public Integer getDeposit_amt() {
		return deposit_amt;
	}
	public void setDeposit_amt(Integer deposit_amt) {
		this.deposit_amt = deposit_amt;
	}
	public Integer getDeposit_pay_flag() {
		return deposit_pay_flag;
	}
	public void setDeposit_pay_flag(Integer deposit_pay_flag) {
		this.deposit_pay_flag = deposit_pay_flag;
	}
	public String getReal_revert_time() {
		return real_revert_time;
	}
	public void setReal_revert_time(String real_revert_time) {
		this.real_revert_time = real_revert_time;
	}
	public Integer getSettle() {
		return settle;
	}
	public void setSettle(Integer settle) {
		this.settle = settle;
	}



	@Override
	public String toString() {
		return "Trans{" +
				"id=" + id +
				", order_no='" + order_no + '\'' +
				", status=" + status +
				", renter_no='" + renter_no + '\'' +
				", renter_phone='" + renter_phone + '\'' +
				", total_amt=" + total_amt +
				", pay_flag=" + pay_flag +
				", deposit_amt=" + deposit_amt +
				", deposit_pay_flag=" + deposit_pay_flag +
				", real_revert_time='" + real_revert_time + '\'' +
				", settle=" + settle +
				", is_dispatching=" + is_dispatching +
				", dispatching_renter_status=" + dispatching_renter_status +
				", prepAuth_result=" + prepAuth_result +
				", pay_time='" + pay_time + '\'' +
				", operator_event='" + operator_event + '\'' +
				", operate_time='" + operate_time + '\'' +
				", owner_no='" + owner_no + '\'' +
				", owner_phone='" + owner_phone + '\'' +
				", depositAuth_result=" + depositAuth_result +
				", deposit_pay_time='" + deposit_pay_time + '\'' +
				", city='" + city + '\'' +
				", rent_amt='" + rent_amt + '\'' +
				", renter_order_no='" + renter_order_no + '\'' +
				", rent_time='" + rent_time + '\'' +
				", revert_time='" + revert_time + '\'' +
				'}';
	}
}
