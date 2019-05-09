package com.autoyol.dao;

import com.autoyol.entity.Car;
import com.autoyol.entity.DepositConfig;
import com.autoyol.entity.Result;


import java.util.List;
import java.util.Map;

public interface CarMapper {
    public Car selectCarInfo(String carPara);
    public void deleteTransFilter(String carNo);
    public void deleteCarFilter(String carNo);
    public Car selectInsureTotalPrices(String carPara);
    public DepositConfig selectCarDepositAmtInCityCode(String carNo);
    public DepositConfig selectCarDepositAmtOutCityCode(String carNo);
    public List<DepositConfig> selectIDepositTextCode();
    public void UpdateCarIsMemno(Map paraMap);
    public Integer SelectCarCount(String mobile);
}
