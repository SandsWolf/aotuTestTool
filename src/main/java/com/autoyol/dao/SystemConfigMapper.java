package com.autoyol.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface SystemConfigMapper {
    public Map<String,Object> selectSysConstantByCode(@Param("codes") String codes);
    public String selectCarParamRatio(@Param("brand_id") String brandId, @Param("type_id") String typeId);
}
