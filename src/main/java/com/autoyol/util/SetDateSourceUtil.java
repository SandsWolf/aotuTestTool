package com.autoyol.util;

/**
 * 设置获取当前线程数据源名称 
 * @author lx
 *
 */
public class SetDateSourceUtil {
    
    //默认数据源  
    public static final String DEFAULT_DATA_SOURCE = "cloud_DB";
      
    /** 
     * 设置需要用的数据源名称 
     *  
     * @param dataSourceName 数据源名称 
     * @retrun 无 
     */  
    public static void setDataSourceName(String dataSourceName) {
        LinkageRoutingDataSource.setTargetDataSource(getDataSourceName(dataSourceName));  
    }    
      
    /** 
     * 获取数据源名称 
     * @param dataSourceName 
     * @return 数据源名称 
     */  
    public static String getDataSourceName(String dataSourceName) {
        String dataSource = dataSourceName;
        if ("baseDB".equals(dataSource)) {
            dataSource = DEFAULT_DATA_SOURCE;  
        } else if ("test_1".equals(dataSource)) {
        	dataSource = "test_1DB";
        } else if ("test_2".equals(dataSource)) {
        	dataSource = "test_2DB";
        } else if ("test_3".equals(dataSource)) {
        	dataSource = "test_3DB";
        }else if ("test_4".equals(dataSource)) {
        	dataSource = "test_4DB";
        } else if ("test_5".equals(dataSource)) {
        	dataSource = "test_5DB";
        } else if ("test_9".equals(dataSource)) {
            dataSource = "test_9DB";
        } else if ("线上".equals(dataSource)) {
        	dataSource = "on_lineDB";
        } else if ("onlineHoliday".equals(dataSource)) {   //线上节日库
            dataSource = "on_lineHolidayDB";
        } else if ("test1_autoFeeService".equals(dataSource)) {
            dataSource = "test1_autoFeeServiceDB";
        }
        //((JdbcTemplate)SpringContextHolder.getBean("jdbcTemplate")).setDataSource((DataSource)SpringContextHolder.getBean(dataSource));  
        return dataSource;    
    }  
}
