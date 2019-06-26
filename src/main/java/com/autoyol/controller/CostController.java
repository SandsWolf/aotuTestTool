package com.autoyol.controller;

import com.alibaba.fastjson.JSON;
import com.autoyol.dao.CarMapper;
import com.autoyol.dao.MemberMapper;
import com.autoyol.dao.TransMapper;
import com.autoyol.dao.V43Mapper;
import com.autoyol.entity.*;
import com.autoyol.service.CostService;
import com.autoyol.util.SetDateSourceUtil;
import com.autoyol.util.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cost")
public class CostController {
    @Resource
    private CostService costService;
    @Resource
    private MemberMapper memberMapper;
    @Resource
    private CarMapper carMapper;
    @Resource
    private TransMapper transMapper;
    @Resource
    private V43Mapper v43Mapper;

    private static final Logger logger = LoggerFactory.getLogger(CostController.class);


    /**
     * 全面保障服务费
     *
     * @param environment
     * @param mobile
     * @param carPara       车辆参数：carNo 或 plateNum
     * @param rentTime
     * @param revertTime
     * @return
     */
    @RequestMapping("/abatementInsure")
    @ResponseBody
    public Result getAbatementInsure(String environment, String mobile, String carPara, String rentTime, String revertTime, String insurePricesType) {
        SetDateSourceUtil.setDataSourceName(environment);
        Result result = new Result();

        if (!ToolUtil.isMobile(mobile)) {
            result = ToolUtil.checkMobile(mobile);
            return result;
        }
        if (!ToolUtil.isTransTime(rentTime)) {
            result = ToolUtil.checkRentTime(rentTime);
            return result;
        }
        if (!ToolUtil.isTransTime(revertTime)) {
            result = ToolUtil.checkRevertTime(revertTime);
            return result;
        }

        Member member = null;
        List<Member> list = memberMapper.selectMemberInfoByMobile(mobile);
        if (list.size() > 0) {
            member = list.get(0);
        } else {
            result.setStatus(1);
            result.setMsg("success");
            result.setData("会员（" + mobile + "）不存在，请确认后再试");
            return result;
        }

        //待确认驾龄开始时间根据哪个字段判断
        String startYear = member.getDri_lic_first_time();
        result = costService.getAbatementInsure(carPara, rentTime, revertTime, startYear, insurePricesType);
        if (result.getStatus() == 0) {
            Map<String,String> responseMap = (Map<String,String>) result.getData();
            responseMap.put("startYear",startYear);
            result.setData(responseMap);
        }
        logger.info("全面保障服务费_result：{}",JSON.toJSONString(result.getData()));
        return result;
    }



    /**
     * 平台保障费
     *
     * @param environment
     * @param mobile
     * @param carPara       车辆参数：carNo 或 plateNum
     * @param rentTime
     * @param revertTime
     * @return
     */
    @RequestMapping("/insureTotalPrices")
    @ResponseBody
    public Result getInsureTotalPrices(String environment, String mobile, String carPara, String rentTime, String revertTime, String insurePricesType) {
        SetDateSourceUtil.setDataSourceName(environment);
        Result result = new Result();

        if (!ToolUtil.isMobile(mobile)) {
            result = ToolUtil.checkMobile(mobile);
            return result;
        }
        if (!ToolUtil.isTransTime(rentTime)) {
            result = ToolUtil.checkRentTime(rentTime);
            return result;
        }
        if (!ToolUtil.isTransTime(revertTime)) {
            result = ToolUtil.checkRevertTime(revertTime);
            return result;
        }

        Member member = null;
        List<Member> list = memberMapper.selectMemberInfoByMobile(mobile);
        if (list.size() > 0) {
            member = list.get(0);
        } else {
            result.setStatus(1);
            result.setMsg("success");
            result.setData("会员（" + mobile + "）不存在，请确认后再试");
            return result;
        }

        //待确认驾龄开始时间根据哪个字段判断
        String startYear = member.getDri_lic_first_time();
        result = costService.getInsureTotalPrices(carPara, rentTime, revertTime, startYear, insurePricesType);
        if (result.getStatus() == 0) {
            Map<String,String> responseMap = (Map<String,String>) result.getData();
            responseMap.put("startYear",startYear);
            result.setData(responseMap);
        }
        logger.info("平台保障费_result：{}",JSON.toJSONString(result.getData()));
        return result;
    }

