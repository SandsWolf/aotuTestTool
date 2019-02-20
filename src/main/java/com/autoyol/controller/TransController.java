package com.autoyol.controller;

import com.alibaba.fastjson.JSON;
import com.autoyol.dao.*;
import com.autoyol.entity.*;
import com.autoyol.http.HttpResponse;
import com.autoyol.http.HttpUtils;
import com.autoyol.service.TransService;
import com.autoyol.service.impl.TimeServiceImpl;
import com.autoyol.util.SetDateSourceUtil;
import com.autoyol.util.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

@Controller
@RequestMapping("/trans")
public class TransController {
	@Resource
	private TransService transService;
	//	@Resource
//	private TimeService timeService;
	@Resource
	private TransMapper transMapper;
	@Resource
	private MemberMapper memberMapper;
	@Resource
	private HolidayMapper holidayMapper;
	@Resource
	private TimeMapper timeMapper;
	@Resource
	private CarMapper carMapper;

	private static final Logger logger = LoggerFactory.getLogger(TransController.class);

	/**
	 *	校验时间轴
	 * @param environment
	 * @param carNo
	 * @param rentTime
	 * @param revertTime
	 * @param srvFlag		使用取还车开关（0：不用）
	 * @return
	 */
	@RequestMapping("/checktimeaxis")
	@ResponseBody
	public Result checkTimeAxis(String environment, String carNo, String rentTime, String revertTime, String srvFlag){
		logger.info("------>开始校验时间轴：environment={},carNo={},rentTime={},revertTime={},srvFlag={}",new String[]{environment,carNo,rentTime,revertTime,srvFlag});
		SetDateSourceUtil.setDataSourceName(environment);
		Result result = new Result();

		if (!ToolUtil.isTransTime(rentTime)) {
			result = ToolUtil.checkRentTime(rentTime);
			return result;
		}
		if (!ToolUtil.isTransTime(revertTime)) {
			result = ToolUtil.checkRevertTime(revertTime);
			return result;
		}

		TimeServiceImpl timeService = new TimeServiceImpl();
		Map<String,List<TimeAxis>> map = new HashMap<String, List<TimeAxis>>();		//将所有表的时间轴list和租期时间轴list存于map作为最终比较参数
		Map<String,Object> resultMap = new HashMap<String,Object>();		//最终结果map

		if("0".equals(srvFlag)){		//不使用取还车服务
			try {
				//租期对应时间轴
				List<TimeAxis> transTimeAxisList = timeService.getTransTimeAxisList(rentTime, revertTime);
				map.put("transTimeAxisList",transTimeAxisList);

				//'car_busy_time'表
				List<CarBusyTime> carBusyTimeList = timeMapper.selectCarBusyTimeList(carNo);
				List<TimeAxis> carBusyTimeTimeAxisList = timeService.getCarBusyTimeTimeAxisList(rentTime, revertTime, carBusyTimeList);
				map.put("\"car_busy_time\"表",carBusyTimeTimeAxisList);

				//'car_filter'表
				List<String> carFilterList = timeMapper.selectCarFilterList(carNo);		//原始car_filter_List
				List<TimeAxis> carFilterTimeAxisList = timeService.getCarFilterTimeAxisList(carFilterList);
				map.put("\"car_filter\"表",carFilterTimeAxisList);

				//'carOrderSetting'表
				List<TimeAxis> carOrderSettingTimeAxisList = timeService.getCarOrderSettingTimeAxisList(timeMapper,rentTime,revertTime,carNo);
				map.put("\"order_setting_activity\"表和\"car_order_setting\"表",carOrderSettingTimeAxisList);

				//'trans_filter'表
				List<TransFilter> transFilterList = timeMapper.selectTransFilterList(carNo);
				List<TimeAxis> transFilterTimeAxisList = timeService.getTransFilterTimeAxisList(transFilterList);
				map.put("\"trans_filter\"表",transFilterTimeAxisList);

				//"trans_reply"表数据
				Map<String,String> checkedTransReplyMap = timeService.checkTransReply(rentTime,revertTime,timeMapper.selectTansReplyList(carNo));
				//"trans"表的'rent_time_lock'数据
				Map<String,Object> checkedTransTimeLockMap = timeService.checkTransTimeLock(carNo,rentTime,revertTime,timeMapper.selectTansTimeLockList(carNo));

				resultMap = timeService.checkNoSrvTimeAxis(map,checkedTransReplyMap,checkedTransTimeLockMap);

				resultMap.put("rentData",("\"" + ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(rentTime))) + "\" ~ \"" + ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(revertTime))) + "\""));	//租期信息
				resultMap.put("rentDay",("" + ToolUtil.getRentDate(rentTime,revertTime)));		//租期天数
			} catch (Exception e){
				logger.error("校验时间轴异常：",e);
			}
		}else{			//使用取还车服务

		}

		result.setStatus(0);
		result.setMsg("success");
		result.setData(resultMap);

		logger.info("------>时间轴校验结束");
		return result;
	}

	/**
	 * 违章押金结算：订单实际还车时间改到18天前，然后调用违章结算接口
	 * @param environment
	 * @param order_no
	 * @return
	 */
	@RequestMapping("/depositamtsettle")
	@ResponseBody
	public Result depositAmtSettle(String environment, String order_no){
		Result result = new Result();
		if("线上".equals(environment)){
			result.setStatus(1);
			result.setMsg("success");
			result.setData("线上环境只能做查询操作");
			return result;
		}

		SetDateSourceUtil.setDataSourceName(environment);
		PathIP pathIP = ToolUtil.getIP(environment);

		result = transService.depositAmtSettle(pathIP,order_no);
		return result;
	}



	/**
	 * 租车押金结算
	 * @param environment
	 * @param order_no
	 * @return
	 */
	@RequestMapping("/settle")
	@ResponseBody
	public Result totalAmtSettle(String environment, String order_no){
		Result result = new Result();
		if("线上".equals(environment)){
			result.setStatus(1);
			result.setMsg("success");
			result.setData("线上环境只能做查询操作");
			return result;
		}

		SetDateSourceUtil.setDataSourceName(environment);
		PathIP pathIP = ToolUtil.getIP(environment);

		result = transService.totalAmtSettle(pathIP,order_no);
		return result;
	}



	/**
	 * 线下支付租车押金
	 * @param environment
	 * @param order_no
	 * @param trans_type
	 * @return
	 */
	@RequestMapping("/paytotalamt")
	@ResponseBody
	public Result payTotalAmt(String environment, String order_no, String trans_type){
		Result result = new Result();
		if("线上".equals(environment)){
			result.setStatus(1);
			result.setMsg("success");
			result.setData("线上环境只能做查询操作");
			return result;
		}

		SetDateSourceUtil.setDataSourceName(environment);
		PathIP pathIP = ToolUtil.getIP(environment);

		Trans paraTrans = transMapper.selectTransByorderNo(order_no);
		if(paraTrans == null){
			result.setStatus(0);
			result.setMsg("success");
			result.setData("\""+environment+"\"环境没有订单： \""+order_no+"\"");
			return result;
		}
		if(paraTrans.getStatus() != 21 && paraTrans.getStatus() != 2){
			result.setStatus(0);
			result.setMsg("success");
			result.setData("订单(" + order_no + ")状态：\"" + paraTrans.getStatus() + "\" ; 订单不满足支付'租车押金'条件 , status应是'2'或'21'");
			return result;
		}
		//当status=21时，使车主接单
		if(paraTrans.getStatus() == 21){
			Member member = memberMapper.selectMemberInfoByMobile(paraTrans.getOwner_phone()).get(0);

			Map<String,String> map = new HashMap<String, String>();
			map.put("Content-Type","application/json; charset=utf-8");
			map.put("connection", "Keep-Alive");
			map.put("User-Agent", "AutoyolEs_console");
			map.put("Accept", "application/json;version=3.0;compress=false");

			//车主的mem_no和token
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("mem_no", member.getReg_no());
			paraMap.put("orderNo", order_no);
			paraMap.put("token", member.getToken());
			paraMap.put("publicToken", member.getToken());
			paraMap.put("OsVersion", "25");
			paraMap.put("confirm", "1");
			paraMap.put("requestId", "90ADF7D73FA11516782668462");
			paraMap.put("androidID", "266c1e21acba91f7");
			paraMap.put("mac", "90ADF7D73FA1");
			paraMap.put("OS", "ANDROID");
			paraMap.put("AppChannelId", "testmarket");
			paraMap.put("appName", "atzucheApp");
			paraMap.put("publicCityCode", "021");
			paraMap.put("IMEI", "866621038858854");
			paraMap.put("AndroidId", "266c1e21acba91f7");
			paraMap.put("AppVersion", "71");
			paraMap.put("PublicLongitude", "121.432752");
			paraMap.put("deviceName", "vivoX9s");
			paraMap.put("PublicLatitude", "31.19551");

			String url = pathIP.getServerIP() + "trans/v7/reqConfirm";
			HttpResponse httpResult = HttpUtils.put(map, url, paraMap, false, false);

			if(!"success".equals(httpResult.getResponseBodyObject().get("resMsg"))){	//车主接单接口调用失败
				paraTrans = transMapper.selectTransByorderNo(order_no);
				result.setStatus(0);
				result.setMsg("success");
				result.setData("订单(" + order_no + ")状态：\"" + paraTrans.getStatus() + "\" ; 车主接单失败");
				return result;
			}
		}

		paraTrans = transMapper.selectTransByorderNo(order_no);
		//订单状态为等待支付租车押金
		if(paraTrans.getStatus() == 2){
			result = transService.payTotalAmtOffline(order_no, paraTrans, trans_type);
		}
		return result;
	}



	/**
	 * 线下支付违章押金
	 * @param environment
	 * @param order_no
	 * @param trans_type
	 * @return
	 */
	@RequestMapping("/paydepositamt")
	@ResponseBody
	public Result payDepositAmt(String environment, String order_no, String trans_type){
		Result result = new Result();
		if("线上".equals(environment)){
			result.setStatus(1);
			result.setMsg("success");
			result.setData("线上环境只能做查询操作");
			return result;
		}

		SetDateSourceUtil.setDataSourceName(environment);
		PathIP pathIP = ToolUtil.getIP(environment);

		Trans paraTrans = transMapper.selectTransByorderNo(order_no);
		if(paraTrans == null){
			result.setStatus(0);
			result.setMsg("success");
			result.setData("\""+environment+"\"环境没有订单： \""+order_no+"\"");
			return result;
		}
		if(paraTrans.getStatus() != 3){
			result.setStatus(0);
			result.setMsg("success");
			result.setData("订单(" + order_no + ")状态：\"" + paraTrans.getStatus() + "\" ; 订单不满足支付'违章押金'条件 , status应是'3'");
			return result;
		}

		//订单状态为等待支付租车押金
		if(paraTrans.getStatus() == 3){
			result = transService.payDepositAmtOffline(order_no, paraTrans, trans_type);
		}
		return result;
	}

	/**
	 * 校验订单租金
	 * @param environment
	 * @param order_no
	 * @return
	 */
	@RequestMapping("/checkrentAmt")
	@ResponseBody
	public Result checkRentAmt(String environment, String order_no){
		SetDateSourceUtil.setDataSourceName(environment);
//		SetDateSourceUtil.setDataSourceName("baseDB");		//自测用
		PathIP pathIP = ToolUtil.getIP(environment);
		Result result = new Result();
		Map<String,String> map = new HashMap<String, String>();

		RentAmtData rentAmtData = transMapper.selectDefaultRentAmtData(order_no);	//获取计算租金的默认参数，当"trans_log"表没关联数据时使用
//		List<String> holidayList = holidayMapper.selectHolidayList();					//节假日list
		List<String> holidayList = transService.getHolidayList(pathIP);					//节假日list
		Trans trans = transMapper.selectTransByorderNo(order_no);					//getTransData
		List<TransLog> transLoglist = transMapper.selectTransLogList(order_no);	//getTransList
		if(rentAmtData==null){
			result.setStatus(0);
			result.setMsg("success");
			result.setData("订单号：\""+order_no+"\"不存在或对应的车辆不存在，请检查数据是否正确");
			return result;
		}
		if(trans==null){
			result.setStatus(0);
			result.setMsg("success");
			result.setData("订单号：\""+order_no+"\"不存在，请检查数据是否正确");
			return result;
		}
		if(holidayList.size()==0){
			result.setStatus(0);
			result.setMsg("success");
			result.setData("\"holiday_setting\"表数据不存在，请检查数据是否正确");
			return result;
		}

//		for(int i=0;i<holidayList.size();i++){		//转换节假日表时间格式，待用。时间格式为yyyyMMddHHmmss
//			holidayList.set(i, holidayList.get(i) + "000000");
//		}
		logger.info("节假日list：{}" + holidayList.toString());

		if(transLoglist.size()==0){	//如没transLog数据则给默认值：trans表和car表联表查询的数据
			map = transService.getRentAmt(holidayList, Double.parseDouble(rentAmtData.getDay_price()), Double.parseDouble(rentAmtData.getHoliday_price()), rentAmtData.getRent_time(), rentAmtData.getRevert_time());
		}else{	//如transLog有数据则正常计算
			map = transService.checkRentAmt(holidayList, transLoglist);
		}


		String resultMsg = "订单开始时间：<span class='sign_span' style='color:red;'>"+ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(trans.getRent_time())))+"</span><br>订单结束时间：<span class='sign_span' style='color:red;'>"+ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(trans.getRevert_time())))+"</span><br>总小时数：<span class='sign_span' style='color:red;'>"+map.get("totalH")+"</span>小时<br>平日价所占租期：<span class='sign_span' style='color:red;'>"+map.get("totalDayRentDate")+"</span>小时<br>节假日价所占租期：<span class='sign_span' style='color:red;'>"+map.get("totalHolidayRentDate")+"</span>小时<br>租期：<span class='sign_span' style='color:red;'>"+map.get("rentDate")+"</span>天<br><br>订单日均价：<span class='sign_span' style='color:red;'>"+map.get("avgDayPrice")+"</span>元<br>订单时均价：<span class='sign_span' style='color:red;'>"+map.get("avgHourPrice")+"</span>元<br>租期天数：<span class='sign_span' style='color:red;'>"+map.get("t")+"</span>天<br>剩余小时数：<span class='sign_span' style='color:red;'>"+map.get("h")+"</span>小时<br>租金（<span class='sign_span' style='color:red;'>规则：日均价 * 租期天数 + 时均价 * 剩余小时数</span>）为：<span class='sign_span' style='color:red;'>"+map.get("rentAmt")+"</span>元<br><br>\"trans\"表中租金：<span class='sign_span' style='color:red;'>"+trans.getRent_amt()+"</span>元";
		if(trans.getRent_amt().equals(map.get("rentAmt"))){
			resultMsg += "<hr style=\"height:1px;border:none;border-top:1px dashed #0066CC;\" />订单租金校验结果：<span class='sign_span' style='color:green;'>PASS</span>";
		}else{
			resultMsg += "<hr style=\"height:1px;border:none;border-top:1px dashed #0066CC;\" />订单租金校验结果：<span class='sign_span' style='color:red;'>FAIL</span>";
		}

		result.setStatus(0);
		result.setMsg("success");
		result.setData(resultMsg);
		return result;
	}

	/**
	 * 计算租金
	 * @param environment
	 * @param startTime		订单开始时间
	 * @param endTime		订单结束时间
	 * @param dayPrice		车辆平日价
	 * @param holiydaPrice	车辆节假日价
	 * @return
	 */
	@RequestMapping("/computerentAmt")
	@ResponseBody
	public Result computeRentAmt (String environment, String startTime, String endTime, String dayPrice, String holiydaPrice){
		SetDateSourceUtil.setDataSourceName(environment);
//		SetDateSourceUtil.setDataSourceName("baseDB");		//自测用
		PathIP pathIP = ToolUtil.getIP(environment);
		Result result = new Result();

//		List<String> holidayList = holidayMapper.selectHolidayList();		//节假日list
		List<String> holidayList = transService.getHolidayList(pathIP);		//节假日list

		if(holidayList.size()==0){
			result.setStatus(0);
			result.setMsg("success");
			result.setData("\"holiday_setting\"表数据不存在，请检查数据是否正确");
			return result;
		}

//		for(int i=0;i<holidayList.size();i++){		//转换节假日表时间格式，待用。时间格式为yyyyMMddHHmmss
//			holidayList.set(i, holidayList.get(i) + "000000");
//		}
		logger.info("节假日list：{}" + holidayList.toString());
		Map<String,String> map = transService.getRentAmt(holidayList, Double.parseDouble(dayPrice), Double.parseDouble(holiydaPrice), startTime, endTime);

		String resultMsg = "订单开始时间：<span class='sign_span' style='color:red;'>" + ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(startTime))) + "</span><br>" +
				"订单结束时间：<span class='sign_span' style='color:red;'>" + ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(endTime))) + "</span><br>" +
				"总小时数：<span class='sign_span' style='color:red;'>" + map.get("totalH")+"</span>小时<br>" +
				"平日价所占租期：<span class='sign_span' style='color:red;'>" + map.get("totalDayRentDate") + "</span>小时<br>" +
				"节假日价所占租期：<span class='sign_span' style='color:red;'>" + map.get("totalHolidayRentDate") + "</span>小时<br>" +
				"租期：<span class='sign_span' style='color:red;'>" + map.get("rentDate") + "</span>天<br><br>" +
				"订单日均价：<span class='sign_span' style='color:red;'>" + map.get("avgDayPrice") + "</span>元<br>" +
				"订单时均价：<span class='sign_span' style='color:red;'>" + map.get("avgHourPrice") + "</span>元<br>" +
				"租期天数：<span class='sign_span' style='color:red;'>" + map.get("t") + "</span>天<br>" +
				"剩余小时数：<span class='sign_span' style='color:red;'>" + map.get("h") + "</span>小时<br>" +
				"租金（<span class='sign_span' style='color:red;'>规则：日均价 * 租期天数 + 时均价 * 剩余小时数</span>）为：<span class='sign_span' style='color:red;'>" + map.get("rentAmt") + "</span>元<br><br><br>";
		result.setStatus(0);
		result.setMsg("success");
		result.setData(resultMsg);
		return result;
	}


	@RequestMapping("/computerentAmtv2")
	@ResponseBody
	public Result computeRentAmtWithWeekend (String environment, String startTime, String endTime, String dayPrice, String weekendPrice, String holiydaPrice){
		SetDateSourceUtil.setDataSourceName(environment);
//		SetDateSourceUtil.setDataSourceName("baseDB");		//自测用
		PathIP pathIP = ToolUtil.getIP(environment);
		Result result = new Result();

//		List<String> holidayList = holidayMapper.selectHolidayList();		//节假日list
		List<String> holidayList = transService.getHolidayList(pathIP);		//节假日list

		if(holidayList.size()==0){
			result.setStatus(0);
			result.setMsg("success");
			result.setData("\"holiday_setting\"表数据不存在，请检查数据是否正确");
			return result;
		}

//		for(int i=0;i<holidayList.size();i++){		//转换节假日表时间格式，待用。时间格式为yyyyMMddHHmmss
//			holidayList.set(i, holidayList.get(i) + "000000");
//		}
		logger.info("节假日list：{}" + holidayList.toString());
		Map<String,String> map = transService.getRentAmtWithWeekend(holidayList, Double.parseDouble(dayPrice),Double.parseDouble(weekendPrice), Double.parseDouble(holiydaPrice), startTime, endTime);

		String resultMsg = "订单开始时间：<span class='sign_span' style='color:red;'>" + ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(startTime))) + "</span><br>" +
				"订单结束时间：<span class='sign_span' style='color:red;'>" + ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(endTime))) + "</span><br>" +
				"总小时数：<span class='sign_span' style='color:red;'>" + map.get("totalH")+"</span>小时<br>" +
				"平日价所占租期：<span class='sign_span' style='color:red;'>" + map.get("totalDayRentDate") + "</span>小时<br>" +
				"周末价所占租期：<span class='sign_span' style='color:red;'>" + map.get("totalWeekendRentDate") + "</span>小时<br>" +
				"节假日价所占租期：<span class='sign_span' style='color:red;'>" + map.get("totalHolidayRentDate") + "</span>小时<br>" +
				"租期：<span class='sign_span' style='color:red;'>" + map.get("rentDate") + "</span>天<br><br>" +
				"订单日均价：<span class='sign_span' style='color:red;'>" + map.get("avgDayPrice") + "</span>元<br>" +
				"订单时均价：<span class='sign_span' style='color:red;'>" + map.get("avgHourPrice") + "</span>元<br>" +
				"租期天数：<span class='sign_span' style='color:red;'>" + map.get("t") + "</span>天<br>" +
				"剩余小时数：<span class='sign_span' style='color:red;'>" + map.get("h") + "</span>小时<br>" +
				"租金（<span class='sign_span' style='color:red;'>规则：日均价 * 租期天数 + 时均价 * 剩余小时数</span>）为：<span class='sign_span' style='color:red;'>" + map.get("rentAmt") + "</span>元<br><br><br>";
		result.setStatus(0);
		result.setMsg("success");
		result.setData(resultMsg);
		return result;
	}


	/**
	 * 计算修改订单后的租车押金
	 * @param environment
	 * @return
	 */
	@RequestMapping("/updateTrans")
