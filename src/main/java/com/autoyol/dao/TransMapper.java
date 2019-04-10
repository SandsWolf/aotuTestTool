package com.autoyol.dao;

import org.apache.ibatis.annotations.Param;
import com.autoyol.entity.*;

import java.util.List;
import java.util.Map;

public interface TransMapper {
	public Trans selectTransByorderNo(String order_no);
	public void updateRealRevertTime(Trans trans);
	public void payTotalAmt(Trans trans);
	public void payDepositAmt(Trans trans);
	public void insertTransPayOffline(TransPayOffline transPayOffline);
	public RentAmtData selectDefaultRentAmtData(String order_no);
	public List<TransLog> selectTransLogList(String order_no);
	public List<TransModificationApplication> selectApplicationList(String order_no);
	public List<TransModificationConsole> selectConsoleList(String order_no);
	public TransLog selectLastTransLog(String order_no);
	public String getDistanceFromCar(@Param("lat") String lat, @Param("lon") String lon, @Param("carNo") String carNo);
	public String getDistance(@Param("A_lon") String aLon, @Param("A_lat") String aLat, @Param("B_lon") String bLon, @Param("B_lat") String bLat);
	public void updateTrans(@Param("rentTime") String rentTime, @Param("revertTime") String revertTime, @Param("orderNo") String orderNo);
	public void deleteTransIllegalSettleUntreated(String orderNo);
	public void deleteTransIllegalSettleFlag(String orderNo);
	public void insertTransIllegalSettleFlag(String orderNo);
	public String selectGetBackCarFeeConfig (Map<String, String> paraMap);



	//测试用
	public void TestUpdateTrans(HolidayTime holidayTime);
	public void updateCPIC(HolidayTime holidayTime);
	public int transCount(HolidayTime holidayTime);
	public int CpicCount(HolidayTime holidayTime);
}
