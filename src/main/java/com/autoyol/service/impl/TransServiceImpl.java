package com.autoyol.service.impl;

import com.alibaba.fastjson.JSON;
import com.autoyol.dao.HolidayMapper;
import com.autoyol.dao.MemberMapper;
import com.autoyol.dao.OtherFunctionMapper;
import com.autoyol.dao.TransMapper;
import com.autoyol.entity.*;
import com.autoyol.http.HttpResponse;
import com.autoyol.http.HttpUtils;
import com.autoyol.service.TransService;
import com.autoyol.util.SetDateSourceUtil;
import com.autoyol.util.ToolUtil;
import jdk.nashorn.internal.ir.IfNode;
import org.hamcrest.core.Is;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TransServiceImpl implements TransService{
	@Resource
	private TransMapper transMapper;
	@Resource
	private OtherFunctionMapper otherFunctionMapper;
	@Resource
	private MemberMapper memberMapper;
	@Resource
	private HolidayMapper holidayMapper;

	private static final Logger logger = LoggerFactory.getLogger(TransServiceImpl.class);

	/**
	 * 违章押金结算：订单实际还车时间改到18天前，然后调用违章结算接口
	 * 1.订单号为空，直接调接口
	 * 2.订单号不存在，直接调接口
	 * 3.订单号存在，时间改成18天前再调接口
	 */
	public Result depositAmtSettle(PathIP pathIP,String order_no) {
		Result result = new Result();
		try {
			Map<String,String> map = new HashMap<String, String>();	//设置请求头
			map.put("User-Agent","AutoyolEs_web");

			String url = null;
			if("test_5".equals(pathIP.getEnvironment()) || "test_9".equals(pathIP.getEnvironment())){
				url = pathIP.getAutoTVServiceIP() + "test/transProcess";
			}else{
				url = pathIP.getAutoTVServiceIP() + "AutoTvService/test/transProcess";
			}

			HttpResponse httpResult = new HttpResponse();
			if("".equals(order_no)){
				httpResult = HttpUtils.get(map,url);

				result.setStatus(0);
				result.setMsg("success");
				result.setData("违章押金结算接口URL：" + url + "<br><br>" + httpResult.toString());
			}else{
				Trans trans = transMapper.selectTransByorderNo(order_no);
				if(trans == null){
					//logger.info("订单trans:"+trans);
					httpResult = HttpUtils.get(map,url);

					result.setStatus(0);
					result.setMsg("success");
					result.setData("违章押金结算接口URL：" + url + "<br><br><span class=\"sign_span\" style=\"color:red;\">" + "未找到订单号：\"" + order_no + "\"</span><br>违章押金结算接口已调用：" + httpResult.toString());
				}else{
					//判断订单是否满足违章押金结算条件
					if((trans.getPay_flag()==7 || trans.getPay_flag()==8 || trans.getPay_flag()==3) && trans.getSettle()==1){
						String nowTime = ToolUtil.getTime2((new Date()).getTime());

						//设置订单城市的违章押金结算时间配置表
						IllegalQueryDayConf illegalQueryDayConf = otherFunctionMapper.selectIllegalQueryDayConf(trans.getCity());	//获取订单城市的违章押金结算时间配置表
						if(illegalQueryDayConf == null){
							//logger.info("illegalQueryDayConf:" + illegalQueryDayConf);
							otherFunctionMapper.insertIllegalQueryDayConf(trans.getCity());			//增加订单城市的违章时间配置
						}else{
							if(illegalQueryDayConf.getTrans_process_day() != 18){
								otherFunctionMapper.updateIllegalQueryDayConf(trans.getCity());	//修改订单城市违章押金结算时间配置
							}
						}

						String realRevertTime = ToolUtil.addDay(nowTime, -18);	//实际还车时间改18天前
						realRevertTime = ToolUtil.addHour(realRevertTime, -1);	//1小时前
						trans.setReal_revert_time(realRevertTime);
						transMapper.updateRealRevertTime(trans);	//修改trans表实际还车时间

						//初始化违章结算条件数据
						transMapper.deleteTransIllegalSettleUntreated(trans.getOrder_no());		//删除违章结算记录表
						transMapper.deleteTransIllegalSettleFlag(trans.getOrder_no());			//删除违章扣款标记表
						transMapper.insertTransIllegalSettleFlag(trans.getOrder_no());			//输入违章扣款标记表
						trans = transMapper.selectTransByorderNo(trans.getOrder_no());
						httpResult = HttpUtils.get(map,url);

						result.setStatus(0);
						result.setMsg("success");
						if (httpResult.getHttpRespCode() == 200) {
							result.setData("违章押金结算接口URL：" + url + "<br><br><span class=\"sign_span\" style=\"color:red;\">" + "订单号：\"" + trans.getOrder_no() + "\" 违章结算成功</span>, real_revert_time：" + trans.getReal_revert_time() + "<br>违章押金结算接口已调用：" + httpResult.toString());
						} else {
							result.setData("违章押金结算接口URL：" + url + "<br><br><span class=\"sign_span\" style=\"color:red;\">" + "订单号：\"" + trans.getOrder_no() + "\" 违章结算失败</span>, real_revert_time：" + trans.getReal_revert_time() + "<br>违章押金结算接口已调用：" + httpResult.toString());
						}
					}else{
						httpResult = HttpUtils.get(map,url);

						result.setStatus(0);
						result.setMsg("success");
						result.setData("违章押金结算接口URL：" + url + "<br><br><span class=\"sign_span\" style=\"color:red;\">" + "订单号：\"" + order_no + "\" 不满足违章押金结算条件</span><br>违章押金结算接口已调用：" + httpResult.toString());
					}
				}
			}
		}catch (Exception e){
			logger.error("违章押金结算异常：",e);
		}

		return result;
	}
	
	
	/**
	 * 租车押金结算：
	 * 1.订单号为空，直接调接口
	 * 2.订单号不存在，直接调接口
	 * 3.订单号存在，判断是否结算成功
	 */
	public Result totalAmtSettle(PathIP pathIP,String order_no) {
		Result result = new Result();

		try {
			Map<String,String> map = new HashMap<String, String>();
			map.put("User-Agent","AutoyolEs_web");
			String url = pathIP.getTransIP() + "settle/autoSrv";
			logger.info("url:{}",url);
			HttpResponse httpResult = new HttpResponse();

			if("".equals(order_no)){
				httpResult = HttpUtils.get(map,url);

				result.setStatus(0);
				result.setMsg("success");
				if (httpResult.getHttpRespCode() == 200) {
					result.setData("租车押金结算接口URL：" + url + "<br><br><span class=\"sign_span\" style=\"color:red;\">结算成功：" + httpResult.toString() + "</span>");
				} else {
					result.setData("租车押金结算接口URL：" + url + "<br><br><span class=\"sign_span\" style=\"color:red;\">结算失败：" + httpResult.toString() + "</span>");
				}

			}else{
				Trans trans = transMapper.selectTransByorderNo(order_no);
				if(trans == null){
					logger.info("订单trans:{}",trans);
					httpResult = HttpUtils.get(map,url);

					result.setStatus(0);
					result.setMsg("success");
					result.setData("租车押金结算接口URL：" + url + "<br><br><span class=\"sign_span\" style=\"color:red;\">" + "未找到订单号：\"" + order_no + "\"</span><br>租车押金结算接口已调用：" + httpResult.toString());
				}else{
					if(trans.getStatus()!=20){
						httpResult = HttpUtils.get(map,url);

						result.setStatus(0);
						result.setMsg("success");
						result.setData("租车押金结算接口URL：" + url + "<br><br><span class=\"sign_span\" style=\"color:red;\">" + "订单号：\"" + order_no + "\"的\"status\"字段不等于20，结算失败</span><br>租车押金结算接口已调用：" + httpResult.toString());
					}else{
						httpResult = HttpUtils.get(map,url);
						trans = transMapper.selectTransByorderNo(order_no);

						if(trans.getStatus()==20 && trans.getSettle()==1){
							result.setStatus(0);
							result.setMsg("success");
							if (httpResult.getHttpRespCode() == 200) {
								result.setData("租车押金结算接口URL：" + url + "<br><br><span class=\"sign_span\" style=\"color:red;\">" + "订单号：\"" + order_no + "\" 结算成功</span><br>租车押金结算接口已调用：" + httpResult.toString());
							} else {
								result.setData("租车押金结算接口URL：" + url + "<br><br><span class=\"sign_span\" style=\"color:red;\">" + "订单号：\"" + order_no + "\" 结算失败</span><br>租车押金结算接口已调用：" + httpResult.toString());
							}
						}else if(trans.getStatus()==20 && trans.getSettle()!=1){
							result.setStatus(0);
							result.setMsg("success");
							result.setData("租车押金结算接口URL：" + url + "<br><br><span class=\"sign_span\" style=\"color:red;\">" + "订单号：\"" + order_no + "\" 结算失败，请检查\"real_revert_time\"字段是否为4小时前</span><br>租车押金结算接口已调用：" + httpResult.toString());
						}
					}
				}
			}
		}catch (Exception e){
			logger.error("租车押金结算异常：",e);
		}
		
		return result;
	}


	/**
	 * 支付租车押金，根据transType区分消费还是预授权
	 * transType = 01 为"消费" + "现金"
	 * transType = 02 为"预授权" + "转账"
	 */
	public Result payTotalAmtOffline(String order_no, Trans paraTrans, String trans_type) {
		Result result = new Result();
		
		/*
		 * 设置trans表数据
		 */
		Trans trans = new Trans();
		trans.setOrder_no(order_no);
		//判断状态
		Integer[] payFlag = {3,6,7,8};
		if(Arrays.asList(payFlag).contains(paraTrans.getPay_flag()) || paraTrans.getStatus()!=2){
			result.setStatus(0);
			result.setMsg("success");
			result.setData("订单(" + order_no + ")状态：\"" + paraTrans.getStatus() + "\" , pay_flag：\"" + paraTrans.getPay_flag() + "\" ; 订单不满足支付'租车押金'条件 , status应是'2' , pay_flag应不等于'3、6、7、8'");
			return result;
		}
		trans.setPay_flag(3);
		trans.setStatus(3);
		//dispatching_renter_status 租客订单状态（1,2,3,4,12），（16）
		//添加调度租客状态
		if(paraTrans.getIs_dispatching()==1){
			trans.setDispatching_renter_status(3);
		}
		trans.setPrepAuth_result(3);
		trans.setOperator_event("线下支付押金");
		
		Date date = new Date();
		trans.setPay_time(ToolUtil.getTime2(date.getTime()));
		trans.setOperate_time(ToolUtil.getTime1(date.getTime()));
		
		/*
		 * 设置trans_pay_offline表数据
		 */
		TransPayOffline transPayOffline = new TransPayOffline();	//添加trans_pay_offline表对象
		if("01".equals(trans_type)){
			transPayOffline.setOrder_no(order_no);
			transPayOffline.setPay_kind("1");
			transPayOffline.setTrans_kind("1");
			transPayOffline.setTrans_type("01");	
			transPayOffline.setTrans_id("123");
			transPayOffline.setTrans_time(ToolUtil.getTime2(date.getTime()));
			transPayOffline.setTrans_amt("" + paraTrans.getTotal_amt());
			transPayOffline.setQn("123");
			transPayOffline.setSource("6");
			transPayOffline.setResp_msg("操作人:admin");
			transPayOffline.setSt("1");
			transPayOffline.setCreate_time(ToolUtil.getTime1(date.getTime()));
			transPayOffline.setLoop_time(ToolUtil.getTime2(date.getTime()));
		}
		if("02".equals(trans_type)){
			transPayOffline.setOrder_no(order_no);
			transPayOffline.setPay_kind("1");
			transPayOffline.setTrans_kind("1");
			transPayOffline.setTrans_type("02");	
			transPayOffline.setTrans_id("123");
			transPayOffline.setTrans_time(ToolUtil.getTime2(date.getTime()));
			transPayOffline.setTrans_amt("" + paraTrans.getTotal_amt());
			transPayOffline.setQn("123");
			transPayOffline.setSource("5");
			transPayOffline.setResp_msg("操作人:admin");
			transPayOffline.setSt("1");
			transPayOffline.setCreate_time(ToolUtil.getTime1(date.getTime()));
			transPayOffline.setLoop_time(ToolUtil.getTime2(date.getTime()));
		}
		
		try {
			transMapper.payTotalAmt(trans);	//修改trans表数据
			transMapper.insertTransPayOffline(transPayOffline);	//新增trans_pay_offline记录
			
			paraTrans = transMapper.selectTransByorderNo(order_no);
			result.setStatus(0);
			result.setMsg("success");
			result.setData("订单(" + order_no + ")状态：\"" + paraTrans.getStatus() + "\" ; 订单支付'租车押金'成功");
		} catch (Exception e) {
			logger.error("支付租车押金异常：",e);
			result.setStatus(0);
			result.setMsg("success");
			result.setData("数据库执行异常！\n"+e);
		}
		
		return result;
	}


	/**
	 * 支付违章押金，根据transType区分消费还是预授权
	 * transType = 01 为"消费" + "现金"
	 * transType = 02 为"预授权" + "转账"
	 */
	public Result payDepositAmtOffline(String order_no, Trans paraTrans, String trans_type) {
		Result result = new Result();
		
		/*
		 * 设置trans表数据
		 */
		Trans trans = new Trans();
		trans.setOrder_no(order_no);
		//判断状态
		Integer[] payFlag = { 3, 6, 7, 8 };
		if(Arrays.asList(payFlag).contains(paraTrans.getDeposit_pay_flag()) || paraTrans.getStatus() != 3){
			result.setStatus(0);
			result.setMsg("success");
			result.setData("订单(" + order_no + ")状态：\"" + paraTrans.getStatus() + "\" , pay_flag：\"" + paraTrans.getPay_flag() + "\" ; 订单不满足支付'违章押金'条件 , status应是'3' , deposit_pay_flag应不等于'3、6、7、8'");
			return result;
		}
		trans.setDeposit_pay_flag(3);
		trans.setStatus(4);
		//dispatching_renter_status 租客订单状态（1,2,3,4,12），（16）
		//添加调度租客状态
		if(paraTrans.getIs_dispatching()==1){
			trans.setDispatching_renter_status(4);
		}
		trans.setOperator_event("线下支付违章押金");
		trans.setDepositAuth_result(3);
		
		Date date = new Date();
		trans.setDeposit_pay_time(ToolUtil.getTime2(date.getTime()));
		
		/*
		 * 设置trans_pay_offline表数据
		 */
		TransPayOffline transPayOffline = new TransPayOffline();	//添加trans_pay_offline表对象
		if("01".equals(trans_type)){
			transPayOffline.setOrder_no(order_no);
			transPayOffline.setPay_kind("2");
			transPayOffline.setTrans_kind("1");
			transPayOffline.setTrans_type("01");	
			transPayOffline.setTrans_id("123");
			transPayOffline.setTrans_time(ToolUtil.getTime2(date.getTime()));
			transPayOffline.setTrans_amt("" + paraTrans.getDeposit_amt());
			transPayOffline.setQn("123");
			transPayOffline.setSource("6");
			transPayOffline.setResp_msg("操作人:admin");
			transPayOffline.setSt("1");
			transPayOffline.setCreate_time(ToolUtil.getTime1(date.getTime()));
			transPayOffline.setLoop_time(ToolUtil.getTime2(date.getTime()));
		}
		if("02".equals(trans_type)){
			transPayOffline.setOrder_no(order_no);
			transPayOffline.setPay_kind("2");
			transPayOffline.setTrans_kind("1");
			transPayOffline.setTrans_type("02");	
			transPayOffline.setTrans_id("123");
			transPayOffline.setTrans_time(ToolUtil.getTime2(date.getTime()));
			transPayOffline.setTrans_amt("" + paraTrans.getDeposit_amt());
			transPayOffline.setQn("123");
			transPayOffline.setSource("5");
			transPayOffline.setResp_msg("操作人:admin");
			transPayOffline.setSt("1");
			transPayOffline.setCreate_time(ToolUtil.getTime1(date.getTime()));
			transPayOffline.setLoop_time(ToolUtil.getTime2(date.getTime()));
		}
		
		try {
			transMapper.payDepositAmt(trans);	//修改trans表数据
			transMapper.insertTransPayOffline(transPayOffline);	//新增trans_pay_offline记录
			
			paraTrans = transMapper.selectTransByorderNo(order_no);
			result.setStatus(0);
			result.setMsg("success");
			result.setData("订单(" + order_no + ")状态：\"" + paraTrans.getStatus() + "\" ; 订单支付'违章押金'成功");
		} catch (Exception e) {
			logger.error("支付违章押金异常：",e);
			result.setStatus(0);
			result.setMsg("success");
			result.setData("数据库执行异常！\n"+e);
		}
		
		return result;
	}


	/**
	 * 用于计算租金（不带周末，不带春节）
	 * 租金的计算规则：
	 * 	1)日均价：总时间精确到小时(如1.25小时)，日均价 =（平日总小时数 * 平日价/总小时数 + 节假日总小时数 * 节假日价/总小时数）再四舍五入
	 * 	2)时均价：时均价 = （日均价/8）再向上取整
	 * 	3)租金：租金 = 租期天数X日均价 + 多余小时数（向上取整,精确到小时） * 时均价
	 * 
	 *  注：传入holidayList前，需先将holiday对象依次+"000000"
	 * @param holidayList	节假日list
	 * @param dayPrice		平日日均价
	 * @param startTime		订单开始时间
	 * @param endTime		订单结束时间
	 */
	public Map<String,String> getRentAmt(List<String> holidayList, double dayPrice, double holiydaPrice, String startTime, String endTime) {
		Map<String,String> rentAmtMap = new HashMap<String, String>();
		int method = 3;		//日均价算法规则
		
		ToolUtil toolUtil = new ToolUtil();
		List<Day> rentDayList = toolUtil.getrentDayList(startTime, endTime, holidayList, new ArrayList<String>());
		logger.info("租期内日期（精确到分钟）:{}",rentDayList.toString());
		logger.info("rentDayList长度:{}",rentDayList.size());
		
		/*
		 * 根据day算小时数
		 */
		double totalDayMinute = 0;			//平日日均价总分钟数
		double totalHolidayMinute = 0;		//节假日日均价总分钟数
		for(int i=0;i<rentDayList.size();i++){
			try {
				Day day = rentDayList.get(i);
				double minute = 0;		//当天分钟数
				
				/*
				 * 调试：当开始时间和结束时间在同一天，则用当天的结束时间 - 当天的开始时间，算出当天分钟数
				 */
				if(rentDayList.size()==2 && rentDayList.get(0).getDayDate().equals(rentDayList.get(1).getDayDate())){	
					logger.info("DayDate:{},TimeDate:{}",rentDayList.get(i).getDayDate(),rentDayList.get(i).getTimeDate());	//1天调试
					minute = toolUtil.getMinute(rentDayList.get(0).getTimeDate(),rentDayList.get(1).getTimeDate());
					int d = i+1;
					logger.info("第{}天：{}分钟",d,minute);
					if(rentDayList.get(i).getIs_special()==1){
						totalHolidayMinute += minute;
					}else{
						totalDayMinute += minute;
					}
					break;
				}
				
				if(i==0){	//统计租期第一天分钟数
					minute = toolUtil.getMinute(rentDayList.get(i).getTimeDate(), rentDayList.get(i+1).getDayDate());
					logger.info("第1天：{}分钟",minute);
					if(rentDayList.get(i).getIs_special()==1){
						totalHolidayMinute += minute;
					}else{
						totalDayMinute += minute;
					}
				}
				if(i==(rentDayList.size()-1)){	//统计租期最后一天分钟数
					minute = toolUtil.getMinute(rentDayList.get(i).getDayDate(),rentDayList.get(i).getTimeDate());
					int d = i+1;
					logger.info("第{}天：{}分钟",d,minute);
					if(rentDayList.get(i).getIs_special()==1){
						totalHolidayMinute += minute;
					}else{
						totalDayMinute += minute;
					}
				}
				if(i!=0 && i!=(rentDayList.size()-1)){	//统计租期中间部分天数分钟数
					minute = toolUtil.getMinute(rentDayList.get(i).getDayDate(),rentDayList.get(i+1).getDayDate());
					int d = i+1;
					logger.info("第{}天：{}分钟",d,minute);
					if(rentDayList.get(i).getIs_special()==1){
						totalHolidayMinute += minute;
					}else{
						totalDayMinute += minute;
					}
				}
			} catch (Exception e) {
				logger.error("计算租金异常：",e);
			}
		}
		
		double totalMinute = totalDayMinute + totalHolidayMinute;	//租期总分钟数
		logger.info("totalDayMinute:{}分钟",totalDayMinute);
		logger.info("totalHolidayMinute:{}分钟",totalHolidayMinute);
		logger.info("totalMinute:{}分钟",totalMinute);
		rentAmtMap.put("totalDayMinute", totalDayMinute+"");
		rentAmtMap.put("totalHolidayMinute", totalHolidayMinute+"");
		rentAmtMap.put("totalMinute",totalMinute+"");
		
		double totalDayRentDate = (int)totalDayMinute/60 + (totalDayMinute%60)/60;	//平日价所占租期（单位：小时）
		double totalHolidayRentDate = (int)totalHolidayMinute/60 + (totalHolidayMinute%60)/60;	//节假日价所占租期（单位：小时）
		logger.info("平日价所占租期：{}小时",totalDayRentDate);
		logger.info("节假日价所占租期：{}小时",totalHolidayRentDate);
		rentAmtMap.put("totalDayRentDate", totalDayRentDate+"");
		rentAmtMap.put("totalHolidayRentDate", totalHolidayRentDate+"");
		
		double dayPartPrice = 0;		//平日价部分日均价
		double holidayPartPrice = 0;	//节假日部分日均价
		double avgDayPrice = 0;			//日均价
		/*
		 * 日均价 = 平日价向下取整 + 节假日价向下取整
		 */
		if(method == 1){
			dayPartPrice = dayPrice * totalDayMinute / totalMinute;
			holidayPartPrice = holiydaPrice * totalHolidayMinute / totalMinute;

			avgDayPrice = toolUtil.floor(dayPartPrice + holidayPartPrice);	//日均价
		}
		
		/*
		 * 日均价 = (平日价 + 节假日价)整体向上取整
		 */
		if(method == 2){
			dayPartPrice = dayPrice * totalDayMinute / totalMinute;
			holidayPartPrice = holiydaPrice * totalHolidayMinute / totalMinute;
			
			avgDayPrice = toolUtil.ceil(dayPartPrice + holidayPartPrice);	//日均价
		}
		
		/*
		 * 日均价 = (平日价 + 节假日价)整体四舍五入
		 */
		if(method == 3){
			dayPartPrice = dayPrice * totalDayMinute / totalMinute;
			holidayPartPrice = holiydaPrice * totalHolidayMinute / totalMinute;
			
			avgDayPrice = toolUtil.round(dayPartPrice + holidayPartPrice, 1d);
		}
		
		logger.info("订单日均价_平日日均价部分:{}",dayPartPrice);
		logger.info("订单日均价_节假日日均价部分:{}",holidayPartPrice);
		logger.info("订单日均价:{}",avgDayPrice);
		rentAmtMap.put("avgDayPrice", avgDayPrice+"");
		
		double avgHourPrice = toolUtil.ceil(avgDayPrice/8);	//订单时均价（向上取整）
		logger.info("订单时均价:{}",avgHourPrice);
		rentAmtMap.put("avgHourPrice", avgHourPrice+"");
		
		double totalH = totalMinute/60;			//总小时数
		double t = (totalH - totalH%24)/24;		//天数
		double h = totalH%24;					//剩余小时数
		if(h>=8){	//剩余小时数≥8小时算一天
			t += 1;
			h = 0;
		}
		logger.info("总小时数:{}",totalH);
		logger.info("天数:{}",t);
		logger.info("剩余小时:{}",h);
		rentAmtMap.put("totalH", totalH+"");
		rentAmtMap.put("t", t+"");
		rentAmtMap.put("h", h+"");
		
		double rentDate = toolUtil.getRentDate(startTime, endTime);	//租期
		logger.info("租期：{}天",rentDate);
		rentAmtMap.put("rentDate", rentDate+"");
		
		/*
		 * 租金计算规则：日均价 * 租期天数 + 时均价 * 剩余小时数（向上取整）
		 */
		int rentAmt = (int)(avgDayPrice*t + avgHourPrice*toolUtil.ceil(h));
		logger.info("租金（规则一.日均价 * 租期天数 + 时均价 * 剩余小时数）为:{}元",rentAmt);
		rentAmtMap.put("rentAmt", rentAmt+"");
		
		return rentAmtMap;
	}


	/**
	 * 用于计算租金（带周末，不带春节）
	 * 租金的计算规则：
	 * 	1)日均价：总时间精确到小时(如1.25小时)，日均价 =（平日总小时数 * 平日价/总小时数 + 周末总小时数 * 周末价/总小时数 + 节假日总小时数 * 节假日价/总小时数）再四舍五入
	 * 	2)时均价：时均价 = （日均价/8）再向上取整
	 * 	3)租金：租金 = 租期天数X日均价 + 多余小时数（向上取整,精确到小时） * 时均价
	 *
	 * @param holidayList
	 * @param dayPrice
	 * @param holiydaPrice
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public Map<String,String> getRentAmtWithWeekend(List<String> holidayList, double dayPrice, double weekendPrice, double holiydaPrice, String startTime, String endTime) {
		Map<String,String> rentAmtMap = new HashMap<String, String>();
		int method = 3;		//日均价算法规则

		ToolUtil toolUtil = new ToolUtil();
		List<Day> rentDayList = toolUtil.getrentDayList(startTime, endTime, holidayList, new ArrayList<String>());
		logger.info("租期内日期（精确到分钟）:{}",rentDayList.toString());
		logger.info("rentDayList长度:{}",rentDayList.size());

		/*
		 * 根据day算小时数
		 */
		double totalDayMinute = 0;			//平日价总分钟数
		double totalWeekendMinute = 0;		//周末价总分钟数
		double totalHolidayMinute = 0;		//节日价总分钟数
		for(int i=0;i<rentDayList.size();i++){
			try {
				Day day = rentDayList.get(i);
				double minute = 0;		//当天分钟数

				/*
				 * 调试：当开始时间和结束时间在同一天，则用当天的结束时间 - 当天的开始时间，算出当天分钟数
				 */
				if(rentDayList.size()==2 && rentDayList.get(0).getDayDate().equals(rentDayList.get(1).getDayDate())){
					logger.info("DayDate:{},TimeDate:{}",rentDayList.get(i).getDayDate(),rentDayList.get(i).getTimeDate());	//1天调试
					minute = toolUtil.getMinute(rentDayList.get(0).getTimeDate(),rentDayList.get(1).getTimeDate());
					int d = i+1;
					logger.info("第{}天：{}分钟",d,minute);
					if(rentDayList.get(i).getIs_special() == 1){
						totalHolidayMinute += minute;
					} else if(rentDayList.get(i).getIs_special() == 2){
						totalWeekendMinute += minute;
					} else {
						totalDayMinute += minute;
					}
					break;
				}

				if(i==0){	//统计租期第一天分钟数
					minute = toolUtil.getMinute(rentDayList.get(i).getTimeDate(), rentDayList.get(i+1).getDayDate());
					logger.info("第1天：{}分钟",minute);
					if(rentDayList.get(i).getIs_special() == 1){
						totalHolidayMinute += minute;
					} else if(rentDayList.get(i).getIs_special() == 2){
						totalWeekendMinute += minute;
					} else {
						totalDayMinute += minute;
					}
				}
				if(i==(rentDayList.size()-1)){	//统计租期最后一天分钟数
					minute = toolUtil.getMinute(rentDayList.get(i).getDayDate(),rentDayList.get(i).getTimeDate());
					int d = i+1;
					logger.info("第{}天：{}分钟",d,minute);
					if(rentDayList.get(i).getIs_special() == 1){
						totalHolidayMinute += minute;
					} else if(rentDayList.get(i).getIs_special() == 2){
						totalWeekendMinute += minute;
					} else {
						totalDayMinute += minute;
					}
				}
				if(i!=0 && i!=(rentDayList.size()-1)){	//统计租期中间部分天数分钟数
					minute = toolUtil.getMinute(rentDayList.get(i).getDayDate(),rentDayList.get(i+1).getDayDate());
					int d = i+1;
					logger.info("第{}天：{}分钟",d,minute);
					if(rentDayList.get(i).getIs_special() == 1){
						totalHolidayMinute += minute;
					} else if(rentDayList.get(i).getIs_special() == 2){
						totalWeekendMinute += minute;
					} else {
						totalDayMinute += minute;
					}
				}
			} catch (Exception e) {
				logger.error("计算租金异常：",e);
			}
		}

//		double totalMinute = totalDayMinute + totalHolidayMinute;	//租期总分钟数
		double totalMinute = totalDayMinute + totalWeekendMinute + totalHolidayMinute;	//租期总分钟数
		logger.info("totalDayMinute:{}分钟",totalDayMinute);
		logger.info("totalHolidayMinute:{}分钟",totalHolidayMinute);
		logger.info("totalWeekendMinute:{}分钟",totalWeekendMinute);
		logger.info("totalMinute:{}分钟",totalMinute);
		rentAmtMap.put("totalDayMinute", totalDayMinute + "");
		rentAmtMap.put("totalWeekendMinute", totalWeekendMinute + "");
		rentAmtMap.put("totalHolidayMinute", totalHolidayMinute + "");
		rentAmtMap.put("totalMinute",totalMinute + "");

		double totalDayRentDate = (int)totalDayMinute/60 + (totalDayMinute%60)/60;	//平日价所占租期（单位：小时）
		double totalWeekendRentDate = (int)totalWeekendMinute/60 + (totalWeekendMinute%60)/60;	//周末价所占租期（单位：小时）
		double totalHolidayRentDate = (int)totalHolidayMinute/60 + (totalHolidayMinute%60)/60;	//节假日价所占租期（单位：小时）
		logger.info("平日价所占租期：{}小时",totalDayRentDate);
		logger.info("周末价所占租期：{}小时",totalWeekendRentDate);
		logger.info("节假日价所占租期：{}小时",totalHolidayRentDate);
		rentAmtMap.put("totalDayRentDate", totalDayRentDate + "");
		rentAmtMap.put("totalWeekendRentDate", totalWeekendRentDate + "");
		rentAmtMap.put("totalHolidayRentDate", totalHolidayRentDate + "");

		double dayPartPrice = 0;		//平日价部分日均价
		double weekendPartPrice = 0;	//周末价部分日均价
		double holidayPartPrice = 0;	//节假日部分日均价
		double avgDayPrice = 0;			//日均价
		/*
		 * 日均价 = 平日价向下取整 + 节假日价向下取整
		 */
		if(method == 1){
			dayPartPrice = dayPrice * totalDayMinute / totalMinute;
			weekendPartPrice = weekendPrice * totalWeekendMinute / totalMinute;
			holidayPartPrice = holiydaPrice * totalHolidayMinute / totalMinute;

			avgDayPrice = toolUtil.floor(dayPartPrice + weekendPartPrice + holidayPartPrice);	//日均价
		}

		/*
		 * 日均价 = (平日价 + 节假日价)整体向上取整
		 */
		if(method == 2){
			dayPartPrice = dayPrice * totalDayMinute / totalMinute;
			weekendPartPrice = weekendPrice * totalWeekendMinute / totalMinute;
			holidayPartPrice = holiydaPrice * totalHolidayMinute / totalMinute;

			avgDayPrice = toolUtil.ceil(dayPartPrice + weekendPartPrice + holidayPartPrice);	//日均价
		}

		/*
		 * 日均价 = (平日价 + 节假日价)整体四舍五入
		 */
		if(method == 3){
			dayPartPrice = dayPrice * totalDayMinute / totalMinute;
			weekendPartPrice = weekendPrice * totalWeekendMinute / totalMinute;
			holidayPartPrice = holiydaPrice * totalHolidayMinute / totalMinute;

			avgDayPrice = toolUtil.round(dayPartPrice + weekendPartPrice + holidayPartPrice, 1d);
		}

		logger.info("订单日均价_平日日均价部分:{}",dayPartPrice);
		logger.info("订单日均价_周末日均价部分:{}",weekendPartPrice);
		logger.info("订单日均价_节假日日均价部分:{}",holidayPartPrice);
		logger.info("订单日均价:{}",avgDayPrice);
		rentAmtMap.put("avgDayPrice", avgDayPrice + "");

		double avgHourPrice = toolUtil.ceil(avgDayPrice / 8);	//订单时均价（向上取整）
		logger.info("订单时均价:{}",avgHourPrice);
		rentAmtMap.put("avgHourPrice", avgHourPrice + "");

		double totalH = totalMinute / 60;			//总小时数
		double t = (totalH - totalH % 24) / 24;		//天数
		double h = totalH % 24;					//剩余小时数
		if(h >= 8){	//剩余小时数≥8小时算一天
			t += 1;
			h = 0;
		}
		logger.info("总小时数:{}",totalH);
		logger.info("天数:{}",t);
		logger.info("剩余小时:{}",h);
		rentAmtMap.put("totalH", totalH+"");
		rentAmtMap.put("t", t+"");
		rentAmtMap.put("h", h+"");

		double rentDate = toolUtil.getRentDate(startTime, endTime);	//租期
		logger.info("租期：{}天",rentDate);
		rentAmtMap.put("rentDate", rentDate + "");

		/*
		 * 租金计算规则：日均价 * 租期天数 + 时均价 * 剩余小时数（向上取整）
		 */
		int rentAmt = (int)(avgDayPrice * t + avgHourPrice * toolUtil.ceil(h));
		logger.info("租金（规则一.日均价 * 租期天数 + 时均价 * 剩余小时数）为:{}元",rentAmt);
		rentAmtMap.put("rentAmt", rentAmt + "");

		return rentAmtMap;
	}

    /**
     * 用于计算租金（带周末，带春节）
     * 租金的计算规则：
     * 	1)日均价：总时间精确到小时(如1.25小时)，日均价 =（平日总小时数 * 平日价/总小时数 + 周末总小时数 * 周末价/总小时数 + 节假日总小时数 * 节假日价/总小时数 + 春节总小时数 * 春节价/总小时数）再四舍五入
     * 	2)时均价：时均价 = （日均价/8）再向上取整
     * 	3)租金：租金 = 租期天数X日均价 + 多余小时数（向上取整,精确到小时） * 时均价
     *
     * @param springHolidayList
     * @param holidayList
     * @param dayPrice
     * @param weekendPrice
     * @param holiydaPrice
     * @param springPrice
     * @param startTime
     * @param endTime
     * @return
     */
	public Map<String,String> getRentAmtWithSpecialPrice(List<String> springHolidayList, List<String> holidayList, double dayPrice, double weekendPrice, double holiydaPrice,double springPrice, String startTime, String endTime) {
		Map<String,String> rentAmtMap = new HashMap<String, String>();
		int method = 3;		//日均价算法规则

		ToolUtil toolUtil = new ToolUtil();
		List<Day> rentDayList = toolUtil.getrentDayList(startTime, endTime, holidayList, springHolidayList);
		logger.info("租期内日期（精确到分钟）:{}",rentDayList.toString());
		logger.info("rentDayList长度:{}",rentDayList.size());

		/*
		 * 根据day算小时数
		 */
		double totalDayMinute = 0;			//平日价总分钟数
		double totalWeekendMinute = 0;		//周末价总分钟数
		double totalHolidayMinute = 0;		//节日价总分钟数
		double totalSpringMinute = 0;		//春节价总分钟数
		for(int i=0;i<rentDayList.size();i++){
			try {
				Day day = rentDayList.get(i);
				double minute = 0;		//当天分钟数

				/*
				 * 调试：当开始时间和结束时间在同一天，则用当天的结束时间 - 当天的开始时间，算出当天分钟数
				 */
				if(rentDayList.size()==2 && rentDayList.get(0).getDayDate().equals(rentDayList.get(1).getDayDate())){
					logger.info("DayDate:{},TimeDate:{}",rentDayList.get(i).getDayDate(),rentDayList.get(i).getTimeDate());	//1天调试
					minute = toolUtil.getMinute(rentDayList.get(0).getTimeDate(),rentDayList.get(1).getTimeDate());
					int d = i+1;
					logger.info("第{}天：{}分钟",d,minute);
					if (rentDayList.get(i).getIs_special() == 1){ //1-节日
						totalHolidayMinute += minute;
					} else if (rentDayList.get(i).getIs_special() == 2){ //2-周末
						totalWeekendMinute += minute;
					} else if (rentDayList.get(i).getIs_special() == 3) { //3-春节
						totalSpringMinute += minute;
					} else { //0-平日
						totalDayMinute += minute;
					}
					break;
				}

				if(i==0){	//统计租期第一天分钟数
					minute = toolUtil.getMinute(rentDayList.get(i).getTimeDate(), rentDayList.get(i+1).getDayDate());
					logger.info("第1天：{}分钟",minute);
					if (rentDayList.get(i).getIs_special() == 1){ //1-节日
						totalHolidayMinute += minute;
					} else if (rentDayList.get(i).getIs_special() == 2){ //2-周末
						totalWeekendMinute += minute;
					} else if (rentDayList.get(i).getIs_special() == 3) { //3-春节
						totalSpringMinute += minute;
					} else { //0-平日
						totalDayMinute += minute;
					}
				}
				if(i==(rentDayList.size()-1)){	//统计租期最后一天分钟数
					minute = toolUtil.getMinute(rentDayList.get(i).getDayDate(),rentDayList.get(i).getTimeDate());
					int d = i+1;
					logger.info("第{}天：{}分钟",d,minute);
					if (rentDayList.get(i).getIs_special() == 1){ //1-节日
						totalHolidayMinute += minute;
					} else if (rentDayList.get(i).getIs_special() == 2){ //2-周末
						totalWeekendMinute += minute;
					} else if (rentDayList.get(i).getIs_special() == 3) { //3-春节
						totalSpringMinute += minute;
					} else { //0-平日
						totalDayMinute += minute;
					}
				}
				if(i!=0 && i!=(rentDayList.size()-1)){	//统计租期中间部分天数分钟数
					minute = toolUtil.getMinute(rentDayList.get(i).getDayDate(),rentDayList.get(i+1).getDayDate());
					int d = i+1;
					logger.info("第{}天：{}分钟",d,minute);
					if (rentDayList.get(i).getIs_special() == 1){ //1-节日
						totalHolidayMinute += minute;
					} else if (rentDayList.get(i).getIs_special() == 2){ //2-周末
						totalWeekendMinute += minute;
					} else if (rentDayList.get(i).getIs_special() == 3) { //3-春节
						totalSpringMinute += minute;
					} else { //0-平日
						totalDayMinute += minute;
					}
				}
			} catch (Exception e) {
				logger.error("计算租金异常：",e);
			}
		}

//		double totalMinute = totalDayMinute + totalHolidayMinute;	//租期总分钟数
		double totalMinute = totalDayMinute + totalWeekendMinute + totalHolidayMinute;	//租期总分钟数
		logger.info("totalDayMinute:{}分钟",totalDayMinute);
		logger.info("totalHolidayMinute:{}分钟",totalHolidayMinute);
		logger.info("totalWeekendMinute:{}分钟",totalWeekendMinute);
		logger.info("totalSpringMinute:{}分钟",totalSpringMinute);
		logger.info("totalMinute:{}分钟",totalMinute);
		rentAmtMap.put("totalDayMinute", totalDayMinute + "");
		rentAmtMap.put("totalWeekendMinute", totalWeekendMinute + "");
		rentAmtMap.put("totalHolidayMinute", totalHolidayMinute + "");
		rentAmtMap.put("totalSpringMinute", totalSpringMinute + "");
		rentAmtMap.put("totalMinute",totalMinute + "");

		double totalDayRentDate = (int)totalDayMinute/60 + (totalDayMinute%60)/60;	//平日价所占租期（单位：小时）
		double totalWeekendRentDate = (int)totalWeekendMinute/60 + (totalWeekendMinute%60)/60;	//周末价所占租期（单位：小时）
		double totalHolidayRentDate = (int)totalHolidayMinute/60 + (totalHolidayMinute%60)/60;	//节假日价所占租期（单位：小时）
		double totalSpringRentDate = (int)totalSpringMinute/60 + (totalSpringMinute%60)/60;	//春节价所占租期（单位：小时）
		logger.info("平日价所占租期：{}小时",totalDayRentDate);
		logger.info("周末价所占租期：{}小时",totalWeekendRentDate);
		logger.info("节假日价所占租期：{}小时",totalHolidayRentDate);
		logger.info("春节价所占租期：{}小时",totalSpringRentDate);
		rentAmtMap.put("totalDayRentDate", totalDayRentDate + "");
		rentAmtMap.put("totalWeekendRentDate", totalWeekendRentDate + "");
		rentAmtMap.put("totalHolidayRentDate", totalHolidayRentDate + "");
		rentAmtMap.put("totalSpringRentDate", totalSpringRentDate + "");

		double dayPartPrice = 0;		//平日价部分日均价
		double weekendPartPrice = 0;	//周末价部分日均价
		double holidayPartPrice = 0;	//节假日部分日均价
		double springPartPrice = 0;		//春节部分日均价
		double avgDayPrice = 0;			//日均价
		/*
		 * 日均价 = 平日价向下取整 + 节假日价向下取整
		 */
		if(method == 1){
			dayPartPrice = dayPrice * totalDayMinute / totalMinute;
			weekendPartPrice = weekendPrice * totalWeekendMinute / totalMinute;
			holidayPartPrice = holiydaPrice * totalHolidayMinute / totalMinute;
			springPartPrice = springPrice * totalSpringMinute / totalMinute;

			avgDayPrice = toolUtil.floor(dayPartPrice + weekendPartPrice + holidayPartPrice + springPartPrice);	//日均价
		}

		/*
		 * 日均价 = (平日价 + 节假日价)整体向上取整
		 */
		if(method == 2){
			dayPartPrice = dayPrice * totalDayMinute / totalMinute;
			weekendPartPrice = weekendPrice * totalWeekendMinute / totalMinute;
			holidayPartPrice = holiydaPrice * totalHolidayMinute / totalMinute;
			springPartPrice = springPrice * totalSpringMinute / totalMinute;

			avgDayPrice = toolUtil.ceil(dayPartPrice + weekendPartPrice + holidayPartPrice + springPartPrice);	//日均价
		}

		/*
		 * 日均价 = (平日价 + 节假日价)整体四舍五入
		 */
		if(method == 3){
			dayPartPrice = dayPrice * totalDayMinute / totalMinute;
			weekendPartPrice = weekendPrice * totalWeekendMinute / totalMinute;
			holidayPartPrice = holiydaPrice * totalHolidayMinute / totalMinute;
			springPartPrice = springPrice * totalSpringMinute / totalMinute;

			avgDayPrice = toolUtil.round((dayPartPrice + weekendPartPrice + holidayPartPrice + springPartPrice), 1d);
		}

		logger.info("订单日均价_平日日均价部分:{}",dayPartPrice);
		logger.info("订单日均价_周末日均价部分:{}",weekendPartPrice);
		logger.info("订单日均价_节假日日均价部分:{}",holidayPartPrice);
		logger.info("订单日均价_春节日均价部分:{}",springPartPrice);
		logger.info("订单日均价:{}",avgDayPrice);
		rentAmtMap.put("avgDayPrice", avgDayPrice + "");

		double avgHourPrice = toolUtil.ceil(avgDayPrice / 8);	//订单时均价（向上取整）
		logger.info("订单时均价:{}",avgHourPrice);
		rentAmtMap.put("avgHourPrice", avgHourPrice + "");

		double totalH = totalMinute / 60;			//总小时数
		double t = (totalH - totalH % 24) / 24;		//天数
		double h = totalH % 24;					//剩余小时数
		if(h >= 8){	//剩余小时数≥8小时算一天
			t += 1;
			h = 0;
		}
		logger.info("总小时数:{}",totalH);
		logger.info("天数:{}",t);
		logger.info("剩余小时:{}",h);
		rentAmtMap.put("totalH", totalH+"");
		rentAmtMap.put("t", t+"");
		rentAmtMap.put("h", h+"");

		double rentDate = toolUtil.getRentDate(startTime, endTime);	//租期
		logger.info("租期：{}天",rentDate);
		rentAmtMap.put("rentDate", rentDate + "");

		/*
		 * 租金计算规则：日均价 * 租期天数 + 时均价 * 剩余小时数（向上取整）
		 */
		int rentAmt = (int)(avgDayPrice * t + avgHourPrice * toolUtil.ceil(h));
		logger.info("租金（规则一.日均价 * 租期天数 + 时均价 * 剩余小时数）为:{}元",rentAmt);
		rentAmtMap.put("rentAmt", rentAmt + "");

		return rentAmtMap;
	}


	/**
	 * 校验订单租金
	 * 逻辑：依次比较数据，如果租期时间发生变化，说明订单租金可能发生变化，计算租期变化的trans_log数据
	 */
	public Map<String, String> checkRentAmt(List<String> holidayList, List<TransLog> transLoglist) {
		Map<String,String> rentAmtMap = new HashMap<String, String>();
		logger.info("transLog数据长度:{}",transLoglist.size());
		
		boolean flag = false;
		for(int i=0;i<transLoglist.size();i++){
			if(i==(transLoglist.size()-1)){
				break;
			}
			logger.info("第{}次执行",(i+1));
			logger.info("-----------------------------------------------------");
			if(!transLoglist.get(i).getRent_time().equals(transLoglist.get(i+1).getRent_time()) || !transLoglist.get(i).getRevert_time().equals(transLoglist.get(i+1).getRevert_time())){
				logger.info("第{}次执行计算租金",(i+1));
				logger.info(transLoglist.get(i+1).getDay_unit_price()+","+transLoglist.get(i+1).getHoliday_price()+","+transLoglist.get(i+1).getRent_time()+","+transLoglist.get(i+1).getRevert_time());
				logger.info("调试holidayList:{}",holidayList);
				rentAmtMap = this.getRentAmt(holidayList, Double.parseDouble(transLoglist.get(i+1).getDay_unit_price()), Double.parseDouble(transLoglist.get(i+1).getHoliday_price()), transLoglist.get(i+1).getRent_time(), transLoglist.get(i+1).getRevert_time());
				logger.info("第{}次执行计算租金结束",(i+1));
				logger.info("-----------------------------------------------------");
				flag = true;
			}
		}
		if(!flag){	//如租期没变化，则默认给第一条transLog数据进行租金计算
			logger.info("用默认值计算租金");
			rentAmtMap = this.getRentAmt(holidayList, Double.parseDouble(transLoglist.get(0).getDay_unit_price()), Double.parseDouble(transLoglist.get(0).getHoliday_price()), transLoglist.get(0).getRent_time(), transLoglist.get(0).getRevert_time());
		}
		
		return rentAmtMap;
	}


    /**
     * 计算修改订单后的租车押金
     * @param springHolidayList     春节日期list
     * @param holidayList           节日日期list
     * @param rentAmtDataList       修改订单数据list
     * @param rentAmtType           0:不带周末，不带春节（2段）  1:带周末，不带春节（3段）  2:使用特供价（4段）
     * @return
     */
	public List<UpdateData> updateTrans(List<String> springHolidayList, List<String> holidayList, List<RentAmtData> rentAmtDataList , int rentAmtType) throws Exception {
//		List<RentAmtData> rentAmtDataList = new ArrayList<RentAmtData>();       //数据源List
		List<UpdateData> updateDataList = new ArrayList<UpdateData>();          //修改订单计算后数据List
		List<RentAmtData> stageList = new ArrayList<RentAmtData>();     //阶段List：记录每个阶段数据
		logger.info("rentAmtDataList:{}",JSON.toJSONString(rentAmtDataList));

		int partIndex = 1;
		int batchIndex = 1;
		for (int i = 0; i < rentAmtDataList.size(); i++) {
			if (i == 0) {
				logger.info("======================");
				logger.info("i = {}",i);
				UpdateData updateData = new UpdateData();
				Map<String, String> map = null;
				if (rentAmtType == 2 && "1".equals(rentAmtDataList.get(i).getUse_special_price_flag())) {
				    logger.info("第 {} 次：===》使用特供价",i);
					map = this.getRentAmtWithSpecialPrice(springHolidayList, holidayList, Double.parseDouble(rentAmtDataList.get(i).getDay_price()), Double.parseDouble(rentAmtDataList.get(i).getWeekend_price()), Double.parseDouble(rentAmtDataList.get(i).getHoliday_price()), Double.parseDouble(rentAmtDataList.get(i).getSpring_price()), rentAmtDataList.get(i).getRent_time(), rentAmtDataList.get(i).getRevert_time());
					updateData.setCarWeekendPrice(rentAmtDataList.get(i).getWeekend_price());
					updateData.setTotalWeekendRentDate(map.get("totalWeekendRentDate"));
					updateData.setCarSpringPrice(rentAmtDataList.get(i).getSpring_price());
					updateData.setTotalSpringRentDate(map.get("totalSpringRentDate"));
				} else if (rentAmtType == 1 || (rentAmtType == 2 && "0".equals(rentAmtDataList.get(i).getUse_special_price_flag()))) {
                    logger.info("第 {} 次：===》带周末价",i);
					map = this.getRentAmtWithWeekend(holidayList, Double.parseDouble(rentAmtDataList.get(i).getDay_price()),Double.parseDouble(rentAmtDataList.get(i).getWeekend_price()), Double.parseDouble(rentAmtDataList.get(i).getHoliday_price()), rentAmtDataList.get(i).getRent_time(), rentAmtDataList.get(i).getRevert_time());
					updateData.setCarWeekendPrice(rentAmtDataList.get(i).getWeekend_price());
					updateData.setTotalWeekendRentDate(map.get("totalWeekendRentDate"));
				} else if (rentAmtType == 0) {
                    logger.info("第 {} 次：===》不带周末价",i);
					map = this.getRentAmt(holidayList, Double.parseDouble(rentAmtDataList.get(i).getDay_price()), Double.parseDouble(rentAmtDataList.get(i).getHoliday_price()), rentAmtDataList.get(i).getRent_time(), rentAmtDataList.get(i).getRevert_time());
				}
				updateData.setUseSpecialPriceFlag(rentAmtDataList.get(i).getUse_special_price_flag());
				updateData.setTotalH(map.get("totalH"));
				updateData.setTotalDayRentDate(map.get("totalDayRentDate"));
				updateData.setTotalHolidayRentDate(map.get("totalHolidayRentDate"));
				updateData.setRentDate(map.get("rentDate"));
				updateData.setAvgDayPrice(map.get("avgDayPrice"));
				updateData.setAvgHourPrice(map.get("avgHourPrice"));
				updateData.setT(map.get("t"));
				updateData.setH(map.get("h"));
				updateData.setRentAmt(map.get("rentAmt"));
				updateData.setStageRentAmt(map.get("rentAmt"));
				updateData.setRentTime(ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(rentAmtDataList.get(i).getRent_time()))));
				updateData.setRevertTime(ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(rentAmtDataList.get(i).getRevert_time()))));
				updateData.setContinueTime(ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(rentAmtDataList.get(i).getRent_time()))));
				updateData.setPart(partIndex + "");
				updateData.setBatch("1");
				updateData.setCarDayPrice(rentAmtDataList.get(i).getDay_price());
				updateData.setCarHolidayPrice(rentAmtDataList.get(i).getHoliday_price());
				updateData.setCarNo(rentAmtDataList.get(i).getCarNo());
				updateData.setType(rentAmtDataList.get(i).getType());
				updateData.setId(rentAmtDataList.get(i).getId());
				updateData.setUpdateFlag("0");
				updateDataList.add(updateData);

				RentAmtData rentAmtData = new RentAmtData();                                //阶段数据：开始时间，结束时间，平日价，节假日价
				rentAmtData.setRent_time(rentAmtDataList.get(i).getRent_time());
				rentAmtData.setRevert_time(rentAmtDataList.get(i).getRevert_time());
				rentAmtData.setDay_price(rentAmtDataList.get(i).getDay_price());
				rentAmtData.setWeekend_price(rentAmtDataList.get(i).getWeekend_price());
				rentAmtData.setHoliday_price(rentAmtDataList.get(i).getHoliday_price());
				rentAmtData.setSpring_price(rentAmtDataList.get(i).getSpring_price());
				rentAmtData.setUse_special_price_flag(rentAmtDataList.get(i).getUse_special_price_flag());
				rentAmtData.setContinueTime(rentAmtDataList.get(i).getRent_time());
				rentAmtData.setPart(partIndex + "");
				rentAmtData.setRentAmt(map.get("rentAmt"));
				rentAmtData.setCarNo(rentAmtDataList.get(i).getCarNo());
				stageList.add(rentAmtData);

				partIndex += 1;     //记录阶段数
			} else {
				logger.info("======================");
				logger.info("i = {}",i);
				String oldRentTime = stageList.get(stageList.size() - 1).getRent_time();
				String oldRevertTime = stageList.get(stageList.size() - 1).getRevert_time();
				String oldDayPrice = stageList.get(stageList.size() - 1).getDay_price();
				String oldWeekendPrice = stageList.get(stageList.size() -1).getWeekend_price();
				String oldHolidayPrice = stageList.get(stageList.size() - 1).getHoliday_price();
				String oldSoringPrice = stageList.get(stageList.size() - 1).getSpring_price();
				String oldContinueTime = stageList.get(stageList.size() - 1).getContinueTime();
				String oldRentAmt = stageList.get(stageList.size() - 1).getRentAmt();
				String oldCarNo = stageList.get(stageList.size() - 1).getCarNo();

				String newRentTime = rentAmtDataList.get(i).getRent_time();
				String newRevertTime = rentAmtDataList.get(i).getRevert_time();
				String newDayPrice = rentAmtDataList.get(i).getDay_price();
				String newWeekendPrice = rentAmtDataList.get(i).getWeekend_price();
				String newHolidayPrice = rentAmtDataList.get(i).getHoliday_price();
				String newSpringPrice = rentAmtDataList.get(i).getSpring_price();
				String newUseSpecialPriceFlag = rentAmtDataList.get(i).getUse_special_price_flag();
				String newCarNo = rentAmtDataList.get(i).getCarNo();

				logger.info("oldRentTime = {}",oldRentTime);
				logger.info("oldRevertTime = {}",oldRevertTime);
				logger.info("oldDayPrice = {}",oldDayPrice);
				logger.info("oldHolidayPrice = {}",oldHolidayPrice);
				logger.info("oldSpringPrice = {}",oldSoringPrice);
				logger.info("oldContinueTime = {}",oldContinueTime);
				logger.info("------");
				logger.info("newRentTime = {}",newRentTime);
				logger.info("newRevertTime = {}",newRevertTime);
				logger.info("newDayPrice = {}",newDayPrice);
				logger.info("newWeekendPrice = {}",newWeekendPrice);
				logger.info("newHolidayPrice = {}",newHolidayPrice);
				logger.info("newSpringPrice = {}",newSpringPrice);
				logger.info("newCarNo = {}",newCarNo);

				if (newCarNo.equals(oldCarNo)) {      //没换车
					if (oldRentTime.equals(newRentTime)) {        //A不变
						if (!oldRevertTime.equals(newRevertTime)) {        //B变
							if (Long.parseLong(newRevertTime) > Long.parseLong(stageList.get(stageList.size() - 1).getRevert_time())) {     //B↑
								UpdateData updateData = new UpdateData();
								Map<String, String> map = null;
								if (rentAmtType == 2 && "1".equals(rentAmtDataList.get(i).getUse_special_price_flag())) {
                                    logger.info("第 {} 次：===》使用特供价",i);
									map = this.getRentAmtWithSpecialPrice(springHolidayList, holidayList, Double.parseDouble(newDayPrice), Double.parseDouble(newWeekendPrice), Double.parseDouble(newHolidayPrice), Double.parseDouble(newSpringPrice),  oldRevertTime, newRevertTime); //延长那段的租期为：上一条的结束时间(oldRevertTime) — 这一条的结束时间(newRevertTime)
									updateData.setCarWeekendPrice(newWeekendPrice);
									updateData.setTotalWeekendRentDate(map.get("totalWeekendRentDate"));
									updateData.setCarSpringPrice(newSpringPrice);
									updateData.setTotalSpringRentDate(map.get("totalSpringRentDate"));
								} else if (rentAmtType == 1 || (rentAmtType == 2 && "0".equals(rentAmtDataList.get(i).getUse_special_price_flag()))) {
                                    logger.info("第 {} 次：===》带周末价",i);
									map = this.getRentAmtWithWeekend(holidayList, Double.parseDouble(newDayPrice),Double.parseDouble(newWeekendPrice), Double.parseDouble(newHolidayPrice), oldRevertTime, newRevertTime);  //延长那段的租期为：上一条的结束时间(oldRevertTime) — 这一条的结束时间(newRevertTime)
									updateData.setCarWeekendPrice(newWeekendPrice);
									updateData.setTotalWeekendRentDate(map.get("totalWeekendRentDate"));
								} else if (rentAmtType == 0) {
                                    logger.info("第 {} 次：===》不带周末价",i);
									map = this.getRentAmt(holidayList, Double.parseDouble(newDayPrice), Double.parseDouble(newHolidayPrice), oldRevertTime, newRevertTime);  //延长那段的租期为：上一条的结束时间(oldRevertTime) — 这一条的结束时间(newRevertTime)
								}
								updateData.setUseSpecialPriceFlag(rentAmtDataList.get(i).getUse_special_price_flag());
								updateData.setTotalH(map.get("totalH"));
								updateData.setTotalDayRentDate(map.get("totalDayRentDate"));
								updateData.setTotalHolidayRentDate(map.get("totalHolidayRentDate"));
								updateData.setRentDate(map.get("rentDate"));
								updateData.setAvgDayPrice(map.get("avgDayPrice"));
								updateData.setAvgHourPrice(map.get("avgHourPrice"));
								updateData.setT(map.get("t"));
								updateData.setH(map.get("h"));
								int totalRentAmt = 0;       //统计总租金
								for (int j = 0; j < stageList.size(); j++) {
									totalRentAmt += Integer.parseInt(stageList.get(j).getRentAmt());
								}
								updateData.setRentAmt((totalRentAmt + Integer.parseInt(map.get("rentAmt"))) + "");
								updateData.setStageRentAmt(map.get("rentAmt"));
								updateData.setRentTime(ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(newRentTime))));
								updateData.setRevertTime(ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(newRevertTime))));
								updateData.setContinueTime(ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(oldRevertTime))));
								logger.info("updateData_ContinueTime:{}",updateData.getContinueTime());
