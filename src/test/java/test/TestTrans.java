package test;

import com.alibaba.fastjson.JSON;
import com.autoyol.dao.*;
import com.autoyol.entity.PathIP;
import com.autoyol.entity.RentAmtData;
import com.autoyol.entity.Result;
import com.autoyol.entity.Trans;
import com.autoyol.service.*;
import com.autoyol.service.impl.TransServiceImpl;
import com.autoyol.util.SetDateSourceUtil;
import com.autoyol.util.ToolUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class TestTrans {
    @Resource
    private TransMapper transMapper;
    @Resource
    private TransService transService;
    @Resource
    private OtherFunctionMapper otherFunctionMapper;
    @Resource
    private MemberMapper memberMapper;
    @Resource
    private CarMapper carMapper;
    @Resource
    private MapService mapService;
    @Resource
    private CostService costService;
    @Resource
    private OtherFunctionService otherFunctionService;
    @Resource
    private TimeService timeService;
    @Resource
    private SystemConfigMapper systemConfigMapper;
    @Resource
    private HolidayMapper holidayMapper;

    private PathIP pathIP;


    @Before
    public void testBefore(){
//        String environment = "test_2";
//        SetDateSourceUtil.setDataSourceName(environment);
//        pathIP = ToolUtil.getIP(environment);
    }

    @Test
    public void testSelectcarno(){
        String environment = "test_1";
        SetDateSourceUtil.setDataSourceName(environment);
        List<Long> list = new ArrayList<>();
        list.addAll(carMapper.selectCarNo("15921237683"));
        System.out.println(list.size());
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));  //.get(index)
        }
    }
    /**
     *  设置订单流程时控制租期时间
     */
    @Test
    public void testStatusFlow(){
        //21 —>4
        String environment = "test_1";
        String orderNo = "555715221181";
        int stepFlag = 1;   //控制订单状态  1:取车  2:还车  3:双方评价
        //1、2控制流程；3、4修改时间
        int timeType = 4;   //1:订单时间快结束（revertTime快结束）	2：订单时间刚开始（rentTime刚开始）  3：订单时间快结束（revertTime 1小时后结束）—"待还车" 4：订单时间快结束（revertTime 5小时后结束）—"用车中"

        SetDateSourceUtil.setDataSourceName(environment);
        PathIP pathIP = ToolUtil.getIP(environment);

        Trans trans = transMapper.selectTransByorderNo(orderNo);
//        if(trans == null){
//            System.out.println("----null----");
//            return;
//        }

        Integer status = trans.getStatus();
        System.out.println("status = " + status);
        Integer[] statusArr = {21,3,4,12,13,18,19,20};
        System.out.println(Arrays.asList(statusArr).contains(status));

        if(Arrays.asList(statusArr).contains(status)){
            if(timeType == 1 || timeType == 2){
                System.out.println(1);
                Result result = transService.setStatusFlow(trans,pathIP,stepFlag,timeType);
                System.out.println("==============================================");
                System.out.println("result:" + JSON.toJSONString(result));
            }else{
                System.out.println(2);
                trans = transMapper.selectTransByorderNo(orderNo);
                List<String> list = transService.modifyRentTime(trans.getRent_time(),trans.getRevert_time(),timeType);   //修改订单rentTime和reverTime时间

                transMapper.updateTrans(list.get(0),list.get(1),trans.getOrder_no());
                System.out.println("修改订单租期时间成功");
            }
        }else{
            //不在这些状态内
        }
    }


    /**
     * 新节假日设置表
     */
    @Test
    public void testHoliday () {
        String environment = "test_4";
        SetDateSourceUtil.setDataSourceName(environment);
        PathIP pathIP = ToolUtil.getIP(environment);

        List<String> totalHolidayList = transService.getHolidayList(pathIP, 0);

        System.out.println("----totalHolidayList:" + JSON.toJSONString(totalHolidayList));

        if(totalHolidayList.contains("20181202000000")) {
            System.out.println(true);
        } else {
            System.out.println(false);
        }
    }

    /**
     * 测试根据手机号查询车辆数量
     */
    @Test
    public void testcarcount(){
        Integer a = carMapper.SelectCarCount("15921237683");
        System.out.println(a);

    }
    @Test
    public void testGetReturnFee () {
        String environment = "test1_autoFeeService";    // test1 取还车费用 系数库
        SetDateSourceUtil.setDataSourceName(environment);
        PathIP pathIP = ToolUtil.getIP(environment);

        String cityCode = "310100";
        String ruleDate = "20190320";

        Map<String, String> paraMap = new HashMap<>();
        paraMap.put("cityCode", cityCode);
        paraMap.put("ruleDate", ruleDate);

        String mapJson = transMapper.selectGetBackCarFeeConfig(paraMap);
        Map<String, Object> map = (Map)JSON.parseObject(mapJson);
        System.out.println("map:" + JSON.toJSONString(map));
        Integer cityCodeRes = (Integer) map.get("cityCode");
        Integer ruleDateRes = (Integer) map.get("ruleDate");
        System.out.println("cityCodeRes:" + JSON.toJSONString(cityCodeRes));
        System.out.println("ruleDateRes:" + JSON.toJSONString(ruleDateRes));

        if (cityCodeRes == Integer.parseInt(cityCode) && ruleDateRes == Integer.parseInt(ruleDate)) {
            // 【基础费】
            Map<String, Object> carBaseFeeVo = (Map<String, Object>)map.get("carBaseFeeVo");
            Map<String, Object> carBaseFeeRule = (Map<String, Object>)carBaseFeeVo.get("carBaseFeeRule");
            BigDecimal orderFee = (BigDecimal) carBaseFeeRule.get("orderFee");          // 普通订单基础费
            BigDecimal packageFee = (BigDecimal) carBaseFeeRule.get("packageFee");      // 套餐订单基础费
            System.out.println("普通订单基础费:" + JSON.toJSONString(orderFee));
            System.out.println("套餐订单基础费:" + JSON.toJSONString(packageFee));


            // 【日期系数】
            Map<String, Object> dateCoefficientVo = (Map<String, Object>)map.get("dateCoefficientVo");
            BigDecimal defaultCoefficient = (BigDecimal) dateCoefficientVo.get("defaultCoefficient");          // 默认【日期系数】
            System.out.println("默认【日期系数】:" + JSON.toJSONString(defaultCoefficient));

            List<Object> dateCoefficientDetailVoList = (List<Object>)dateCoefficientVo.get("dateCoefficientDetailVoList");
            System.out.println("dateCoefficientDetailVoList:" + JSON.toJSONString(dateCoefficientDetailVoList));
            for (int i = 0; i <dateCoefficientDetailVoList.size() ; i++) {
                Map<String, Object> dateCoefficientDetailVo = (Map<String, Object>)dateCoefficientDetailVoList.get(i);
                Integer startDate = (Integer) dateCoefficientDetailVo.get("startDate");
                Integer endDate = (Integer) dateCoefficientDetailVo.get("endDate");
                BigDecimal coefficient = (BigDecimal) dateCoefficientDetailVo.get("coefficient");
                System.out.println("startDate:" + JSON.toJSONString(startDate));
                System.out.println("endDate:" + JSON.toJSONString(endDate));
                System.out.println("coefficient:" + JSON.toJSONString(coefficient));
                System.out.println("-----------");
            }

        } else {
            System.out.println("------------> 数据有误，请检查");
            System.out.println("------------> cityCodeRes：" + cityCodeRes);
            System.out.println("------------> ruleDateRes：" + ruleDateRes);
        }



    }





    public static void main(String[] args) {
        String a = "ruleContent\": \"{\"cityCode\":310100,\"ruleDate\":20190320,\"carBaseFeeVo\":{\"cityCode\":310100,\"ruleDate\":20190320,\"carBaseFeeRule\":{\"orderFee\":30.00,\"packageFee\":50.00}},\"dateCoefficientVo\":{\"cityCode\":310100,\"ruleDate\":20190320,\"defaultCoefficient\":2.00,\"dateCoefficientDetailVoList\":[{\"detailName\":\"x\",\"startDate\":20190314,\"endDate\":20190419,\"coefficient\":1.50},{\"detailName\":\"x\",\"startDate\":20190420,\"endDate\":20190425,\"coefficient\":0.00}]},\"timePeriodCoefficientVo\":{\"cityCode\":310100,\"ruleDate\":20190320,\"defaultCoefficient\":0.00,\"timePeriodDetailVoList\":[{\"detailName\":\"x\",\"startTimeperiod\":15,\"endTimeperiod\":100,\"coefficient\":2.00},{\"detailName\":\"x\",\"startTimeperiod\":415,\"endTimeperiod\":600,\"coefficient\":3.00},{\"detailName\":\"x\",\"startTimeperiod\":1015,\"endTimeperiod\":1245,\"coefficient\":0.40},{\"detailName\":\"x\",\"startTimeperiod\":1700,\"endTimeperiod\":2000,\"coefficient\":0.60}]},\"distanceCoefficientVo\":{\"cityCode\":310100,\"ruleDate\":20190320,\"defaultCoefficient\":2.00,\"distanceDetailVoList\":[{\"detailName\":\"x\",\"distanceLeft\":0.00,\"distanceRight\":0.00,\"coefficient\":1.00},{\"detailName\":\"x\",\"distanceLeft\":0.00,\"distanceRight\":5.00,\"coefficient\":1.50},{\"detailName\":\"x\",\"distanceLeft\":5.00,\"distanceRight\":10000.00,\"coefficient\":3.00}]},\"carFeeCicrleVo\":{\"cityCode\":310100,\"ruleDate\":20190320,\"circleVersion\":1,\"defaultCoefficient\":2.00,\"carFeeCicrleRule\":[{\"id\":\"114\",\"detailName\":\"内圈\",\"cicrleContent\":\"xxxxx\",\"coefficient\":2.00,\"circleVersion\":1}]},\"carFeeChannelCoefficientVo\":{\"cityCode\":310100,\"ruleDate\":20190320,\"carFeeChannelRules\":[{\"channelName\":\"default\",\"coefficient\":1.00},{\"channelName\":\"app\",\"coefficient\":1.00},{\"channelName\":\"ota\",\"coefficient\":0.00},{\"channelName\":\"scooter\",\"coefficient\":0.00}]}}";

//        Map<String, Object> map = (Map)JSON.parseObject(a);
        System.out.println(JSON.toJSONString(a));


        Double x = 6.8120d;
    }





}
