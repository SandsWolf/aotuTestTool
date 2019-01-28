package com.autoyol.service.impl;

import com.autoyol.dao.TimeMapper;
import com.autoyol.entity.*;
import com.autoyol.service.TimeService;
import com.autoyol.util.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TimeServiceImpl implements TimeService {
    private static final Logger logger = LoggerFactory.getLogger(TimeServiceImpl.class);
    
    //单天时间轴
    public static final String[] TIME_AXIS_OF_DAY = {"000000", "001500", "003000", "004500", "010000", "011500", "013000", "014500", "020000", "021500", "023000", "024500", "030000", "031500", "033000", "034500", "040000", "041500", "043000", "044500", "050000", "051500", "053000", "054500", "060000", "061500", "063000", "064500", "070000", "071500", "073000", "074500", "080000", "081500", "083000", "084500", "090000", "091500", "093000", "094500", "100000", "101500", "103000", "104500", "110000", "111500", "113000", "114500", "120000", "121500", "123000", "124500", "130000", "131500", "133000", "134500", "140000", "141500", "143000", "144500", "150000", "151500", "153000", "154500", "160000", "161500", "163000", "164500", "170000", "171500", "173000", "174500", "180000", "181500", "183000", "184500", "190000", "191500", "193000", "194500", "200000", "201500", "203000", "204500", "210000", "211500", "213000", "214500", "220000", "221500", "223000", "224500", "230000", "231500", "233000", "234500", "240000"};
    //多天时间轴
    public static final String[] TIME_AXIS_OF_DAYS = {"000000", "001500", "003000", "004500", "010000", "011500", "013000", "014500", "020000", "021500", "023000", "024500", "030000", "031500", "033000", "034500", "040000", "041500", "043000", "044500", "050000", "051500", "053000", "054500", "060000", "061500", "063000", "064500", "070000", "071500", "073000", "074500", "080000", "081500", "083000", "084500", "090000", "091500", "093000", "094500", "100000", "101500", "103000", "104500", "110000", "111500", "113000", "114500", "120000", "121500", "123000", "124500", "130000", "131500", "133000", "134500", "140000", "141500", "143000", "144500", "150000", "151500", "153000", "154500", "160000", "161500", "163000", "164500", "170000", "171500", "173000", "174500", "180000", "181500", "183000", "184500", "190000", "191500", "193000", "194500", "200000", "201500", "203000", "204500", "210000", "211500", "213000", "214500", "220000", "221500", "223000", "224500", "230000", "231500", "233000", "234500"};

    private String color = "dodgerblue";    //设置前端数据字体颜色    #FAA732、dodgerblue、darkorange、orange

    /**
     * 校验不使用取还车服务的请求是否可租
     * @param map                       "car_busy_time"，"car_filter"，"trans_filter"，"order_setting_activity"，"car_order_setting"等表
     * @param checkedTransReplyMap      "trans_reply"
     * @param checkedTransTimeLockMap   "trans"表的'rent_time_lock'
     * @return
     */
    public Map<String,Object> checkNoSrvTimeAxis(Map<String,List<TimeAxis>> map, Map<String,String> checkedTransReplyMap, Map<String,Object> checkedTransTimeLockMap){
        List<TimeAxisResult> timeAxisResultList = new ArrayList<TimeAxisResult>();  //时间轴结果对象List
        Map<String,Object> resultMap = new HashMap<String,Object>();		//最终结果map

        Map<String,List<TimeAxis>> checkedResultMap = this.checkTimeAxis(map);
        Set<Map.Entry<String,List<TimeAxis>>> entrySet = checkedResultMap.entrySet();

        //设置"car_busy_time"，"car_filter"，"trans_filter"，"order_setting_activity"，"car_order_setting"等表返回结果
        for(Map.Entry<String,List<TimeAxis>> e : entrySet){
            String key = e.getKey();				//对比的时间轴key
            List<TimeAxis> checkedTimeAxislist = e.getValue();		//被对比的时间轴
            String timeAxisFlag = "1";
            String msg = "";
            String resultMsg = "";
            for (int i = 0; i <checkedTimeAxislist.size() ; i++) {
                TimeAxis checkedTimeAxis = checkedTimeAxislist.get(i);
                if(checkedTimeAxis.getFlag()=="0"){  //只要有一个不可租，则整个表就不可租
                    timeAxisFlag = "0";
                }
                msg = checkedTimeAxis.getMsg();
                if(checkedTimeAxis.getTimeListString()!="[]"){
                    resultMsg += "&nbsp;&nbsp;&nbsp;&nbsp;<span class='dsign_span' style='color:"+color+";'>\"" + ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(checkedTimeAxis.getDate()+"000000"))).substring(0,10) + "\" 的 "+ checkedTimeAxis.getTimeListString() + " 不可租</span><br>";
                }
            }
            TimeAxisResult timeAxisResult = new TimeAxisResult();
            timeAxisResult.setName(key);
            timeAxisResult.setTimeAxisFlag(timeAxisFlag);
            timeAxisResult.setMsg("<span class='dsign_span' style='color:"+color+";'>" + msg + "</span>");
            timeAxisResult.setResultMsg(resultMsg);

            timeAxisResultList.add(timeAxisResult);
        }

        //设置"trans_reply"表返回结果
        String timeAxisFlag = "1";
        if(checkedTransReplyMap.get("flag")=="0"){
            timeAxisFlag = "0";
        }
        TimeAxisResult transReplyTimeAxisResult = new TimeAxisResult();             //时间轴结果对象
        transReplyTimeAxisResult.setName("\"trans_reply\"表");
        transReplyTimeAxisResult.setTimeAxisFlag(timeAxisFlag);
        transReplyTimeAxisResult.setMsg("<span class='dsign_span' style='color:"+color+";'>" + checkedTransReplyMap.get("msg") + "</span>");
        transReplyTimeAxisResult.setResultMsg("<span class='dsign_span' style='color:"+color+";'>" + checkedTransReplyMap.get("resultMsg") + "</span>");
        timeAxisResultList.add(transReplyTimeAxisResult);


        //设置"trans"表的'rent_time_lock'返回结果
        timeAxisFlag = "1";
        if(checkedTransTimeLockMap.get("flag")=="0"){
            timeAxisFlag = "0";
        }
        String msg = "";
        List<String> transTimeLockTimeAxisList = (List<String>)checkedTransTimeLockMap.get("msgList");
        if(transTimeLockTimeAxisList.size()==0){
            msg = "<span class='dsign_span' style='color:"+color+";'>\"trans\"表中carNo(4439552671)没有不可租限制</span>";
        }else{
//            msg += "\"trans\"表中carNo(4439552671)的不可租限制为：<br>";
            msg += "<br>";
            for (int i = 0; i < transTimeLockTimeAxisList.size(); i++) {
                msg += "&nbsp;&nbsp;&nbsp;&nbsp;<span class='dsign_span' style='color:"+color+";'>" + transTimeLockTimeAxisList.get(i) + "</span><br>";
            }
        }
        TimeAxisResult transTimeLockTimeAxisResult = new TimeAxisResult();          //时间轴结果对象
        transTimeLockTimeAxisResult.setName("\"trans\"表'rent_time_lock'字段");
        transTimeLockTimeAxisResult.setTimeAxisFlag(timeAxisFlag);
        transTimeLockTimeAxisResult.setMsg(msg);
        transTimeLockTimeAxisResult.setResultMsg("<span class='dsign_span' style='color:"+color+";'>" + (String)checkedTransTimeLockMap.get("resultMsg") + "</span>");
        timeAxisResultList.add(transTimeLockTimeAxisResult);


        resultMap.put("timeAxisList",timeAxisResultList);

        boolean checkedFlag = true;				//
        for(Map.Entry<String,List<TimeAxis>> e : entrySet){
            List<TimeAxis> checkedTimeAxislist = e.getValue();		//被对比的时间轴
            for (int i = 0; i <checkedTimeAxislist.size() ; i++) {
                if(checkedTimeAxislist.get(i).getFlag()!="1"){
                    checkedFlag = false;
                    break;
                }
            }
        }
        if(checkedFlag && (String)checkedTransReplyMap.get("flag")=="1" && (String)checkedTransTimeLockMap.get("flag")=="1"){
            resultMap.put("resultFlag","1");
        }else{
            resultMap.put("resultFlag","0");
        }

        return resultMap;
    }


    //把时间轴转化成xxxx-xxxx时间形式
    //date      日期：如20171229
    //timeList  时间轴List
    public String turnTimeString(String date, List<String> timeList){
        if(timeList.size()==0){
            return "[]";
        }

        List<Long> timeStampList = new ArrayList<Long>();	//时间戳list
        for(String str:timeList){	//把car_filter转成时间戳List
            timeStampList.add(Long.parseLong(ToolUtil.getTimestamp(date + str)));
        }

//		Long[] ls = (Long[]) timeStampList.toArray(new Long[timeStampList.size()]);		//list转数组
//		Arrays.sort(ls);
//		timeStampList = Arrays.asList(ls);

        String timrStr = timeList.get(0) + "_";
        long x = 900000;	//15分钟的毫秒数
        for(int i=1;i<timeStampList.size();i++){
            long y = timeStampList.get(i) - timeStampList.get(i-1);
            String thisTimeStr = ToolUtil.getTime2(timeStampList.get(i)).substring(8, 14);
            String beforeTimeStr = ToolUtil.getTime2(timeStampList.get(i-1)).substring(8, 14);
//            thisTimeStr = thisTimeStr.substring(0,2) + ":" + thisTimeStr.substring(2,4) + ":" + thisTimeStr.substring(4,6);
//            beforeTimeStr = beforeTimeStr.substring(0,2) + ":" + beforeTimeStr.substring(2,4) + ":" + beforeTimeStr.substring(4,6);
            if(!timrStr.endsWith(",")){
                if(x == y){
                    timrStr = timrStr.substring(0,(timrStr.lastIndexOf("_")+1));
                    timrStr += thisTimeStr;
                }else{
                    timrStr += ",";
                }
            }else{
                long z = timeStampList.get(i-1) - timeStampList.get(i-2);
                if(x == y){
                    timrStr += beforeTimeStr + "_" + thisTimeStr;
                }else{
                    timrStr += beforeTimeStr + "," + thisTimeStr + "_";
                }
//				timrStr += beforeTimeStr + "," + thisTimeStr + "_";
            }

            if(i==(timeStampList.size()-1)){
                if(timrStr.endsWith(",")){
                    timrStr += thisTimeStr;
                }
                if(timrStr.endsWith("_")){
                    timrStr = timrStr.substring(0, timrStr.length()-1);
                }
            }
        }
        String[] s = timrStr.split(",");
        String str = "";
        for(int i=0;i<s.length;i++){	//给每个时间分段加引号
            //当时间轴的最后一个时间点是240000时，则去掉它
            if(s[i].endsWith("_000000")){
                String string = s[i].substring(0,7);
                s[i] = string + "234500";
            }

//            if(i<s.length-1){
//                str += "\"" + s[i] + "\",";
//            }else{
//                str += "\"" + s[i] + "\"";
//            }
            if(i<s.length-1){
                str += "[" + s[i] + "],";
            }else{
                str += "[" + s[i] + "]";
            }
        }


        /*
         *  转样式："[230000_234500],[000000_070000]" ————>"[23:00:00_23:45:00]、[00:00:00_07:00:00]"
         */
        String msg = "";
        String[] strs = str.split(",");
        for (int i = 0; i < strs.length; i++) {
            String startTime = strs[i].substring(1,7);
            String endTime = strs[i].substring(8,14);

            startTime = startTime.substring(0,2) + ":" + startTime.substring(2,4) + ":" + startTime.substring(4,6);
            endTime = endTime.substring(0,2) + ":" + endTime.substring(2,4) + ":" + endTime.substring(4,6);
            if(i == (strs.length-1)){
                msg += "[" + startTime + "_" + endTime + "]";
            }else{
                msg += "[" + startTime + "_" + endTime + "]、";
            }
        }

        return msg;
    }

    /**
     * 用于单个比较"租期时间轴对象List"和"不可租时间轴对象List"：如'car_filter','car_busy_time','trans_reply','trans_filter','order_setting_activity','car_order_setting'等表
     * @param transTimeAxisList  	租期时间轴对象List
     * @param noServiceList			不可租时间轴对象List
     * @return
     */
    public List<TimeAxis> compareTimeAxis(List<TimeAxis> transTimeAxisList, List<TimeAxis> noServiceList){
//        logger.info("transTimeAxisList长度：" + transTimeAxisList.size());
//        logger.info("noServiceList长度：" + noServiceList.size());
        List<TimeAxis> comparedList = new ArrayList<TimeAxis>();	//对比后时间轴
        String msg = "";        //给前段的提示信息
        for(int i=0;i<transTimeAxisList.size();i++){
            TimeAxis comparedTimeAxis = new TimeAxis();			//对比后放数据的对象
            List<String> comparedTimeList = new ArrayList<String>();	//对比后方时间轴的数组
            TimeAxis transTimeAxis = transTimeAxisList.get(i);	//租期单个对象
            for(int j=0;j<noServiceList.size();j++){
                TimeAxis noServiceTimeAxis = noServiceList.get(j);	//不可阻对象
                msg = noServiceTimeAxis.getMsg();       //对比发现不可租，就给提示信息赋值
//                logger.info("--"+noServiceTimeAxis.getMsg()+"--");
                if(transTimeAxis.getDate().equals(noServiceTimeAxis.getDate())){	//对比日期
                    List<String> transTimeList = transTimeAxis.getTimeList();			//租期时间轴
                    List<String> noServiceTimeList = noServiceTimeAxis.getTimeList();	//不可阻时间轴
                    for(int k=0;k<transTimeList.size();k++){	//循环租期内某天的时间轴
                        String time = transTimeList.get(k);		//租期时间点
                        for(int m=0;m<noServiceTimeList.size();m++){	//循环不可阻时间轴
                            if(time.equals(noServiceTimeList.get(m))){	//发现租期内某天的某个时间点不可阻
                                comparedTimeList.add(time);
//                                msg = noServiceTimeAxis.getMsg();       //对比发现不可租，就给提示信息赋值
                            }
                        }
                    }
                }
            }

            if(comparedTimeList.size()!=0){
                comparedTimeAxis.setDate(transTimeAxis.getDate());
                comparedTimeAxis.setTimeList(comparedTimeList);
                comparedTimeAxis.setFlag("0"); //当租期与时间轴对比，满足不可租时，给flag赋值true
                comparedTimeAxis.setMsg(msg);
                comparedTimeAxis.setTimeListString(this.turnTimeString(transTimeAxis.getDate(),comparedTimeList));
            }else{
                comparedTimeAxis.setDate(transTimeAxis.getDate());
                comparedTimeAxis.setTimeList(new ArrayList<String>());
                comparedTimeAxis.setFlag("1");    //当租期与时间轴对比，满足不可租时，给flag赋值false
                comparedTimeAxis.setMsg(msg);        //当租期与时间轴对比，满足不可租时，把msg的值制空
                comparedTimeAxis.setTimeListString(this.turnTimeString(transTimeAxis.getDate(),new ArrayList<String>()));
            }
            comparedList.add(comparedTimeAxis);

        }
        return comparedList;
    }

    /**
     *  校验'trans'表中'rent_time_lock'字段相关逻辑
     *  返回数据中的msg为表中不可租时间轴逻辑，resultMsg为比较后不可租时间轴逻辑
     * @param rentTime              格式：yyyyMMddHHmmss
     * @param revertTime            格式：yyyyMMddHHmmss
     * @param transTimeList         trans表数据
     * @return                      map:flag，msgList(所有trans表中数据)，resultMsg(对比结果msg)
     */
    public Map<String,Object> checkTransTimeLock(String carNo, String rentTime, String revertTime, List<TransTimeLock> transTimeList){
        Map<String,Object> map = new HashMap<String,Object>();
        long time = Long.parseLong(rentTime);
        long time2 = Long.parseLong(revertTime);

        String resultMsg = "";
        List<String> msgList = new ArrayList<String>();
        for(TransTimeLock ttl:transTimeList){
            msgList.add("【订单号：" + ttl.getOrder_no() + "（" + ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(ttl.getRent_time()))) + " ~ " + ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(ttl.getRevert_time()))) + "）】 不可租");