    /**
     *  取还车费用
     * @param environment
     * @param cityCode
     * @param carNo
     * @param srvFlag
     * @param getCoordinate
     * @param returnCoordinate
     * @param rentTime
     * @param revertTime
     * @return
     */
    @RequestMapping("/getReturnCost")
    @ResponseBody
    public Result getReturnCost(String environment, String cityCode, String carNo, String srvFlag, String getCoordinate, String returnCoordinate, String rentTime, String revertTime) {
        SetDateSourceUtil.setDataSourceName(environment);
        Result result = new Result();

        Car car = carMapper.selectCarInfo(carNo);
        if (car == null) {
            result.setStatus(0);
            result.setMsg("success");
            result.setData("<br><span class='sign_span' style='color:red;'>carNo="+carNo+"，车辆不存在</span>");
            return result;
        }

        /*
         * <option value="1">使用取换车服务(1,1)</option>
         * <option value="2">使用取车服务(1,0)</option>
         * <option value="0">不使用服务(0,0)</option>
         */
        if ("0".equals(srvFlag)) {  //不使用服务(0,0)
            result.setStatus(0);
            result.setMsg("success");
            result.setData("<br><span class='sign_span' style='color:red;'>不使用取还车服务，不产生费用</span>");
            return result;
        }

        if ("2".equals(srvFlag)) {  //使用取车服务(1,0)
            Map<String,String> paraMap = new HashMap<String,String>();
            String[] strings = getCoordinate.split(",");	//解析经纬度
            String ALon = strings[0];
            String ALat = strings[1];
            String BLon = car.getGet_car_lon();
            String BLat = car.getGet_car_lat();

            paraMap.put("timeStr",rentTime);
            paraMap.put("cityCode",cityCode);
            paraMap.put("ALon",ALon);
            paraMap.put("ALat",ALat);
            paraMap.put("BLon",BLon);
            paraMap.put("BLat",BLat);

            Map<String,String> resultMap = costService.getReturnCost(paraMap);
            result.setStatus(2);
            result.setMsg("success");
            result.setData(resultMap);
        }

        if ("1".equals(srvFlag)) {  //使用取换车服务(1,1)
            //取车服务费
            Map<String,String> getParaMap = new HashMap<String,String>();
            String[] getStrings = getCoordinate.split(",");	//解析经纬度
            String getALon = getStrings[0];
            String getALat = getStrings[1];
            String BLon = car.getGet_car_lon();
            String BLat = car.getGet_car_lat();

            getParaMap.put("timeStr",rentTime);
            getParaMap.put("cityCode",cityCode);
            getParaMap.put("ALon",getALon);
            getParaMap.put("ALat",getALat);
            getParaMap.put("BLon",BLon);
            getParaMap.put("BLat",BLat);
            Map<String,String> getResultMap = costService.getReturnCost(getParaMap);

            //还车服务费
            Map<String,String> returnParaMap = new HashMap<String,String>();
            String[] returnStrings = returnCoordinate.split(",");	//解析经纬度
            String returnALon = returnStrings[0];
            String returnALat = returnStrings[1];

            returnParaMap.put("timeStr",revertTime);
            returnParaMap.put("cityCode",cityCode);
            returnParaMap.put("ALon",returnALon);
            returnParaMap.put("ALat",returnALat);
            returnParaMap.put("BLon",BLon);
            returnParaMap.put("BLat",BLat);
            Map<String,String> returnResultMap = costService.getReturnCost(returnParaMap);

            Map<String,Object> resultMap = new HashMap<String,Object>();
            resultMap.put("getResultMap",getResultMap);
            resultMap.put("returnResultMap",returnResultMap);
            result.setStatus(1);
            result.setMsg("success");
            result.setData(resultMap);
        }
        return result;
    }