//                            updateData.setPart((Integer.parseInt(updateDataList.get(updateDataList.size()-1).getPart())+1) + "");   //上一条updateData对象的path+1
								updateData.setPart(partIndex + "");   //上一条updateData对象的path+1
								updateData.setBatch(updateDataList.get(updateDataList.size() - 1).getBatch());     //设置第x阶段
								updateData.setTotalRentDate(ToolUtil.getRentDate(oldRentTime,newRevertTime) + "");
								updateData.setCarDayPrice(newDayPrice);
								updateData.setCarHolidayPrice(newHolidayPrice);
								updateData.setCarNo(rentAmtDataList.get(i).getCarNo());
								updateData.setType(rentAmtDataList.get(i).getType());
								updateData.setId(rentAmtDataList.get(i).getId());
								updateData.setUpdateFlag("1");
								updateData.setMsg("起租时间不变，还车时间延长");
								updateDataList.add(updateData);
								logger.info("updateData:{}",JSON.toJSON(updateData));
								logger.info("----------------------");

								RentAmtData rentAmtData = new RentAmtData();                                //阶段数据：开始时间，结束时间，平日价，节假日价
								rentAmtData.setRent_time(newRentTime);
								rentAmtData.setRevert_time(newRevertTime);
								rentAmtData.setDay_price(newDayPrice);
								rentAmtData.setWeekend_price(newWeekendPrice);
								rentAmtData.setHoliday_price(newHolidayPrice);
								rentAmtData.setSpring_price(newSpringPrice);
								rentAmtData.setUse_special_price_flag(newUseSpecialPriceFlag);
