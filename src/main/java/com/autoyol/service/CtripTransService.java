package com.autoyol.service;


import com.autoyol.entity.Result;

public interface CtripTransService {

    public Result createTrans(String pickupDate, String returnDate, String uri, String cityCode, String vehicleCode);
    public Result selectCtripInventory(String pickupDate, String returnDate, String uri, String cityCode);

}
