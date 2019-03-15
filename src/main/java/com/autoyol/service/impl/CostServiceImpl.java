package com.autoyol.service.impl;

import com.alibaba.fastjson.JSON;
import com.autoyol.dao.CarMapper;
import com.autoyol.dao.MemberMapper;
import com.autoyol.dao.OtherFunctionMapper;
import com.autoyol.dao.SystemConfigMapper;
import com.autoyol.entity.*;
import com.autoyol.http.HttpResponse;
import com.autoyol.http.HttpUtils;
import com.autoyol.service.CostService;
import com.autoyol.service.OtherFunctionService;
import com.autoyol.service.SystemService;
import com.autoyol.service.TransService;
import com.autoyol.util.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.geom.Point2D;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CostServiceImpl implements CostService {
    @Resource
    private CarMapper carMapper;
    @Resource
    private MemberMapper memberMapper;
    @Resource
    private OtherFunctionService otherFunctionService;
    @Resource
    private OtherFunctionMapper otherFunctionMapper;
    @Resource
    private TransService transService;
    @Resource
    private SystemService systemService;
    @Resource
    private SystemConfigMapper systemConfigMapper;

    private static final Logger logger = LoggerFactory.getLogger(CostServiceImpl.class);


    /**
     * 全面保障服务费
     *
     * @param carPara           车辆参数：carNo 或 plateNum
     * @param rentTime          开始时间
     * @param revertTime        结束时间
     * @param driLicGetTime     驾驶证生效时间，格式：yyyy-MM-dd
     * @return
     */
    public Result getAbatementInsure(String carPara, String rentTime, String revertTime, String driLicGetTime) {
        Result result = new Result();
        Map<String,String> responseMap = new HashMap<String,String>();
        Car car = carMapper.selectCarInfo(carPara);
        if(car == null){
            logger.info("车辆不存在");
            result.setStatus(1);
            result.setMsg("success");
            result.setData("车辆（" + carPara + "）不存在，请确认后再试");
            return result;
        }

        int guidePurchasePrice = car.getGuide_purchase_price();         //车辆购置价
        double rentDays = ToolUtil.getRentDate(rentTime,revertTime);    //租期天数
        logger.info("租期天数：{}",rentDays);

        Integer[] partOne = {40,30,25,20};
        Integer[] partTwo = {50,40,35,30};
        Integer[] partThree = {80,60,50,40};

        Map<String,List<Integer>> map = new HashMap<String, List<Integer>>();
        map.put("partOne",Arrays.asList(partOne));
        map.put("partTwo",Arrays.asList(partTwo));
        map.put("partThree",Arrays.asList(partThree));

        List<Integer> priceList = new ArrayList<Integer>();
        if (guidePurchasePrice < 250000) {
            priceList = map.get("partOne");
            responseMap.put("priceList","取第一段费用：{" + priceList.get(0) + "," + priceList.get(1) + "," + priceList.get(2) + "," + priceList.get(3) + "}");
            logger.info("车辆购置价：{},取第一段费用：{},{},{},{}",new int[]{guidePurchasePrice,priceList.get(0),priceList.get(1),priceList.get(2),priceList.get(3)});
        }
        if (guidePurchasePrice >= 250000 && guidePurchasePrice <= 400000) {
            priceList = map.get("partTwo");
            responseMap.put("priceList","取第二段费用：{" + priceList.get(0) + "," + priceList.get(1) + "," + priceList.get(2) + "," + priceList.get(3) + "}");
            logger.info("车辆购置价：{},取第二段费用：{},{},{},{}",new int[]{guidePurchasePrice,priceList.get(0),priceList.get(1),priceList.get(2),priceList.get(3)});
        }
        if (guidePurchasePrice > 400000) {
            priceList = map.get("partThree");
            responseMap.put("priceList","取第三段费用：{" + priceList.get(0) + "," + priceList.get(1) + "," + priceList.get(2) + "," + priceList.get(3) + "}");
            logger.info("车辆购置价：{},取第三段费用：{},{},{},{}",new int[]{guidePurchasePrice,priceList.get(0),priceList.get(1),priceList.get(2),priceList.get(3)});
        }

        int abatementInsure = 0;  //全面保障服务费
        if (rentDays <= 7 ) {
            abatementInsure = ToolUtil.ceil(rentDays * priceList.get(0));
        }
        if (rentDays >7 && rentDays <= 15) {
            int partOnePrice = 7 * priceList.get(0);
            int partTwoPrice = ToolUtil.ceil((rentDays - 7) * priceList.get(1));
            abatementInsure = partOnePrice + partTwoPrice;
            logger.info("partOnePrice：{}",partOnePrice);
            logger.info("partTwoPrice：{}",partTwoPrice);
        }
        if (rentDays >15 && rentDays <= 25) {
            int partOnePrice = 7 * priceList.get(0);
            int partTwoPrice = (15 - 7) * priceList.get(1);
            int partThreePrice = ToolUtil.ceil((rentDays - 15) * priceList.get(2));
            abatementInsure = partOnePrice + partTwoPrice + partThreePrice;
            logger.info("partOnePrice：{}",partOnePrice);
            logger.info("partTwoPrice：{}",partTwoPrice);
            logger.info("partThreePrice：{}",partThreePrice);
        }
        if (rentDays > 25) {
            int partOnePrice = 7 * priceList.get(0);
            int partTwoPrice = (15 - 7) * priceList.get(1);
            int partThreePrice = (25 - 15) * priceList.get(2);
            int partFourPrice = ToolUtil.ceil((rentDays - 25) * priceList.get(3));
            abatementInsure = partOnePrice + partTwoPrice + partThreePrice + partFourPrice;
            logger.info("partOnePrice：{}",partOnePrice);
            logger.info("partTwoPrice：{}",partTwoPrice);
            logger.info("partThreePrice：{}",partThreePrice);
            logger.info("partFourPrice：{}",partFourPrice);
        }

        //向上取整
        Map<String,String> resultMap =  getTotalAbatementInsure(driLicGetTime,abatementInsure,1);  //计算上系数后的【全面保障服务费】

        logger.info("租期：{}",rentDays);
        logger.info("全面保障服务费：{}",resultMap.get("getIndexCost"));

        responseMap.put("guidePurchasePrice","" + guidePurchasePrice);   //车辆购置价
        responseMap.put("rentDays","" + rentDays);          //租期
        responseMap.put("index",resultMap.get("index"));    //驾龄系数
        responseMap.put("msg",resultMap.get("msg"));        //驾龄系数取值描述
        responseMap.put("abatementInsure","" + resultMap.get("getIndexCost"));  //全面保障服务费

        result.setStatus(0);
        result.setMsg("success");
        result.setData(responseMap);
        return result;
    }




    /**
     * 平台保障费
     *
     * @param carPara           车辆参数：carNo 或 plateNum
     * @param rentTime          开始时间
     * @param revertTime        结束时间
     * @param driLicGetTime     驾驶证生效时间，格式：yyyy-MM-dd
     * @return
     */
    public Result getInsureTotalPrices(String carPara, String rentTime, String revertTime, String driLicGetTime) {
        Result result = new Result();

        int insureTotalPrices = 0;  //平台保障费
        Car car = carMapper.selectInsureTotalPrices(carPara);
        if(car == null) {
            logger.info("车辆不满足判断条件");
            result.setStatus(1);
            result.setMsg("success");
            result.setData("车辆（" + carPara + "）不存在或不满足判断条件，请确认后再试");
            return result;
        }
        logger.info("car：{}",JSON.toJSONString(car));
        int insuranceValue = car.getInsurance_value();    //平台保障费/日

        double rentDays = ToolUtil.getRentDate(rentTime,revertTime);
        insureTotalPrices = ToolUtil.floor(rentDays * insuranceValue);

        Map<String,String> resultMap = getIndex(driLicGetTime);     //计算"租客驾龄系数"
        double index = Double.parseDouble(resultMap.get("index"));  //"租客驾龄系数"
        insureTotalPrices = ToolUtil.floor(ToolUtil.ceil(car.getInsurance_value() * index) * rentDays);

        logger.info("租期：{}",rentDays);
        logger.info("平台保障费：{}",resultMap.get("getIndexCost"));

        Map<String,String> responseMap = new HashMap<String,String>();
        responseMap.put("guidePurchasePrice","" + car.getGuide_purchase_price());       //车辆购置价
        responseMap.put("insuranceValue","" + car.getInsurance_value());                 //平台保障费/日
        responseMap.put("rentDays","" + rentDays);        //租期
        responseMap.put("index",resultMap.get("index"));  //驾龄系数
        responseMap.put("msg",resultMap.get("msg"));      //驾龄系数取值描述
        responseMap.put("insureTotalPrices","" + insureTotalPrices);  //平台保障费

        result.setStatus(0);
        result.setMsg("success");
        result.setData(responseMap);
        return result;
    }


    /**
     * 根据"初次领证日期"计算"租客驾龄系数"
     * @param year
     * @return
     */
    public Map<String,String> getIndex(String year){
        Map<String,String> resultMap = new HashMap<String, String>();
        double initIndex = 1.0;                     //驾龄> 1年  租客驾龄系数
        double extIndex = 1.2;                      //驾龄<= 1年  租客驾龄系数

        try {
            if (year == null || isOverOneYear(year)) {    //驾龄大于1年，或驾龄为空
                if (year == null) {
                    resultMap.put("msg","驾龄为null");
                } else {
                    resultMap.put("msg","驾龄>1年");
                }
                resultMap.put("index","" + initIndex);
            } else {
                resultMap.put("msg","驾龄<=1年");
                resultMap.put("index","" + extIndex);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error("计算【租客驾龄系数】异常：",e);
        }
        return resultMap;
    }



    /**
     *
     * @param year  对比年份：驾龄开始年份
     * @param cost  费用
     * @param type  取整类型：1：向上取整  2：向下取整  3：四舍五入（精确到个位）
     * @return
     */
    public Map<String,String> getTotalAbatementInsure (String year, int cost, int type) {
        Map<String,String> resultMap = new HashMap<String, String>();
        double initIndex = 1.0;                     //驾龄> 1年  租客驾龄系数
        double extIndex = 1.2;                      //驾龄<= 1年  租客驾龄系数

        double finalCost = 0.0;
        double index = 0.0;
        try {
            if (year == null || isOverOneYear(year)) {    //驾龄大于1年，或驾龄为空
                if (year == null) {
                    resultMap.put("msg","驾龄为null");
                } else {
                    resultMap.put("msg","驾龄>1年");
                }
                index = initIndex;
                finalCost = cost * initIndex;
            } else {
                resultMap.put("msg","驾龄<=1年");
                index = extIndex;
                finalCost = cost * extIndex;
            }
        } catch (ParseException e) {
            logger.error("计算【租客驾龄系数】异常：",e);
        }

        resultMap.put("index","" + index);      //驾龄系数
        logger.info("驾龄系数：{}",index);
        logger.info("驾龄系数费用：{}",finalCost);

        //取整规则，"getIndexCost"：费用
        if(type == 1) {
            resultMap.put("getIndexCost","" + ToolUtil.ceil(finalCost));
        } else if(type == 2) {
            resultMap.put("getIndexCost","" + ToolUtil.floor(finalCost));
        } else if(type == 3) {
            resultMap.put("getIndexCost","" + (int) ToolUtil.round(finalCost,1));
        }

        return resultMap;
    }


    /**
     * 判断时间是否超过1年
     * @param year
     * @return
     * @throws ParseException
     */
    public boolean isOverOneYear(String year) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        long getTime = sdf.parse(year).getTime();
        long nowTime = (new Date()).getTime();

        long yearTime = 365 * 24 * 60 * 60 * 1000L;     //一年的毫秒数

        return (nowTime - getTime) > yearTime;
    }


    /**
     * 获取会员车辆押金减免比例
     * @param ip
     * @param mobile
     * @return
     */
    public Integer getRelief(String ip, String mobile) {
        Integer alreadyReliefPercentage = null;    //减免比例
        String token = "";
        if (memberMapper.selectMemberInfoByMobile(mobile).size() > 0) {
            token = memberMapper.selectMemberInfoByMobile(mobile).get(0).getToken();
        } else {
            return alreadyReliefPercentage;
        }

        //查看减免押金比例接口
        String uri = "/h5/v46/relief/detail?token=memToken&requestId=1537521716971&OS=ANDROID&AppVersion=84";
        uri = uri.replace("memToken",token);

        String url = ip + uri;
        logger.info("url：{}",url);
        Map<String,String> headerMap = new HashMap<String, String>();   //请求头
        headerMap.put("Accept","application/json;version=3.0;compress=false");
        headerMap.put("Accept-Encoding","gzip, deflate");
        headerMap.put("Accept-Language","zh-CN,en-US;q=0.8");
        headerMap.put("User-Agent","Mozilla/5.0 (Linux; Android 7.1.2; vivo X9s Build/N2G47H; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36/atzuche");
        headerMap.put("X-Requested-With","com.Autoyol.auto");
        headerMap.put("Content-Type","application/json");

        HttpResponse httpResult = HttpUtils.get(headerMap,url);
//        logger.info("httpResult：{}",JSON.toJSONString(httpResult));
//        logger.info("=========");

        //减免比例
        if (httpResult.getHttpRespCode() == 200) {
            Map<String,Object> responseBodyObject = httpResult.getResponseBodyObject();
            Map<String,Object> data = (Map<String,Object>)responseBodyObject.get("data");
            logger.info("data：{}",JSON.toJSONString(data));

            Map<String,Object> memberLevelTaskInfoVo = (Map<String,Object>)data.get("totalReliefInfoVO");
            alreadyReliefPercentage = (Integer) memberLevelTaskInfoVo.get("alreadyReliefPercentage");
        } else {
            return alreadyReliefPercentage;
        }
        return alreadyReliefPercentage;
    }


    /**
     * 计算车辆押金
     * @param serverIP
     * @param
     * @param carNo
     * @return
     */
    public Result getCarDepositAmt(String serverIP, Member member, String carNo) {
        String mobile = member.getMobile();
        Result result = new Result();
        Map<String,String> resultMap = new HashMap<String,String>();

        int carDepositAmt = 300;    //内部员工，车辆押金固定300元
        resultMap.put("InternalStaff","" + member.getInternal_staff());         //内部员工标识
        if (member.getInternal_staff() == 0) {      //非内部员工
            Integer alreadyReliefPercentage = getRelief(serverIP,mobile);    //会员减免比例
            if (alreadyReliefPercentage == null) {              //调用获取会员减免比例接口失败
                logger.info("获取会员减免比例失败");
                result.setStatus(1);
                result.setMsg("success");
                result.setData("获取会员减免比例失败");
                return result;
            }
            logger.info("车辆押金减免比例：{}",alreadyReliefPercentage);

            double percent = (100 - alreadyReliefPercentage) / 100d;         //未减免比例(百分率)
            logger.info("percent：{}",percent);

            DepositConfig depositConfig = carMapper.selectCarDepositAmtInCityCode(carNo);  //押金配置表对象
            if (depositConfig == null) {
                logger.info("城市code不存在");
                depositConfig = carMapper.selectCarDepositAmtOutCityCode(carNo);
            }
            logger.info("depositConfig：{}",depositConfig.toString());

            int depositValue = depositConfig.getDeposit_value();    //原始车辆押金
            carDepositAmt = ToolUtil.ceil(depositValue * percent);     //不知道取整规则，暂时向上取整

            Float carParamRatio = 0.0f;
            Car car = carMapper.selectCarInfo(carNo);
            if (car != null && car.getBrand() != null && car.getType() != null) {
                String ratio = systemConfigMapper.selectCarParamRatio(car.getBrand(),car.getType());
                if (ratio != null) {
                    carParamRatio = Float.parseFloat(ratio);
                }
            }
            carParamRatio = carParamRatio == null? (1 + 0.0f) : (1 + carParamRatio);
            carDepositAmt = ToolUtil.ceil(carDepositAmt * carParamRatio);
            carDepositAmt = carDepositAmt<0 ? 0 : carDepositAmt;
            logger.info("车辆押金：{}",carDepositAmt);

            resultMap.put("cityCode","" + depositConfig.getCity());                 //车辆所在cityCode
            resultMap.put("surplusPrice","" + depositConfig.getSurplus_price());    //车辆残值
            resultMap.put("depositValue","" + depositValue);                        //原始车辆押金
            resultMap.put("alreadyReliefPercentage","" + alreadyReliefPercentage);  //会员减免比例
            resultMap.put("carParamRatio","" + carParamRatio);                      //车辆品牌系数
        }
        resultMap.put("CarDepositAmt","" + carDepositAmt);                      //车辆押金

        result.setStatus(0);
        result.setMsg("success");
        result.setData(resultMap);

        return result;
    }


    /**
     * 计算违章押金
     * @param cityCode
     * @param plateNum
     * @param member
     * @return
     */
    public Result getDepositAmt(String cityCode, String plateNum, Member member) {
        Result result = new Result();
        Map<String,String> resultMap = new HashMap<String,String>();

//        logger.info(otherFunctionService.getValue("special_city_code"));
//        logger.info(otherFunctionService.getValue("special_illegal_deposit_amt"));

        int renterInternalStaff = member.getInternal_staff();   //内部员工标识
        Integer depositAmt = null;
//        String cityCodes = otherFunctionService.getValue("special_city_code");
        String cityCodes = systemService.getValue("special_city_code");
        logger.info("cityCodes：{}",cityCodes);
        Integer specialDepositAmt = Integer.parseInt(systemService.getValue("special_illegal_deposit_amt"));
        logger.info("specialDepositAmt:{}",specialDepositAmt);
        // 1.如果车牌是以“粤”开头车辆，并且城市为：（北京、上海、广州、深证、南京、杭州）；租用这些车租客支付的违章押金费用调整为2000元
        if ("粤".equals(plateNum.substring(0, 1)) && cityCodes.contains(cityCode)) {
            depositAmt = specialDepositAmt;
            resultMap.put("msg","车牌是以“粤”开头，并且城市为：（北京、上海、广州、深证、南京、杭州）。违章押金费用调整为" + specialDepositAmt + "元");
        } else {
            if (renterInternalStaff == 1) {     //内部员工
                depositAmt = 1;
                resultMap.put("msg",member.getMobile() + "：内部员工");
            }
            if (renterInternalStaff == 0) {     //非内部员工
                List<DepositConfig> depositList = carMapper.selectIDepositTextCode();

                if (null != depositList && depositList.size() > 0) {
                    for (DepositConfig dc : depositList) {
                        if (dc.getDeposit_type() == 2 && dc.getCity_code() == Integer.parseInt(cityCode)) {
                            depositAmt = dc.getDeposit_value();
                            break;
                        }
                    }

                    //判断默认配置
                    if (depositAmt == null) {
                        for (DepositConfig dc : depositList) {
                            if(dc.getDeposit_type() == 4){
                                depositAmt = dc.getDeposit_value();
                                break;
                            }
                        }
                    }
                }
                resultMap.put("msg",member.getMobile() + "：非内部员工");
            }
        }
        resultMap.put("cityCode","" + cityCode);        //车辆cityCode
        resultMap.put("depositAmt","" + depositAmt);    //违章押金
        result.setStatus(0);
        result.setMsg("success");
        result.setData(resultMap);

        return result;
    }


    /**
     * 计算附加驾驶保险金额
     * @param rentTime
     * @param revertTime
     * @param num
     * @return
     */
    public Result getExtraDriverInsure(String rentTime, String revertTime, int num) {
        Result result = new Result();
        Map<String,String> resultMap = new HashMap<String,String>();

        String rentTimeTimestamp = ToolUtil.getTimestamp(rentTime);
        String revertTimeTimestamp = ToolUtil.getTimestamp(revertTime);

        long daysTimestamp = Long.parseLong(revertTimeTimestamp) - Long.parseLong(rentTimeTimestamp);
        int days = (int) (daysTimestamp/(24*60*60*1000));	//订单总天数（精确到天），如果订单开始时间和结束时间在同一天则days=0
        if (daysTimestamp%(24*60*60*1000) > 0 ) {
            days += 1;
        }

        logger.info("租期（天数进位）：{}",days);
        logger.info("fee：{}",(num * days * 20));

        resultMap.put("rentDate","" + ToolUtil.getRentDate(rentTime,revertTime));    //真实租期
        resultMap.put("days","" + days);    //租期（天数进位）
        resultMap.put("fee","" + (num * days * 20));
        result.setStatus(0);
        result.setMsg("success");
        result.setData(resultMap);
        return result;
    }


    /**
     * 计算取 or 还车费用
     * 1.租期（区分rent和revert）
     * 2.cityCode
     * 3.租客经纬度（ALon、ALat）
     * 4.车辆经纬度（BLon、BLat）
     * @param paraMap
     * @return
     */
    public Map<String,String> getReturnCost(Map<String,String> paraMap) {
        Result result = new Result();
        Map<String,String> resultMap = new HashMap<String,String>();

        String timeStr = paraMap.get("timeStr");        //租期（rentTime或revertTime）
        String cityCode = paraMap.get("cityCode");      //cityCode
        String ALon = paraMap.get("ALon");              //租客经纬度（ALon、ALat）
        String ALat = paraMap.get("ALat");              //租客经纬度（ALon、ALat）
        String BLon = paraMap.get("BLon");              //车辆经纬度（BLon、BLat）
        String BLat = paraMap.get("BLat");              //车辆经纬度（BLon、BLat）

        Integer deliveryServiceCost = 0;            //非套餐类订单基础费用
        Integer packageDeliveryServiceCost = 0;     //套餐类订单基础费用
        City city = otherFunctionMapper.selectCityByCode(cityCode);
        if (city == null) {
            deliveryServiceCost = 35;
            packageDeliveryServiceCost = 40;
        } else {
            deliveryServiceCost = city.getDelivery_service_cost();
            packageDeliveryServiceCost = city.getPackage_delivery_service_cost();
        }

        int time = Integer.parseInt(timeStr.substring(8,12));   //时 + 分 部分
        logger.info("time：{}",time);

        double overRate = this.overRate(time);          //特殊时间段系数

        Map<String,String> distanceMap = transService.getDistanceFromCar(ALon,ALat,BLon,BLat);
        double distanceRate = Double.parseDouble(distanceMap.get("index"));

        logger.info("球面距离：{}",distanceMap.get("distance1"));
        logger.info("展示距离：{}",distanceMap.get("distance2"));
        logger.info("基础费用：{}",deliveryServiceCost);
        logger.info("特殊时间段系数：{}",overRate);
        logger.info("距离系数：{}",distanceRate);

        int fee = ToolUtil.ceil(deliveryServiceCost * overRate * distanceRate);
        logger.info("取 or 还车费用：{}",fee);

        resultMap.put("distance1",distanceMap.get("distance1"));        //球面距离
        resultMap.put("distance2","" + distanceMap.get("distance2"));   //展示距离
        resultMap.put("deliveryServiceCost","" + deliveryServiceCost);  //基础费用
        resultMap.put("overRate","" + overRate);                        //特殊时间段系数
        resultMap.put("distanceRate","" + distanceRate);                //距离系数
        resultMap.put("cost","" + fee);                                 //普通订单取 or 还车费用
        resultMap.put("packageCost","" + ToolUtil.ceil(packageDeliveryServiceCost * overRate));      //套餐订单取 or 还车费用

//        result.setStatus(0);
//        result.setMsg("success");
//        result.setData(resultMap);
        return resultMap;
    }

    /**
     * 计算取 or 还车费用
     * 1.租期（区分rent和revert）
     * 2.cityCode
     * 3.租客经纬度（ALon、ALat）
     * 4.车辆经纬度（BLon、BLat）
     * @param paraMap
     * @return
     */
    public Map<String,String> getReturnCostNew(Map<String,String> paraMap) {
        Result result = new Result();
        Map<String,String> resultMap = new HashMap<String,String>();

        String timeStr = paraMap.get("timeStr");        //租期（rentTime或revertTime）
        String cityCode = paraMap.get("cityCode");      //cityCode
        String ALon = paraMap.get("ALon");              //租客经纬度（ALon、ALat）
        String ALat = paraMap.get("ALat");              //租客经纬度（ALon、ALat）
        String BLon = paraMap.get("BLon");              //车辆经纬度（BLon、BLat）
        String BLat = paraMap.get("BLat");              //车辆经纬度（BLon、BLat）

        Integer deliveryServiceCost = 0;            //非套餐类订单基础费用
        Integer packageDeliveryServiceCost = 0;     //套餐类订单基础费用
        City city = otherFunctionMapper.selectCityByCode(cityCode);
        if (city == null) {
            deliveryServiceCost = 35;
            packageDeliveryServiceCost = 40;
        } else {
            deliveryServiceCost = city.getDelivery_service_cost();
            packageDeliveryServiceCost = city.getPackage_delivery_service_cost();
        }

        int time = Integer.parseInt(timeStr.substring(8,12));   //时 + 分 部分
        logger.info("time：{}",time);

        double overRate = this.overRate(time);          //特殊时间段系数

        Map<String,String> distanceMap = transService.getDistanceNewRow(ALon,ALat,BLon,BLat,paraMap.get("distance"));
        double distanceRate = Double.parseDouble(distanceMap.get("distanceRate"));      //距离系数


//        double rangeRate = 1.0d;       //取还车圈层系数
        double rangeRate = 0.8d;        //圈内系数从1变更为0.8
        String coordinate = city.getFree_address_range();   //免费取还车范围坐标集
//        logger.info("coordinate：{}",coordinate);
        if (coordinate != null && coordinate != "") {
            String[] points =  coordinate.split("],");
            double[] lons = new double[points.length];
            double[] lats = new double[points.length];

            for (int i = 0; i < points.length; i++) {
                if (i != (points.length - 1)) {
                    String lon = points[i].substring(1,points[i].lastIndexOf(","));
                    lons[i] = Double.parseDouble(lon);

                    String lat = points[i].substring(points[i].lastIndexOf(",") + 1);
                    lats[i] = Double.parseDouble(lat);
                } else {
                    String lon = points[i].substring(1,points[i].lastIndexOf(","));
                    lons[i] = Double.parseDouble(lon);

                    String lat = points[i].substring((points[i].lastIndexOf(",") + 1),points[i].lastIndexOf("]"));
                    lats[i] = Double.parseDouble(lat);
                }
            }

            if (!isInPolygon(Double.parseDouble(ALon),Double.parseDouble(ALat),lons,lats)) {
//                rangeRate = 1.4d;     //外圈系数
                rangeRate = 1.1d;       //圈外系数从1.4变更为1.1
            }
        }


        logger.info("球面距离：{}",distanceMap.get("distance1"));
        logger.info("球面距离系数：{}",distanceMap.get("ballRatio"));
        logger.info("展示距离：{}",distanceMap.get("distance2"));

        logger.info("基础费用：{}",deliveryServiceCost);
        logger.info("特殊时间段系数：{}",overRate);
        logger.info("距离系数：{}",distanceRate);

        logger.info("取还车圈层系数：{}",rangeRate);

        int fee = ToolUtil.ceil(deliveryServiceCost * overRate * distanceRate * rangeRate);
        logger.info("取 or 还车费用：{}",fee);

        resultMap.put("distance1",distanceMap.get("distance1"));        //球面距离
        resultMap.put("distance2","" + distanceMap.get("distance2"));   //展示距离
        resultMap.put("deliveryServiceCost","" + deliveryServiceCost);  //基础费用
        resultMap.put("packageDeliveryServiceCost","" + packageDeliveryServiceCost);    //套餐基础费用
        resultMap.put("ballRatio","" + distanceMap.get("ballRatio"));   //球面距离系数
        resultMap.put("overRate","" + overRate);                        //特殊时间段系数
        resultMap.put("distanceRate","" + distanceRate);                //距离系数
        resultMap.put("rangeRate","" + rangeRate);                      //取还车圈层系数
        resultMap.put("cost","" + fee);                                 //普通订单取 or 还车费用
        resultMap.put("packageCost","" + ToolUtil.ceil(packageDeliveryServiceCost * overRate * rangeRate));      //套餐订单取 or 还车费用

//        result.setStatus(0);
//        result.setMsg("success");
//        result.setData(resultMap);
        return resultMap;
    }


    /**
     * 计算特殊时间段系数（取还车服务费用）
     * @param time
     * @return
     */
    private double overRate(int time) {
        double overRate = 1.0d;
        if(time >= 0 && time <= 830) {
            overRate = 2.3d;
        } else if(time > 830 && time <= 1000) {
            overRate = 1.6d;
        } else if(time >= 1700 && time <= 2000) {
            overRate = 1.6d;
        } else if(time >= 2100 && time < 2400) {
            overRate = 2.3d;
        }
        return overRate;
    }




    /**
     * 根据order对象计算超里程费用和油费，返回result对象
     * @param orderInfo
     * @return
     */
    public Result getMileageOilCost (PathIP pathIP, OrderInfo orderInfo) {  //,int testTotalAmt,int testCodeFlag
        Map<String, Object> costMap = new HashMap<>();
        Result result = new Result();

//        Double rentDate = util.getRentDate(order.getRent_time(), order.getRevert_time());		//租期
        Double rentDate = ToolUtil.getRentDate(orderInfo.getRent_time(), orderInfo.getRevert_time());   //租期
        Integer fuelSize = orderInfo.getOil_volume();												//油箱容量
//        Double gasPrice = orderInfo.getMolecule() / 10d;											//油价
        Double gasPrice = (orderInfo.getMolecule() * 1d) / orderInfo.getDenominator();		        //油价
//        Double oilScaleDenominator = orderInfo.getOil_scale_denominator();                         //油表刻度分母
        Double oilScaleDenominator =  16d;
        Integer serviceCost = orderInfo.getServiceCost();											//油量服务费
        Integer totalAmt = orderInfo.getTotal_amt();												//车辆押金
        String renterToken = orderInfo.getToken();													//租客token
        Integer settle = orderInfo.getSettle();

        Integer ownerGetMileage = orderInfo.getGet_car_mileage_owner();								//车主取车时里程数
        Integer ownerReturnMileage = orderInfo.getReturn_car_mileage_owner();						//车主还车时里程数
        Integer ownerGetGraduation = orderInfo.getGet_car_oil_scale_owner();						//车主取车时油量刻度
        Integer ownerReturnGraduation = orderInfo.getReturn_car_oil_scale_owner();					//车主还车时油量刻度
        Integer ownerDayMileage = orderInfo.getDay_miles_owner();									//车主日限里程数：0表示不限
        Integer ownerDayPrice = orderInfo.getGuide_day_price();									    //车主日均价

        Integer renterGetMileage = orderInfo.getGet_car_mileage_renter();							//租客取车时里程数
        Integer renterReturnMileage = orderInfo.getReturn_car_mileage_renter();						//租客还车时里程数
        Integer renterGetGraduation = orderInfo.getGet_car_oil_scale_renter();						//租客取车时油量刻度
        Integer renterReturnGraduation = orderInfo.getReturn_car_oil_scale_renter();				//租客还车时油量刻度
        Integer renterDayMileage = orderInfo.getDay_miles_renter();									//租客日限里程数：0表示不限
        Integer renterDayPrice = orderInfo.getGuide_day_price();									//租客日均价

        Integer ownerMileCost = null;		    //车主超里程费用
        Integer ownerOilCost = null;		    //车主油费
        Integer ownerOilServiceCost = null;     //车主加油服务费
        Integer platformServiceCost = null;     //平台加油服务费（问车主收取）
        Integer ownerOilSubsidy = null;         //取还车油费补贴（平台补贴车主）
        Integer ownerOilGetSubsidy = null;      //取还车油费补贴_取车部分
        Integer ownerOilReturnSubsidy = null;   //取还车油费补贴_还车部分

        Integer renterMileCost = null;		    //租客超里程费用
        Integer renterOilCost = null;		    //租客油费
        Integer renterOilServiceCost = null;    //租客加油服务费
        Integer totalConsumeAmt = null;         //租客总消费
        Integer codeFlag = null;                //"费用明细"接口调用标识：0调用接口失败，1调用接口成功
        String ownerOilCostMsg = null;
        String ownerMileCostMsg = null;
        String renterOilCostMsg = null;
        String renterMileCostMsg = null;

        OilCostInfo ownerOilCostInfo = null;                //车主油费计算结果info
        MileCostInfo ownerMileCostInfo = null;              //车主超里程费计算结果info
        OilCostInfo renterOilCostInfo = null;               //租客油费计算结果info
        MileCostInfo renterMileCostInfo = null;             //租客超里程费计算结果info
        MileOilCostDataInfo mileOilCostDataInfo = null;     //超里程&油费计算公共数据info

        Map<String,Object> paramMap = new HashMap<>();  //计算用参数map
        if (ownerGetMileage != null && ownerReturnMileage != null && ownerGetGraduation != null && ownerReturnGraduation != null && renterGetMileage != null && renterReturnMileage != null && renterGetGraduation != null && renterReturnGraduation != null) {
            //车主油费
            Map<String,String> ownerOilCostMap = null;
            try {
                paramMap.put("settle",settle);
                //车主刻度
                paramMap.put("ownerGetGraduation",ownerGetGraduation);
                paramMap.put("ownerReturnGraduation",ownerReturnGraduation);
                paramMap.put("ownerGetMileage",ownerGetMileage);
                paramMap.put("ownerReturnMileage",ownerReturnMileage);
                //租客刻度
                paramMap.put("renterGetGraduation",renterGetGraduation);
                paramMap.put("renterReturnGraduation",renterReturnGraduation);
                paramMap.put("renterGetMileage",renterGetMileage);
                paramMap.put("renterReturnMileage",renterReturnMileage);

                paramMap.put("oilScaleDenominator",oilScaleDenominator);
                paramMap.put("fuelSize",fuelSize);
                paramMap.put("gasPrice",gasPrice);

//                if (systemService.getValue("give_owner_server_oil_cost_limit") != "") {
//                    paramMap.put("giveOwnerServerOilCostLimit",Integer.parseInt(systemService.getValue("give_owner_server_oil_cost_limit")));
//                }

                ownerOilCostMap = ownerOilCost(paramMap);
            } catch (Exception e) {
                logger.error("计算【车主端油费】异常：",e);
                result.setStatus(1);
                result.setMsg("success");
                result.setData("----车主端油费计算报错，请确认数据是否正常----");
                return result;
            }
            ownerOilCost = Integer.parseInt(ownerOilCostMap.get("ownerOilCost"));
            ownerOilServiceCost = Integer.parseInt(ownerOilCostMap.get("ownerOilServiceCost"));
            platformServiceCost = Integer.parseInt(ownerOilCostMap.get("platformServiceCost"));
            ownerOilSubsidy = Integer.parseInt(ownerOilCostMap.get("ownerOilSubsidy"));
            ownerOilGetSubsidy = Integer.parseInt(ownerOilCostMap.get("ownerOilGetSubsidy"));
            ownerOilReturnSubsidy = Integer.parseInt(ownerOilCostMap.get("ownerOilReturnSubsidy"));
            ownerOilCostMsg = ownerOilCostMap.get("msg");


            //超里程&油费计算公共数据info
            mileOilCostDataInfo = new MileOilCostDataInfo();
            mileOilCostDataInfo.setOwnerGetMileage("" + ownerGetMileage);
            mileOilCostDataInfo.setOwnerReturnMileage("" + ownerReturnMileage);

            //车主超里程费用
            ownerGetMileage = renterGetMileage;			//新需求：车主也按照租客里程数计算
            ownerReturnMileage = renterReturnMileage;	//新需求：车主也按照租客里程数计算
            if(ownerDayMileage == 0){		//日限里程：不限    无超里程费用
                ownerMileCost = 0;
                ownerMileCostMsg = "车主超里程费用 = 0，车主日限里程为不限(0)";
            }else{
                if((ownerReturnMileage - ownerGetMileage) > ownerDayMileage * rentDate){
                    double dayPrice = 0;
                    if(ownerDayPrice < 100){
                        dayPrice = 0.5;
                        ownerMileCostMsg = "车主【超里程费】用租客\"里程数\"计算。车主\"日均价\"<100，按照100算。";
                    }

                    if(ownerDayPrice > 1000){
                        dayPrice = 5;
                        ownerMileCostMsg = "车主【超里程费】用租客\"里程数\"计算。车主\"日均价\">1000，按照1000算。";
                    }

                    if(ownerDayPrice >= 100 && ownerDayPrice <= 1000){
                        dayPrice = ownerDayPrice * 5/1000d;
                        ownerMileCostMsg = "车主【超里程费】用租客\"里程数\"计算。车主\"日均价\"正常计算。";
                    }

                    Map<String,String> ownerMileageCostMap = null;  //,testTotalAmt,testCodeFlag
                    try {
                        paramMap.put("settle",settle);
                        paramMap.put("ownerGetMileage",ownerGetMileage);
                        paramMap.put("ownerReturnMileage",ownerReturnMileage);
                        paramMap.put("ownerDayMileage",ownerDayMileage);
                        paramMap.put("rentDate",rentDate);
                        paramMap.put("dayPrice",dayPrice);
                        paramMap.put("totalAmt",totalAmt);
                        paramMap.put("renterToken",renterToken);
                        paramMap.put("orderNo",orderInfo.getOrder_no());
                        paramMap.put("pathIP",pathIP);

                        ownerMileageCostMap = ownerMileageCost(paramMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("计算【车主端超里程费】异常：",e);
                        result.setStatus(1);
                        result.setMsg("success");
                        result.setData("----车主端超里程费计算报错，请确认数据是否正常----");
                        return result;
                    }

                    if (settle == 1) {
                        if (ownerMileageCostMap.get("totalConsumeAmt") == null && ownerMileageCostMap.get("codeFlag") == null) {
                            totalConsumeAmt = 0;
                            codeFlag = 0;
                        }else {
                            totalConsumeAmt = Integer.parseInt(ownerMileageCostMap.get("totalConsumeAmt"));
                            codeFlag = Integer.parseInt(ownerMileageCostMap.get("codeFlag"));
                        }
                    }
                    ownerMileCost = Integer.parseInt(ownerMileageCostMap.get("ownerMileCost"));

                    if (ownerMileageCostMap.get("msg") != null) {
                        ownerMileCostMsg += ownerMileageCostMap.get("msg");
                    }
                }else if(ownerGetMileage > ownerReturnMileage){	//还车里程数比取车里程数大
                    ownerMileCost = 0;
                    ownerMileCostMsg = "车主超里程费用 = 0，原因：取车里程数 > 还车里程数";
                }else if((ownerReturnMileage - ownerGetMileage) <= ownerDayMileage * rentDate){	//不满足条件
                    ownerMileCost = 0;
                    ownerMileCostMsg = "车主超里程费用 = 0，原因：不满足条件:N = A2 - A1 > 车辆日限里程数L × 租期T";
                }
            }



            //租客油费
            Map<String,String> renterOilCostMap = null;
            try {
                paramMap.put("renterGetGraduation",renterGetGraduation);
                paramMap.put("renterReturnGraduation",renterReturnGraduation);
                paramMap.put("fuelSize",fuelSize);
                paramMap.put("gasPrice",gasPrice);
                paramMap.put("oilScaleDenominator",oilScaleDenominator);

                renterOilCostMap = renterOilCost(paramMap);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("计算【租客端油费】异常：",e);
                result.setStatus(1);
                result.setMsg("success");
                result.setData("----租客端油费计算报错，请确认数据是否正常----");
                return result;
            }
            renterOilCost = Integer.parseInt(renterOilCostMap.get("renterOilCost"));
            renterOilServiceCost = Integer.parseInt(renterOilCostMap.get("renterOilServiceCost"));
            renterOilCostMsg = renterOilCostMap.get("msg");

            //租客超里程费用
            if(renterDayMileage == 0){	//日限里程：不限    无超里程费用
                renterMileCost = 0;
                renterMileCostMsg = "租客超里程费用=0，租客日限里程为不限(0)";
            }else{
                if((renterReturnMileage - renterGetMileage) > renterDayMileage * rentDate){
                    double dayPrice = 0;
                    if(renterDayPrice < 100){
                        dayPrice = 0.5;
                        renterMileCostMsg = "租客\"日均价\"<100，按照100算。";
                    }

                    if(renterDayPrice > 1000){
                        dayPrice = 5;
                        renterMileCostMsg = "租客\"日均价\">1000，按照1000算。";
                    }

                    if(renterDayPrice >= 100 && renterDayPrice <= 1000){
                        dayPrice = renterDayPrice * 5/1000d;
                        renterMileCostMsg = "租客\"日均价\"正常计算。";
                    }
                    Map<String,String> renterMileageCostMap = null;
                    try {
                        paramMap.put("renterGetMileage",renterGetMileage);
                        paramMap.put("renterReturnMileage",renterReturnMileage);
                        paramMap.put("renterDayMileage",renterDayMileage);
                        paramMap.put("rentDate",rentDate);
                        paramMap.put("dayPrice",dayPrice);

                        renterMileageCostMap = renterMileageCost(paramMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("计算【租客端超里程费】异常：",e);
                        result.setStatus(1);
                        result.setMsg("success");
                        result.setData("----租客端超里程费计算报错，请确认数据是否正常----");
                        return result;
                    }
                    renterMileCost = Integer.parseInt(renterMileageCostMap.get("renterMileCost"));
                }else if(renterGetMileage > renterReturnMileage){	//还车里程数比取车里程数大
                    renterMileCost = 0;
                    renterMileCostMsg = "租客超里程费用=0，原因：取车里程数>还车里程数";
                }else if((renterReturnMileage - renterGetMileage) <= renterDayMileage * rentDate){	//不满足条件
                    renterMileCost = 0;
                    renterMileCostMsg = "租客超里程费用=0，原因：不满足条件:M=B2-B1>车辆日限里程数L×租期T";
                }
            }

            if (orderInfo.getCar_owner_type() == 35) {      //代管车：车主不收取超里程&油费
                ownerOilCost = 0;
                ownerOilServiceCost = 0;
                ownerOilCostMsg = "代管车：车主不收取油费";

                ownerMileCost = 0;
                ownerMileCostMsg = "代管车：车主不收取超里程费";
            }


            //超里程&油费计算公共数据info
            mileOilCostDataInfo.setCarNo(orderInfo.getCar_no());
            mileOilCostDataInfo.setRenterNo(orderInfo.getRenter_no());
            mileOilCostDataInfo.setOwnerNo(orderInfo.getOwner_no());
            mileOilCostDataInfo.setRentTime(ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(orderInfo.getRent_time()))));
            mileOilCostDataInfo.setRevertTime(ToolUtil.getTime1(Long.parseLong(ToolUtil.getTimestamp(orderInfo.getRevert_time()))));
            mileOilCostDataInfo.setRentDate("" + rentDate);
            mileOilCostDataInfo.setOilVolume("" + fuelSize);
            mileOilCostDataInfo.setMolecule("" + gasPrice);
            mileOilCostDataInfo.setOilScaleDenominator("" + oilScaleDenominator);
            if (settle == 1) {
                mileOilCostDataInfo.setTotalConsumeAmt("" + totalConsumeAmt);
                mileOilCostDataInfo.setCodeFlag("" + codeFlag);
            } else {
                mileOilCostDataInfo.setTotalConsumeAmt("");
                mileOilCostDataInfo.setCodeFlag("");
            }
            mileOilCostDataInfo.setStatus("" + orderInfo.getStatus());
            mileOilCostDataInfo.setSettle("" + settle);
            mileOilCostDataInfo.setTotalAmt("" + totalAmt);


            //车主油费计算结果info
            ownerOilCostInfo = new OilCostInfo();
            ownerOilCostInfo.setGetCarOilScale("" + ownerGetGraduation);
            ownerOilCostInfo.setReturnCarOilScale("" + ownerReturnGraduation);
//                ownerOilCostInfo.setOilVolume("" + fuelSize);
//                ownerOilCostInfo.setMolecule("" + gasPrice);
            ownerOilCostInfo.setOilCost("" + ownerOilCost);
            if (renterOilServiceCost == 0 && settle == 1 && orderInfo.getCar_owner_type() != 35) {
                ownerOilServiceCost = 0;
                ownerOilCostMsg += "租客端\"加油服务费\"=0，所以车主端\"加油服务费\"=0";
            }
            ownerOilCostInfo.setOilServiceCost("" + ownerOilServiceCost);
            ownerOilCostInfo.setPlatformServiceCost("" + platformServiceCost);
            ownerOilCostInfo.setOwnerOilSubsidy("" + ownerOilSubsidy);
            ownerOilCostInfo.setOwnerOilGetSubsidy("" + ownerOilGetSubsidy);
            ownerOilCostInfo.setOwnerOilReturnSubsidy("" + ownerOilReturnSubsidy);
            if (ownerOilCostMsg ==  null || ownerOilCostMsg == "") {
                ownerOilCostInfo.setMsg("");
            } else {
                ownerOilCostInfo.setMsg(ownerOilCostMsg);
            }

            //车主超里程费计算结果info
            ownerMileCostInfo = new MileCostInfo();
            ownerMileCostInfo.setGetCarMileage("" + ownerGetMileage);
            ownerMileCostInfo.setReturnCarMileage("" + ownerReturnMileage);
            ownerMileCostInfo.setDayMiles("" + ownerDayMileage);
            ownerMileCostInfo.setDayPrice("" + ownerDayPrice);
            ownerMileCostInfo.setMileCost("" + ownerMileCost);
//                ownerMileCostInfo.setTotalConsumeAmt("" + totalConsumeAmt);
//                ownerMileCostInfo.setCodeFlag("" + codeFlag);
            if (ownerMileCostMsg ==  null || ownerMileCostMsg == "") {
                ownerMileCostInfo.setMsg("");
            } else {
                ownerMileCostInfo.setMsg(ownerMileCostMsg);
            }


            //租客油费计算结果info
            renterOilCostInfo = new OilCostInfo();
            renterOilCostInfo.setGetCarOilScale("" + renterGetGraduation);
            renterOilCostInfo.setReturnCarOilScale("" + renterReturnGraduation);
//                renterOilCostInfo.setOilVolume("" + fuelSize);
//                renterOilCostInfo.setMolecule("" + gasPrice);
            renterOilCostInfo.setOilCost("" + renterOilCost);
            renterOilCostInfo.setOilServiceCost("" + renterOilServiceCost);
            if (renterOilCostMsg ==  null || renterOilCostMsg == "") {
                renterOilCostInfo.setMsg("");
            } else {
                renterOilCostInfo.setMsg(renterOilCostMsg);
            }

            //租客超里程费计算结果info
            renterMileCostInfo = new MileCostInfo();
            renterMileCostInfo.setGetCarMileage("" + renterGetMileage);
            renterMileCostInfo.setReturnCarMileage("" + renterReturnMileage);
            renterMileCostInfo.setDayMiles("" + renterDayMileage);
            renterMileCostInfo.setDayPrice("" + renterDayPrice);
            renterMileCostInfo.setMileCost("" + renterMileCost);
            if (renterMileCostMsg ==  null || renterMileCostMsg == "") {
                renterMileCostInfo.setMsg("");
            } else {
                renterMileCostInfo.setMsg(renterMileCostMsg);
            }

            costMap.put("mileOilCostDataInfo",mileOilCostDataInfo);
            costMap.put("ownerOilCostInfo",ownerOilCostInfo);
            costMap.put("ownerMileCostInfo",ownerMileCostInfo);
            costMap.put("renterOilCostInfo",renterOilCostInfo);
            costMap.put("renterMileCostInfo",renterMileCostInfo);

            result.setStatus(0);
            result.setMsg("success");
            result.setData(costMap);
            logger.info("----订单未结算，实时计算超里程&油费----");
        } else {
            result.setStatus(1);
            result.setMsg("success");
            result.setData("----订单未结算，但trans_ext表计算费用字段有为null，无法计算费用----");
            logger.info("----订单未结算，但trans_ext表计算费用字段有为null，无法计算费用----");
        }
        return result;
    }

    /**
     * 调"费用明细"接口获取总消费金额
     * @param pathIP
     * @param orderNo
     * @param renterToken
     * @return
     */
    public Map<String,Integer> transRenterCostDetail (PathIP pathIP, String orderNo, String renterToken) {
        Map<String,Integer> costMap = new HashMap<String,Integer>();
        String uri = "v33/trans/renter/cost/detail";
        Map<String,String> headerMap = new HashMap<String, String>();
        headerMap.put("Accept", "application/json;version=3.0;compress=false");
        headerMap.put("Content-Type", "application/json");
        headerMap.put("User-Agent", "AutoyolEs_web");

        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("orderNo",orderNo);
        paraMap.put("token",renterToken);
        paraMap.put("requestId","336");

        HttpResponse httpResult = HttpUtils.post(headerMap, (pathIP.getServerIP() + uri), paraMap, false, false);
        Integer totalConsumeAmt = 0;        //总消费
        Integer codeFlag = 0;               //接口调用标识：0调用接口失败，1调用接口成功
        if (httpResult.getHttpRespCode() == 200) {
            Map<String,Object> responseBodyObject = httpResult.getResponseBodyObject();
            if ("000000".equals(responseBodyObject.get("resCode"))) {
                Map<String,Object> dataMap = (Map<String,Object>)responseBodyObject.get("data");
                totalConsumeAmt = (Integer)dataMap.get("totalConsumeAmt");
                codeFlag = 1;
            }
        }
        costMap.put("totalConsumeAmt",totalConsumeAmt);
        costMap.put("codeFlag",codeFlag);
        return costMap;
    }

    /**
     * 车主端超里程费用计算(向上取整)
     * @param paramMap
     * @return
     */
    public Map<String,String> ownerMileageCost(Map<String,Object> paramMap){   //,int testTotalAmt,int testCodeFlag
        logger.info("========>ownerMileageCostParam：{}",JSON.toJSONString(paramMap));
        Integer settle = (Integer) paramMap.get("settle");
        Integer getMileage = (Integer) paramMap.get("ownerGetMileage");         //车主取车时里程数
        Integer returnMileage = (Integer) paramMap.get("ownerReturnMileage");   //车主还车时里程数
        Integer dayMileage = (Integer) paramMap.get("ownerDayMileage");         //车主日限里程数
        Double rentDate = (Double) paramMap.get("rentDate");                    //租期
        Double dayPrice = (Double) paramMap.get("dayPrice");                    //日均价
        Integer totalAmt = (Integer) paramMap.get("totalAmt");
        String renterToken = (String) paramMap.get("renterToken");
        String orderNo = (String) paramMap.get("orderNo");
        PathIP pathIP = (PathIP) paramMap.get("pathIP");

        Map<String,String> ownerMileageCostMap = new HashMap<>();
        int ownerMileCost = ToolUtil.ceil(((returnMileage - getMileage) - dayMileage * rentDate) * dayPrice);    //车主超里程费用

        if (settle == 1) {  //结算时才触发【车主端收到的最大超里程费用 <= (租车押金-租客总消费)】逻辑
            int initialOwnerMileCost = ownerMileCost;   //初始的车主超里程费用

//            Map<String,Integer> transRenterCostDetail = transRenterCostDetail(pathIP,orderNo,renterToken);      //调"费用明细"接口获取总消费金额
        /*
        调试代码
         */
//        Map<String,Integer> transRenterCostDetail = new HashMap<>();
//        transRenterCostDetail.put("totalConsumeAmt",testTotalAmt);
//        transRenterCostDetail.put("codeFlag",testCodeFlag);

            //车主端收到的最大超里程费用 <= (租车押金-租客总消费)
//            if (ownerMileCost >= (totalAmt - transRenterCostDetail.get("totalConsumeAmt"))) {
//                if ((totalAmt - transRenterCostDetail.get("totalConsumeAmt")) >= 0) {
//                    ownerMileCost = totalAmt - transRenterCostDetail.get("totalConsumeAmt");
//                } else {
//                    ownerMileCost = 0;
//                }
//                ownerMileageCostMap.put("msg","由于初始超里程费：" + initialOwnerMileCost + "，>(租车押金-租客总消费)的费用：" + (totalAmt - transRenterCostDetail.get("totalConsumeAmt")) + "，所以超里程费 = (租车押金-租客总消费)");
//            }

            ownerMileageCostMap.put("ownerMileCost","" + ownerMileCost);                                    //车主超里程费用
//            ownerMileageCostMap.put("totalConsumeAmt","" + transRenterCostDetail.get("totalConsumeAmt"));   //租客总消费
//            ownerMileageCostMap.put("codeFlag","" + transRenterCostDetail.get("codeFlag"));                 //"费用明细"接口调用标识：0调用接口失败，1调用接口成功
//            ownerMileageCostMap.put("totalConsumeAmt","");   //租客总消费
//            ownerMileageCostMap.put("codeFlag","");                 //"费用明细"接口调用标识：0调用接口失败，1调用接口成功
        } else {
            ownerMileageCostMap.put("ownerMileCost","" + ownerMileCost);                                    //车主超里程费用
        }
        return ownerMileageCostMap;
    }


    /**
     * 车主端油费计算(向下取整)，可以为负数
     * @param paramMap
     * @return
     */
    public Map<String,String> ownerOilCost(Map<String,Object> paramMap) {
        logger.info("========>ownerOilCostParam：{}",JSON.toJSONString(paramMap));
        Integer ownerGetGraduation = (Integer) paramMap.get("ownerGetGraduation");          //车主取车时邮箱刻度
        Integer ownerReturnGraduation = (Integer) paramMap.get("ownerReturnGraduation");    //车主还车时邮箱刻度
        Integer ownerGetMileage = (Integer) paramMap.get("ownerGetMileage");                //车主取车时里程数
        Integer ownerReturnMileage = (Integer) paramMap.get("ownerReturnMileage");          //车主还车时里程数

        Integer renterGetGraduation = (Integer) paramMap.get("renterGetGraduation");        //租客取车时邮箱刻度
        Integer renterReturnGraduation = (Integer) paramMap.get("renterReturnGraduation");  //租客还车时邮箱刻度
        Integer renterGetMileage = (Integer) paramMap.get("renterGetMileage");              //租客取车时里程数
        Integer renterReturnMileage = (Integer) paramMap.get("renterReturnMileage");        //租客还车时里程数

        Double oilScaleDenominator = (Double) paramMap.get("oilScaleDenominator");        //油表刻度分母
        Integer fuelSize = (Integer) paramMap.get("fuelSize");                          //邮箱容积
        Double gasPrice = (Double) paramMap.get("gasPrice");                            //油价

        /*
        计算车主给平台加油服务费 — 车主端增加加油服务费收费项
         */
        int platformServiceCost = 0;        //平台加油服务费
        if ((renterGetGraduation > ownerGetGraduation) && (ownerGetGraduation <= 4)) {
            platformServiceCost = -25;
        }

        /*
        计算车主端"油费" & "加油服务费" — 车主处油费结算全部根据租客交接油量差额计算，不再单独结算
         */
        ownerGetGraduation = renterGetGraduation;
        ownerReturnGraduation = renterReturnGraduation;

        int m = ownerGetGraduation - ownerReturnGraduation;	//油量刻度差
        Map<String,String> ownerOilCostMap = new HashMap<>();
        int ownerOilCost = 0;           //车主油费
        int ownerOilServiceCost = 0;    //车主加油服务费
        String msg = "";
        msg = "车主【油费】用租客\"油量刻度\"计算。";

        ownerOilCost = ToolUtil.floor((m / oilScaleDenominator * fuelSize) * gasPrice);

        if (ownerOilCost > 0) {
            ownerOilServiceCost = ToolUtil.floor(ownerOilCost / 4d);
        }

//        if (m == 1) {
//            ownerOilCost = 0;
//            ownerOilCost = 0;
//            msg += "由于邮箱刻度差=1，所以不计算油费。";
//        }

//        if (settle == 0) {
//            ownerOilServiceCost = 0;
//            msg += "订单没结算，不计算【加油服务费】";
//        }


        /*
        车主端增加取还车油费补贴
         */
        int mileageLimit = 10;                  //门槛公里数
        double subsidizedUnitPrice = 0.8d;      //取还车补贴单价

        int ownerGetSubsidizedCost = 0;         //取车补贴(给车主)
        if ((renterGetMileage - ownerGetMileage) > mileageLimit) {
            ownerGetSubsidizedCost = ToolUtil.ceil((renterGetMileage - ownerGetMileage - mileageLimit) * subsidizedUnitPrice);
        }

        int ownerReturnSubsidizedCost = 0;      //还车补贴(给车主)
        if ((ownerReturnMileage - renterReturnMileage) > mileageLimit) {
            ownerReturnSubsidizedCost = ToolUtil.ceil((ownerReturnMileage - renterReturnMileage - mileageLimit) * subsidizedUnitPrice);
        }

        //取还车油费补贴
        int ownerOilSubsidy = ownerGetSubsidizedCost + ownerReturnSubsidizedCost;

        ownerOilCostMap.put("platformServiceCost","" + platformServiceCost);    //平台加油服务费
        ownerOilCostMap.put("ownerOilCost","" + ownerOilCost);                  //车主油费
        ownerOilCostMap.put("ownerOilServiceCost","" + ownerOilServiceCost);    //车主加油服务费
        ownerOilCostMap.put("ownerOilSubsidy","" + ownerOilSubsidy);            //取还车油费补贴
        ownerOilCostMap.put("ownerOilGetSubsidy","" + ownerGetSubsidizedCost);            //取还车油费补贴_取车部分
        ownerOilCostMap.put("ownerOilReturnSubsidy","" + ownerReturnSubsidizedCost);      //取还车油费补贴_还车部分
        ownerOilCostMap.put("msg",msg);
        return ownerOilCostMap;
    }


    /**
     * 租客端超里程费用计算(向上取整)
     * @param paramMap
     * @return
     */
    public Map<String,String> renterMileageCost(Map<String,Object> paramMap){
        logger.info("========>renterMileageCostParam：{}",JSON.toJSONString(paramMap));
        Integer getMileage = (Integer) paramMap.get("renterGetMileage");        //租客取车时里程数
        Integer returnMileage = (Integer) paramMap.get("renterReturnMileage");  //租客还车时里程数
        Integer dayMileage = (Integer) paramMap.get("renterDayMileage");        //租客日限里程数
        Double rentDate = (Double) paramMap.get("rentDate");                    //租期
        Double dayPrice = (Double) paramMap.get("dayPrice");                    //日均价

        Map<String,String> renterMileageCostMap = new HashMap<>();
        int renterMileCost = ToolUtil.ceil(((returnMileage - getMileage) - dayMileage * rentDate) * dayPrice);
        renterMileageCostMap.put("renterMileCost","" + renterMileCost);
        return renterMileageCostMap;
    }


    /**
     * 租客端油费计算(向下取整)，可以为负数
     * @param paramMap
     * @return
     */
    public Map<String,String> renterOilCost(Map<String,Object> paramMap){
        logger.info("========>renterOilCostParam：{}",JSON.toJSONString(paramMap));
        Integer getGraduation = (Integer) paramMap.get("renterGetGraduation");          //租客取车时邮箱刻度
        Integer returngraduation = (Integer) paramMap.get("renterReturnGraduation");    //租客还车时邮箱刻度
        Double oilScaleDenominator = (Double) paramMap.get("oilScaleDenominator");    //油表刻度分母
        Integer fuelSize = (Integer) paramMap.get("fuelSize");                          //邮箱容积
        Double gasPrice = (Double) paramMap.get("gasPrice");                            //油价

        int m = getGraduation - returngraduation;
        logger.info("原值：{}",(m / oilScaleDenominator * fuelSize) * gasPrice);
        Map<String,String> renterOilCostMap = new HashMap<>();
        int renterOilCost = 0;	        //租客油费
        int renterOilServiceCost = 0;   //租客加油服务费
        String msg = "";

        renterOilCost = ToolUtil.floor((m / oilScaleDenominator * fuelSize) * gasPrice);
        logger.info("renterOilCost：{}",renterOilCost);

        if(renterOilCost > 0){
            renterOilServiceCost = ToolUtil.floor(renterOilCost / 4d);
        }

//        if (m == 1) {
//            renterOilCost = 0;
//            renterOilCost = 0;
//            msg += "由于邮箱刻度差=1，所以不计算油费。";
//        }

//        if (settle == 0) {
//            renterOilServiceCost = 0;
//            msg += "订单没结算，不计算【加油服务费】";
//        }

        renterOilCostMap.put("renterOilCost","" + renterOilCost);               //租客油费
        renterOilCostMap.put("renterOilServiceCost","" + renterOilServiceCost); //租客加油服务费
        renterOilCostMap.put("msg",msg);
        return renterOilCostMap;
    }


    /**
     * 判断是否在多边形区域内
     *
     * @param pointLon
     *            要判断的点的纵坐标
     * @param pointLat
     *            要判断的点的横坐标
     * @param lon
     *            区域各顶点的纵坐标数组
     * @param lat
     *            区域各顶点的横坐标数组
     * @return
     */
    public static boolean isInPolygon(double pointLon, double pointLat, double[] lon, double[] lat) {
        // 将要判断的横纵坐标组成一个点
        Point2D.Double point = new Point2D.Double(pointLon, pointLat);
        // 将区域各顶点的横纵坐标放到一个点集合里面
        List<Point2D.Double> pointList = new ArrayList<Point2D.Double>();
        double polygonPoint_x = 0.0, polygonPoint_y = 0.0;
        for (int i = 0; i < lon.length; i++) {
            polygonPoint_x = lon[i];
            polygonPoint_y = lat[i];
            Point2D.Double polygonPoint = new Point2D.Double(polygonPoint_x, polygonPoint_y);
            pointList.add(polygonPoint);
        }
        return check(point, pointList);
    }

    /**
     * 一个点是否在多边形内
     *
     * @param point
     *            要判断的点的横纵坐标
     * @param polygon
     *            组成的顶点坐标集合
     * @return
     */
    private static boolean check(Point2D.Double point, List<Point2D.Double> polygon) {
        java.awt.geom.GeneralPath peneralPath = new java.awt.geom.GeneralPath();

        Point2D.Double first = polygon.get(0);
        // 通过移动到指定坐标（以双精度指定），将一个点添加到路径中
        peneralPath.moveTo(first.x, first.y);
        polygon.remove(0);
        for (Point2D.Double d : polygon) {
            // 通过绘制一条从当前坐标到新指定坐标（以双精度指定）的直线，将一个点添加到路径中。
            peneralPath.lineTo(d.x, d.y);
        }
        // 将几何多边形封闭
        peneralPath.lineTo(first.x, first.y);
        peneralPath.closePath();
        // 测试指定的 Point2D 是否在 Shape 的边界内。
        return peneralPath.contains(point);
    }


}
