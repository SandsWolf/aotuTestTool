package com.autoyol.util;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.StringUtils;

/**
 * 设置数据源 
 * 
 * Spring提供的AbstractRoutingDataSource类来根据请求路由到不同的数据源.
 * 具体做法是:
 *	1.在配置文件中先设置两个不同的dataSource代表不同的数据源，
 *  2.再建一个总的dynamicDataSource，根据不同的请求去设置dynamicDataSource
 *  3.SqlSessionFactory Bean配置对应dynamicDataSource
 *
 *
 * 代码中实现：
 * 	1.需要调用数据的代码中设置dataSource类型	例:CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_ATSQL);
 * 	2.spring容器调用该方法获取dataSource类型
 * 	3.通过配置文件连接对应数据库
 * @author lx
 *
 */
public class LinkageRoutingDataSource extends AbstractRoutingDataSource {
    
    //目标数据源  
    private static final ThreadLocal<String> TARGET_DATA_SOURCE = new ThreadLocal<String>();
    //默认数据源--指标监控的  
    public static final String DEFAULT_DATA_SOURCE = "cloud_DB";
      
    /** 
     * 根据setDateSourceUtil类设置进去的当前线程数据源进行数据源切换 
     *  
     * @param
     * @return 数据源名称 
     */  
    protected Object determineCurrentLookupKey() {
        String targetDataSource = TARGET_DATA_SOURCE.get();
        if (StringUtils.isEmpty(targetDataSource)) {
            targetDataSource = DEFAULT_DATA_SOURCE; //默认数据源为指标监控数据源  
            TARGET_DATA_SOURCE.set(targetDataSource);  
        }  
        return targetDataSource;  
    }    
      
    /** 
     * 设置数据源名 
     * @param target 
     */  
    public static void setTargetDataSource(String target) {
        TARGET_DATA_SOURCE.set(target);  
    }  
      
    /** 
     * 取数据源名 
     * @return 
     */  
    public static String getTargetDataSource(){
        return TARGET_DATA_SOURCE.get();  
    }
}
