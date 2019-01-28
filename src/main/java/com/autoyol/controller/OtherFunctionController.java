package com.autoyol.controller;

import com.autoyol.dao.CarMapper;
import com.autoyol.entity.Car;
import com.autoyol.entity.PathIP;
import com.autoyol.entity.Result;
import com.autoyol.service.OtherFunctionService;
import com.autoyol.util.SetDateSourceUtil;
import com.autoyol.util.ToolUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/other")
public class OtherFunctionController {
	@Resource
	private OtherFunctionService otherFunctionService;
	@Resource
	private CarMapper carMapper;


	/**
	 * 清operation_text表缓存
	 * @param environment
	 * @return
	 */
	@RequestMapping("/clearmemory")
	@ResponseBody
	public Result clearMemory(String environment){
		Result result = new Result();
		if("线上".equals(environment)){
			result.setStatus(1);
			result.setMsg("success");
			result.setData("线上环境只能做查询操作");
			return result;
		}

		SetDateSourceUtil.setDataSourceName(environment);
		PathIP pathIP = ToolUtil.getIP(environment);

		result = otherFunctionService.clearMemory(pathIP);
		return result;
	}


	/**
	 * 定时任务（订单状态）
	 * 1.设置XX分钟后
	 * 2.设置定时任务时间
	 */
	@RequestMapping("/transtimedtask")
	@ResponseBody
	public Result setTransTimedTask(String environment, String time, String type){
		Result result = new Result();
		if("线上".equals(environment)){
			result.setStatus(1);
			result.setMsg("success");
			result.setData("线上环境只能做查询操作");
			return result;
		}

		SetDateSourceUtil.setDataSourceName(environment);

		result = otherFunctionService.setTransTimedTask(time, type);
		return result;
	}


	/**
	 * 定时任务（违章）
	 * 1.设置XX分钟后
	 * 2.设置定时任务时间
	 */
	@RequestMapping("/Illegaltimedtask")
	@ResponseBody
	public Result setIllegalTimedTask(String environment, String time, String type){
		Result result = new Result();
		if("线上".equals(environment)){
			result.setStatus(1);
			result.setMsg("success");
			result.setData("线上环境只能做查询操作");
			return result;
		}

		SetDateSourceUtil.setDataSourceName(environment);

		result = otherFunctionService.setIllegalTimedTask(time, type);
		return result;
	}



	/**
	 * 设置登录验证码
	 * @param environment
	 * @param mobile
	 * @param code
	 * @return
	 */
	@RequestMapping("/setlogincode")
	@ResponseBody
	public Result setLoginCode(String environment, String mobile, String code){
		Result result = new Result();
		if("线上".equals(environment)){
			result.setStatus(1);
			result.setMsg("success");
			result.setData("线上环境只能做查询操作");
			return result;
		}

		SetDateSourceUtil.setDataSourceName(environment);

		if (!ToolUtil.isMobile(mobile)) {
			result = ToolUtil.checkMobile(mobile);
			return result;
		}

		if ("".equals(code)) {
			code = "111111";
		}

		result = otherFunctionService.setRedis(environment, mobile, code);
		return result;
	}


	/**
	 * 根据距离计算提前延后时间
	 * @param environment
	 * @param cityCode
	 * @param lonA
	 * @param latA
	 * @param lonB
	 * @param latB
	 * @param carPara	校验车辆：cityCode和isLocal根据这辆车判断
	 * @return
	 */
	@RequestMapping("/getBeforeAfterTimeByDistance")
	@ResponseBody
	public Result getBeforeAfterTimeByDistance(String environment, String cityCode, String lonA, String latA, String lonB, String latB, String carPara){
		SetDateSourceUtil.setDataSourceName(environment);
		Result result = new Result();

//		Car carA = carMapper.selectCarInfo(carParaA);
//		Car carB = carMapper.selectCarInfo(carParaB);
		Car car = carMapper.selectCarInfo(carPara);

		if (car == null) {
			result.setStatus(1);
			result.setMsg("success");
			result.setData("<br><span class='sign_span' style='color:red;'>车辆（" + carPara + "）不存在，请检查数据再试</span>");
			return result;
		}

		Car carA = new Car();
		Car carB = new Car();
		carA.setGet_car_lon(lonA);
		carA.setGet_car_lat(latA);
		carB.setGet_car_lon(lonB);
		carB.setGet_car_lat(latB);
		carA.setIs_local(car.getIs_local());

//		boolean flagA = (carA == null);
//		boolean flagB = (carB == null);
//		if (flagA || flagB) {
//			result.setStatus(1);
//			result.setMsg("success");
//			String str = "";
//			if (flagA) {
//				str = carParaA;
//			}
//			if (flagB) {
//				str = carParaB;
//			}
//			if (flagA && flagB) {
//				str = carParaA + "、" + carParaB;
//			}
//			System.out.println("str:" + str);
//			result.setData("<br><span class='sign_span' style='color:red;'>车辆（" + str + "）不存在，请检查数据再试</span>");
//			return result;
//		}
		result = otherFunctionService.getBeforeAfterTimeByDistance(cityCode,carA,carB);

		return result;
	}



}
