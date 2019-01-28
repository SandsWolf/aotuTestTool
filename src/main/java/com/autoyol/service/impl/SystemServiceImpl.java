package com.autoyol.service.impl;

import com.autoyol.dao.SystemConfigMapper;
import com.autoyol.service.SystemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class SystemServiceImpl implements SystemService {
    @Resource
    private SystemConfigMapper systemConfigMapper;

    /**
     * 获取key对应的value的值
     * @param code
     * @return
     */
    public String getValue(String code){
        Map<String,Object> map = systemConfigMapper.selectSysConstantByCode(code);
        if (map == null || map.isEmpty()) {
            return "";
        }else{
            return map.get("c_value").toString();
        }
    }
}
