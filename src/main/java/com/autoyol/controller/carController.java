package com.autoyol.controller;


import com.autoyol.dao.CarMapper;
import com.autoyol.entity.PathIP;
import com.autoyol.entity.Result;
import com.autoyol.service.CarService;
import com.autoyol.util.SetDateSourceUtil;
import com.autoyol.util.ToolUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/car")
public class carController {
    @Resource
    private CarService carService;
    @Resource
    private CarMapper CarMapper;


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

}
