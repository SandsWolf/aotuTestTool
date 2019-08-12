package com.autoyol.dao;

import com.autoyol.entity.City;
import com.autoyol.entity.IllegalQueryDayConf;
import com.autoyol.entity.TransModificationApplication;

import java.util.List;
import java.util.Map;

public interface OtherFunctionMapper {
	public IllegalQueryDayConf selectIllegalQueryDayConf(String city);	//查"上海"的违章押金结算时间配置表
	public void insertIllegalQueryDayConf(String city);	//加"上海"的违章押金结算时间配置表
	public void updateIllegalQueryDayConf(String city);
	public void updateTransTimedTask(String NEXT_FIRE_TIME);
	public String selectTransNextFireTime();
	public void updateIllegalTimedTask(String NEXT_FIRE_TIME);
	public String selectIllegalNextFireTime();
	public List<TransModificationApplication> selectTransModificationApplication(String order_no);
	public List<City> selectCityList();
	public City selectCityByCode(String cityCode);
//	public Map<String,Object> getSysConstantByCode(@Param("code")String code);
	public void updateErrorLogCheckFlag(Map paraMap);
}
