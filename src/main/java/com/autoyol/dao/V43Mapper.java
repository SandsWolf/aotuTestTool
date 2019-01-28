package com.autoyol.dao;


import com.autoyol.entity.OrderInfo;

public interface V43Mapper {
	public OrderInfo selectOrderMsg(String order_no);
	public Integer selectRenterDayPrice(String order_no);
	public String selectServiceCost(String c_name);
	public void updateTrans_ext(OrderInfo order);
	public void clearTrans_ext(String order_no);
	public int checkTransExt(String order_no);
}
