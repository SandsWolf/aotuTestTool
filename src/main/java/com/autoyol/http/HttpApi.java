package com.autoyol.http;

import org.testng.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


/**
 * 测试的核心工具
 * Created by andy on 15/11/25.
 */
public class HttpApi {
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(HttpApi.class);
    
    private static Map<String, String> headerMap = RequestHeaderMap.headerMap().get("AutoyolEs_web");//请求头

    private String url = null;//请求的URL
    
    private RequestMethod method = RequestMethod.POST;//请求的类型

    private boolean compressed = true;//是否压缩

    private boolean encrypted =false;//是否加密上传
    
    private Map<String,Object> requestMap = null;//请求参数
    
    private Object resData = null;//返回结果中data的值
    
    private Map<String,Object> result = null;//返回结果

    private Object expectObj = null;//期望的结果
    
    private Map<String, String> expectErrorResponse = null;//期望的错误响应
    
    private boolean isCheck = true;
    
    /**
     * 验证器集合，将需要调用的验证器都放在该集合中，
     * Key 需要调用的验证器
     * Value 验证器所需的参数
     */
    private HashMap<String,Object> valiatedMap = new HashMap<String,Object>();
    
    public HttpApi url(String url) {
    	if(!org.springframework.util.StringUtils.isEmpty(url)){
    		if(url.startsWith("null")){
    			url = url.replace("null", "");
    		}
    		if(!url.startsWith("http"))//拿配置文件里的访问地址
    			this.url = new StringBuffer(Constans.AUTO_SERVER_URL).append(url).toString();
    		else
    			this.url = url;
    	}
        return this;
    }
    
    /**
     * 设置请求头
     * @param headerMap
     * @return
     */
    public HttpApi headerMap(Map<String,String> headerMap) {
    	this.headerMap = headerMap;
        return this;
    }

    /**
     * 设置请求方式
     * @param requestMethod
     * @return
     */
    public HttpApi method(RequestMethod requestMethod) {
        this.method = requestMethod;
        return this;
    }
    /**
     * 设置是否压缩,默认是压缩
     * @return
     */
    public HttpApi compressed(boolean compressed){
        this.compressed = compressed;
        return this;
    }

    public HttpApi request(Map<String, Object> requestMap) {
        this.requestMap=requestMap;
        return this;
    }


    /**
     * 设置加密上传,未调用本方法,默认设置为不加密
     * @return
     */
    public HttpApi encrypted(boolean encrypted){
        this.encrypted=encrypted;
        return this;
    }
    
    /**
     *设置是否检查返回码
     * @return
     */
    public HttpApi setIsCheck(boolean isCheck){
        this.isCheck=isCheck;
        return this;
    }
    