    /**
     *  新_取还车费用
     * @param environment
     * @param cityCode
     * @param carNo
     * @param srvFlag
     * @param getCoordinate
     * @param returnCoordinate
     * @param rentTime
     * @param revertTime
     * @return
     */
    @RequestMapping("/getReturnCostNew")
    @ResponseBody
    public Result getReturnCostNew(String environment, String cityCode, String carNo, String srvFlag, String getCoordinate, String returnCoordinate, String rentTime, String revertTime, String distance) {
        SetDateSourceUtil.setDataSourceName(environment);
        Result result = new Result();

        if (distance == null) {
            distance = "";
        }

        Car car = carMapper.selectCarInfo(carNo);
        if (car == null) {
            result.setStatus(0);
            result.setMsg("success");
            result.setData("<br><span class='sign_span' style='color:red;'>carNo=" + carNo + "，车辆不存在</span>");
            return result;
        }

        /*
         * <option value="1">使用取换车服务(1,1)</option>
         * <option value="2">使用取车服务(1,0)</option>
         * <option value="0">不使用服务(0,0)</option>
         */
        if ("0".equals(srvFlag)) {  //不使用服务(0,0)
            result.setStatus(0);
            result.setMsg("success");
            result.setData("<br><span class='sign_span' style='color:red;'>不使用取还车服务，不产生费用</span>");
            return result;
        }

        if ("2".equals(srvFlag)) {  //使用取车服务(1,0)
            Map<String,String> paraMap = new HashMap<String,String>();
            String[] strings = getCoordinate.split(",");	//解析经纬度
            String ALon = strings[0];
            String ALat = strings[1];
            String BLon = car.getGet_car_lon();
            String BLat = car.getGet_car_lat();

            paraMap.put("timeStr",rentTime);
            paraMap.put("cityCode",cityCode);
            paraMap.put("ALon",ALon);
            paraMap.put("ALat",ALat);
            paraMap.put("BLon",BLon);
            paraMap.put("BLat",BLat);
            paraMap.put("distance",distance);

            Map<String,String> resultMap = costService.getReturnCostNew(paraMap);
            result.setStatus(2);
            result.setMsg("success");
            result.setData(resultMap);
        }

        if ("1".equals(srvFlag)) {  //使用取换车服务(1,1)
            //取车服务费
            Map<String,String> getParaMap = new HashMap<String,String>();
            String[] getStrings = getCoordinate.split(",");	//解析经纬度
            String getALon = getStrings[0];
            String getALat = getStrings[1];
            String BLon = car.getGet_car_lon();
            String BLat = car.getGet_car_lat();

            getParaMap.put("timeStr",rentTime);
            getParaMap.put("cityCode",cityCode);
            getParaMap.put("ALon",getALon);
            getParaMap.put("ALat",getALat);
            getParaMap.put("BLon",BLon);
            getParaMap.put("BLat",BLat);
            getParaMap.put("distance",distance);
            Map<String,String> getResultMap = costService.getReturnCostNew(getParaMap);

            //还车服务费
            Map<String,String> returnParaMap = new HashMap<String,String>();
            String[] returnStrings = returnCoordinate.split(",");	//解析经纬度
            String returnALon = returnStrings[0];
            String returnALat = returnStrings[1];

            returnParaMap.put("timeStr",revertTime);
            returnParaMap.put("cityCode",cityCode);
            returnParaMap.put("ALon",returnALon);
            returnParaMap.put("ALat",returnALat);
            returnParaMap.put("BLon",BLon);
            returnParaMap.put("BLat",BLat);
            returnParaMap.put("distance",distance);
            Map<String,String> returnResultMap = costService.getReturnCostNew(returnParaMap);

            Map<String,Object> resultMap = new HashMap<String,Object>();
            resultMap.put("getResultMap",getResultMap);
            resultMap.put("returnResultMap",returnResultMap);
            result.setStatus(1);
            result.setMsg("success");
            result.setData(resultMap);
        }
        return result;
    }


    /**
     * 计算附加驾驶保险金额
     * @param environment
     * @param num
     * @param rentTime
     * @param revertTime
     * @return
     */
    @RequestMapping("/extraDriverInsure")
    @ResponseBody
    public Result getExtraDriverInsure(String environment, String num, String rentTime, String revertTime) {
        SetDateSourceUtil.setDataSourceName(environment);
        Result result = new Result();

        if ("0".equals(num)) {
            result.setStatus(1);
            result.setMsg("success");
            result.setData("附加驾驶人数量=0，不会产生【附加驾驶人】费用");
        } else {
            result = costService.getExtraDriverInsure(rentTime,revertTime,Integer.parseInt(num));
        }
        return result;
    }


    /**
     * 计算车辆押金
     * @param environment
     * @param mobile
     * @param carNo
     * @return
     */
    @RequestMapping("/carDepositAmt")
    @ResponseBody
    public Result getCarDepositAmt(String environment, String mobile, String carNo) {
        SetDateSourceUtil.setDataSourceName(environment);
        PathIP pathIP = ToolUtil.getIP(environment);
        Result result = new Result();

        Car car = carMapper.selectCarInfo(carNo);
        if (car == null) {
            result.setStatus(1);
            result.setMsg("success");
            result.setData("车辆（carNo=" + carNo + "）不存在，请确认后再试");
            return result;
        }
        List<Member> list = memberMapper.selectMemberInfoByMobile(mobile);
        if (list.size() == 0) {
            result.setStatus(1);
            result.setMsg("success");
            result.setData("会员（" + mobile + "）不存在，请确认后再试");
            return result;
        }

        result = costService.getCarDepositAmt(pathIP.getServerIP(),list.get(0),carNo);
        return result;
    }


