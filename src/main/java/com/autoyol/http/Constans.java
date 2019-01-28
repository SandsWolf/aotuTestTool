package com.autoyol.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;


public class Constans {
	
	private static Logger logger = LoggerFactory.getLogger(Constans.class);
	
	private final static String RESOURCE_NAME = "/autoServerConfig.properties";
	
	public static String AUTO_SERVER_URL;
	
	static{
		Properties properties  = new Properties();
        try {
        	properties.load(Constans.class.getResourceAsStream(RESOURCE_NAME));
        	AUTO_SERVER_URL = properties.getProperty("auto.serverUrl");
        }catch(IOException e){
        	logger.error("加载{}失败....",RESOURCE_NAME);
        }
	}
}
