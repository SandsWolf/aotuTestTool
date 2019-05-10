package com.autoyol.controller;


import com.alibaba.fastjson.JSON;
import com.autoyol.dao.CarMapper;
import com.autoyol.dao.MemberMapper;
import com.autoyol.entity.Car;
import com.autoyol.entity.Member;
import com.autoyol.entity.Result;
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
     * @param environment
     * @param car_no
     * @return
     */

    @RequestMapping("/reSetRent")
    @ResponseBody
    public Result reSetRent(String environment, String car_no){
        Result result = new Result();
        if("线上".equals(environment)){
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
     * @param carPara
     * @param mobile
     * @param environment
     */
    @RequestMapping(value = {"/updatecarmemno"}, method = {RequestMethod.POST})
    @ResponseBody
    public Result UpdateCarMemno(String environment, String mobile, String carPara){
        SetDateSourceUtil.setDataSourceName(environment);
        Result result = new Result();
        if (!ToolUtil.isMobile(mobile)){
            result = ToolUtil.checkMobile(mobile);
            return result;
        }

        Car car = CarMapper.selectCarInfo(carPara);
        if(car == null){
            logger.info("车辆不存在");
            result.setStatus(1);
            result.setMsg("success");
            result.setData("车辆（" + carPara + "）不存在，请确认后再试");
            return result;
        }

        List<Member> memberList = memberMapper.selectMemberInfoByMobile(mobile);
        if (memberList.size() <= 0){
            result.setStatus(1);
            result.setMsg("success");
            result.setData("会员（" + mobile + "）不存在，请确认后再试");
            return result;
        }

        Map<String,String> paramap = new HashMap<>();
        paramap.put("carPara",carPara);
        paramap.put("mobile",mobile);
        try {
            CarMapper.UpdateCarIsMemno(paramap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("更换车牌_Result",JSON.toJSONString(result.getData()));
        result.setStatus(000000);
        result.setMsg("success");
        result.setData("恭喜" + mobile + "用户，偷车成功!!!");
        return result;
    }

    /**
     * 统计该手机号下车的数量
     * @param mobile
     * @return 车的数量
     */
    @RequestMapping(value = {"/memcarcount"}, method = {RequestMethod.POST})
    @ResponseBody
    public Result MemberCarCount(String environment,String mobile){
        SetDateSourceUtil.setDataSourceName(environment);
        Result result = new Result();
        if (!ToolUtil.isMobile(mobile)){
            result = ToolUtil.checkMobile(mobile);
            return result;
        }
        List<Member> memberList = memberMapper.selectMemberInfoByMobile(mobile);

        if (memberList.size() <= 0){
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
        logger.info("统计会员名下车的总数_Result",JSON.toJSONString(result.getData()));
        result.setStatus(000000);
        result.setMsg("success");
        result.setData(mobile + "用户名下车的总数量为：" + count + "car_no：" +  carregnolist.toString());
        return result;
    }
}


