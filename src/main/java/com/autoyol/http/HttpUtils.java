package com.autoyol.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.Charsets;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.DecompressingEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

/**
 * 用于测试人员测试 app 接口的工具类
 * Created by andy on 15/11/23.
 */
public class HttpUtils {
    private final static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static final String ENCRYPT_KEY = "20140808152306151";

    /**
     * 使用 Post 方法上传参数并获取服务器响应数据
     *
     */
	public static HttpResponse post(Map<String,String> headerMap, String url, Map<String, Object> paraMap, boolean compressed, boolean encrypted) {
        HttpPost post = new HttpPost(url);
        for(String key :headerMap.keySet())
        	post.addHeader(key, headerMap.get(key));
        System.out.println(post.getAllHeaders());
        String content = null;
        try {
            content = objectMapper.writeValueAsString(paraMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (encrypted)
            content = encrypt(content);

        byte[] buf = content.getBytes(Charsets.UTF_8);
        if (compressed)
            buf = compress(content);
        ByteArrayEntity ss=new ByteArrayEntity(buf);
		System.out.println("888888--"+ss+"--5555555");
        post.setEntity(new ByteArrayEntity(buf));
        requestLog(post,content);//打印传入参数
        return execute(post);

    }	
	
    /**
     * 使用 get 方法上传信息
     * @param url
     * @return
     */
	public static HttpResponse get(Map<String,String> headerMap, String url) {
        HttpGet get = new HttpGet(url);
        for(String key : headerMap.keySet())
        	get.addHeader(key, headerMap.get(key));
        
        requestLog(get,null);
        return execute(get);
    }
	
	/**
     * 使用 get 方法上传信息
     * @param url
     * @return
     */
	public static HttpResponse get(String url) {
        HttpGet get = new HttpGet(url);
        
        requestLog(get,null);
        return execute(get);
    }
 
	public static HttpResponse put(Map<String,String> headerMap, String uri, Map<String,Object> paramsMap, boolean compressed, boolean encrypted) {
		HttpPut put = new HttpPut(uri);
		for(String key : headerMap.keySet())
			put.addHeader(key,headerMap.get(key));

        String content = null;
        try {
            content = objectMapper.writeValueAsString(paramsMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (encrypted)
            content = encrypt(content);

        byte[] buf = content.getBytes(Charsets.UTF_8);
        if (compressed)
            buf = compress(content);
        
        put.setEntity(new ByteArrayEntity(buf));
        requestLog(put,content);
		return execute(put);
	}
		
	@SuppressWarnings("unchecked")
	private static HttpResponse execute(HttpUriRequest request){
    	System.out.println("request is what:"+request);
    	CloseableHttpClient httpClient = HttpClients.createDefault();
    	HttpResponse actualHttpResponse = new HttpResponse();
        try {
        	org.apache.http.HttpResponse response = httpClient.execute(request);
            int code = response.getStatusLine().getStatusCode();
            actualHttpResponse.setHttpRespCode(code);
            if (code == 200) {
            	/*Modify Start JiHaiqing 20160623 *****************************************************/
            	//DecompressingEntity entity = (DecompressingEntity) response.getEntity();
            	//Map<String,Object> json = objectMapper.readValue(entity.getContent(), Map.class);
            	//actualHttpResponse.setResponseBodyObject(json);
            	/*Modify End JiHaiqing 20160623 *****************************************************
            	/*add ****/
//              Map<String,Object> json = objectMapper.readValue(response.getEntity().getContent(), Map.class);
//              actualHttpResponse.setResponseBodyObject(json);
            	
            	try {
            		Map<String,Object> json = objectMapper.readValue(response.getEntity().getContent(), Map.class);
                    actualHttpResponse.setResponseBodyObject(json);
				} catch (Exception e) {
					Map<String,Object> json = new HashMap<String, Object>();
					json.put("data","完成");
					actualHttpResponse.setResponseBodyObject(json);
				}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	if(httpClient != null)
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        }
        return actualHttpResponse;
    }
	
	public static HttpResponse postRequest(String url, Map<String, Object> paraMap, boolean compressed, boolean encrypted) {
     CloseableHttpClient httpClient = HttpClients.createDefault();
     HttpPost post = new HttpPost(url);
	   post.addHeader("User-Agent", "AutoyolEs_web");
	   post.addHeader("Content-Type","application/json;charset=UTF-8");
	   
	   ObjectMapper objectMapper = new ObjectMapper();
	   String content = null;
	   try {
	       content = objectMapper.writeValueAsString(paraMap);
	   } catch (JsonProcessingException e) {
	       e.printStackTrace();
	   }
	
	   if (encrypted) {
	       content = encrypt(content);
	   }

	   byte[] buf = content.getBytes(Charsets.UTF_8);
	   if (compressed) {
	       buf = compress(content);
	   }

	   HttpResponse actualHttpResponse = new HttpResponse();

	   post.setEntity(new ByteArrayEntity(buf));
	   try {
	       org.apache.http.HttpResponse response = httpClient.execute(post);
	       int code = response.getStatusLine().getStatusCode();
	       actualHttpResponse.setHttpRespCode(code);
	       if (code == 200) {
	           DecompressingEntity entity = (DecompressingEntity) response.getEntity();
	           BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
	           Map json = objectMapper.readValue(entity.getContent(), Map.class);
	           logger.info("-----------------------------");
	           logger.info("返回的 json 对象是{}",json);
	           logger.info("-----------------------------");
	           actualHttpResponse.setResponseBodyObject(json);
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
       return actualHttpResponse;
	}
	
	  public static HttpResponse getRapJson(String url) {
	        CloseableHttpClient httpClient = HttpClients.createDefault();
	        HttpGet get = new HttpGet(url);
	       
	        HttpResponse actualHttpResponse = new HttpResponse();
	        try {
	            org.apache.http.HttpResponse response = httpClient.execute(get);
	            int code = response.getStatusLine().getStatusCode();
	            actualHttpResponse.setHttpRespCode(code);
	            logger.debug("httpResponseCode is : " + code);
	            if (code == 200) {
	                HttpEntity entity =  response.getEntity();

	                Map json = objectMapper.readValue(entity.getContent(), Map.class);
	                logger.info("-----------------------------");
	                logger.info("返回的 json 对象是{}",json);
	                logger.info("-----------------------------");
	                actualHttpResponse.setResponseBodyObject(json);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return actualHttpResponse;

	    }

	/**
     * 加密上传的信息
     *
     * @param content
     * @return
     */
    public static String encrypt(String content) {
        String result = content;
        try {
            result = SecurityUtils.cryption(ENCRYPT_KEY, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * 压缩上传的信息
     *
     * @param content
     * @return
     */
    public static byte[] compress(String content) {
        ByteArrayOutputStream out = null;
        GZIPOutputStream gzipOutputStream = null;
        try {
            out = new ByteArrayOutputStream();
            gzipOutputStream = new GZIPOutputStream(out);
            gzipOutputStream.write(content.getBytes(Charsets.UTF_8));
            gzipOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (gzipOutputStream != null) {
                try {
                    gzipOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return out.toByteArray();
    }

	  
	private static void requestLog(HttpRequestBase requestBase, String params){
	  logger.info("-----------------------------");
	  logger.info("{}",requestBase.getRequestLine());
	  HeaderIterator headerIterator = requestBase.headerIterator();
	  while (headerIterator.hasNext()) {
		  BasicHeader header = (BasicHeader)headerIterator.next();
		  logger.info("-----------------------------");
		  logger.info("{}:{}",header.getName(),header.getValue());
		
	  }
	  if(params != null){
		  logger.info("-----------------------------");
		  logger.info("传入参数{}",params);
	  }
	}
}