    /**
     * 计算违章押金
     * @param environment
     * @param mobile
     * @param carNo
     * @return
     */
    @RequestMapping("/depositAmt")
    @ResponseBody
    public Result getDepositAmt(String environment, String mobile, String carNo) {
        SetDateSourceUtil.setDataSourceName(environment);
        Result result = new Result();

        List<Member> list = memberMapper.selectMemberInfoByMobile(mobile);
        if (list.size() == 0) {
            result.setStatus(1);
            result.setMsg("success");
            result.setData("会员（" + mobile + "）不存在，请确认后再试");
            return result;
        }
        Car car = carMapper.selectCarInfo(carNo);
        if (car == null) {
            result.setStatus(1);
            result.setMsg("success");
            result.setData("车辆（carNo=" + carNo + "）不存在，请确认后再试");
            return result;
        }

        result = costService.getDepositAmt(car.getCity(),car.getPlate_num(),list.get(0));
        return result;
    }


    /**
     * 计算超里程 & 油费
     * @param environment
     * @param orderNo
     * @return
     */
    @RequestMapping("/mileOilCost")
    @ResponseBody
    public Result getMileOilCost(String environment, String orderNo) {  //,String testTotalAmt,String testCodeFlag
        SetDateSourceUtil.setDataSourceName(environment);
        PathIP pathIP = ToolUtil.getIP(environment);
        Result result = new Result();

        Trans trans = transMapper.selectTransByorderNo(orderNo);
        if (trans == null) {
            logger.info("----订单号（\" + order_no + \"）不存在----");
            result.setStatus(1);
            result.setMsg("success");
            result.setData("----订单号（" + orderNo + "）不存在----");
            return result;
        }

        Car car = carMapper.selectCarInfo(trans.getCar_no());
        if (car == null) {
            logger.info("----车辆号（\" + trans.getCar_no() + \"）不存在----");
            result.setStatus(1);
            result.setMsg("success");
            result.setData("----车辆号（" + trans.getCar_no() + "）不存在----");
            return result;
        }

        if (v43Mapper.checkTransExt(orderNo) == 0) {
            logger.info("----trans_ext表中不存在orderNo=\\\"\" + order_no + \"\\\"的数据----");
            result.setStatus(1);
            result.setMsg("success");
            result.setData("----trans_ext表中不存在orderNo=\"" + orderNo + "\"的数据----");
            return result;
        }

        List<Member> memberList = memberMapper.selectMemberInfoByMobile(trans.getRenter_phone());
        if (memberList.size() == 0) {
            logger.info("----会员（mobile=\" + trans.getRenter_phone() + \"）不存在----");
            result.setStatus(1);
            result.setMsg("success");
            result.setData("----会员（mobile=" + trans.getRenter_phone() + "）不存在----");
            return result;
        }


        OrderInfo orderInfo = v43Mapper.selectOrderMsg(orderNo);     //计算费用信息对象

        result = costService.getMileageOilCost(pathIP,orderInfo);   //,Integer.parseInt(testTotalAmt),Integer.parseInt(testCodeFlag)
        if (result.getStatus() == 0) {
            Map<String,Object> costMap = (Map<String,Object>)result.getData();

            if(orderInfo.getRenter_order_no() == null){	//普通订单或代步车券
                logger.info("----订单（" + orderNo + "）是普通订单----");
                costMap.put("isPackage","0");
            }else if(orderInfo.getRenter_order_no() != null){	//是套餐订单
                logger.info("----订单（" + orderNo + "）是套餐订单----");
                costMap.put("isPackage","1");
            }

            Map<String,String> settleMileOilCostInfo = new HashMap<>();
            if(orderInfo.getSettle() == 0){	//订单未结算
                logger.info("================订单未结算================");
            }

            if(orderInfo.getSettle() == 1){	//订单已结算
                logger.info("================订单已结算================");

                settleMileOilCostInfo.put("ownerMileageCost","" + orderInfo.getMileage_cost_owner());           //车主超里程费
                settleMileOilCostInfo.put("ownerOilCost","" + orderInfo.getOil_cost_owner());                   //车主油费
                settleMileOilCostInfo.put("ownerOilServiceCost","" + orderInfo.getOil_service_cost_owner());    //车主加油服务费
                settleMileOilCostInfo.put("newOwnerOilSubsidy","" + orderInfo.getNew_owner_oil_subsidy());      //新_车主取还车补贴（平台补贴给车主）
                settleMileOilCostInfo.put("newOwnerOilServiceCost","" + orderInfo.getNew_owner_oil_service_cost()); //新_车主加油服务费（车主补贴给平台）

                settleMileOilCostInfo.put("renterMileageCost","" + orderInfo.getMileage_cost_renter());           //租客超里程费
                settleMileOilCostInfo.put("renterOilCost","" + orderInfo.getOil_cost_renter());                   //租客油费
                settleMileOilCostInfo.put("renterOilServiceCost","" + orderInfo.getOil_service_cost_renter());    //租客加油服务费

                costMap.put("settleMileOilCostInfo",settleMileOilCostInfo);
            }

            result.setData(costMap);
            logger.info("计算超里程 & 油费_result：{}",JSON.toJSON(result.getData()));
        }


        return result;
    }

}
