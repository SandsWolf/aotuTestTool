package com.autoyol.entity;

public class PathIP{
	private String environment;			//环境
	private String serverIP;			//serverIP
	private String webconsoleIP;		//老管理后台IP
	private String webgatewayIP;		//网关服务IP
	private String autoConsoleApiIP;	//新管理后台IP
	private String autoTVServiceIP;		//违章独立结算服务
	private String transIP;				//结算服务
	private String redisIp;				//redis IP地址
	private String carCommentIP;		//评价服务
	private String virtualuserIP;		//虚拟会员服务：新token

	public String getInterFaceIP() {
		return InterFaceIP;
	}

	public void setInterFaceIP(String interFaceIP) {
		InterFaceIP = interFaceIP;
	}

	private String InterFaceIP;		//Interface

	public String getCarCommentIP() {
		return carCommentIP;
	}

	public void setCarCommentIP(String carCommentIP) {
		this.carCommentIP = carCommentIP;
	}

	public String getVirtualuserIP() {
		return virtualuserIP;
	}

	public void setVirtualuserIP(String virtualuserIP) {
		this.virtualuserIP = virtualuserIP;
	}

	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public String getServerIP() {
		return serverIP;
	}
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	public String getWebconsoleIP() {
		return webconsoleIP;
	}
	public void setWebconsoleIP(String webconsoleIP) {
		this.webconsoleIP = webconsoleIP;
	}
	public String getWebgatewayIP() {
		return webgatewayIP;
	}
	public void setWebgatewayIP(String webgatewayIP) {
		this.webgatewayIP = webgatewayIP;
	}
	public String getAutoConsoleApiIP() {
		return autoConsoleApiIP;
	}
	public void setAutoConsoleApiIP(String autoConsoleApiIP) {
		this.autoConsoleApiIP = autoConsoleApiIP;
	}
	public String getAutoTVServiceIP() {
		return autoTVServiceIP;
	}
	public void setAutoTVServiceIP(String autoTVServiceIP) {
		this.autoTVServiceIP = autoTVServiceIP;
	}
	public String getTransIP() {
		return transIP;
	}
	public void setTransIP(String transIP) {
		this.transIP = transIP;
	}
	public String getRedisIp() {
		return redisIp;
	}
	public void setRedisIp(String redisIp) {
		this.redisIp = redisIp;
	}
	
	
	
}