//								rentAmtData.setContinueTime(newRentTime);	//疑问
								rentAmtData.setContinueTime(oldRevertTime);
								rentAmtData.setRentAmt(map.get("rentAmt"));
//                            rentAmtData.setPart((Integer.parseInt(updateDataList.get(updateDataList.size()-1).getPart())+1) + "");
								rentAmtData.setPart(partIndex + "");
								rentAmtData.setCarNo(newCarNo);
								stageList.add(rentAmtData);

								partIndex += 1;
							} else {      //B↓
								int index = 0;      //记录新租期在第x段
								UpdateData updateData = new UpdateData();
								Map<String, String> map = new HashMap<String, String>();
								logger.info("stageList:{}",JSON.toJSON(stageList));
								logger.info("=====================");
								for (int j = 0; j < stageList.size(); j++) {
									if (Long.parseLong(newRevertTime) < Long.parseLong(stageList.get(j).getRevert_time())) {
										index = j + 1;
										updateData.setContinueTime(ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(stageList.get(j).getContinueTime()))));
										logger.info("map_dayP:{}",Double.parseDouble(stageList.get(j).getDay_price()));
										logger.info("map_holidayP:{}",Double.parseDouble(stageList.get(j).getHoliday_price()));
										logger.info("map_rentTime:{}",stageList.get(j).getContinueTime());
										logger.info("map_revertTime:{}",newRevertTime);
										if (rentAmtType == 2 && "1".equals(stageList.get(j).getUse_special_price_flag())) {
                                            logger.info("第 {} 次：===》使用特供价",i);
											map = this.getRentAmtWithSpecialPrice(springHolidayList, holidayList, Double.parseDouble(stageList.get(j).getDay_price()), Double.parseDouble(stageList.get(j).getWeekend_price()), Double.parseDouble(stageList.get(j).getHoliday_price()), Double.parseDouble(stageList.get(j).getSpring_price()), stageList.get(j).getContinueTime(), newRevertTime); //缩短那段的租期为：所在那段的开始时间(continueTime) — 所在那段的结束时间(newRevertTime)
											updateData.setCarWeekendPrice(stageList.get(j).getWeekend_price());
											updateData.setTotalWeekendRentDate(map.get("totalWeekendRentDate"));
											updateData.setCarSpringPrice(stageList.get(j).getSpring_price());
											updateData.setTotalSpringRentDate(map.get("totalSpringRentDate"));

											updateData.setUseSpecialPriceFlag(stageList.get(j).getUse_special_price_flag());
										} else if (rentAmtType == 1 || (rentAmtType == 2 && "0".equals(stageList.get(j).getUse_special_price_flag()))) {
                                            logger.info("第 {} 次：===》带周末价",i);
											logger.info("map_weekendP:{}",Double.parseDouble(stageList.get(j).getWeekend_price()));
											map = this.getRentAmtWithWeekend(holidayList, Double.parseDouble(stageList.get(j).getDay_price()),Double.parseDouble(stageList.get(j).getWeekend_price()), Double.parseDouble(stageList.get(j).getHoliday_price()), stageList.get(j).getContinueTime(), newRevertTime);  //缩短那段的租期为：所在那段的开始时间(continueTime) — 所在那段的结束时间(newRevertTime)
											updateData.setCarWeekendPrice(stageList.get(j).getWeekend_price());
											updateData.setTotalWeekendRentDate(map.get("totalWeekendRentDate"));

											updateData.setUseSpecialPriceFlag(stageList.get(j).getUse_special_price_flag());
										} else if (rentAmtType == 0) {
                                            logger.info("第 {} 次：===》不带周末价",i);
											map = this.getRentAmt(holidayList, Double.parseDouble(stageList.get(j).getDay_price()), Double.parseDouble(stageList.get(j).getHoliday_price()), stageList.get(j).getContinueTime(), newRevertTime);  //缩短那段的租期为：所在那段的开始时间(continueTime) — 所在那段的结束时间(newRevertTime)

                                            updateData.setUseSpecialPriceFlag(stageList.get(j).getUse_special_price_flag());
										}
										updateData.setCarDayPrice(stageList.get(j).getDay_price());
										updateData.setCarHolidayPrice(stageList.get(j).getHoliday_price());
										break;
									}
								}
								logger.info("map:{}",JSON.toJSON(map));

								updateData.setTotalH(map.get("totalH"));
								updateData.setTotalDayRentDate(map.get("totalDayRentDate"));
								updateData.setTotalHolidayRentDate(map.get("totalHolidayRentDate"));
								updateData.setRentDate(map.get("rentDate"));
								updateData.setAvgDayPrice(map.get("avgDayPrice"));
								updateData.setAvgHourPrice(map.get("avgHourPrice"));
								updateData.setT(map.get("t"));
								updateData.setH(map.get("h"));
								int totalRentAmt = 0;       //统计总租金
								logger.info("totalRentAmt:{}",totalRentAmt);
								if (index != 1) {      //当回到第一段时，不能累加
//									for (int k = 0; k < index; k++) {		//疑问
									for (int k = 0; k < (index - 1); k++) {
										totalRentAmt += Integer.parseInt(stageList.get(k).getRentAmt());
										logger.info("增加项：{}",Integer.parseInt(stageList.get(k).getRentAmt()));
										logger.info("totalRentAmt:{}",totalRentAmt);
									}
								}
								logger.info("-totalRentAmt-:{}",totalRentAmt);
								updateData.setRentAmt((totalRentAmt + Integer.parseInt(map.get("rentAmt"))) + "");
								updateData.setStageRentAmt(map.get("rentAmt"));
								updateData.setRentTime(ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(newRentTime))));
								updateData.setRevertTime(ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(newRevertTime))));