//	@RequestMapping(value = "/updateTrans", method = RequestMethod.POST,consumes = "application/json")
	@ResponseBody
	public Result updateTrans(String environment, String mapJson){
		SetDateSourceUtil.setDataSourceName(environment);
//		SetDateSourceUtil.setDataSourceName("baseDB");		//自测用
		PathIP pathIP = ToolUtil.getIP(environment);
		Result result = new Result();

		Map<String, Object> map = (Map)JSON.parseObject(mapJson);
		logger.info("map:{}" + JSON.toJSONString(map));

		int rentAmtType = Integer.parseInt((String) map.get("rentAmtType"));	// 计算租金类型：1-带周末价  0-不带周末价

		List<Map<String,String>> transList = new ArrayList<>();
		for (int i = 0; i < (map.size() - 1); i++) {
			int index = i + 1;
			Map<String, String> transData = (Map<String, String>)map.get("trans" + index);
			logger.info("transData:{}" + JSON.toJSONString(transData));

			if (!StringUtils.isEmpty(transData.get("rentTime")) && !StringUtils.isEmpty(transData.get("revertTime")) && !StringUtils.isEmpty(transData.get("dayPrice")) && !StringUtils.isEmpty(transData.get("holiydaPrice"))) {
//				if (transData.get("rentTime") != null & !StringUtils.isEmpty(transData.get("rentTime")) & !ToolUtil.isTransTime(transData.get("rentTime"))) {
//					logger.info("index：{}",index + ":3");
//					result = ToolUtil.checkRentTime(transData.get("rentTime"));
//					return result;
//				}
//				if (transData.get("revertTime") != null & !StringUtils.isEmpty(transData.get("revertTime")) & !ToolUtil.isTransTime(transData.get("revertTime"))) {
//					logger.info("index：{}",index + ":3");
//					result = ToolUtil.checkRentTime(transData.get("revertTime"));
//					return result;
//				}

				transList.add(transData);
			}
		}
		logger.info("transList:{}" + JSON.toJSONString(transList));

//		List<String> holidayList = holidayMapper.selectHolidayList();		//节假日list
		List<String> holidayList = transService.getHolidayList(pathIP);		//节假日list
		List<RentAmtData> rentAmtDataList = new ArrayList<RentAmtData>();

		if(holidayList.size()==0){
			result.setStatus(0);
			result.setMsg("success");
			result.setData("\"holiday_setting\"表数据不存在，请检查数据是否正确");
			return result;
		}

//		for(int i=0;i<holidayList.size();i++){		//转换节假日表时间格式，待用。时间格式为yyyyMMddHHmmss
//			holidayList.set(i, holidayList.get(i) + "000000");
//		}
		logger.info("节假日list：{}" + holidayList.toString());

		String carNo = "1";
		for (int i = 0; i <transList.size(); i++) {
			if(transList.get(i).get("rentTime") != "" && transList.get(i).get("revertTime") != "" && transList.get(i).get("dayPrice") != "" && transList.get(i).get("holiydaPrice") != ""){
				RentAmtData rentAmtData = new RentAmtData();
				rentAmtData.setRent_time(transList.get(i).get("rentTime"));
				rentAmtData.setRevert_time(transList.get(i).get("revertTime"));
				rentAmtData.setDay_price(transList.get(i).get("dayPrice"));
				rentAmtData.setWeekend_price(transList.get(i).get("weekendPrice"));
				rentAmtData.setHoliday_price(transList.get(i).get("holiydaPrice"));
				rentAmtData.setCarNo(carNo);
				rentAmtDataList.add(rentAmtData);
			}
		}
		logger.info("rentAmtDataList:{}",JSON.toJSON(rentAmtDataList));

		List<UpdateData> updateDataList = transService.updateTrans(holidayList,rentAmtDataList,rentAmtType);
		logger.info("updateDataList:{}",JSON.toJSON(updateDataList));

		result.setStatus(0);
		result.setMsg(updateDataList.size() + "");
		result.setData(updateDataList);

		return result;
	}


	/**
	 * 校验修改订单租金
	 * @param environment
	 * @param order_no
	 * @return
	 */
	@RequestMapping("/newCheckrentAmt")
	@ResponseBody
	public Result checkRentAmtNew(String environment, String order_no,String rentAmtType){
		/*
		833671607081 三段
		387240607081 三段换车
		384101907081 没有改变价格两次延后还车时间
		465105907081 修改价格后修改了取车时间
		700325907081 修改价格后延后还车时间
		439489907081 修改价格后提前还车时间
		545278907081 修改价格后先延后还车时间，在提前还车时间至原订单时间中
		370216907081 调度后跟换车牌，修改价格后延时还车在提前还车时间
		272381907081 管理后台延后还车时间后，修改价格，管理后台延后还车时间

		102852017081 1.延长，2.缩短，3.再延长  ①——>②——>①——>④
		202355017081 1.延长还车时间，2.延后取车时间，3.延长还车时间
		564489117081 1.延长还车时间，2.修改地址，3.缩短还车时间

		419431117081 穿插的订单

		419431117081  test2
		574739507081  线上
		 */
		SetDateSourceUtil.setDataSourceName(environment);
		PathIP pathIP = ToolUtil.getIP(environment);
		Result result = new Result();

		List<RentAmtData> rentAmtDataList = new ArrayList<RentAmtData>();       			//数据源List
		List<UpdateData> updateDataList = new ArrayList<UpdateData>();          			//修改订单计算后数据List
		Map<String,Object> map = new HashMap<String,Object>();
//		List<String> holidayList = holidayMapper.selectHolidayList();		//节假日list
		List<String> holidayList = transService.getHolidayList(pathIP);		//节假日list
		Trans trans = transMapper.selectTransByorderNo(order_no);					//getTransData
		if(trans == null){
			result.setStatus(0);
			result.setMsg("success");
			result.setData("订单号：\""+order_no+"\"不存在，请检查数据是否正确");
			return result;
		}
		if(holidayList.size() == 0){
			result.setStatus(0);
			result.setMsg("success");
			result.setData("\"holiday_setting\"表数据不存在，请检查数据是否正确");
			return result;
		}

//		for(int i=0;i<holidayList.size();i++){		//转换节假日表时间格式，待用。时间格式为yyyyMMddHHmmss
//			holidayList.set(i, holidayList.get(i) + "000000");
//		}
		logger.info("节假日list：{}" + holidayList.toString());

		List<TransModificationApplication> applicationList = transMapper.selectApplicationList(order_no);
		List<TransModificationConsole> consoleList = transMapper.selectConsoleList(order_no);

		for (int i = 0; i < applicationList.size(); i++) {
			RentAmtData rentAmtData  = new RentAmtData();
			rentAmtData.setDay_price(applicationList.get(i).getDay_unit_price());
			rentAmtData.setWeekend_price(applicationList.get(i).getWeekend_price());
			rentAmtData.setHoliday_price(applicationList.get(i).getHoliday_price());
			rentAmtData.setRent_time(applicationList.get(i).getRent_time());
			rentAmtData.setRevert_time(applicationList.get(i).getRevert_time());
			rentAmtData.setCarNo(applicationList.get(i).getCar_no());
			rentAmtData.setOperatorTime(Long.parseLong(ToolUtil.getTimestamp1(applicationList.get(i).getOperator_time())));
			rentAmtData.setType(1);
			rentAmtData.setId(applicationList.get(i).getId());
			rentAmtDataList.add(rentAmtData);
		}
		for (int i = 0; i < consoleList.size(); i++) {
			RentAmtData rentAmtData  = new RentAmtData();
			rentAmtData.setDay_price(consoleList.get(i).getDay_unit_price());
			rentAmtData.setWeekend_price(consoleList.get(i).getWeekend_price());
			rentAmtData.setHoliday_price(consoleList.get(i).getHoliday_price());
			rentAmtData.setRent_time(consoleList.get(i).getRent_time());
			rentAmtData.setRevert_time(consoleList.get(i).getRevert_time());
			rentAmtData.setCarNo(consoleList.get(i).getCar_no());
			rentAmtData.setOperatorTime(Long.parseLong(ToolUtil.getTimestamp1(consoleList.get(i).getOperator_time())));
			rentAmtData.setType(2);
			rentAmtData.setId(consoleList.get(i).getId());
			rentAmtDataList.add(rentAmtData);
		}

		if(rentAmtDataList.size() != 1){
			RentAmtData[] rentAmtDatas = rentAmtDataList.toArray(new RentAmtData[rentAmtDataList.size()]);

			RentAmtData temp = new RentAmtData();
			for (int i = 0; i < rentAmtDatas.length; i++) {
				for (int j = (i+1); j < rentAmtDatas.length; j++) {
					if(rentAmtDatas[i].getOperatorTime() > rentAmtDatas[j].getOperatorTime()){
						temp = rentAmtDatas[i];
						rentAmtDatas[i] = rentAmtDatas[j];
						rentAmtDatas[j] = temp;
					}
				}
			}
			rentAmtDataList = Arrays.asList(rentAmtDatas);
		}

		updateDataList = transService.updateTrans(holidayList,rentAmtDataList,Integer.parseInt(rentAmtType));

		logger.info("updateDataList：{}",JSON.toJSONString(updateDataList));

		map.put("rentAmt",trans.getRent_amt());
		map.put("updateRentAmt",updateDataList.get(updateDataList.size()-1).getRentAmt());
		map.put("updateDataList",updateDataList);

		result.setStatus(0);
		result.setMsg("success");
		result.setData(map);
		return result;
	}


	/**
	 * 计算车辆到定位地址的"球面距离"、"展示距离"、"距离系数"，用于取还车费用计算
	 * @param environment
	 * @param carNo
	 * @param coordinate
	 * @return
	 */
	@RequestMapping("/getDistance")
	@ResponseBody
	public Result getDistance(String environment, String carNo, String coordinate){
		SetDateSourceUtil.setDataSourceName(environment);
		Result result = new Result();
		String resultMsg = "";

		Car car = carMapper.selectCarInfo(carNo);
		if (car == null) {
			result.setStatus(0);
			result.setMsg("success");
			result.setData("<br><span class='sign_span' style='color:red;'>carNo="+carNo+"，车辆不存在</span>");
			return result;
		}

		try {
			String[] strings = coordinate.split(",");	//解析经纬度
			logger.info("strings[0]：{}",strings[0]);
			logger.info("strings[1]：{}",strings[1]);
			logger.info("carLon：{}",car.getGet_car_lon());
			logger.info("carLat：{}",car.getGet_car_lat());
			// 老系数
//			Map<String,String> distanceMap = transService.getDistanceFromCar(strings[0],strings[1],car.getGet_car_lon(),car.getGet_car_lat());
//			resultMsg += "</span><br>球面距离：<span class='sign_span' style='color:red;'>\""+distanceMap.get("distance1")+" km\"</span><br>";
//			resultMsg += "</span><br>展示距离：<span class='sign_span' style='color:red;'>\""+distanceMap.get("distance2")+" km\"</span><br>";
//			resultMsg += "</span><br>展示距离估算系数：<span class='sign_span' style='color:red;'>\""+distanceMap.get("ballRatio")+"\"</span><br>";
//			resultMsg += "</span><br>费用距离系数：<span class='sign_span' style='color:red;'>\""+distanceMap.get("distanceRate")+"\"</span><br>";

			// 新系数
			Map<String,String> distanceMap = transService.getDistanceNewRow(strings[0],strings[1],car.getGet_car_lon(),car.getGet_car_lat(),"");
			resultMsg += "</span><br>球面距离：<span class='sign_span' style='color:red;'>\""+distanceMap.get("distance1")+" km\"</span><br>";
			resultMsg += "</span><br>展示距离：<span class='sign_span' style='color:red;'>\""+distanceMap.get("distance2")+" km\"</span><br>";
			resultMsg += "</span><br>展示距离估算系数：<span class='sign_span' style='color:red;'>\""+distanceMap.get("ballRatio")+"\"</span><br>";
			resultMsg += "</span><br>费用距离系数：<span class='sign_span' style='color:red;'>\""+distanceMap.get("distanceRate")+"\"</span><br>";
		}catch (Exception e){
			result.setStatus(0);
			result.setMsg("success");
			result.setData("<br><span class='sign_span' style='color:red;'>carNo="+carNo+"，车辆不存在</span>");
			return result;
		}

		result.setStatus(0);
		result.setMsg("success");
		result.setData(resultMsg);
		return result;
	}


	/**
	 * 订单状态流转
	 * @param environment
	 * @param orderNo
	 * @param stepFlag	//1:取车  2:还车  3:双方评价
	 * @return
	 */
	@RequestMapping("/statusFlow")
	@ResponseBody
	public Result setStatusFlow(String environment, String orderNo, String stepFlag){
		Result result = new Result();
		if("线上".equals(environment)){
			result.setStatus(1);
			result.setMsg("success");
			result.setData("线上环境只能做查询操作");
			return result;
		}

		SetDateSourceUtil.setDataSourceName(environment);
		PathIP pathIP = ToolUtil.getIP(environment);

		Trans trans = transMapper.selectTransByorderNo(orderNo);
		if(trans == null){
			logger.info("订单号：{}不存在",orderNo);
			result.setStatus(0);
			result.setMsg("success");
			result.setData("<span class='sign_span' style='color:red;'>订单号不存在，请确认环境或订单号是否正确</span>");
			return result;
		}

		logger.info("开始订单状态流转");
		int step = Integer.parseInt(stepFlag);	//1:取车  2:还车  3:双方评价

		Integer status = trans.getStatus();
		logger.info("status = {}",status);
		Integer[] statusArr = {21,3,4,12,13,18,19,20};

		if(Arrays.asList(statusArr).contains(status)){
			result = transService.setStatusFlow(trans,pathIP,step,1);
		}else{
			result.setStatus(0);
			result.setMsg("success");
			result.setData("<span class='sign_span' style='color:red;'>订单：" + orderNo + "状态为：" + trans.getStatus() + "，不在状态可流转范围内</span>");
			//不在这些状态内
		}

		return result;
	}


}
