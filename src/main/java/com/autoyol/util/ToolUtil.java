package com.autoyol.util;

import com.autoyol.entity.Day;
import com.autoyol.entity.PathIP;
import com.autoyol.entity.Result;
import com.autoyol.entity.Trans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToolUtil {
	private static final Logger logger = LoggerFactory.getLogger(ToolUtil.class);

	/**
	 * List<Trans>转已结算List<Trans>
	 * @param list
	 * @return
	 */
	public static List<Trans> getSettleList(List<Trans> list){
		List<Trans> transList = new ArrayList<Trans>();
		for(Trans trans : list){
			if(trans.getSettle() == 1){
				transList.add(trans);
			}
		}
		return transList;
	}

	/**
	 * List<Trans>转List<orderNo>
	 * @param list
	 * @return
	 */
	public static List<String> getOrderNoList(List<Trans> list){
		List<String> orderNoList = new ArrayList<String>();
		for(Trans trans : list){
			orderNoList.add(trans.getOrder_no());
		}
		return orderNoList;
	}

	/**
	 * 时间戳转时间格式：yyyy-MM-dd HH:mm:ss
	 * @param timestamp
	 * @return
	 */
	public static String getTime1(long timestamp){
		Date date = new Date();
		date.setTime(timestamp);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	/**
	 * 时间戳转时间格式：yyyyMMddHHmmss
	 * @param timestamp
	 * @return
	 */
	public static String getTime2(long timestamp){
		Date date = new Date();
		date.setTime(timestamp);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(date);
	}

	/**
	 * date转时间格式：yyyyMMddHHmmss
	 * @return
	 */
	public static String getTime3(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.SECOND, 0);
		cal.add(Calendar.MINUTE, 1);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(cal.getTime());
	}

	/**
	 * 时间戳计算(分钟)
	 * 输入：时间戳的long   minute分钟的int
	 * 输出:时间戳的long
	 * @param timestamp
	 * @param minute
	 * @return
	 */
	public static String addMinute1(long timestamp, int minute){
		Date date = new Date();
		date.setTime(timestamp);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.SECOND,0);

		cal.add(cal.MINUTE,minute);
		return "" + cal.getTime().getTime();
	}

	/**
	 * 时间计算(分钟)
	 * 输入：yyyyMMddHHmmss格式的String   minute分钟的int
	 * 输出:yyyyMMddHHmmss格式的String
	 * @param minute
	 * @return
	 */
	public static String addMinute2(String time, int minute){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();

		try {
			cal.setTime(sdf.parse(time));
			cal.add(Calendar.MINUTE,minute);
		} catch (ParseException e) {
			logger.error("时间计算(分钟)异常：",e);
		}
		return sdf.format(cal.getTime());
	}

	/**
	 * 时间计算(小时)
	 * 输入：yyyyMMddHHmmss格式的String   hour小时的int
	 * 输出:yyyyMMddHHmmss格式的String
	 * @return
	 */
	public static String addHour(String date, int hour){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();

		try {
			cal.setTime(sdf.parse(date));
			cal.add(Calendar.HOUR,hour);
		} catch (ParseException e) {
			logger.error("时间计算(小时)异常：",e);
		}
		return sdf.format(cal.getTime());
	}

	/**
	 * 时间计算(天)
	 * 输入：yyyyMMddHHmmss格式的String   day天的int
	 * 输出:yyyyMMddHHmmss格式的String
	 * @return
	 */
	public static String addDay(String date, int day){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar cal = Calendar.getInstance();

		try {
			cal.setTime(sdf.parse(date));
			cal.add(Calendar.DAY_OF_MONTH,day);
		} catch (ParseException e) {
			logger.error("时间计算(天)异常：",e);
		}
		return sdf.format(cal.getTime());
	}

	/**
	 * 时间格式转时间戳
	 * 输入：yyyyMMddHHmmss格式的String
	 * 输出:时间戳的String
	 * @param time
	 * @return
	 */
	public static String getTimestamp(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		String timestamp = null;
		try {
			timestamp = "" + sdf.parse(time).getTime();
		} catch (ParseException e) {
			logger.error("时间格式转时间戳异常：",e);
		}
		return timestamp;
	}

	/**
	 * 时间格式转时间戳
	 * 输入：yyyy-MM-dd HH:mm:ss格式的String
	 * 输出:时间戳的String
	 * @param time
	 * @return
	 */
	public static String getTimestamp1(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String timestamp = null;
		try {
			timestamp = "" + sdf.parse(time).getTime();
		} catch (ParseException e) {
			logger.error("时间格式转时间戳异常：",e);
		}
		return timestamp;
	}


	/**
	 * 四舍五入算法：
	 * 	num：需要四舍五入的数
	 *  place：精确到几位（如  个位：1;十位：10;百位：100;十分位：0.1;百分位：0.01等）
	 * @param num
	 * @param place
	 * @return
	 */
	public static double round(double num,double place){
		double ratio = 1/place ;	//系数
		double x = num * (1/place ) - (int)(num * (1/place ));

		if(x >= 0.5d){
			num = (int)(num * ratio + 1)/ratio;
		}else{
			num = (int)(num * ratio)/ratio;
		}

		return num;
	}


	/**
	 * 租期计算(四舍五入、保留小数点后2位)
	 * @param getTime
	 * @param returnTime
	 * @return
	 */
	public static double getRentDate(String getTime, String returnTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		double day = 0;

		try {
			Date date1 = sdf.parse(getTime);
			Date date2 = sdf.parse(returnTime);

			long time1 = date1.getTime();
			long time2 = date2.getTime();

//			logger.info(time1);
//			logger.info(time2);

			long time = time2 - time1;
			long dl = time%(24*60*60*1000);	//不满一天
			long hl = dl%(60*60*1000);		//不满一小时

			day = (time-dl)/(24*60*60*1000d);			//天
			double hour = (dl-hl)/(60*60*1000d);		//小时
			double minute = hl/(60* 1000d);				//分钟

//			logger.info(day+"天");
//			logger.info(hour+"小时");
//			logger.info(minute+"分钟");

			if(minute!=0){
				hour += 1;
			}

			if(hour>=8){
				day += 1;
			}else{
				day += hour/8;
			}

//			logger.info("===========");
//			logger.info(day+"天");

			double x = day*100 - ((int)(day*100));
//			logger.info(day*100+"-"+((int)(day*100))+"="+x);
			if(x>=0.5){
				day = (((int)(day*100))+1)/100d;
			}

//			logger.info("===========");
//			logger.info(day+"天");
		} catch (Exception e) {
			logger.error("租期计算(四舍五入、保留小数点后2位)异常：",e);
		}
		return day;
	}

	/**
	 * 小数向上取整
	 * @param amt
	 * @return
	 */
	public static int ceil(double amt){
		int upAmt = 0;
		if (amt >= 0 ) {
			if(amt%1 != 0){
				upAmt = (int)((amt - amt%1) + 1);
			}else{
				upAmt = (int)amt;
			}
		} else {
			if(amt%1 != 0){
				upAmt = (int)((amt - amt%1) - 1);
			}else{
				upAmt = (int)amt;
			}
		}

		return upAmt;
	}

	/**
	 * 小数向下取整
	 * @param amt
	 * @return
	 */
	public static int floor(double amt){
		int downAmt = 0;
		if (amt >=0) {
			if(amt%1 != 0){
				downAmt = (int)(amt - amt%1);
			}else{
				downAmt = (int)amt;
			}
		} else {
			if(amt%1 != 0){
				downAmt = (int)(amt - amt%1 -1);
			}else{
				downAmt = (int)amt;
			}
		}

		return downAmt;
	}

	/**
	 * 计算当天剩余分钟数
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public double getMinute(String startTime, String endTime) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		long time = sdf.parse(endTime).getTime() - sdf.parse(startTime).getTime();
		return (time/(60*1000d));
	}

	/**
	 * 计算租期内那几天满足节假日
	 * @param startTime
	 * @param endTime
	 * @param holidayList
	 * @return
	 */
	public List<Day> getrentDayList(String startTime, String endTime, List<String> holidayList, List<String> springHolidayList){
		List<Day> rentDayList = new ArrayList<Day>();	//统计租期内日期（精确到天）list：startDay和endDay的'timeDate'字段有值（真实起租和还车时间）

		String startTimeDay = startTime.substring(0,8) + "000000";
		String endTimeDay = endTime.substring(0,8) + "000000";

		logger.info("startTimeDay:{}",startTimeDay);
		logger.info("endTimeDay:{}",endTimeDay);

		String startTimestamp = ToolUtil.getTimestamp(startTimeDay);
		String endTimestamp = ToolUtil.getTimestamp(endTimeDay);

		long daysTimestamp = Long.parseLong(endTimestamp) - Long.parseLong(startTimestamp);
		int days = (int) (daysTimestamp / (24 * 60 * 60 * 1000));	//订单总天数（精确到天），如果订单开始时间和结束时间在同一天则days=0
		logger.info("订单总天数：{}",days);

		String indexDay = startTimeDay;
		for(int i = 0;i <= days;i++){
			Day day = new Day();
			day.setDayDate(indexDay);
			rentDayList.add(day);
			indexDay = ToolUtil.addDay(indexDay, 1);
		}

		logger.info("租期内日期（精确到天）：{}",rentDayList.toString());

		for(int i = 0;i < rentDayList.size();i++){
			Day day = new Day();
			if(startTimeDay.equals(rentDayList.get(i).getDayDate())){	//设置租期起始day的起租时间
				day.setTimeDate(startTime);
			}
			if(endTimeDay.equals(rentDayList.get(i).getDayDate())){		//设置租期结束day的还车时间
				day.setTimeDate(endTime);
			}

//			if(holidayList.contains(rentDayList.get(i).getDayDate())){
//				day.setIs_special(1);
//				day.setDayDate(rentDayList.get(i).getDayDate());
//			}else{
//				day.setIs_special(0);
//				day.setDayDate(rentDayList.get(i).getDayDate());
//			}

//			if(holidayList.contains(rentDayList.get(i).getDayDate())){	// 节日
//				day.setIs_special(1);
//				day.setDayDate(rentDayList.get(i).getDayDate());
//			}else if(!holidayList.contains(rentDayList.get(i).getDayDate()) && isWeekend(rentDayList.get(i).getDayDate())){		// 周末
//				day.setIs_special(2);
//				day.setDayDate(rentDayList.get(i).getDayDate());
//			} else {	// 平日
//				day.setIs_special(0);
//				day.setDayDate(rentDayList.get(i).getDayDate());
//			}

			if (springHolidayList.contains(rentDayList.get(i).getDayDate())) { // 春节
				day.setIs_special(3);
				day.setDayDate(rentDayList.get(i).getDayDate());
			} else if (holidayList.contains(rentDayList.get(i).getDayDate())){	// 节日
				day.setIs_special(1);
				day.setDayDate(rentDayList.get(i).getDayDate());
			} else if (!holidayList.contains(rentDayList.get(i).getDayDate()) && isWeekend(rentDayList.get(i).getDayDate())){		// 周末
				day.setIs_special(2);
				day.setDayDate(rentDayList.get(i).getDayDate());
			} else {	// 平日
				day.setIs_special(0);
				day.setDayDate(rentDayList.get(i).getDayDate());
			}
			rentDayList.set(i, day);

			/*
			 * //调试 当开始时间和结束时间在同一天时，分别存开始时间和结束时间。如：
			 * [{'dayDate':20180405000000,'timeDate':20180405103000,'is_holiday':1},
			 *  {'dayDate':20180405000000,'timeDate':20180405213000,'is_holiday':1}]
			 */
			if(rentDayList.size() == 1){
				Day day1 = new Day();
				day1.setDayDate(day.getDayDate());
				day1.setIs_special(day.getIs_special());
				day1.setTimeDate(day.getTimeDate());
				day.setTimeDate(startTime);
				logger.info("day:{}",day);
				logger.info("day1:{}",day1);
				rentDayList = new ArrayList<Day>();
				rentDayList.add(day);
				rentDayList.add(day1);
			}
		}
		logger.info("rentDayList：{}",rentDayList.toString());

		return rentDayList;
	}


	/**
	 * 判断日期是否周末
	 * 时间格式：yyyyMMddHHmmss
	 * @param day
	 * @return
	 * @throws ParseException
	 */
	public static boolean isWeekend(String day){
		DateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = null;
		try {
			date = format1.parse(day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
			return true;
		} else{
			return false;
		}
	}


	/**
	 * 根据环境获取URL
	 * @param environment
	 * @return
	 */
	public static PathIP getIP(String environment){
		PathIP pathIP = new PathIP();
		if("test_1".equals(environment)){
			pathIP.setEnvironment(environment);
			pathIP.setServerIP("http://10.0.3.205:7064/");
			pathIP.setWebconsoleIP("http://10.0.3.234:9031");
			pathIP.setAutoTVServiceIP("http://10.0.3.205:7262/");
			pathIP.setTransIP("http://10.0.3.205:8086/");
			pathIP.setRedisIp("10.0.3.200");
			pathIP.setCarCommentIP("http://10.0.3.234:8018/");
			pathIP.setVirtualuserIP("http://10.0.3.234:1036/");
			pathIP.setInterFaceIP("http://10.0.3.205:998/");
		}
		if("test_2".equals(environment)){
			pathIP.setEnvironment(environment);
			pathIP.setServerIP("http://10.0.3.213:7064/");
			pathIP.setWebconsoleIP("http://10.0.3.235:9031/");
			pathIP.setAutoTVServiceIP("http://10.0.3.213:7262/");
			pathIP.setTransIP("http://10.0.3.211:8086/");
			pathIP.setRedisIp("10.0.3.211");
			pathIP.setCarCommentIP("http://10.0.3.211:8018/");
			pathIP.setVirtualuserIP("http://10.0.3.252:1036/");
			pathIP.setInterFaceIP("http://10.0.3.213:998/");

		}
		if("test_3".equals(environment)){
			pathIP.setEnvironment(environment);
			pathIP.setServerIP("http://10.0.3.207:7064/");
			pathIP.setWebconsoleIP("http://10.0.3.206:9031/");
			pathIP.setAutoTVServiceIP("http://10.0.3.207:7262/");
			pathIP.setTransIP("http://10.0.3.207:8086/");
			pathIP.setRedisIp("10.0.3.206");
			pathIP.setCarCommentIP("http://10.0.3.236:8018/");
			pathIP.setVirtualuserIP("http://10.0.3.253:1036/");
			pathIP.setInterFaceIP("http://10.0.3.207:998/");

		}
		if("test_4".equals(environment)){
			pathIP.setEnvironment(environment);
			pathIP.setServerIP("http://10.0.3.223:7064/");
			pathIP.setWebconsoleIP("http://10.0.3.237:9031/");
			pathIP.setAutoTVServiceIP("http://10.0.3.223:7262/");
			pathIP.setTransIP("http://10.0.3.224:8086/");
			pathIP.setRedisIp("10.0.3.224");
			pathIP.setCarCommentIP("http://10.0.3.237:8018/");
			pathIP.setVirtualuserIP("http://10.0.3.223:1036/");
			pathIP.setInterFaceIP("http://10.0.3.223:998/");

		}
		if("test_5".equals(environment)){
			pathIP.setEnvironment(environment);
			pathIP.setServerIP("http://114.55.235.185:7064/");
			pathIP.setWebconsoleIP("http://114.55.235.185:9031/");
			pathIP.setAutoTVServiceIP("http://120.27.160.15:28888/");
			pathIP.setTransIP("http://114.55.235.185:8086/");
			pathIP.setRedisIp("121.199.4.107");
			pathIP.setCarCommentIP("http://120.27.160.15:8018/");
			pathIP.setVirtualuserIP("http://121.199.4.107:1036/");
			pathIP.setInterFaceIP("http://114.55.235.185:998/");
		}
		if("test_6".equals(environment)){
			pathIP.setEnvironment(environment);
			pathIP.setServerIP("http://10.0.3.250:7064/");
			pathIP.setWebconsoleIP("http://10.0.3.249:9031/");
			pathIP.setAutoTVServiceIP("http://10.0.3.250:7262/");
			pathIP.setTransIP("");  // test6 没结算服务
			pathIP.setRedisIp("10.0.3.49");
			pathIP.setCarCommentIP("");
			pathIP.setVirtualuserIP("");
			pathIP.setInterFaceIP("http://10.0.3.250:998/");
		}
		if("test_9".equals(environment)){
			pathIP.setEnvironment(environment);
			pathIP.setServerIP("http://47.96.104.13:7064/");
			pathIP.setWebconsoleIP("http://47.96.104.13:9031/");
			pathIP.setAutoTVServiceIP("http://47.98.244.225:28888/");
			pathIP.setTransIP("http://47.96.104.13:8086/");
			pathIP.setRedisIp("47.96.104.13");
			pathIP.setCarCommentIP("");
			pathIP.setVirtualuserIP("");
			pathIP.setInterFaceIP("http://47.96.104.13:998/");

		}
		if("线上".equals(environment)){
			pathIP.setEnvironment(environment);
			pathIP.setServerIP("http://114.55.11.127:7064/");
//			pathIP.setWebconsoleIP("http://114.55.235.185:9031/");
//			pathIP.setAutoTVServiceIP("http://120.27.160.15:28888/");
//			pathIP.setTransIP("http://114.55.235.185:8086/");
//			pathIP.setRedisIp("121.199.4.107");
		}

		return pathIP;
	}


	/**
	 * 计算租期小时数(向上取整)
	 * @param getTime
	 * @param returnTime
	 * @return
	 */
	public static double getRentDateTrans(String getTime, String returnTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		double day = 0;
		double totalHour = 0;

		try {
			Date date1 = sdf.parse(getTime);
			Date date2 = sdf.parse(returnTime);

			long time1 = date1.getTime();
			long time2 = date2.getTime();

//			logger.info(time1);
//			logger.info(time2);

			long time = time2 - time1;
			long dl = time%(24*60*60*1000);	//不满一天
			long hl = dl%(60*60*1000);		//不满一小时

			day = (time-dl)/(24*60*60*1000d);			//天
			double hour = (dl-hl)/(60*60*1000d);		//小时
			double minute = hl/(60* 1000d);				//分钟

//			logger.info(day+"天");
//			logger.info(hour+"小时");
//			logger.info(minute+"分钟");

			if(minute!=0){
				hour += 1;
			}

			totalHour = day*24 + hour;

//			if(hour>=8){
//				day += 1;
//			}else{
//				day += hour/8;
//			}

//			logger.info("===========");
//			logger.info(day+"天");

//			double x = day*100 - ((int)(day*100));
//			logger.info(day*100+"-"+((int)(day*100))+"="+x);

//			logger.info("===========");
//			logger.info(day+"天");
		} catch (Exception e) {
			logger.error("计算租期小时数(向上取整)异常：",e);
		}
		return totalHour;
	}


	/**
	 * 校验输入的时间格式是否为：yyyyMMddHHmmss
	 * @param transTime
	 * @return
	 */
	public static boolean isTransTime(String transTime){
		// 订单租期时间正则表达式规则
		String regEx = "^(\\d{4})((0[1-9])|1[0-2])((0[1-9])|([1-2][0-9])|(3[0-1]))(([0-1][0-9])|(2[0-3]))(00|15|30|45)00";
		// 编译正则表达式
		Pattern pattern = Pattern.compile(regEx);
		// 忽略大小写的写法
		// Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(transTime);
		// 字符串是否与正则表达式相匹配
		return matcher.matches();
	}


	/**
	 * 校验输入的手机号格式是否为：11位数字，并以1开头
	 * @param mobile
	 * @return
	 */
	public static boolean isMobile (String mobile){
		// 手机号正则表达式规则
		String regEx = "^(1)\\d{10}";
		// 编译正则表达式
		Pattern pattern = Pattern.compile(regEx);
		// 忽略大小写的写法
		// Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(mobile);
		// 字符串是否与正则表达式相匹配
		return matcher.matches();
	}


	/**
	 * 校验订单开始时间格式是否正确
	 * @param rentTime
	 * @return
	 */
	public static Result checkRentTime(String rentTime){
		Result result = new Result();
		result.setStatus(1);
		result.setMsg("success");
		result.setData("开始时间（" + rentTime + "）格式错误，请确认后再试<br><br>例子：20070102203000");
		return result;
	}

	/**
	 * 校验订单结束时间格式是否正确
	 * @param revertTime
	 * @return
	 */
	public static Result checkRevertTime(String revertTime){
		Result result = new Result();
		result.setStatus(1);
		result.setMsg("success");
		result.setData("结束时间（" + revertTime + "）格式错误，请确认后再试<br><br>例子：20080226121500");
		return result;
	}


	/**
	 * 校验手机号格式是否正确
	 * @param mobile
	 * @return
	 */
	public static Result checkMobile(String mobile){
		Result result = new Result();
		result.setStatus(1);
		result.setMsg("success");
		result.setData("手机号（" + mobile + "）格式错误，格式为11位并且已【1】开头的数字，请确认后再试");
		return result;
	}





	public static void main(String[] args) {
		String date = ToolUtil.getTime3(new Date());
		logger.info(date);
		date = ToolUtil.addMinute2(date, 5);
		logger.info(date);


		String time = ToolUtil.getTimestamp(date);
		logger.info(time);

//		String date = ToolUtil.addMinute1(1513242000000L, Integer.parseInt("5"));
//		logger.info("date:" + date);

	}
}
