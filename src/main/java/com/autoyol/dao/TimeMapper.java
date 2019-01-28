package com.autoyol.dao;

import com.autoyol.entity.*;

import java.util.List;
import java.util.Map;

public interface TimeMapper {
	public List<CarBusyTime> selectCarBusyTimeList(String car_no);
	public List<String> selectCarFilterList(String car_no);
	public List<TransFilter> selectTransFilterList(String car_no);
	public List<OrderSettingActivity> selectOrderSettingActivity(Map<String, String> map);
	public CarOrderSetting selectCarOrderSetting(String car_no);
	public List<TransReply> selectTansReplyList(String car_no);
	public List<TransTimeLock> selectTansTimeLockList(String car_no);
}
