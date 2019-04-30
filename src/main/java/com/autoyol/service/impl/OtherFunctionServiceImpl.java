package com.autoyol.service.impl;

import com.autoyol.dao.OtherFunctionMapper;
import com.autoyol.entity.Car;
import com.autoyol.entity.PathIP;
import com.autoyol.entity.Result;
import com.autoyol.http.HttpResponse;
import com.autoyol.http.HttpUtils;
import com.autoyol.service.OtherFunctionService;
import com.autoyol.service.TransService;
import com.autoyol.util.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class OtherFunctionServiceImpl implements OtherFunctionService{
	@Resource
	private OtherFunctionMapper otherFunctionMapper;
	@Resource
	private TransService transService;

	private static final Logger logger = LoggerFactory.getLogger(OtherFunctionServiceImpl.class);

	/**
	 * 清除operation_text表缓存
	 */
	public Result clearMemory(PathIP pathIP) {
		Result result = new Result();
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("User-Agent","AutoyolEs_web");
		String url = pathIP.getServerIP() + "sysParam/loadOperationText";
		HttpResponse httpResult = HttpUtils.get(map,url);
		
		result.setStatus(0);
		result.setMsg("success");
		result.setData(httpResult.getResponseBodyObject().toString());
		
		return result;
	}
	
	/**
	 * 定时任务（订单状态）
	 * 1.设置XX分钟后（type=1）
	 * 2.设置定时任务时间（type=2）
	 */
	public Result setTransTimedTask(String time, String type) {
		Result result = new Result();
		
		if("0".equals(type)){
			String timestamp = otherFunctionMapper.selectTransNextFireTime();	//获取订单相关定时任务时间戳
			
			if(timestamp == null){
				result.setStatus(0);
				result.setMsg("success");
				result.setData("\"qrtz_triggers\"表\"TRIGGER_NAME\"字段为transStatManageJobTrigger的数据异常");
			}else{
				String calTime = ToolUtil.getTime1(Long.parseLong(timestamp));
				
				result.setStatus(0);
				result.setMsg("success");
				result.setData("下一个定时任务时间戳：" + timestamp + "<br><br>定时任务执行时间为：" + calTime);
			}
		}else if("1".equals(type)){
			String date = ToolUtil.getTime3(new Date());
			date = ToolUtil.addMinute2(date, Integer.parseInt(time));
 			String timestamp = ToolUtil.getTimestamp(date);
 			
 			try {
 				otherFunctionMapper.updateTransTimedTask(timestamp);	//修改订单相关定时任务时间戳
 				
 				timestamp = otherFunctionMapper.selectTransNextFireTime();	//获取订单相关定时任务时间戳
 				if(timestamp == null){
 					result.setStatus(0);
 					result.setMsg("success");
 					result.setData("\"qrtz_triggers\"表\"TRIGGER_NAME\"字段为transStatManageJobTrigger的数据异常");
 				}else{
 					String calTime = ToolUtil.getTime1(Long.parseLong(timestamp));
 					
 					result.setStatus(0);
 					result.setMsg("success");
 					result.setData("下一个定时任务时间戳：" + timestamp + "<br><br>定时任务执行时间为：" + calTime);
 				}
			} catch (Exception e) {
				logger.error("type = 1，定时任务（订单状态）异常：",e);
				result.setStatus(0);
				result.setMsg("success");
				result.setData("\"qrtz_triggers\"表没\"TRIGGER_NAME\"字段为transStatManageJobTrigger的数据");
			}
		}else if("2".equals(type)){
			String timestamp = ToolUtil.getTimestamp(time);
			
			try {
 				otherFunctionMapper.updateTransTimedTask(timestamp);	//修改订单相关定时任务时间戳
 				
 				timestamp = otherFunctionMapper.selectTransNextFireTime();	//获取订单相关定时任务时间戳
 				if(timestamp == null){
 					result.setStatus(0);
 					result.setMsg("success");
 					result.setData("\"qrtz_triggers\"表没\"TRIGGER_NAME\"字段为transStatManageJobTrigger的数据");
 				}else{
 					String calTime = ToolUtil.getTime1(Long.parseLong(timestamp));
 					
 					result.setStatus(0);
 					result.setMsg("success");
 					result.setData("下一个定时任务时间戳：" + timestamp + "<br><br>定时任务执行时间为：" + calTime);
 				}
			} catch (Exception e) {
				logger.error("type = 2，定时任务（订单状态）异常：",e);
				result.setStatus(0);
				result.setMsg("success");
				result.setData("\"qrtz_triggers\"表没\"TRIGGER_NAME\"字段为transStatManageJobTrigger的数据");
			}
		}
		
		return result;
	}
	
	/**
	 * 定时任务（订单状态）
	 * 1.设置XX分钟后（type=1）
	 * 2.设置定时任务时间（type=2）
	 */
	public Result setIllegalTimedTask(String time, String type) {
		Result result = new Result();
		
		if("0".equals(type)){
			String timestamp = otherFunctionMapper.selectIllegalNextFireTime();	//获取违章相关定时任务时间戳
			
			if(timestamp == null){
				result.setStatus(0);
				result.setMsg("success");
				result.setData("\"qrtz_triggers\"表\"TRIGGER_NAME\"字段为trans10HMessageIllegalTaskTrigger的数据异常");
			}else{
				String calTime = ToolUtil.getTime1(Long.parseLong(timestamp));
				
				result.setStatus(0);
				result.setMsg("success");
				result.setData("下一个定时任务时间戳：" + timestamp + "<br><br>定时任务执行时间为：" + calTime);
			}
		}else if("1".equals(type)){
			String date = ToolUtil.getTime3(new Date());
			date = ToolUtil.addMinute2(date, Integer.parseInt(time));
 			String timestamp = ToolUtil.getTimestamp(date);
 			
 			try {
 				otherFunctionMapper.updateIllegalTimedTask(timestamp);	//修改违章相关定时任务时间戳
 				
 				timestamp = otherFunctionMapper.selectIllegalNextFireTime();	//获取违章相关定时任务时间戳
 				if(timestamp == null){
 					result.setStatus(0);
 					result.setMsg("success");
 					result.setData("\"qrtz_triggers\"表\"TRIGGER_NAME\"字段为trans10HMessageIllegalTaskTrigger的数据异常");
 				}else{
 					String calTime = ToolUtil.getTime1(Long.parseLong(timestamp));
 					
 					result.setStatus(0);
 					result.setMsg("success");
 					result.setData("下一个定时任务时间戳：" + timestamp + "<br><br>定时任务执行时间为：" + calTime);
 				}
			} catch (Exception e) {
				logger.error("type = 1，定时任务（订单状态）异常：",e);
				result.setStatus(0);
				result.setMsg("success");
				result.setData("\"qrtz_triggers\"表没\"TRIGGER_NAME\"字段为trans10HMessageIllegalTaskTrigger的数据");
			}
		}else if("2".equals(type)){
			String timestamp = ToolUtil.getTimestamp(time);
			
			try {
 				otherFunctionMapper.updateIllegalTimedTask(timestamp);	//修改违章相关定时任务时间戳
 				
 				timestamp = otherFunctionMapper.selectIllegalNextFireTime();	//获取违章相关定时任务时间戳
 				if(timestamp == null){
 					result.setStatus(0);
 					result.setMsg("success");
 					result.setData("\"qrtz_triggers\"表没\"TRIGGER_NAME\"字段为trans10HMessageIllegalTaskTrigger的数据");
 				}else{
 					String calTime = ToolUtil.getTime1(Long.parseLong(timestamp));
 					
 					result.setStatus(0);
 					result.setMsg("success");
 					result.setData("下一个定时任务时间戳：" + timestamp + "<br><br>定时任务执行时间为：" + calTime);
 				}
			} catch (Exception e) {
				logger.error("type = 2，定时任务（订单状态）异常：",e);
				result.setStatus(0);
				result.setMsg("success");
				result.setData("\"qrtz_triggers\"表没\"TRIGGER_NAME\"字段为trans10HMessageIllegalTaskTrigger的数据");
			}
		}
		
		return result;
	}

	
	/**
	 * 设置登录验证码
	 */
	public Result setRedis(String environment, String mobile, String code) {
		Result result = new Result();
		PathIP pathIP = ToolUtil.getIP(environment);
		String redisIP = pathIP.getRedisIp();
		
		Jedis jedis = new Jedis(redisIP);	//连接本地的 Redis 服务
		if(!"PONG".equals(jedis.ping())){	//查看服务是否运行
			result.setStatus(0);
			result.setMsg("success");
			result.setData("\""+environment+"\"环境的Redis服务没在运行，请检查");
			return result;
		}
		
		String str = jedis.set("LgCode:mobile:"+mobile, code);	//设置 redis 字符串数据
        if("OK".equals(str)){
        	result.setStatus(0);
			result.setMsg("success");
			result.setData("登录验证码设置成功：mobile="+mobile+" ; 验证码："+jedis.get("LgCode:mobile:"+mobile));
        }else{
        	result.setStatus(0);
			result.setMsg("success");
			result.setData("登录验证码设置成功失败，请检查");
        }
		
		return result;
	}



	/**
	 * 根据距离计算提前延后时间
	 * @param carA		校验车辆：cityCode和isLocal根据这辆车判断
	 * @param carB
	 * @return
	 */
	public Result getBeforeAfterTimeByDistance(String cityCode, Car carA, Car carB) {
		Result result = new Result();
//		String cityCode = carA.getCity();

		String aLon = carA.getGet_car_lon();	//A车取车位置经度
		String aLat = carA.getGet_car_lat();	//A车取车位置纬度
		String bLon = carB.getGet_car_lon();	//B车取车位置经度
		String bLat = carB.getGet_car_lat();	//B车取车位置纬度

		Map<String,String> distanceMap = transService.getDistance(aLon,aLat,bLon,bLat);

		logger.info("======");
		logger.info("球面距离：{}",distanceMap.get("distance1"));
		logger.info("展示距离：{}",distanceMap.get("distance2"));
		double distance2 = Double.parseDouble(distanceMap.get("distance2"));    //展示距离

		String time = "";
		String[] cityArr = {"310100","330100","440300","110100"};   //限行城市
		boolean flag = Arrays.asList(cityArr).contains(cityCode);	//是否限行
		if (flag) {    //限行
			distanceMap.put("isLimit","是");	//是否限行
			if (carA.getIs_local() == 1) {
				if (distance2 <= 10) {
					time = "1小时";
				}
				if (distance2 > 10 && distance2 <= 20) {
					time = "1.5小时";
				}
				if (distance2 > 20) {
					time = "2小时";
				}
			} else {
				if (distance2 <= 10) {
					time = "1.5小时";
				}
				if (distance2 > 10 && distance2 <= 20) {
					time = "2小时";
				}
				if (distance2 > 20) {
					time = "2.5小时";
				}
			}
		} else {
			distanceMap.put("isLimit","否");	//是否限行
			if (distance2 <= 10) {
				time = "1小时";
			}
			if (distance2 > 10 && distance2 <= 20) {
				time = "1.5小时";
			}
			if (distance2 > 20) {
				time = "2小时";
			}
		}

		logger.info("是否限行：{}",flag);
		logger.info("是否本地：{}",carA.getIs_local());
		logger.info("限行：{}",time);

		String isLocal =  (carA.getIs_local() == 1) ? "是" : "否";
		distanceMap.put("isLocal",isLocal);
		distanceMap.put("beforeBackTime",time);
		result.setStatus(0);
		result.setMsg("success");
		result.setData(distanceMap);

		return result;
	}


//	/**
//	 * 获取key对应的value的值
//	 * @param code
//	 * @return
//	 */
//	public String getValue(String code){
//		Map<String,Object> map = otherFunctionMapper.getSysConstantByCode(code);
//		if (map == null || map.isEmpty()) {
//			return "";
//		}else{
//			return map.get("c_value").toString();
//		}
//	}
}
