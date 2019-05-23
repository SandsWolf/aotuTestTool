package com.autoyol.controller;


import com.autoyol.entity.Result;
import com.autoyol.service.CtripTransService;
import com.autoyol.util.ToolUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


@Controller
@RequestMapping("/ctrip")
public class CtripTransController {
    @Resource
    private CtripTransService  ctripTransService;

    /**
     * 携程套餐下单
     * @param environment
     * @param pickupDate
     * @param returnDate
     * @param cityCode
     * @param vehicleCode
     * @return
     */
    @RequestMapping("/createTrans")
    @ResponseBody
    public Result createTrans(String environment,String pickupDate,String returnDate,String cityCode,String vehicleCode){
        Result result = new Result();
        if("线上".equals(environment)){
            result.setStatus(1);
            result.setMsg("success");
            result.setData("线上环境只能做查询操作");
            return result;
        }

        String uri = ToolUtil.getIP(environment).getInterFaceIP();

        result = ctripTransService.createTrans(pickupDate,returnDate,uri,cityCode,vehicleCode);
        return result;
    }



    /**
     * 携程套餐查库存
     * @param environment
     * @param pickupDate
     * @param returnDate
     * @param cityCode
     * @return
     */
    @RequestMapping("/selectInventory")
    @ResponseBody
    public Result selectCtripInventory(String environment,String pickupDate,String returnDate,String cityCode){
        Result result = new Result();
        if("线上".equals(environment)){
            result.setStatus(1);
            result.setMsg("success");
            result.setData("线上环境只能做查询操作");
            return result;
        }

        String uri = ToolUtil.getIP(environment).getInterFaceIP();

        result = ctripTransService.selectCtripInventory(pickupDate,returnDate,uri,cityCode);

        return result;
    }

}