//            msgList.add(ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(ttl.getRent_time()))) + " ~ " + ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(ttl.getRevert_time()))));
            long transRentTime = Long.parseLong(ttl.getRent_time());
            long transRevertTime = Long.parseLong(ttl.getRevert_time());
            if(((transRentTime<=time && time<=transRevertTime) || (transRentTime<=time2 && time2<=transRevertTime) ||(time<=transRentTime &&time2>=transRevertTime)) &&ttl.getRent_time_lock()==1){
                resultMsg += "&nbsp;&nbsp;&nbsp;&nbsp;【订单号：" + ttl.getOrder_no() + "（" + ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(ttl.getRent_time()))) + " ~ " + ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(ttl.getRevert_time()))) + "）】<br>";
//                resultMsg += ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(ttl.getRent_time()))) + " ~ " + ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(ttl.getRevert_time()))) + "、";
            }
        }

        map.put("msgList",msgList);

        if(resultMsg.length()==0){      //租期时间轴不冲突
            map.put("flag","1");
            map.put("resultMsg","请求租期与\"trans\"表中carNo("+carNo+")对应锁定的租期不冲突");
        }else {                         //租期时间轴冲突
            map.put("flag","0");
            if(resultMsg.length()==0){
                map.put("resultMsg","请求租期与\"trans\"表中：<br>" + resultMsg + "对应锁定的租期冲突");
            }else{
//                map.put("resultMsg","请求租期与\"trans\"表中：" + resultMsg.substring(0,(resultMsg.lastIndexOf("、"))) + "对应锁定的租期冲突");
                map.put("resultMsg","请求租期与\"trans\"表中：<br>" + resultMsg + "对应锁定的租期冲突");
            }
        }

        return map;
    }

    /**
     *  校验租期是否满足配置的最大、最短租期（'trans_reply'表）
     *  返回数据中的msg为表中不可租时间轴逻辑，resultMsg为比较后不可租时间轴逻辑
     * @param rentTime              格式：yyyyMMddHHmmss
     * @param revertTime            格式：yyyyMMddHHmmss
     * @param transReplyList        trans_reply表数据
     * @return                      map:flag，msg(所有trans_reply表中数据)，resultMsg(对比结果msg)
     */
    public Map<String,String> checkTransReply(String rentTime, String revertTime, List<TransReply> transReplyList){
        Map<String,String> map = new HashMap<String,String>();
        double rentDate = ToolUtil.getRentDateTrans(rentTime, revertTime);  //计算租期时长（小时数）

        //没有限制最长最短租期（数据库中没数据），取默认值，默认最短租期8小时，最长租期744小时
        if(transReplyList.size()==0) {
            if (rentDate >= 8.0 && rentDate <= 744.0) {
                map.put("flag","1");
                map.put("resultMsg","请求租期时长：" + rentDate + "小时。可租：满足默认时间（最小租期8小时，最大租期744小时）");
                map.put("msg","\"trans_reply\"表中没数据，取默认数据：最小租期8小时，最大租期744小时");
            } else {
                map.put("flag","0");
                map.put("resultMsg","请求租期时长：" + rentDate + "小时。不可租：不满足默认时间（最小租期8小时，最大租期744小时）");
                map.put("msg","\"trans_reply\"表中没数据，取默认数据：最小租期8小时，最大租期744小时");
            }
        }else{
            for(TransReply transreply:transReplyList){
                if(transreply != null){
                    int replyMin = transreply.getReply_min();
                    int replyMax = transreply.getReply_max();
                    if((rentDate >= replyMin && rentDate <= replyMax)){
                        map.put("flag","1");
                        map.put("resultMsg","请求租期时长：" + rentDate + "小时。可租：满足trans_reply表（最小租期"+replyMin+"小时，最大租期"+replyMax+"小时）");
                        map.put("msg","\"trans_reply\"表中有数据：最小租期"+replyMin+"小时，最大租期"+replyMax+"小时");
                    }else{
                        map.put("flag","0");
                        map.put("resultMsg","请求租期时长：" + rentDate + "小时。不可租：不满足trans_reply表（最小租期"+replyMin+"小时，最大租期"+replyMax+"小时）");
                        map.put("msg","\"trans_reply\"表中有数据：最小租期"+replyMin+"小时，最大租期"+replyMax+"小时");
                    }
                }
            }
        }
        return map;
    }

    /**
     * 计算租期内时间轴
     * @param rentTime		格式：yyyyMMddHHmmss
     * @param revertTime	格式：yyyyMMddHHmmss
     * @return
     * @throws ParseException
     */
    public List<TimeAxis> getTransTimeAxisList(String rentTime, String revertTime) throws ParseException {
        List<TimeAxis> timeAxisList = new ArrayList<TimeAxis>();
        int x = this.daysBetween(rentTime,revertTime);	//租期间隔天数
        if(x == 0){		//租期开始和结束时间在同一天  ok
            TimeAxis timeAxis = new TimeAxis();
            timeAxis.setDate(rentTime.substring(0,8));
            timeAxis.setTimeList(this.getNoServiceTimeList(rentTime.substring(8, 12), revertTime.substring(8, 12)));
            timeAxisList.add(timeAxis);
        }
        if(x == 1){		//租期开始和结束时间是隔天
            TimeAxis timeAxis1 = new TimeAxis();
            timeAxis1.setDate(rentTime.substring(0,8));
            timeAxis1.setTimeList(this.getNoServiceTimeList(rentTime.substring(8, 12), "2400"));

            TimeAxis timeAxis2 = new TimeAxis();
            timeAxis2.setDate(revertTime.substring(0,8));
            timeAxis2.setTimeList(this.getNoServiceTimeList("0000", revertTime.substring(8, 12)));

            timeAxisList.add(timeAxis1);
            timeAxisList.add(timeAxis2);
        }
        if(x > 1){		//租期>1天
            for(int i=0;i<(x+1);i++){
                TimeAxis timeAxis = new TimeAxis();
                if(i==0){			//租期第一天
                    timeAxis.setDate(rentTime.substring(0,8));
                    timeAxis.setTimeList(this.getNoServiceTimeList(rentTime.substring(8, 12), "2400"));
                }else if(i==x){		//租期最后一天
                    timeAxis.setDate(revertTime.substring(0,8));
                    timeAxis.setTimeList(this.getNoServiceTimeList("0000", revertTime.substring(8, 12)));
                }else{				//租期中间
                    timeAxis.setDate(ToolUtil.addDay(rentTime, i).substring(0, 8));
                    timeAxis.setTimeList(Arrays.asList(this.TIME_AXIS_OF_DAY));
                }
                timeAxisList.add(timeAxis);
            }
        }


        return timeAxisList;
    }

    /**
     * 判断'order_setting_activity'和'car_order_setting'两表逻辑
     * 逻辑如下：
     *      当租期与'order_setting_activity'表时间轴不重叠，则可租
     * 			当租期与'order_setting_activity'表时间轴重叠，则判断'car_order_setting'表，
     * 				当'car_order_setting'无值，则可租
     * 				当'car_order_setting'有值，则判断是否接单('is_acceptance'字段)
     * 					不接单：则不可租
     * 					接单：则判断租期是否满足起租天数('rental_days'字段)
     * 						满足几天起租：可租
     * 						不满足几天起租：不可租
     * @param timeMapper
     * @param rentTime          格式：yyyyMMddHHmmss
     * @param revertTime        格式：yyyyMMddHHmmss
     * @param carNo
     * @return
     * @throws ParseException
     */
    public List<TimeAxis> getCarOrderSettingTimeAxisList(TimeMapper timeMapper, String rentTime, String revertTime, String carNo) throws ParseException {
        String rentDate = rentTime.substring(0,8);
        String revertDate = revertTime.substring(0,8);

        Map<String,String> map = new HashMap<String,String>();
        map.put("rentDate",rentDate);
        map.put("revertDate",revertDate);

        List<TimeAxis> timeAxisList = new ArrayList<TimeAxis>();    //不可租时间轴
        List<OrderSettingActivity> orderSettingActivitiesList = timeMapper.selectOrderSettingActivity(map);

        if(orderSettingActivitiesList.size()==0){   //租期与'order_setting_activity'表不重叠
            TimeAxis timeAxis = new TimeAxis();
            List<String> noServiceTimeList = new ArrayList<String>();
            timeAxis.setMsg("\"order_setting_activity\"表没有不可租限制");
            timeAxis.setTimeList(noServiceTimeList);
            timeAxisList.add(timeAxis);
            return timeAxisList;
        }else{
            CarOrderSetting carOrderSetting = timeMapper.selectCarOrderSetting(carNo);
            if(carOrderSetting==null){      //carNo在'car_order_setting'表无值
                TimeAxis timeAxis = new TimeAxis();
                List<String> noServiceTimeList = new ArrayList<String>();
                timeAxis.setMsg("\"car_order_setting\"表没有不可租限制");
                timeAxis.setTimeList(noServiceTimeList);
                timeAxisList.add(timeAxis);
                return timeAxisList;
            }else{
                if(carOrderSetting.getIs_acceptance()==0){  //carNo在'car_order_setting'表有值，不接单（'is_acceptance'字段=0）
                    timeAxisList = this.getTransTimeAxisList(rentTime,revertTime);
                    String msg = "";
                    for (TimeAxis t:timeAxisList){
//                        msg += t.getDate() +"、";
                        msg += ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(t.getDate()+"000000"))).substring(0,10) +"、";
                    }

                    if(msg.endsWith("、")){
                        msg = "【" + msg.substring(0,msg.lastIndexOf("、")) + "】全天";
                    }else{
                        msg = "【" + msg + "】全天";
                    }

                    for (TimeAxis t:timeAxisList){
                        t.setMsg(msg);
                    }
                    return timeAxisList;
                }else {
//                    int rentDays = this.daysBetween(rentTime,revertTime);    //租期天数：疑问：超8小时进位吗？
                    double rentDays = ToolUtil.getRentDate(rentTime,revertTime);    //租期天数
//                    logger.info("rentDays:" + rentDays);
                    if(rentDays >= carOrderSetting.getRental_days()){           //则判断租期是否满足起租天数('rental_days'字段)
                        return timeAxisList;
                    }else{
                        timeAxisList = this.getTransTimeAxisList(rentTime,revertTime);
                        String msg = "";
                        for (TimeAxis t:timeAxisList){
//                            msg += t.getDate() +"、";
                            msg += ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(t.getDate()+"000000"))).substring(0,10) +"、";
                        }

                        if(msg.endsWith("、")){
                            msg = "【" + msg.substring(0,msg.lastIndexOf("、")) + "】全天";
                        }else{
                            msg = "【" + msg + "】全天";
                        }

                        for (TimeAxis t:timeAxisList){
                            t.setMsg(msg);
                        }
                        return timeAxisList;
                    }
                }
            }
        }
    }

    /**
     * 判断trans_filter表数据：
     * 	如：car_no在trans_filter表中没有数据，表示无不可阻时间轴
     *     car_no在trans_filter表中只有1条数据且'get_return_flag'=0，表示有不可阻时间轴，不可租时间轴已该数据为准
     * 	   car_no在trans_filter表中有2条数据且'get_return_flag'=1，表示有不可阻时间轴，不可租时间轴已'get_return_flag'=1数据为准
     * @param transFilterList
     * @return
     * @throws ParseException
     */
    public List<TimeAxis> getTransFilterTimeAxisList(List<TransFilter> transFilterList) throws ParseException {
        List<TimeAxis> timeAxisList = new ArrayList<TimeAxis>();

        if(transFilterList.size()==0){	//无不可阻时间轴
            TimeAxis timeAxis = new TimeAxis();
            List<String> noServiceTimeList = new ArrayList<String>();
            timeAxis.setMsg("\"trans_filter\"表没有不可租限制");
            timeAxis.setTimeList(noServiceTimeList);
            timeAxisList.add(timeAxis);
            return timeAxisList;
        }else{							//有不可阻时间轴
            String rentTime = "";
            String revertTime = "";
            if(transFilterList.size()==1){
                rentTime = transFilterList.get(0).getRent_time();
                revertTime = transFilterList.get(0).getRevert_time();
            }else if(transFilterList.size()==2){
                for(TransFilter t:transFilterList){
                    if(t.getGet_return_flag()==1){
                        rentTime = t.getRent_time();
                        revertTime = t.getRevert_time();
                    }
                }
            }

            String msgRentime = ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(rentTime)));         //前段展示msg
            String msgRevertTime = ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(revertTime)));    //前段展示msg
            String msg = "【" + msgRentime + " ~ " + msgRevertTime + "】";     //前段展示msg：样式："2018-04-27 13:00:00 ~ 2018-04-30 15:00:00"

            int x = this.daysBetween(rentTime,revertTime);	//租期间隔天数
            if(x == 0){		//租期开始和结束时间在同一天  ok
                TimeAxis timeAxis = new TimeAxis();
                timeAxis.setDate(rentTime.substring(0,8));
                timeAxis.setTimeList(this.getNoServiceTimeList(rentTime.substring(8, 12), revertTime.substring(8, 12)));
                timeAxis.setMsg(msg);
                timeAxisList.add(timeAxis);
            }
            if(x == 1){		//租期开始和结束时间是隔天
                TimeAxis timeAxis1 = new TimeAxis();
                timeAxis1.setDate(rentTime.substring(0,8));
                timeAxis1.setTimeList(this.getNoServiceTimeList(rentTime.substring(8, 12), "2400"));
                timeAxis1.setMsg(msg);

                TimeAxis timeAxis2 = new TimeAxis();
                timeAxis2.setDate(revertTime.substring(0,8));
                timeAxis2.setTimeList(this.getNoServiceTimeList("0000", revertTime.substring(8, 12)));
                timeAxis2.setMsg(msg);

                timeAxisList.add(timeAxis1);
                timeAxisList.add(timeAxis2);
            }
            if(x > 1){		//租期>1天
                for(int i=0;i<(x+1);i++){
                    TimeAxis timeAxis = new TimeAxis();
                    if(i==0){			//租期第一天
                        timeAxis.setDate(rentTime.substring(0,8));
                        timeAxis.setTimeList(this.getNoServiceTimeList(rentTime.substring(8, 12), "2400"));
                        timeAxis.setMsg(msg);
                    }else if(i==x){		//租期最后一天
                        timeAxis.setDate(revertTime.substring(0,8));
                        timeAxis.setTimeList(this.getNoServiceTimeList("0000", revertTime.substring(8, 12)));
                        timeAxis.setMsg(msg);
                    }else{				//租期中间
                        timeAxis.setDate(ToolUtil.addDay(rentTime, i).substring(0, 8));
                        timeAxis.setTimeList(Arrays.asList(this.TIME_AXIS_OF_DAY));
                        timeAxis.setMsg(msg);
                    }
                    timeAxisList.add(timeAxis);
                }
            }