//								updateData.setContinueTime(ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(newRentTime))));
								updateData.setPart(index + "");   //租期缩短所在那段
								updateData.setBatch(updateDataList.get(updateDataList.size() - 1).getBatch());     //设置第x阶段
								updateData.setTotalRentDate(ToolUtil.getRentDate(oldRentTime,newRevertTime) + "");
								updateData.setCarNo(rentAmtDataList.get(i).getCarNo());
								updateData.setType(rentAmtDataList.get(i).getType());
								updateData.setId(rentAmtDataList.get(i).getId());
								updateData.setUpdateFlag("1");
								updateData.setMsg("起租时间不变，还车时间提前");
								updateDataList.add(updateData);
								logger.info("updateData:{}",JSON.toJSON(updateData));

//                            RentAmtData rentAmtData = new RentAmtData();                                //阶段数据：开始时间，结束时间，平日价，节假日价
////                            rentAmtData.setRent_time(newRentTime);
////                            rentAmtData.setRevert_time(newRevertTime);
////                            rentAmtData.setDay_price(newDayPrice);
////                            rentAmtData.setHoliday_price(newHolidayPrice);
////                            rentAmtData.setContinueTime(newRentTime);
////                            rentAmtData.setRentAmt(map.get("rentAmt"));
////                            stageList.add(rentAmtData);

								List<RentAmtData> newStageList = new ArrayList<RentAmtData>();
								if (index == 1) {       //当回到第一段时，重新记录第一段
									RentAmtData rentAmtData = new RentAmtData();                                //阶段数据：开始时间，结束时间，平日价，节假日价
									rentAmtData.setRent_time(newRentTime);
									rentAmtData.setRevert_time(newRevertTime);
									rentAmtData.setDay_price(newDayPrice);
									rentAmtData.setWeekend_price(newWeekendPrice);
									rentAmtData.setHoliday_price(newHolidayPrice);
									rentAmtData.setSpring_price(newSpringPrice);
									rentAmtData.setUse_special_price_flag(newUseSpecialPriceFlag);
									rentAmtData.setContinueTime(newRentTime);
									rentAmtData.setRentAmt(map.get("rentAmt"));
//                            rentAmtData.setPart((Integer.parseInt(updateDataList.get(updateDataList.size()-1).getPart())+1) + "");
									rentAmtData.setPart(partIndex + "");
									rentAmtData.setCarNo(newCarNo);
									newStageList.add(rentAmtData);
								} else {
									for (int j = 0; j < index; j++) {
										newStageList.add(stageList.get(j));
									}
								}

								stageList = newStageList;
								logger.info("stageList:{}",JSON.toJSONString(stageList));

