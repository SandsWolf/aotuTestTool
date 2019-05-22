package com.autoyol.service;


import com.autoyol.entity.Result;

public interface CtripTransService {

    public Result createTrans(String pickupDate, String returnDate, String uri);

}