//			this.removeLastTimePoint(timeAxisList);
            return timeAxisList;
        }
    }



    /**
     * 按照日期排序car_filter表并统计不可租时间轴，可能返回空list
     * @param carFilterList
     * @return
     */
    public List<TimeAxis> getCarFilterTimeAxisList(List<String> carFilterList){
        List<TimeAxis> timeAxisList = new ArrayList<TimeAxis>();
        List<Long> timeStampList = new ArrayList<Long>();	//时间戳list
        if(carFilterList.size()==0){
            TimeAxis timeAxis = new TimeAxis();
            List<String> noServiceTimeList = new ArrayList<String>();
            timeAxis.setMsg("\"car_filter\"表没有不可租限制");
            timeAxis.setTimeList(noServiceTimeList);
            timeAxisList.add(timeAxis);
            return timeAxisList;
        }else{
            String msg = "";                                //前段展示msg：样式："20180618、20180619、20180620"

            for(String str:carFilterList){	//把car_filter转成时间戳List
                timeStampList.add(Long.parseLong(ToolUtil.getTimestamp(str+"000000")));
//                msg += str + "、";
                msg += ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(str+"000000"))).substring(0,10) + "、";
            }

            if(msg.endsWith("、")){
                msg = "【" + msg.substring(0,msg.lastIndexOf("、")) + "】全天";
            }else{
                msg = "【" + msg + "】全天";
            }

            Long[] ls = (Long[]) timeStampList.toArray(new Long[timeStampList.size()]);		//list转数组
            Arrays.sort(ls);
            timeStampList = Arrays.asList(ls);


            for(Long l : timeStampList){		//将时间戳List转成yyyyMMdd List
                TimeAxis timeAxis = new TimeAxis();
                timeAxis.setDate(ToolUtil.getTime2(l).substring(0,8));
                timeAxis.setTimeList(Arrays.asList(this.TIME_AXIS_OF_DAY));
                timeAxis.setMsg(msg);
                timeAxisList.add(timeAxis);
            }
        }

        return timeAxisList;
    }

    /**
     * 计算租期日期内的car_busy_time表不可租时间轴，可能返回空list
     * @param rentTime				订单开始时间，格式：yyyyMMddHHmmss
     * @param revertTime			订单结束时间，格式：yyyyMMddHHmmss
     * @param carBusyTimeList		car_busy_time表数据
     * @return
     * @throws ParseException
     */
    public List<TimeAxis> getCarBusyTimeTimeAxisList(String rentTime, String revertTime, List<CarBusyTime> carBusyTimeList) throws ParseException {
        List<TimeAxis> timeAxisList = new ArrayList<TimeAxis>();
        for(int i=0;i<(this.daysBetween(rentTime,revertTime)+1);i++){
            String time = ToolUtil.addHour(rentTime, i*24);

            List<String> noServiceTimeList = new ArrayList<String>();
            TimeAxis timeAxis = new TimeAxis();

            String date = time.substring(0,8);		//日期
            timeAxis.setDate(date);


            if(carBusyTimeList.size()==0){
                timeAxis.setMsg("\"car_busy_time\"表没有不可租限制");
                timeAxis.setTimeList(noServiceTimeList);
            }else{
//    			logger.info(carBusyTimeList.toString());
                //1.判断时间是星期几
                int week = this.getWeek(time);	//租期时间对应的星期几
                String msg = "";                        //前段展示msg：样式："星期一、星期二、星期三"

                for(CarBusyTime carBusyTime : carBusyTimeList){
                    if(carBusyTime.getIs_open()==1){
                        //2.将car_busy_time表的'week'字段转成list
                        String[] weeks = carBusyTime.getWEEK().split(",");
                        List<String> weekList = Arrays.asList(weeks);
//    					logger.info(weekList.toString());

                        for(String w : weekList){
                            int x = Integer.parseInt(w);
                            switch (x){
                                case 2 :
                                    msg += "星期一、";
                                    break;
                                case 3 :
                                    msg += "星期二、";
                                    break;
                                case 4 :
                                    msg += "星期三、";
                                    break;
                                case 5 :
                                    msg += "星期四、";
                                    break;
                                case 6 :
                                    msg += "星期五、";
                                    break;
                                case 7 :
                                    msg += "星期六、";
                                    break;
                                case 1 :
                                    msg += "星期天、";
                                    break;
                                default :
                                    break;
                            }
                        }


                        if(weekList.contains(""+week)){
                            //3.比较car_busy_time表的'begin_time'字段和'end_time'字段时间大小
                            String beginTime = carBusyTime.getBegin_time();
                            String endTime = carBusyTime.getEnd_time();

                            noServiceTimeList = this.getNoServiceTimeList(beginTime, endTime);
                        }

                        if(msg.endsWith("、")){
                            msg = "【" + msg.substring(0,msg.lastIndexOf("、")) + "】的 \"" + this.turnTimeString(date,noServiceTimeList) + "\"";
                        }else{
                            msg = "【" + msg + "】的 \"" + this.turnTimeString(date,noServiceTimeList) + "\"";
                        }
                    }
                }
                timeAxis.setTimeList(noServiceTimeList);	//时间轴
                timeAxis.setMsg(msg);
            }
            timeAxisList.add(timeAxis);
        }
        return timeAxisList;
    }


    /**
     * 用于去掉时间轴最后一个"240000"的元素
     * @param timeAxesList
     */
    public void removeLastTimePoint(List<TimeAxis> timeAxesList){
        for (int i = 0; i < timeAxesList.size(); i++) {
            List<String> timeList = timeAxesList.get(i).getTimeList();
            if(timeList.get(timeList.size()-1)=="240000"){
                timeList.remove("240000");
            }
        }
    }


    /**
     * 星期1-7，(2,3,4,5,6,7,1表示每天),1表示周日,2表示周一,以此类推
     * @param time   格式：yyyyMMddHHmmss
     * @return
     */
    public int getWeek(String time){
        Date date = new Date();
        Calendar cal = Calendar.getInstance();

        String t = ToolUtil.getTimestamp(time);
        date.setTime(Long.parseLong(t));
        cal.setTime(date);

        return cal.get(Calendar.DAY_OF_WEEK);
    }


    /**
     * 根据'begin_time'和'end_time'字段算出不可租时间轴（可参考"car_busy_time"表）
     * @param beginTime			格式：HHmm
     * @param endTime			格式：HHmm
     * @return
     */
    public List<String> getNoServiceTimeList(String beginTime, String endTime){
        List<String> noServiceTimeList = new ArrayList<String>();
        try {
            if(beginTime.length()<=3){	//补全类似'315'这种数据成'0315'
                String t = "";
                for(int i=0;i<(4-beginTime.length());i++){
                    t += "0";
                }
                beginTime = t + beginTime;
            }
            if(endTime.length()<=3){	//补全类似'315'这种数据成'0315'
                String t = "";
                for(int i=0;i<(4-endTime.length());i++){
                    t += "0";
                }
                endTime = t + endTime;
            }
            int beginHour =  Integer.parseInt(beginTime.substring(0,2));	//begin_time小时
            int beginMinute = Integer.parseInt(beginTime.substring(2));		//begin_time分钟
            int endHour =  Integer.parseInt(endTime.substring(0,2));		//end_time小时
            int endMinute = Integer.parseInt(endTime.substring(2));			//end_time分钟
//			logger.info("begin_time：" + beginHour + ":" + beginMinute);
//			logger.info("end_time：" + endHour + ":" + endMinute);

            if(beginHour == endHour && endMinute > beginMinute){	//例：begin_time：3:15,end_time：3:45
                noServiceTimeList = this.getNoServiceTimeListByMinute(beginHour, beginMinute, endHour, endMinute, noServiceTimeList);
            }
            if(beginHour == endHour && endMinute < beginMinute){	//例：begin_time：3:45,end_time：3:15
                noServiceTimeList = this.getNoServiceTimeListByHour(beginHour, beginMinute, 24, 0, noServiceTimeList);
                noServiceTimeList = this.getNoServiceTimeListByHour(0, 0, endHour, endMinute, noServiceTimeList);
            }
            if(beginHour < endHour){	//例：begin_time：7:00,end_time：23:15
                noServiceTimeList = this.getNoServiceTimeListByHour(beginHour, beginMinute, endHour, endMinute, noServiceTimeList);
            }
            if(beginHour > endHour){	//例：begin_time：23:00,end_time：3:15
                noServiceTimeList = this.getNoServiceTimeListByHour(beginHour, beginMinute, 24, 0, noServiceTimeList);
                noServiceTimeList = this.getNoServiceTimeListByHour(0, 0, endHour, endMinute, noServiceTimeList);
            }
        } catch (Exception e) {
            logger.error("计算不可租时间轴异常：",e);
        }
        return noServiceTimeList;
    }

    /**
     * 计算"开始时间"和"结束时间"是同一小时的不可租时间轴
     * @param beginHour
     * @param beginMinute
     * @param endHour
     * @param endMinute
     * @param noServiceTimeList
     * @return
     */
    public List<String> getNoServiceTimeListByMinute(int beginHour, int beginMinute, int endHour, int endMinute, List<String> noServiceTimeList){
        int x = (endMinute - beginMinute)/15 - 1;
//		logger.info("时间差刻度：" + x);

        String bh = "" + beginHour;
        if(bh.length()==1){
            bh = "0" + bh;
        }
        noServiceTimeList.add("" + (beginHour<=9?("0"+beginHour):beginHour) + (beginMinute<=9?("0"+beginMinute):beginMinute) + "00");
        for(int i=0;i<x;i++){
//			noServiceTimeList.add(bh + ":" + (beginMinute + (i+1)*15));
            noServiceTimeList.add(bh + (beginMinute + (i+1)*15) + "00");
        }
        noServiceTimeList.add("" + (endHour<=9?("0"+endHour):endHour) + (endMinute<=9?("0"+endMinute):endMinute) + "00");
//		logger.info(noServiceTimeList.toString());
        return noServiceTimeList;
    }

    /**
     * 计算"开始时间"和"结束时间"是不同小时的不可租时间轴
     * @param beginHour
     * @param beginMinute
     * @param endHour
     * @param endMinute
     * @param noServiceTimeList
     * @return
     */
    public List<String> getNoServiceTimeListByHour(int beginHour, int beginMinute, int endHour, int endMinute, List<String> noServiceTimeList){
        int x = (45 - beginMinute)/15;	//开始小时时间差刻度
//		logger.info("开始小时时间差刻度：" + x);

        noServiceTimeList.add("" + (beginHour<=9?("0"+beginHour):beginHour) + (beginMinute<=9?("0"+beginMinute):beginMinute) + "00");
        for(int i=0;i<x;i++){	//开始小时时间轴
            if(beginHour<=9){
//				noServiceTimeList.add("0" + beginHour + ":" + (beginMinute + (i+1)*15));
                noServiceTimeList.add("0" + beginHour + (beginMinute + (i+1)*15) + "00");
            }else{
//				noServiceTimeList.add("" + beginHour + ":" + (beginMinute + (i+1)*15));
                noServiceTimeList.add("" + beginHour + (beginMinute + (i+1)*15) + "00");
            }
        }

        for(int i=0;i<(endHour-beginHour-1);i++){	//中间小时时间轴
            if((beginHour+i+1)<=9){
//				noServiceTimeList.add("0" + (beginHour+i+1) + ":00");
//				noServiceTimeList.add("0" + (beginHour+i+1) + ":15");
//				noServiceTimeList.add("0" + (beginHour+i+1) + ":30");
//				noServiceTimeList.add("0" + (beginHour+i+1) + ":45");
                noServiceTimeList.add("0" + (beginHour+i+1) + "0000");
                noServiceTimeList.add("0" + (beginHour+i+1) + "1500");
                noServiceTimeList.add("0" + (beginHour+i+1) + "3000");
                noServiceTimeList.add("0" + (beginHour+i+1) + "4500");
            }else{
//				noServiceTimeList.add("" + (beginHour+i+1) + ":00");
//				noServiceTimeList.add("" + (beginHour+i+1) + ":15");
//				noServiceTimeList.add("" + (beginHour+i+1) + ":30");
//				noServiceTimeList.add("" + (beginHour+i+1) + ":45");
                noServiceTimeList.add("" + (beginHour+i+1) + "0000");
                noServiceTimeList.add("" + (beginHour+i+1) + "1500");
                noServiceTimeList.add("" + (beginHour+i+1) + "3000");
                noServiceTimeList.add("" + (beginHour+i+1) + "4500");
            }
        }

        int y = endMinute/15;		//结束小时时间差刻度，简化(endMinute - 0)/15
//		logger.info("结束小时时间差刻度：" + y);
        for(int i=0;i<y;i++){	//结束小时时间轴
            if(endHour<=9){
//				noServiceTimeList.add("0" + endHour + ":" + ((i*15)==0?"00":(i*15)));
                noServiceTimeList.add("0" + endHour + ((i*15)==0?"00":(i*15)) + "00");
            }else{
//				noServiceTimeList.add("" + endHour + ":" + ((i*15)==0?"00":(i*15)));
                noServiceTimeList.add("" + endHour + ((i*15)==0?"00":(i*15)) + "00");
            }
        }
        noServiceTimeList.add("" + (endHour<=9?("0"+endHour):endHour) + (endMinute<=9?("0"+endMinute):endMinute) + "00");
//		logger.info(noServiceTimeList.toString());
        return noServiceTimeList;
    }


    /**
     * 计算租期间隔天数，如20180510——20180513：间隔3天
     * @param startDate  格式：yyyyMMddHHmmss
     * @param endDate	   格式：yyyyMMddHHmmss
     * @return
     * @throws ParseException
     */
    public int daysBetween(String startDate, String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar cal = Calendar.getInstance();

        startDate = startDate.substring(0,8) + "000000";
        endDate = endDate.substring(0,8) + "000000";
        cal.setTime(sdf.parse(startDate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(endDate));
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 遍历Map处理检查租期时间轴是否可租（与'car_filter','car_busy_time','trans_reply','trans_filter','order_setting_activity','car_order_setting'等表对比）
     * @param map
     */
    public Map<String,List<TimeAxis>> checkTimeAxis(Map<String,List<TimeAxis>> map){
        Map<String,List<TimeAxis>> resultMap = new HashMap<String,List<TimeAxis>>();	            //存放对比数据map
        List<TimeAxis> transTimeAxisList = map.get("transTimeAxisList");	    //从map中找到"租期时间轴"(transTimeAxisList)
        map.remove("transTimeAxisList");
        Set<Map.Entry<String,List<TimeAxis>>> entrySet = map.entrySet();        //处理租期之外所有的时间轴集合
        for(Map.Entry<String,List<TimeAxis>> e : entrySet){
            String key = e.getKey();	            //对比的时间轴key
            List<TimeAxis> list = e.getValue();		//被对比的时间轴

            List<TimeAxis> comparedList = this.compareTimeAxis(transTimeAxisList,list);
//            logger.info("comparedList:" + comparedList);
            resultMap.put(key,comparedList);
        }
        return resultMap;
    }




    /**
     * 合并时间轴：合并某个日子的所有时间轴		ok
     * @param map
     */
    public void combineTimeAxis(Map<String,List<TimeAxis>> map){
        List<TimeAxis> combineList = new ArrayList<TimeAxis>();	//解析map时候存放数据的list

        Set<Map.Entry<String,List<TimeAxis>>> entrySet = map.entrySet();
        logger.info("开始合并时间轴");
        logger.info("entrySet：{}",entrySet.size());
        for(Map.Entry<String,List<TimeAxis>> e : entrySet){
            String key = e.getKey();
            List<TimeAxis> list = e.getValue();		//某张表的时间轴对象list，有多天
            logger.info("key：{}",key);
            for(int i=0;i<list.size();i++){
                String date = list.get(i).getDate();		//日期
                List<String> timeList = list.get(i).getTimeList();	//时间轴

                logger.info("date：{}",date);
                logger.info("timeList:{}",timeList);
                logger.info("--------------");

                logger.info("combineList长度：{}",combineList.size());
                if(combineList.size()==0){
                    TimeAxis timeAxis = new TimeAxis();
                    List<List<String>> allTimeList = new ArrayList<List<String>>();
                    allTimeList.add(timeList);
                    timeAxis.setDate(date);
                    timeAxis.setAllTimeList(allTimeList);

                    combineList.add(timeAxis);
                }else{
                    boolean flag = true;
                    for(int j=0;j<combineList.size();j++){
                        TimeAxis t = combineList.get(j);
                        if(date.equals(t.getDate())){	//合并list中有这个日期，则在对应对象的"allTimeList"属性中累加新时间轴list
                            t.getAllTimeList().add(timeList);
                            flag = false;
                            break;
                        }
                    }
                    if(flag){
                        TimeAxis timeAxis = new TimeAxis();
                        List<List<String>> allTimeList = new ArrayList<List<String>>();
                        allTimeList.add(timeList);
                        timeAxis.setDate(date);
                        timeAxis.setAllTimeList(allTimeList);

                        combineList.add(timeAxis);
                    }
                }
            }
            logger.info("==============================================");
        }

        logger.info("合并list数据");	//合并ok
        logger.info("combineList_size,{}",combineList.size());
        for(int i=0;i<combineList.size();i++){
            logger.info("date：{}",combineList.get(i).getDate());
            logger.info("getAllTimeList：{}",combineList.get(i).getAllTimeList());
            logger.info("--------------");
        }
    }
}
