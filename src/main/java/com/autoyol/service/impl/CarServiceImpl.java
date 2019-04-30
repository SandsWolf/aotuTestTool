package com.autoyol.service.impl;

import com.autoyol.dao.CarMapper;
import com.autoyol.entity.Car;
import com.autoyol.entity.Result;
import com.autoyol.service.CarService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CarServiceImpl implements CarService {
    @Resource
    private CarMapper carMapper;

    public Result reSetRent(String car_no){
        Result result = new Result();

        Car  car  = carMapper.selectCarInfo(car_no);
        car_no = car.getReg_no();

        if (car_no != null && car_no != ""){
            result.setStatus(0);
            result.setMsg("success");
            result.setData("重置成功，若车辆仍不可租请联系作者优化代码");

            carMapper.deleteCarFilter(car_no);
            carMapper.deleteTransFilter(car_no);

        }else {
            result.setStatus(0);
            result.setMsg("success");
            result.setData("车辆不存在，请重试");
        }
        return result;

    }
}
