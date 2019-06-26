package com.autoyol.service;

import com.autoyol.entity.Member;
import com.autoyol.entity.OrderInfo;
import com.autoyol.entity.PathIP;
import com.autoyol.entity.Result;

import java.util.Map;

public interface CostService {
    public Result getAbatementInsure(String carPara, String rentTime, String revertTime, String driLicGetTime, String insurePricesType);
    public Result getInsureTotalPrices(String carPara, String rentTime, String revertTime, String driLicGetTime, String insurePricesType);
    public Result getCarDepositAmt(String serverIP, Member member, String carNo);
    public Result getDepositAmt(String cityCode, String plateNum, Member member);
    public Result getExtraDriverInsure(String rentTime, String revertTime, int num);
    public Map<String,String> getReturnCost(Map<String, String> paraMap);
    public Map<String,Integer> transRenterCostDetail (PathIP pathIP, String orderNo, String renterToken);
    public Result getMileageOilCost(PathIP pathIP, OrderInfo order);   //, int testTotalAmt, int testCodeFlag
    public Map<String,String> getReturnCostNew(Map<String, String> paraMap);
}