    public void execute(EventNotifier ... events) {
    	String className = Thread.currentThread().getStackTrace()[2].getClassName();
    	String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
    	logger.info(">>>>>>{}.{}<<<<<<",className,methodName);
    	HttpResponse response = null;
    	switch (method) {
		case GET:
			response = HttpUtils.get(headerMap, url);
			break;
		case POST:
			System.out.println("headerMap is~~~~~~~~~~~~"+headerMap+"~~compressed is:"+compressed+"~~encrypted is:"+encrypted);
			response = HttpUtils.post(headerMap, url, requestMap, compressed, encrypted);
			break;
		default:
			response = HttpUtils.put(headerMap, url, requestMap, compressed, encrypted);
			break;
		}
        Assert.assertEquals(response.getHttpRespCode(), 200);
        this.result = response.getResponseBodyObject();
        this.resData = result.get("data");
        String resCode = (String)result.get("resCode");
        String resMsg  = (String)result.get("resMsg");
        if(events!=null && events.length>0){
        	for(EventNotifier event:events){
        		event.call(result);
        	}
        }
        
        
        logger.info("-----------------------------");
        logger.info("  返回结果{}",result);
        logger.info("-----------------------------\n");
        try {
        	if(!isCheck)
        	{
        		//不执行检查
        	}
        	else if(expectErrorResponse!=null){//错误请求
				String errorCode = expectErrorResponse.get("resCode");
				String errorMsg  = expectErrorResponse.get("resMsg");
				if(errorCode != null)
					Assert.assertEquals(resCode,errorCode);
				if(errorMsg != null)
					Assert.assertEquals(resMsg,errorMsg);
			}else if(resCode.equals("000000")){
//				valiate(resultMap, expectMap);
			    newValiate();
			}else{//
				Assert.fail("resCode:"+resCode+"\t\tresMsg:"+resMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			clear();
		}
    }

    /**
     * 清空所有的参数
     */
    private void clear() {
    	this.url = null;
        
    	this.method = RequestMethod.POST;

    	this.compressed = true;

    	this.encrypted =false;
        
    	this.requestMap = null;
        
    	this.resData = null;
        
    	this.result = null;

    	this.expectObj = null;	
        
    	this.expectErrorResponse= null;
    	
    	this.valiatedMap = new HashMap<String,Object>();
	}

	/**
     * 验证返回的json对象是否满足期望
     * @param resultData
     * @param expectObj
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void newValiate() {
    	
    	if(valiatedMap == null)
    		Assert.assertTrue(true, "验证器集合为空，不需要验证该方法的返回结果");
    	
    	Validator validator = new Validator();
    	for(String key : valiatedMap.keySet()){
    		Method _method = null;
    		try {
				_method = validator.getClass().getDeclaredMethod(key, Map.class);
				HashMap<String,Object> paramsMap = (HashMap)valiatedMap.get(key);
				if(paramsMap ==null){
					paramsMap = new HashMap<String,Object>();
				}
				paramsMap.put("resultData", resData);
				paramsMap.put("result", result);
				_method.invoke(validator,paramsMap);
				
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				logger.error(e.getTargetException().getMessage());
				Assert.fail(e.getTargetException().getMessage());
			}finally{
//				 logger.info("-----------------------------");
//	             logger.info("{}返回的 json 对象是{}",_method.getName(),resultData);
//	             logger.info("-----------------------------");
			}
    	}
	}

//	/**
//     * 验证返回的 json 对象是否满足期望
//     * @param resultMap
//     * @param expectMap
//     */
//    protected void valiate(Map resultMap,Map expectMap){
//        Set<String> keySet = expectMap.keySet();
//        for(String key:keySet){
//            Object object = resultMap.get(key);
//            Assert.assertNotNull(object);
//            System.out.println("object is "+object.getClass());
//            //FIXME
//            if(object instanceof Map){
//                Object expectResult = expectMap.get(key);
//                Assert.assertTrue(expectResult instanceof Map);
//                valiate((Map)object,(Map)expectMap);
//            }else{
//                Assert.assertEquals(object,expectMap.get(key));
//            }
//        }
//    }
    
    /**
     * 设置期望返回的data中的json对象
     * @param obj
     * @return
     */
    public HttpApi expectData(Object obj){
    	this.expectObj = obj;
    	Map<String,Object> paramsMap = new HashMap<String,Object>();
    	paramsMap.put("expectObj", expectObj);
    	this.valiatedMap.put("valiateResult", paramsMap);
    	return this;
    }
    
    /**
     * 设置HTTP的请求头
     * @param headerMap
     * @return
     */
    public HttpApi addHttpHeader(Map<String,String> headerMap){
    	if(headerMap != null)
    		HttpApi.headerMap = headerMap;
    	return this;
    }
    
    /**
     * 期望下标为index的结果为expectObj
     * @param propertName 属性名称
     * @param index 下标
     * @param expectObj
     * @return
     */
    public HttpApi expectResultPropertIndexOf(String propertName, int index, Object expectObj){
    	
    	Map<String,Object> paramsMap = new HashMap<String,Object>();
    	paramsMap.put("propert", propertName);
    	paramsMap.put("index", index);
    	paramsMap.put("expectObj", expectObj);
    	this.valiatedMap.put("valiateCollectionsIndexOfValue", paramsMap);
    	return this;
    }
    
    /**
     * 期望属性名propertName的结果为expectObj
     * @param propertName
     * @param expectObj 
     * @return
     */
    public HttpApi expectResultPropertValue(String propertName, Object expectObj){
    	Map<String,Object> paramsMap = new HashMap<String,Object>();
    	paramsMap.put("propert", propertName);
    	paramsMap.put("expectObj", expectObj);
    	this.valiatedMap.put("valiatePropertValue", paramsMap);
    	return this;
    }
    
    /**
     * 期望属性名propertName的结果长度为propertLength
     * @param propertName
     * @param size
     * @return
     */
    public HttpApi expectPropertSize(String propertName, int size){
    	Map<String,Object> paramsMap = new HashMap<String,Object>();
    	paramsMap.put("propert", propertName);
    	paramsMap.put("size", size);
    	this.valiatedMap.put("validateSize", paramsMap);
    	return this;
    }
    
    /**
     * 设置期望返回的data的size
     * @param length
     * @return
     */
    public HttpApi expectResultSize(int size){
    	Map<String,Object> paramsMap = new HashMap<String,Object>();
    	paramsMap.put("size", size);
    	this.valiatedMap.put("validateSize", paramsMap);
    	return this;
    }
    
    /**
     * 设置期望返回的data中的json字符串是ArrayList
     * @return
     */
    public HttpApi expectIsList(){
    	String key = "isList";
    	this.valiatedMap.put(key, null);
    	return this;
    }
    
    /**
     * 设置期望返回的data中的json字符串是HashMap
     * @return
     */
    public HttpApi expectIsMap(){
    	this.valiatedMap.put("isMap", null);
    	return this;
    }
    
    public HttpApi expectErrorResponse(Map<String, String> errorResposne) {
        this.expectErrorResponse = errorResposne;
        return this;
    }
    
    /**
     *设置期望返回的json字符串和expectJson相等
     */
    public HttpApi expectJson(String expectJson){
    	Map<String,Object> paramsMap = new HashMap<String,Object>();
    	paramsMap.put("json", expectJson);
    	
    	this.valiatedMap.put("valiatedJson", paramsMap);
    	return this;
    }
}