//                            partIndex += 1;
							}
						} else {                                          //B不变
							logger.info("第{}条数据租期没变化，不计算",(i + 1));
							UpdateData updateData = new UpdateData();
							updateData.setType(rentAmtDataList.get(i).getType());
							updateData.setId(rentAmtDataList.get(i).getId());
							updateData.setUseSpecialPriceFlag(rentAmtDataList.get(i).getUse_special_price_flag());
							logger.info("租期不变是rentAmtDataList：{}",JSON.toJSON(rentAmtDataList));

//							Map<String, String> map = this.getRentAmt(holidayList, Double.parseDouble(rentAmtDataList.get(i).getDay_price()), Double.parseDouble(rentAmtDataList.get(i).getHoliday_price()), rentAmtDataList.get(i).getRent_time(), rentAmtDataList.get(i).getRevert_time());
//							updateData.setRentAmt(map.get("rentAmt"));

							String rentAmt = null;
							for (int j = (updateDataList.size() - 1); j >= 0; j--) {
								if (updateDataList.get(j).getUpdateFlag().equals("1")) {
									rentAmt = updateDataList.get(j).getRentAmt();
									break;
								}
							}

							updateData.setRentAmt(rentAmt);
							updateData.setUpdateFlag("0");
							updateData.setMsg("租期没发生变化，租金保持不变");
							updateDataList.add(updateData);
						}
					} else {                                      //A变
						UpdateData updateData = new UpdateData();
						Map<String, String> map = null;
						if (rentAmtType == 2 && "1".equals(rentAmtDataList.get(i).getUse_special_price_flag())) {
                            logger.info("第 {} 次：===》使用特供价",i);
							map = this.getRentAmtWithSpecialPrice(springHolidayList, holidayList, Double.parseDouble(newDayPrice), Double.parseDouble(newWeekendPrice), Double.parseDouble(newHolidayPrice), Double.parseDouble(newSpringPrice), newRentTime, newRevertTime);
							updateData.setCarWeekendPrice(newWeekendPrice);
							updateData.setTotalWeekendRentDate(map.get("totalWeekendRentDate"));
							updateData.setCarSpringPrice(newSpringPrice);
							updateData.setTotalSpringRentDate(map.get("totalSpringRentDate"));
						} else if (rentAmtType == 1 || (rentAmtType == 2 && "0".equals(rentAmtDataList.get(i).getUse_special_price_flag()))) {
                            logger.info("第 {} 次：===》带周末价",i);
							map = this.getRentAmtWithWeekend(holidayList, Double.parseDouble(newDayPrice),Double.parseDouble(newWeekendPrice), Double.parseDouble(newHolidayPrice), newRentTime, newRevertTime);
							updateData.setCarWeekendPrice(newWeekendPrice);
							updateData.setTotalWeekendRentDate(map.get("totalWeekendRentDate"));
						} else if (rentAmtType == 0) {
                            logger.info("第 {} 次：===》不带周末价",i);
							map = this.getRentAmt(holidayList, Double.parseDouble(newDayPrice), Double.parseDouble(newHolidayPrice), newRentTime, newRevertTime);
						}
						updateData.setUseSpecialPriceFlag(rentAmtDataList.get(i).getUse_special_price_flag());
						updateData.setTotalH(map.get("totalH"));
						updateData.setTotalDayRentDate(map.get("totalDayRentDate"));
						updateData.setTotalHolidayRentDate(map.get("totalHolidayRentDate"));
						updateData.setRentDate(map.get("rentDate"));
						updateData.setAvgDayPrice(map.get("avgDayPrice"));
						updateData.setAvgHourPrice(map.get("avgHourPrice"));
						updateData.setT(map.get("t"));
						updateData.setH(map.get("h"));
						updateData.setRentAmt(map.get("rentAmt"));
						updateData.setStageRentAmt(map.get("rentAmt"));
						updateData.setRentTime(ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(newRentTime))));
						updateData.setRevertTime(ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(newRevertTime))));
						updateData.setContinueTime(ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(newRentTime))));
						updateData.setPart("1");
						logger.info("batch:{}",(stageList.size() + 1));
						logger.info("stageList:{}",JSON.toJSONString(stageList));
