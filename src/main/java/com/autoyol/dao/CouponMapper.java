package com.autoyol.dao;

import org.apache.ibatis.annotations.Param;

public interface CouponMapper {
    public void insertCoupon(@Param("memNo") String memNo,@Param("random") String random);
}
