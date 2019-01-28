package com.autoyol.service;

import com.autoyol.dao.TimeMapper;
import com.autoyol.entity.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface TimeService {
    public String turnTimeString(String date, List<String> timeList);
    public List<TimeAxis> compareTimeAxis(List<TimeAxis> transTimeAxisList, List<TimeAxis> noServiceList);
    public Map<String,Object> checkTransTimeLock(String carNo, String rentTime, String revertTime, List<TransTimeLock> transTimeList);
    public Map<String,String> checkTransReply(String rentTime, String revertTime, List<TransReply> transReplyList);
    public List<TimeAxis> getTransTimeAxisList(String rentTime, String revertTime) throws ParseException;
    public List<TimeAxis> getCarOrderSettingTimeAxisList(TimeMapper timeMapper, String rentTime, String revertTime, String carNo) throws ParseException;
    public List<TimeAxis> getTransFilterTimeAxisList(List<TransFilter> transFilterList) throws ParseException;
    public List<TimeAxis> getCarFilterTimeAxisList(List<String> carFilterList);
    public List<TimeAxis> getCarBusyTimeTimeAxisList(String rentTime, String revertTime, List<CarBusyTime> carBusyTimeList) throws ParseException;
    public Map<String,List<TimeAxis>> checkTimeAxis(Map<String, List<TimeAxis>> map);
    public Map<String,Object> checkNoSrvTimeAxis(Map<String, List<TimeAxis>> map, Map<String, String> checkedTransReplyMap, Map<String, Object> checkedTransTimeLockMap);
    public int daysBetween(String startDate, String endDate) throws ParseException;
}
