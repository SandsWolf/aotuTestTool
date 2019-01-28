package com.autoyol.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    // 需要加密的字段数组 。这里需要和jdbc.properties中一致
    private String[] encryptPropNames = { "online_username", "online_pwd", "onlineHoliday_username", "onlineHoliday_pwd", "online_url", "onlineHoliday_url"};


    //解密
    protected String convertProperty(String propertyName,String propertyValue) {
        if(judgeEncryptOrNot(propertyName)) {
            try {
                String realPropertyValue = AESUtil.aesDecrypt(propertyValue);
                return  realPropertyValue;//返回解密后的值
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return propertyValue;
    }

    //判断是否需要加密
    public boolean judgeEncryptOrNot(String propertyName) {
        for(String encryptPropName : encryptPropNames ) {
            if(encryptPropName.equals(propertyName)) {
                return true;
            }
        }
        return false;
    }
}
