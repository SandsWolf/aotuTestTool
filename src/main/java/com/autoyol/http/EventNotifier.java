package com.autoyol.http;


/**
 * 事件监听者
 * @author Jian
 *
 */
public class EventNotifier {

	//具体回调的对象
	private CallBack callBack;
	
	//返回的数据
	private Object data;
	
	public void setCallFunc(CallBack callBack) {  
	        this.callBack = callBack;  
	}  
	
    public void call(Object data) {
    	this.data=callBack.setData(data);  
    }  
    
    public Object getData() {
		return data;
	}
}
