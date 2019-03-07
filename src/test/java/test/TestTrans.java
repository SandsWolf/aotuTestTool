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


    public static void main(String[] args) {
        String a = "0";
        String b = "100";
        RentAmtData rentAmtData = new RentAmtData();

        rentAmtData.setDay_price("0".equals(a) ? b : a);

        System.out.println(rentAmtData.getDay_price());
    }





}