//                    updateData.setBatch((stageList.size()+1) + "");     //设置第x阶段
						batchIndex += 1;
						updateData.setBatch(batchIndex + "");     //设置第x阶段
						updateData.setTotalRentDate(ToolUtil.getRentDate(newRentTime,newRevertTime) + "");
						updateData.setCarDayPrice(newDayPrice);
						updateData.setCarHolidayPrice(newHolidayPrice);
						updateData.setCarNo(rentAmtDataList.get(i).getCarNo());
						updateData.setType(rentAmtDataList.get(i).getType());
						updateData.setId(rentAmtDataList.get(i).getId());
						updateData.setUpdateFlag("1");
						updateData.setMsg("起租时间发生变化，租金轴重新计算");
						updateDataList.add(updateData);

						RentAmtData rentAmtData = new RentAmtData();                                //阶段数据：开始时间，结束时间，平日价，节假日价
						rentAmtData.setRent_time(newRentTime);
						rentAmtData.setRevert_time(newRevertTime);
						rentAmtData.setDay_price(newDayPrice);
						rentAmtData.setWeekend_price(newWeekendPrice);
						rentAmtData.setHoliday_price(newHolidayPrice);
						rentAmtData.setSpring_price(newSpringPrice);
						rentAmtData.setUse_special_price_flag(newUseSpecialPriceFlag);
						rentAmtData.setContinueTime(newRentTime);
						rentAmtData.setRentAmt(map.get("rentAmt"));
						rentAmtData.setPart("1");
						rentAmtData.setCarNo(newCarNo);
						stageList.clear();
						stageList.add(rentAmtData);

						partIndex = 2;
					}
				} else {         //换车
					logger.info("换车：newDayPrice = {}",newDayPrice);
					logger.info("换车：newHolidayPrice = {}",newHolidayPrice);
					logger.info("换车：newRentTime = {}",newRentTime);
					logger.info("换车：newRevertTime = {}",newRevertTime);
					UpdateData updateData = new UpdateData();
					Map<String, String> map = null;
					if (rentAmtType == 2 && "1".equals(rentAmtDataList.get(i).getUse_special_price_flag())) {
                        logger.info("第 {} 次：===》使用特供价",i);
						map = this.getRentAmtWithSpecialPrice(springHolidayList, holidayList, Double.parseDouble(newDayPrice),Double.parseDouble(newWeekendPrice), Double.parseDouble(newHolidayPrice), Double.parseDouble(newSpringPrice), newRentTime, newRevertTime);
						updateData.setCarWeekendPrice(newWeekendPrice);
						updateData.setTotalWeekendRentDate(map.get("totalWeekendRentDate"));
						updateData.setCarSpringPrice(newSpringPrice);
						updateData.setTotalSpringRentDate(map.get("totalSpringRentDate"));
					} else if (rentAmtType == 1 || (rentAmtType == 2 && "0".equals(rentAmtDataList.get(i).getUse_special_price_flag()))) {
                        logger.info("第 {} 次：===》带周末价",i);
						map = this.getRentAmtWithWeekend(holidayList, Double.parseDouble(newDayPrice),Double.parseDouble(newWeekendPrice), Double.parseDouble(newHolidayPrice), newRentTime, newRevertTime);
						updateData.setCarWeekendPrice(newWeekendPrice);
						updateData.setTotalWeekendRentDate(map.get("totalWeekendRentDate"));
					} else if (rentAmtType == 0) {
                        logger.info("第 {} 次：===》不带周末价",i);
						map = this.getRentAmt(holidayList, Double.parseDouble(newDayPrice), Double.parseDouble(newHolidayPrice), newRentTime, newRevertTime);
					}
					logger.info("换车后租期：{}",map.get("rentAmt"));
					updateData.setUseSpecialPriceFlag(rentAmtDataList.get(i).getUse_special_price_flag());
					updateData.setTotalH(map.get("totalH"));
					updateData.setTotalDayRentDate(map.get("totalDayRentDate"));
					updateData.setTotalHolidayRentDate(map.get("totalHolidayRentDate"));
					updateData.setRentDate(map.get("rentDate"));
					updateData.setAvgDayPrice(map.get("avgDayPrice"));
					updateData.setAvgHourPrice(map.get("avgHourPrice"));
					updateData.setT(map.get("t"));
					updateData.setH(map.get("h"));
					updateData.setRentAmt(map.get("rentAmt"));
					updateData.setStageRentAmt(map.get("rentAmt"));
					updateData.setRentTime(ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(newRentTime))));
					updateData.setRevertTime(ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(newRevertTime))));
					updateData.setContinueTime(ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(newRentTime))));
					updateData.setPart("1");
