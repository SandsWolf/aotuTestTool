package com.autoyol.service;

import com.autoyol.entity.*;

import java.util.List;
import java.util.Map;

public interface TransService {
	public Result depositAmtSettle(PathIP pathIP, String order_no);
	public Result totalAmtSettle(PathIP pathIP, String order_no);
	public Result payTotalAmtOffline(String order_no, Trans paraTrans, String trans_type);
	public Result payDepositAmtOffline(String order_no, Trans paraTrans, String trans_type);
	public Map<String,String> getRentAmt(List<String> holidayList, double dayPrice, double holiydaPrice, String startTime, String endTime);
	public Map<String,String> getRentAmtWithWeekend(List<String> holidayList, double dayPrice,double weekendPrice, double holiydaPrice, String startTime, String endTime);
	public Map<String,String> checkRentAmt(List<String> holidayList, List<TransLog> transLoglist);
	public List<UpdateData> updateTrans(List<String> holidayList, List<RentAmtData> rentAmtDataList, int rentAmtType);
	public List<String> modifyRentTime(String rentTime, String revertTime, int type);
	public Map<String,String> getDistanceFromCar(String ALon, String ALat, String BLon, String BLat);
	public Map<String,String> getDistance(String aLon, String aLat, String bLon, String bLat);
	public Result setStatusFlow(Trans trans, PathIP pathIP, int stepFlag, int timeType);

	public Map<String,String> getDistanceNewRow(String ALon, String ALat, String BLon, String BLat, String distance);
	public List<String> getHolidayList(PathIP pathIP);

//	public Map<String,Object> evaluationRecordAdd(Trans trans,String type,String url);
//	public Map<String,Object> presentTime(String orderNo,String memNo,String token,String url);
//	public Map<String,Object> reqConfirm(Trans trans,Member member,String url);

}
