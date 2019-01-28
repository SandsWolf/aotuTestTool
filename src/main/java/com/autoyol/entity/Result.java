package com.autoyol.entity;

import java.io.Serializable;


public class Result implements Serializable {
	  private int status;	//状态
	  private String msg;	//消息
	  private Object data;	//数据
	  
	public int getStatus() {
		return status;
	}
	public String getMsg() {
		return msg;
	}
	public Object getData() {
		return data;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "{status="+status+",msg="+msg+",data="+data.toString()+"}";
	}
}
