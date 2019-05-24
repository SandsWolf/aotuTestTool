package com.autoyol.controller;


import ch.qos.logback.core.joran.conditional.ElseAction;
import com.alibaba.fastjson.JSON;
import com.autoyol.dao.CarMapper;
import com.autoyol.dao.MemberMapper;
import com.autoyol.entity.Car;
import com.autoyol.entity.Member;
import com.autoyol.entity.PathIP;
import com.autoyol.entity.Result;
import com.autoyol.http.HttpResponse;
import com.autoyol.http.HttpUtils;
import com.autoyol.service.CarService;
import com.autoyol.util.SetDateSourceUtil;
import com.autoyol.util.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/car")
public class CarController {
    @Resource
    private CarService carService;
    @Resource
    private CarMapper CarMapper;
    @Resource
    private MemberMapper memberMapper;
    private static final Logger logger = LoggerFactory.getLogger(CarController.class);


    /**
     * 重置车辆信息，使车辆可租
     *
     * @param environment
     * @param car_no
     * @return
     */

    @RequestMapping("/reSetRent")
    @ResponseBody
    public Result reSetRent(String environment, String car_no) {
        Result result = new Result();
        if ("线上".equals(environment)) {
            result.setStatus(1);
            result.setMsg("success");
            result.setData("线上环境只能做查询操作");
            return result;
        }

        SetDateSourceUtil.setDataSourceName(environment);

        result = carService.reSetRent(car_no);
        return result;
    }


    /**
     * 更换车牌
     *
     * @param carPara
     * @param mobile
     * @param environment
     */
    @RequestMapping(value = {"/updatecarmemno"}, method = {RequestMethod.POST})
    @ResponseBody
    public Result UpdateCarMemno(String environment, String mobile, String carPara) {
        SetDateSourceUtil.setDataSourceName(environment);
        Result result = new Result();
        if (!ToolUtil.isMobile(mobile)) {
            result = ToolUtil.checkMobile(mobile);
            return result;
        }

        Car car = CarMapper.selectCarInfo(carPara);
        if (car == null) {
            logger.info("车辆不存在");
            result.setStatus(1);
            result.setMsg("success");
            result.setData("车辆（" + carPara + "）不存在，请确认后再试");
            return result;
        }

        List<Member> memberList = memberMapper.selectMemberInfoByMobile(mobile);
        if (memberList.size() <= 0) {
            result.setStatus(1);
            result.setMsg("success");
            result.setData("会员（" + mobile + "）不存在，请确认后再试");
            return result;
        }

        Map<String, String> paramap = new HashMap<>();
        paramap.put("carPara", carPara);
        paramap.put("mobile", mobile);
        try {
            CarMapper.UpdateCarIsMemno(paramap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("更换车牌_Result", JSON.toJSONString(result.getData()));
        result.setStatus(000000);
        result.setMsg("success");
        result.setData("恭喜" + mobile + "用户，偷车成功!!!");
        return result;
    }

    /**
     * 统计该手机号下车的数量
     *
     * @param mobile
     * @return 车的数量
     */
    @RequestMapping(value = {"/memcarcount"}, method = {RequestMethod.POST})
    @ResponseBody
    public Result MemberCarCount(String environment, String mobile) {
        SetDateSourceUtil.setDataSourceName(environment);
        Result result = new Result();
        if (!ToolUtil.isMobile(mobile)) {
            result = ToolUtil.checkMobile(mobile);
            return result;
        }
        List<Member> memberList = memberMapper.selectMemberInfoByMobile(mobile);

        if (memberList.size() <= 0) {
            result.setStatus(1);
            result.setMsg("success");
            result.setData("会员（" + mobile + "）不存在，请确认后再试");
            return result;
        }


        List<Long> carregnolist = new ArrayList<>();
        carregnolist.addAll(CarMapper.selectCarNo(mobile));
        Integer count = 0;
        try {
            count = CarMapper.SelectCarCount(mobile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("统计会员名下车的总数_Result", JSON.toJSONString(result.getData()));
        result.setStatus(000000);
        result.setMsg("success");
        result.setData(mobile + "用户名下车的总数量为：" + count + "car_no：" + carregnolist.toString());
        return result;
    }


    /**
     * 收藏车辆
     *
     * @param car
     * @return 收藏车辆
     */
    @RequestMapping(value = {"/favorites"}, method = {RequestMethod.POST})
    @ResponseBody
    public Result Favourites(String environment, String carNo, String mobile) {
        SetDateSourceUtil.setDataSourceName(environment);
        PathIP pathIP = ToolUtil.getIP(environment);
        List<Member> memberList = memberMapper.selectMemberInfoByMobile(mobile);

        Result result = new Result();

        if (memberList.size() <= 0) {
            result.setStatus(1);
            result.setMsg("success");
            result.setData("会员（" + mobile + "）不存在，请确认后再试");
            return result;
        }

        String memNo = memberList.get(0).getReg_no();
        String token = memberList.get(0).getToken();


        String url = pathIP.getServerIP() + "/car/favorites";

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "application/json; charset=utf-8");
        headerMap.put("connection", "Keep-Alive");
        headerMap.put("User-Agent", "AutoyolEs_console");
        headerMap.put("Accept", "application/json;version=3.0;compress=false");

        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("publicCityCode", "021");
        paraMap.put("IMEI", "EF818E1E-771C-469B-A769-869C6153EFC7");
        paraMap.put("AppVersion", "91");
        paraMap.put("PublicLatitude", "31.171986");
        paraMap.put("PublicLongitude", "121.409134");
        paraMap.put("deviceName", "iPhone7Plus");
        paraMap.put("token", token);
        paraMap.put("OsVersion", "12.2");
        paraMap.put("mem_no", memNo);
        paraMap.put("OS", "IOS");
        paraMap.put("ModuleName", "car");
        paraMap.put("publicToken", token);
        paraMap.put("requestId", "EF818E1E-771C-469B-A769-869C6153EFC74789206853931208794");
        paraMap.put("carNo", carNo);
        HttpResponse httpResult = HttpUtils.post(headerMap, url, paraMap, false, false);

        result.setStatus(0);
        result.setMsg("success");
        result.setData("收藏成功" + httpResult.getResponseBodyObject().toString());


        return result;
    }
}