//                    logger.info("batch:" + (stageList.size()+1) + "");
//                    logger.info("stageList:" + JSON.toJSONString(stageList));
//                    updateData.setBatch((stageList.size()+1) + "");     //设置第x阶段
					batchIndex += 1;
					updateData.setBatch(batchIndex + "");     //设置第x阶段
					updateData.setTotalRentDate(ToolUtil.getRentDate(newRentTime,newRevertTime) + "");
					updateData.setCarDayPrice(newDayPrice);
					updateData.setCarHolidayPrice(newHolidayPrice);
					updateData.setCarNo(rentAmtDataList.get(i).getCarNo());
					updateData.setType(rentAmtDataList.get(i).getType());
					updateData.setId(rentAmtDataList.get(i).getId());
					updateData.setUpdateFlag("1");
					updateData.setMsg("换车（carNo从\"" + oldCarNo + "\"换成\"" + newCarNo + "\"），租金轴重新计算");
					updateDataList.add(updateData);

					RentAmtData rentAmtData = new RentAmtData();                                //阶段数据：开始时间，结束时间，平日价，节假日价
					rentAmtData.setRent_time(newRentTime);
					rentAmtData.setRevert_time(newRevertTime);
					rentAmtData.setDay_price(newDayPrice);
					rentAmtData.setWeekend_price(newWeekendPrice);
					rentAmtData.setHoliday_price(newHolidayPrice);
					rentAmtData.setSpring_price(newSpringPrice);
					rentAmtData.setUse_special_price_flag(newUseSpecialPriceFlag);
					rentAmtData.setContinueTime(newRentTime);
					rentAmtData.setRentAmt(map.get("rentAmt"));
					rentAmtData.setPart("1");
					rentAmtData.setCarNo(newCarNo);
					stageList.clear();
					stageList.add(rentAmtData);

					partIndex = 2;
				}


			}
		}

		return updateDataList;
	}


	/**
	 * 根据租期整体修改取车时间和还车时间，用于计算租期时间，快速结算订单用
	 * @param rentTime
	 * @param revertTime
	 * @param type  1:订单时间快结束（revertTime快结束）	2：订单时间刚开始（rentTime刚开始）  3：订单时间快结束（revertTime 1小时后结束）—"待还车" 4：订单时间快结束（revertTime 5小时后结束）—"用车中"
	 * @return
	 */
	public List<String> modifyRentTime(String rentTime, String revertTime, int type){
		List<String> timeList = new ArrayList<String>();
		long t = Long.parseLong(ToolUtil.getTimestamp(revertTime)) - Long.parseLong(ToolUtil.getTimestamp(rentTime));

		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		int minute = cal.get(Calendar.MINUTE);

		if(type == 1){
			if(minute < 30){
				cal.set(Calendar.MINUTE,29);
				revertTime = ToolUtil.getTime3(cal.getTime());

				Date rentDate = new Date();
				rentDate.setTime(cal.getTime().getTime() - t);
				rentTime = ToolUtil.getTime3(rentDate);
			}else{
				cal.set(Calendar.MINUTE,-1);
				cal.add(Calendar.HOUR,1);
				revertTime = ToolUtil.getTime3(cal.getTime());

				Date rentDate = new Date();
				rentDate.setTime(cal.getTime().getTime() - t);
				rentTime = ToolUtil.getTime3(rentDate);
			}
		}
		if(type == 2){
			if(minute < 30){
				cal.set(Calendar.MINUTE,-1);
				rentTime = ToolUtil.getTime3(cal.getTime());

				Date revertDate = new Date();
				revertDate.setTime(cal.getTime().getTime() + t);
				revertTime = ToolUtil.getTime3(revertDate);
			}else{
				cal.set(Calendar.MINUTE,29);
				rentTime = ToolUtil.getTime3(cal.getTime());

				Date revertDate = new Date();
				revertDate.setTime(cal.getTime().getTime() + t);
				revertTime = ToolUtil.getTime3(revertDate);
			}
		}
		if(type == 3){
			if(minute < 30){
				cal.set(Calendar.MINUTE,29);
				cal.add(Calendar.HOUR,1);		//1小时
				revertTime = ToolUtil.getTime3(cal.getTime());

				Date rentDate = new Date();
				rentDate.setTime(cal.getTime().getTime() - t);
				rentTime = ToolUtil.getTime3(rentDate);
			}else{
				cal.set(Calendar.MINUTE,-1);
				cal.add(Calendar.HOUR,2);
				revertTime = ToolUtil.getTime3(cal.getTime());

				Date rentDate = new Date();
				rentDate.setTime(cal.getTime().getTime() - t);
				rentTime = ToolUtil.getTime3(rentDate);
			}
		}
        if(type == 4){
            if(minute < 30){
                cal.set(Calendar.MINUTE,29);
                cal.add(Calendar.HOUR,5);		//5小时
                revertTime = ToolUtil.getTime3(cal.getTime());

                Date rentDate = new Date();
                rentDate.setTime(cal.getTime().getTime() - t);
                rentTime = ToolUtil.getTime3(rentDate);
                logger.info("rentTime：{}",rentTime);
                logger.info("revertTime：{}",revertTime);
                logger.info("t：{}",t/60000);
            }else{
                cal.set(Calendar.MINUTE,-1);
                cal.add(Calendar.HOUR,6);
                revertTime = ToolUtil.getTime3(cal.getTime());

                Date rentDate = new Date();
                rentDate.setTime(cal.getTime().getTime() - t);
                rentTime = ToolUtil.getTime3(rentDate);
                logger.info("rentTime：{}",rentTime);
                logger.info("revertTime：{}",revertTime);
                logger.info("t：{}",t/60000);
            }
        }
		logger.info("====修改订单时间完成====");
		logger.info("rentTime：{}",rentTime);
		logger.info("revertTime：{}",revertTime);
		timeList.add(rentTime);
		timeList.add(revertTime);

		return timeList;
	}


	/**
	 * 计算两点之间的"球面距离"、"展示距离"、"距离系数"，用于取还车费用计算
	 * @param ALon
	 * @param ALat
	 * @param BLon
	 * @param BLat
	 * @return
	 */
	public Map<String,String> getDistanceFromCar(String ALon, String ALat, String BLon, String BLat){
		Map<String,String> distanceMap = getDistance(ALon,ALat,BLon,BLat);

		double distance2 = Double.parseDouble(distanceMap.get("distance2"));	//展示距离
		Double distanceRate = 0.0;     //距离系数
		if(distance2 <= 15){
			distanceRate = 1.0;
		} else if(15 < distance2 && distance2 <= 25){
			distanceRate = 1.3;
		} else if(25 < distance2 && distance2 <= 33){
			distanceRate = 1.6;
		} else if(distance2 > 33){
			distanceRate = 2.0;
		}
		distanceMap.put("distanceRate","" + distanceRate);
		logger.info("距离系数 = {}",distanceRate);

		return distanceMap;
	}

	/**
	 * 根据2个坐标计算球面距离(distance1)和展示距离(distance2)
	 * @param aLon	A点经度
	 * @param aLat	A点维度
	 * @param bLon	B点经度
	 * @param bLat	B点维度
	 * @return
	 */
	public Map<String,String> getDistance(String aLon, String aLat, String bLon, String bLat) {
		Double distance1 = Double.parseDouble(transMapper.getDistance(aLon,aLat,bLon,bLat));      //球面距离
        Map<String,String> distanceMap = this.getShowDistance(distance1);

		distanceMap.put("distance1","" + ToolUtil.round(distance1,0.01));	//精确到小数点后2位的球面距离
		return distanceMap;
	}


	/**
	 * 根据"展示距离"计算"取还车距离系数"（新规则）
	 * @param ShowDistance
	 * @return
	 */
	public double getDistanceIndex(double ShowDistance){
		Double index = 0.0;     //距离系数
		if(ShowDistance <= 5){
			index = 1.0;
		} else if(5 < ShowDistance && ShowDistance <= 10){
			index = 1.0;
		} else if(10 < ShowDistance && ShowDistance <= 15){
			index = 1.1;
		} else if(15 < ShowDistance && ShowDistance <= 20){
			index = 1.1;
		} else if(20 < ShowDistance && ShowDistance <= 25){
			index = 1.2;
		} else if(25 < ShowDistance && ShowDistance <= 30){
			index = 1.6;
		} else if(30 < ShowDistance && ShowDistance <= 35){
			index = 1.8;
		} else if(35 < ShowDistance && ShowDistance <= 40){
			index = 2.1;
		} else if(40 < ShowDistance && ShowDistance <= 45){
			index = 2.4;
		} else if(45 < ShowDistance && ShowDistance <= 50){
			index = 2.7;
		} else if(50 < ShowDistance && ShowDistance <= 55){
			index = 3.0;
		} else if(55 < ShowDistance && ShowDistance <= 60){
			index = 3.3;
		} else if(60 < ShowDistance && ShowDistance <= 75){
			index = 3.8;
		} else if(75 < ShowDistance && ShowDistance <= 80){
			index = 4.3;
		} else if(ShowDistance > 80){
			index = 6.3;
		}
		logger.info("距离系数 = {}",index);

		return index;
	}

	/**
	 * 根据2个坐标计算球面距离(distance1)和展示距离(distance2)
	 * @param aLon	A点经度
	 * @param aLat	A点维度
	 * @param bLon	B点经度
	 * @param bLat	B点维度
	 * @return
	 */
	public Map<String,String> getDistanceNewRow(String aLon, String aLat, String bLon, String bLat, String distance) {
		Double distance1 = 0.0;  //球面距离
		if (distance != "") {
			distance1 = Double.parseDouble(distance);
		} else {
			distance1 = Double.parseDouble(transMapper.getDistance(aLon,aLat,bLon,bLat));
		}

        Map<String,String> distanceMap = this.getShowDistance(distance1);
//		double distanceRate = this.getDistanceIndex(distance2);	//取还车系数
        double distanceRate = this.getDistanceIndex(Double.parseDouble(distanceMap.get("distance2")));	//取还车系数
		distanceMap.put("distance1","" + ToolUtil.round(distance1,0.01));	//精确到小数点后2位的球面距离
		distanceMap.put("distanceRate","" + distanceRate);								//取还车系数
		return distanceMap;
	}


    /**
     * 通过球面距离计算展示距离
     * @param distance1：球面距离
     * @return
     */
	public Map<String,String> getShowDistance(double distance1) {
        Map<String,String> distanceMap = new HashMap<String, String>();

        double ballRatio = 0.0;
        double distance2 = 0.0;     //展示距离
        if(0 < distance1 && distance1 <=5){
            ballRatio = 2.0;
            distance2 = distance1 * ballRatio;
        } else if(5 < distance1 && distance1 <=10){
            ballRatio = 1.6;
            distance2 = distance1 * ballRatio;
        } else if(10 < distance1 && distance1 <=15){
            ballRatio = 1.35;
            distance2 = distance1 * ballRatio;
        } else if(15 < distance1 && distance1 <=20){
            ballRatio = 1.2;
            distance2 = distance1 * ballRatio;
        } else if(20 < distance1 && distance1 <=25){
            ballRatio = 1.2;
            distance2 = distance1 * ballRatio;
        } else if(25 < distance1 && distance1 <=30){
            ballRatio = 1.1;
            distance2 = distance1 * ballRatio;
        } else if(30 < distance1 && distance1 <=35){
            ballRatio = 1.05;
            distance2 = distance1 * ballRatio;
        } else if(35 < distance1 && distance1 <=40){
            ballRatio = 1.05;
            distance2 = distance1 * ballRatio;
        } else if(40 < distance1 && distance1 <=45){
//			ballRatio = 1.01;
            ballRatio = 1.0;
            distance2 = distance1 * ballRatio;
        } else if(45 < distance1 && distance1 <=50){
//			ballRatio = 0.96;
            ballRatio = 1.0;
            distance2 = distance1 * ballRatio;
        } else if(50 < distance1 && distance1 <=55){
//			ballRatio = 0.93;
            ballRatio = 1.0;
            distance2 = distance1 * ballRatio;
        } else if(55 < distance1 && distance1 <=60){
//			ballRatio = 0.9;
            ballRatio = 1.0;
            distance2 = distance1 * ballRatio;
        }  else if(distance1 > 60){
            ballRatio = 1.0;
            distance2 = distance1 * ballRatio;
        }
        distance2 = ToolUtil.round(distance2,0.01);

        distanceMap.put("distance2","" + ToolUtil.round(distance2,0.01));	//精确到小数点后2位的展示距离
        distanceMap.put("ballRatio","" + ballRatio);									//球面距离估算系数

        return distanceMap;
    }


	/**
	 * 订单状态流转
	 * @param trans
	 * @param pathIP
	 * @param stepFlag	订单流程标识
	 * @param timeType	租期时间类型标识
	 * @return
	 */
	public Result setStatusFlow(Trans trans,PathIP pathIP,int stepFlag,int timeType){
		Result result = new Result();
		String waitTime = "";
		logger.info("================================");
		logger.info("orderNo：{}",trans.getOrder_no());

		//车主接单：21 —> 4
		if(trans.getStatus() == 21){
			String url = pathIP.getServerIP() + "trans/v7/reqConfirm";
			String ownerPhone = trans.getOwner_phone();
			Member ownerMember = memberMapper.selectMemberInfoByMobile(ownerPhone).get(0);

			Map<String,Object> responseBodyMap = this.reqConfirm(trans,ownerMember,url);    //车主同意接单
//			logger.info(JSON.toJSONString(responseBodyMap));
			if(!"000000".equals(responseBodyMap.get("resCode"))){
				//车主接单失败
				result.setStatus(0);
				result.setMsg("success");
				result.setData("<span class='sign_span' style='color:red;'>第一步：车主接单失败，请确认订单数据</span><br><br>" + responseBodyMap.toString());
				return result;
			}

			logger.info("-----车主接单成功-----");
			logger.info("========================");
			trans = transMapper.selectTransByorderNo(trans.getOrder_no());
		}

		//取车：3,4 —> 12 时间
		if(stepFlag >= 1){
			//source=401或400，是携程订单，可以在status=3时租客取车
			if(trans.getStatus() == 4 || (trans.getStatus() == 3 && trans.getSource() == 400) || (trans.getStatus() == 3 && trans.getSource() == 401) || (trans.getStatus() == 3 && trans.getSource() == 3)){
				String rentTime = trans.getRent_time();
				String revertTime = trans.getRevert_time();
				logger.info("timeType：{}",timeType);
//				List<String> list = this.modifyRentTime(rentTime,revertTime,timeType);   //修改订单rentTime和reverTime时间
				List<String> list = this.modifyRentTime(rentTime,revertTime,4);   //修改订单rentTime和reverTime时间

				transMapper.updateTrans(list.get(0),list.get(1),trans.getOrder_no());
				logger.info("修改订单租期时间成功");
				long startTime = (new Date()).getTime();
				logger.info("----开始时间----：{}",ToolUtil.getTime1(startTime));

				if(setTimeTask()){     //设置定时任务时间 + 等待定时任务执行
					logger.info("定时任务执行成功");
				}else{
					logger.info("定时任务执行失败");
				}

				long endTime = (new Date()).getTime();
				logger.info("----结束时间----：{}",ToolUtil.getTime1(endTime));
				long time = ((endTime - startTime)/1000-2);		//定时任务执行时长
				logger.info("----耗时----：{}秒",time);
				waitTime = "<br><br>定时任务总共执行了\"" + time + "\"秒";

				logger.info("-----第一步：租客取车完成-----");
				logger.info("========================");
				trans = transMapper.selectTransByorderNo(trans.getOrder_no());

				if (trans.getStatus() == 3 || trans.getStatus() == 4) {
					result.setStatus(0);
					result.setMsg("success");
					result.setData("第一步：租客取车完成_用车中。订单状态：" + trans.getStatus() + "<br><br>测试环境问题，\"定时任务\"状态变更：【" + trans.getStatus() + " —> 12】可能会延迟1、2分钟，请稍候");
				} else {
					result.setStatus(0);
					result.setMsg("success");
					result.setData("第一步：租客取车完成_用车中。订单状态：" + trans.getStatus());
				}
			} else {
				logger.info("订单不满足取车条件");
			}
		}

		if (stepFlag >= 2) {
			if (trans.getStatus() == 12) {
				List<String> list = this.modifyRentTime(trans.getRent_time(),trans.getRevert_time(),3);   //修改订单rentTime和reverTime时间

				transMapper.updateTrans(list.get(0),list.get(1),trans.getOrder_no());
				logger.info("修改订单租期时间成功");
				trans = transMapper.selectTransByorderNo(trans.getOrder_no());

				result.setStatus(0);
				result.setMsg("success");
				result.setData("第二步：租客取车完成_待还车。订单状态：" + trans.getStatus());
			}
		}

		//还车：12 —> 13
		if(stepFlag >= 3){
			if(trans.getStatus() == 12){
				String url = pathIP.getServerIP() + "trans/v7/presentTime";		//老提前还车接口
				String memNo = trans.getOwner_no();
				String token = memberMapper.selectMemberInfoByMobile(trans.getOwner_phone()).get(0).getToken();
				logger.info("mem_no:{}",memNo);
				logger.info("token:{}",token);

				Map<String,Object> responseBodyMap = this.presentTime(trans.getOrder_no(),memNo,token,url); //提前还车

				if(!"000000".equals(responseBodyMap.get("resCode"))){
					result.setStatus(0);
					result.setMsg("success");
					result.setData("<span class='sign_span' style='color:red;'>第三步：车主提前还车失败，请确认订单数据</span><br><br>" + responseBodyMap.toString());
					return result;
				}

//				logger.info("接口返回结果：" + JSON.toJSONString(responseBodyMap));
				logger.info("-----第三步：车主提前还车完成-----");
				logger.info("========================");
				trans = transMapper.selectTransByorderNo(trans.getOrder_no());

				result.setStatus(0);
				result.setMsg("success");
				result.setData("第三步：车主提前还车完成。订单状态：" + trans.getStatus());
			}
		}

		//双方评价：//13 —> 18、19
		if(stepFlag >= 4){
			logger.info("status:{}",trans.getStatus());
			/*
			 * carComment 服务
			 * test1 :http://10.0.3.234:8018/
			 * test2 :http://10.0.3.211:8018/
			 * test3 :http://10.0.3.236:8018/
			 * test4 :http://10.0.3.237:8018/
			 * test5 :http://120.27.160.15:8018/
			 * */
			String url = pathIP.getCarCommentIP() + "/evaluationRecord/add"; // 直调评价服务URI
			logger.info("url:{}",url);
			if(trans.getStatus()==13){  //待双方评价
				String type = "2";
				Map<String,Object> responseBodyMap = this.evaluationRecordAdd(trans,type,url);

				if(!"000000".equals(responseBodyMap.get("resCode"))){
					result.setStatus(0);
					result.setMsg("success");
					result.setData("<span class='sign_span' style='color:red;'>第四步：车主评价失败，请确认订单数据</span><br><br>" + responseBodyMap.toString());
					return result;
				}

				logger.info("status=13：车主评价成功");
//				logger.info(JSON.toJSONString(responseBodyMap));
				logger.info("========================");
				trans = transMapper.selectTransByorderNo(trans.getOrder_no());

				result.setStatus(0);
				result.setMsg("success");
				result.setData("车主评价成功。订单状态：" + trans.getStatus());
			}

			if(trans.getStatus()==18){  //待车主评价
				String type = "2";
				Map<String,Object> responseBodyMap = this.evaluationRecordAdd(trans,type,url);

				if(!"000000".equals(responseBodyMap.get("resCode"))){
					result.setStatus(0);
					result.setMsg("success");
					result.setData("<span class='sign_span' style='color:red;'>第四步：车主评价失败，请确认订单数据</span><br><br>" + responseBodyMap.toString());
					return result;
				}

				logger.info("status=18：车主评价成功");
//				logger.info(JSON.toJSONString(responseBodyMap));
				logger.info("========================");
				trans = transMapper.selectTransByorderNo(trans.getOrder_no());

				result.setStatus(0);
				result.setMsg("success");
				result.setData("车主评价成功。订单状态：" + trans.getStatus());
			}

			if(trans.getStatus()==19){  //待租客评价
				String type = "1";
				Map<String,Object> responseBodyMap = this.evaluationRecordAdd(trans,type,url);

				if(!"000000".equals(responseBodyMap.get("resCode"))){
					result.setStatus(0);
					result.setMsg("success");
					result.setData("<span class='sign_span' style='color:red;'>第四步：租客评价失败，请确认订单数据</span><br><br>" + responseBodyMap.toString());
					return result;
				}

//				logger.info(JSON.toJSONString(responseBodyMap));
				logger.info("status=19：租客评价成功");
				logger.info("========================");
				trans = transMapper.selectTransByorderNo(trans.getOrder_no());

				result.setStatus(0);
				result.setMsg("success");
				result.setData("租客评价成功。订单状态：" + trans.getStatus());
			}

			//修改实际还车时间到5小时前
			if(trans.getStatus()==20 && trans.getSettle()==0){
				String realRevertTime = ToolUtil.addHour(trans.getReal_revert_time(),-5);
				trans.setReal_revert_time(realRevertTime);
				transMapper.updateRealRevertTime(trans);

				trans = transMapper.selectTransByorderNo(trans.getOrder_no());

				result.setStatus(0);
				result.setMsg("success");
				result.setData("第四步：双方评价完成。订单状态：" + trans.getStatus());
			}
		}

		if(!"".equals(waitTime)){		//拼接等待定时任务时间
//			result.setData("<span class='sign_span' style='color:red;'>" + result.getData() + waitTime + "</span>");
			result.setData(result.getData() + waitTime);
		}
//		else{
////			result.setData("<span class='sign_span' style='color:red;'>" + result.getData() + "</span>");
//			trans = transMapper.selectTransByorderNo(trans.getOrder_no());
//			logger.info("请检查下这笔订单是否满足操作条件");
//			result.setStatus(0);
//			result.setMsg("success");
//			result.setData("<span class='sign_span' style='color:red;'>订单状态：" + trans.getStatus() + "，请检查下这笔订单是否满足操作条件</span><br><br>" );
//		}

//		logger.info("result：" + JSON.toJSONString(result));
//		logger.info("data = " + result.getData());
		if(result.getData() == null){	//前端兼容
			trans = transMapper.selectTransByorderNo(trans.getOrder_no());
			logger.info("请检查下这笔订单是否满足操作条件");
			result.setStatus(0);
			result.setMsg("success");
			result.setData("<span class='sign_span' style='color:red;'>订单状态：" + trans.getStatus() + "、source：" + trans.getSource() + "，请检查下这笔订单是否满足操作条件</span><br><br>" );
		}
		return result;
	}



	/**
	 * 双方评价。type=1：租客评价  type=2：车主评价
	 * @param type
	 * @param url
	 * @return
	 */
	public Map<String,Object> evaluationRecordAdd(Trans trans, String type, String url){
		Map<String,String> headerMap = new HashMap<String, String>();   //请求头
		Map<String, Object> paraMap = new HashMap<String, Object>();    //请求参数

		if("1".equals(type)){   //租客评价
			String renterPhone = trans.getRenter_phone();
			String memNo = memberMapper.selectMemberInfoByMobile(renterPhone).get(0).getReg_no(); //获取租客memNo

			//headerMap.put("Atzuche-Token", renterMember.getToken());    //网关头
			headerMap.put("X-AUTH-ID", memNo); //直调评价服务头
			headerMap.put("Accept", "application/json;version=3.0;compress=false");
			headerMap.put("Accept-Encoding", "gzip, deflate");
			headerMap.put("Accept-Language", "zh-CN,en-US;q=0.8");
			headerMap.put("User-Agent", "Mozilla/5.0 (Linux; Android 7.1.2; vivo X9s Build/N2G47H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36/atzuche");
			headerMap.put("X-Requested-With", "com.Autoyol.auto");
			headerMap.put("Cookie", "Hm_lvt_940ac302dc9436169f5e98f17aca1589=1534498902; Hm_lpvt_940ac302dc9436169f5e98f17aca1589=1534557785");
			headerMap.put("Content-Type", "application/json;charset=UTF-8");

			Map<String, Object> evaluationMap1 = new HashMap<String, Object>();
			evaluationMap1.put("optionCode", "renter_jbck");
			evaluationMap1.put("score", "5");
			Map<String, Object> evaluationMap2 = new HashMap<String, Object>();
			evaluationMap2.put("optionCode", "renter_jstx");
			evaluationMap2.put("score", "5");
			Map<String, Object> evaluationMap3 = new HashMap<String, Object>();
			evaluationMap3.put("optionCode", "renter_ptfw");
			evaluationMap3.put("score", "5");
			List<Map<String, Object>> evaluationList = new ArrayList<Map<String, Object>>();
			evaluationList.add(evaluationMap1);
			evaluationList.add(evaluationMap2);
			evaluationList.add(evaluationMap3);

			List<Map<String, Object>> evaluationTagList = new ArrayList<Map<String, Object>>();

			paraMap.put("orderNo", trans.getOrder_no());
			paraMap.put("level", "1");
			paraMap.put("content", "来来来可口可乐了看看默默摸摸摸默默摸摸摸");
			paraMap.put("type", type);
			paraMap.put("evaluationOptionRecordReqVos", evaluationList);
			paraMap.put("evaluationTagRecordReqVos", evaluationTagList);
		}

		if("2".equals(type)){  //车主评价
			String ownerPhone = trans.getOwner_phone();
			String memNo = memberMapper.selectMemberInfoByMobile(ownerPhone).get(0).getReg_no();  //获取车主memNo

			//headerMap.put("Atzuche-Token", ownerMember.getToken());   //网关头
			headerMap.put("X-AUTH-ID", memNo); //直调评价服务头
			headerMap.put("Accept", "application/json;version=3.0;compress=false");
			headerMap.put("Accept-Encoding", "gzip");
			headerMap.put("Accept-Language", "zh-CN,en-US;q=0.8");
			headerMap.put("User-Agent", "Mozilla/5.0 (Linux; Android 7.1.2; vivo X9s Build/N2G47H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36/atzuche");
			headerMap.put("Cookie", "Hm_lvt_940ac302dc9436169f5e98f17aca1589=1533982658; Hm_lpvt_940ac302dc9436169f5e98f17aca1589=1533982658");
			headerMap.put("Content-Type", "application/json;charset=UTF-8");
			headerMap.put("X-Requested-With", "com.Autoyol.auto");

			Map<String, Object> evaluationMap = new HashMap<String, Object>();
			evaluationMap.put("optionCode", "owner_ptfw");
			evaluationMap.put("score", "5");
			List<Map<String, Object>> evaluationList = new ArrayList<Map<String, Object>>();
			evaluationList.add(evaluationMap);

			paraMap.put("orderNo", trans.getOrder_no());
			paraMap.put("level", "1");
			paraMap.put("content", "来来来可口可乐了看看噜啦噜啦嘞考虑考虑健健康康健健康康");
			paraMap.put("type", type);
			paraMap.put("evaluationOptionRecordReqVos", evaluationList);
		}

		HttpResponse httpResult = HttpUtils.post(headerMap, url, paraMap, false, false);
		return httpResult.getResponseBodyObject();
	}


	/**
	 * 车主提前还车
	 * @param orderNo
	 * @param memNo
	 * @param token
	 * @param url
	 * @return
	 */
	public Map<String,Object> presentTime(String orderNo, String memNo, String token, String url){
		Map<String,String> headerMap = new HashMap<String, String>();
		headerMap.put("Accept", "application/json;version=3.0;compress=false");
		headerMap.put("Content-Type","application/json");
		headerMap.put("User-Agent","AutoyolEs_web");

		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("orderNo", orderNo);
		paraMap.put("mem_no", memNo);
		paraMap.put("token", token);
		paraMap.put("publicToken", token);
		paraMap.put("OsVersion", "25");
		paraMap.put("requestId", "90ADF7D73FA11533980815889");
		paraMap.put("androidID", "1435bad4bc727e96");
		paraMap.put("mac", "90ADF7D73FA1");
		paraMap.put("OS", "ANDROID");
		paraMap.put("AppChannelId", "baidu");
		paraMap.put("appName", "atzucheApp");
		paraMap.put("publicCityCode", "021");
		paraMap.put("flag", "2");
		paraMap.put("IMEI", "866621038858854");
		paraMap.put("AndroidId", "1435bad4bc727e96");
		paraMap.put("AppVersion", "80");
		paraMap.put("PublicLongitude", "121.432784");
		paraMap.put("deviceName", "vivoX9s");
		paraMap.put("PublicLatitude", "31.195575");
		paraMap.put("ownerFlag", "1");

		HttpResponse httpResult = HttpUtils.post(headerMap, url, paraMap, false, false);
		return httpResult.getResponseBodyObject();
	}


	/**
	 * 车主同意接单
	 * @param trans
	 * @param member
	 * @param url
	 * @return
	 */
	public Map<String,Object> reqConfirm(Trans trans, Member member, String url){
		Map<String,String> headerMap = new HashMap<String, String>();
		headerMap.put("Accept", "application/json;version=3.0;compress=false");
		headerMap.put("Accept-Encoding", "gzip");
		headerMap.put("User-Agent", "Autoyol_80:Android_24|7C943861B04338B241055AA99E7BC967D301310100E4247E397914EA14");

		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("token", member.getToken());
		paraMap.put("publicToken", member.getToken());
		paraMap.put("mem_no", trans.getOwner_no());
		paraMap.put("orderNo", trans.getOrder_no());
		paraMap.put("OsVersion", "24");
		paraMap.put("confirm", "1");
		paraMap.put("requestId", "C40BCB8649861534564016140");
		paraMap.put("androidID", "6458a139085d34df");
		paraMap.put("mac", "C40BCB864986");
		paraMap.put("OS", "ANDROID");
		paraMap.put("AppChannelId", "xiaomi");
		paraMap.put("appName", "atzucheApp");
		paraMap.put("publicCityCode", "021");
		paraMap.put("IMEI", "864454031102472");
		paraMap.put("AndroidId", "6458a139085d34df");
		paraMap.put("AppVersion", "80");
		paraMap.put("PublicLongitude", "121.4328");
		paraMap.put("deviceName", "MI5s");
		paraMap.put("PublicLatitude", "31.195585");

		HttpResponse httpResult = HttpUtils.put(headerMap, url, paraMap, false, false);
		return httpResult.getResponseBodyObject();
	}


	/**
	 * 获取网络时间，延时2秒。设置定时任务并等待定时任务执行成功
	 * @return
	 */
	public boolean setTimeTask(){
        /*
        String webUrl1 = "http://www.bjtime.cn";//bjTime
        String webUrl2 = "http://www.baidu.com";//百度
        String webUrl3 = "http://www.taobao.com";//淘宝
        String webUrl4 = "http://www.ntsc.ac.cn";//中国科学院国家授时中心
        String webUrl5 = "http://www.360.cn";//360
        String webUrl6 = "http://www.beijing-time.org";//beijing-time
         */
		String webUrl = "http://www.baidu.com";//百度
		long timestamp = getWebsiteDatetime(webUrl);    //获取网络(百度)时间戳
		timestamp += 2000; // 加2秒

		otherFunctionMapper.updateTransTimedTask("" + timestamp);
		String nextTimestamp = otherFunctionMapper.selectTransNextFireTime();
		logger.info("设置定时任务执行时间:{}",ToolUtil.getTime1(Long.parseLong(nextTimestamp)));

		while (nextTimestamp.equals(""+timestamp)){
			nextTimestamp = otherFunctionMapper.selectTransNextFireTime();
//			logger.info();
//			logger.info("-------------------");
//			logger.info("设置的定时任务时间：" + timestamp);
//			logger.info("表里的定时任务时间：" + nextTimestamp);
//			logger.info("是否相等：" + nextTimestamp.equals(""+timestamp));
			try {
				Thread.sleep(1000);
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		nextTimestamp = otherFunctionMapper.selectTransNextFireTime();
		logger.info("执行完后定时任务执行时间:{}",ToolUtil.getTime1(Long.parseLong(nextTimestamp)));
		return true;
	}


	/**
	 * 获取指定网站的日期时间
	 * @param webUrl
	 * @return
	 */
	public static long getWebsiteDatetime(String webUrl){
		try {
			URL url = new URL(webUrl);// 取得资源对象
			URLConnection uc = url.openConnection();// 生成连接对象
			uc.connect();// 发出连接
			long ld = uc.getDate();// 读取网站日期时间
			return ld;
		} catch (MalformedURLException e) {
			logger.error("MalformedURLException：",e);
		} catch (IOException e) {
			logger.error("IOException：",e);
		}
		return 0;
	}


	/**
	 * 查询holiday_setting表获取节日list
	 * @param pathIP	环境
	 * @param type		0：所以节日（包括春节）  1：春节
	 * @return
	 */
	public List<String> getHolidayList(PathIP pathIP, int type){
		List<Map<String,Long>> holidaySettingList = new ArrayList<>();
		List<String> totalHolidayList = new ArrayList<>();

		if ("线上".equals(pathIP.getEnvironment())) {
			String environment = "onlineHoliday";		// 线上节日库
			SetDateSourceUtil.setDataSourceName(environment);

			if (type == 0) {
				holidaySettingList = holidayMapper.selectHolidaySettingList();
			} else if (type == 1) {
				holidaySettingList = holidayMapper.selectSpringHolidaySettingList();
			}

			SetDateSourceUtil.setDataSourceName(pathIP.getEnvironment());
		} else {
			if (type == 0) {
				holidaySettingList = holidayMapper.selectHolidaySettingList();
			} else if (type == 1) {
				holidaySettingList = holidayMapper.selectSpringHolidaySettingList();
			}
		}
		logger.info("holidaySettingList:{}",JSON.toJSON(holidaySettingList));

		if (holidaySettingList.size() == 0) {
			return totalHolidayList;
		} else {
			for (int i = 0; i < holidaySettingList.size(); i++) {
				List<String> holidayList = getBetweenDate(holidaySettingList.get(i).get("real_start_date"),holidaySettingList.get(i).get("real_end_date"));
				totalHolidayList.addAll(holidayList);
			}

			for(int i=0;i<totalHolidayList.size();i++){		//转换节假日表时间格式，待用。时间格式为yyyyMMddHHmmss
				totalHolidayList.set(i, totalHolidayList.get(i) + "000000");
			}
		}

		logger.info("----totalHolidayList:{}",JSON.toJSON(totalHolidayList));
		return totalHolidayList;
	}

	/**
	 * 获取两个日期字符串之间的日期集合
	 * @param startTime:String
	 * @param endTime:String
	 * @return list:yyyy-MM-dd
	 */
	public static List<String> getBetweenDate(Long startTime, Long endTime){

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		// 声明保存日期集合
		List<String> list = new ArrayList<String>();
		try {
			// 转化成日期类型
			Date startDate = sdf.parse("" + startTime);
			Date endDate = sdf.parse("" + endTime);

			//用Calendar 进行日期比较判断
			Calendar calendar = Calendar.getInstance();
			while (startDate.getTime()<=endDate.getTime()){
				// 把日期添加到集合
				list.add(sdf.format(startDate));
				// 设置日期
				calendar.setTime(startDate);
				//把日期增加一天
				calendar.add(Calendar.DATE, 1);
				// 获取增加后的日期
				startDate=calendar.getTime();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return list;
	}



}
