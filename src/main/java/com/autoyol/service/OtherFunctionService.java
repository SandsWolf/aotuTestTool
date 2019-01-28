package com.autoyol.service;

import com.autoyol.entity.Car;
import com.autoyol.entity.PathIP;
import com.autoyol.entity.Result;

public interface OtherFunctionService {
	public Result clearMemory(PathIP pathIP);
	public Result setTransTimedTask(String time, String type);
	public Result setIllegalTimedTask(String time, String type);
	public Result setRedis(String environment, String mobile, String code);
	public Result getBeforeAfterTimeByDistance(String cityCode, Car carA, Car carB);
//	public String getValue(String code);
}
