package com.autoyol.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * <b>Function: </b>
 *
 * @author jlsong
 * @date 2014-9-16
 * @file Com.java
 * @package com.autoyol.util.hw.news
 * @project autoyol
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class ApiUtils {

	public  static Logger logger = LoggerFactory.getLogger(ApiUtils.class);

	private static final String FAILURE = "failure";

	private static final CloseableHttpClient client = HttpClientBuilder
			.create().build();

	private static RequestConfig requestConfig = RequestConfig.custom()
			.setConnectionRequestTimeout(20000).setConnectTimeout(20000)
			.setSocketTimeout(20000).build();

	/**
	 * 硬件接口地址生成
	 *
	 * @param params
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String getUrl(String baseUrl, Map<String, Object> params,
								String md5Key) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		StringBuffer sbuff = new StringBuffer();
		int start = 0;
		for (Map.Entry<String, Object> param : params.entrySet()) {
			if (start == 0) {
				sbuff.append(param.getKey()).append("=")
						.append(param.getValue());
			} else {
				sbuff.append("&" + param.getKey()).append("=")
						.append(param.getValue());
			}
			start++;
		}
		String signStr = sbuff.toString();
		sbuff.append(md5Key);
		String sign = md5Encode(sbuff.toString());
		StringBuffer sb = new StringBuffer(baseUrl);
		sb.append(signStr).append("&");
		sb.append("sign=").append(sign);
		return sb.toString();
	}

	/**
	 * get 请求参数拼接
	 * @param baseUrl
	 * @param params
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getUrl(String baseUrl, Map<String, Object> params) throws NoSuchAlgorithmException {
		StringBuffer sbuff = new StringBuffer();
		int start = 0;
		for (Map.Entry<String, Object> param : params.entrySet()) {
			if (start == 0) {
				sbuff.append(param.getKey()).append("=")
						.append(param.getValue());
			} else {
				sbuff.append("&" + param.getKey()).append("=")
						.append(param.getValue());
			}
			start++;
		}
		String signStr = baseUrl+sbuff.toString();
		return signStr;
	}

	/**
	 * 指令格式生成
	 *
	 * @param params
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, Object> getInstruct(String cmd, Map<String, Object> params, String key) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> headerParams = new HashMap<String, String>();
		headerParams.put("cmd", cmd);
		map.put("header", headerParams);
		TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
		treeMap.putAll(params);
		if(isData(params)){
			StringBuffer sbff = new StringBuffer();
			for(Map.Entry<String, Object> param: treeMap.entrySet()){
				sbff.append(param.getKey()).append(param.getValue());
			}
			String sign = ApiUtils.md5Encode(sbff.toString().toUpperCase() + key);
			logger.info(sign);
			treeMap.put("sign", sign);
		}
		if(!treeMap.isEmpty()){
			map.put("body", treeMap);
		}
		return map;
	}

	/**
	 * 是否存在data值
	 * @param params
	 * @return
	 */
	private static boolean isData(Map<String, Object> params){
		boolean flag = true;
		for(Map.Entry<String, Object> m: params.entrySet()){
			if(m.getKey().equals("data")){
				return flag = false;
			}
		}
		return flag;
	}
//
//	/**
//	 * 接口回复格式验证
//	 * @return
//	 * @throws Exception
//	 */
//	public static String verify(String cmd, String rpStr) throws Exception{
//		if(rpStr != null && !"".equals(rpStr)){
//			//EncryptionDecryption encry = new EncryptionDecryption();
//			//rpStr = encry.decrypt(rpStr);
//			Map<String, Object> params = parseDataForMap(rpStr);
//			if(params!=null && params.size() > 0){
//				JSONObject obj = JSONObject.fromObject(params.get("header"));
//				if(!obj.get("cmd").equals(cmd)){
//					return FAILURE;
//				}else{
//					return params.get("body").toString();
//				}
//			}
//		}
//		return rpStr;
//	}

	/**
	 * MD5加密
	 *
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String md5Encode(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes("utf-8"));
		byte b[] = md.digest();
		int i;
		StringBuilder buf = new StringBuilder("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}
		return buf.toString();
	}
//
//	/**
//	 * GET请求
//	 *
//	 * @param url
//	 * @return
//	 * @throws Exception
//	 */
//	public static String sendHttpGet(String url) throws Exception {
//		String resContent = null;
//		CloseableHttpResponse response = null;
//		try {
//			HttpGet httpGet = new HttpGet(url.replace(" ", "%20"));
//			httpGet.setConfig(requestConfig);
//			response = client.execute(httpGet);
//			int resCode = response.getStatusLine().getStatusCode();
//			if (resCode == 200) {
//				resContent = EntityUtils.toString(response.getEntity());
//			}
//		} catch (Exception e) {
//			logger.error("", e);
//			throw e;
//		} finally {
//			if (response != null) {
//				response.close();
//			}
//		}
//		return resContent;
//	}
//
//	private static final String APPLICATION_JSON = "application/json";
//
//	@SuppressWarnings({ "resource" })
//	public static String sendHttpPostJson(String url, Map<String, Object> params) throws Exception {
//		String resContent = null;
//		HttpResponse httpResponse = null;
//		try {
//			JSONObject obj = JSONObject.fromObject(params);
//			DefaultHttpClient httpClient = new DefaultHttpClient();
//			HttpPost httpPost = new HttpPost(url);
//			httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
//			StringEntity se = new StringEntity(obj.toString(),"UTF-8");
//			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
//			httpPost.setEntity(se);
//			httpResponse = httpClient.execute(httpPost);
//			int resCode = httpResponse.getStatusLine().getStatusCode();
//			if (resCode == 200) {
//				HttpEntity httpEntity = httpResponse.getEntity();
//				BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(httpEntity.getContent(),"UTF-8"));
//				StringBuilder entityStringBuilder = new StringBuilder();
//				String line = null;
//				while ((line = bufferedReader.readLine()) != null) {
//					entityStringBuilder.append(line);
//				}
//				resContent = entityStringBuilder.toString();
//			}
//		} catch (Exception e) {
//			logger.error("", e);
//			throw e;
//		}
//		return resContent;
//	}

	/**
	 * POST请求
	 *
	 * @param url
	 * @param params  key value
	 * @return
	 * @throws Exception
	 */
	public static String sendHttpPost(String url, Map<String, Object> params)
			throws Exception {
		String resContent = null;
		CloseableHttpResponse response = null;
		try {
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for (Map.Entry<String, Object> param : params.entrySet()) {
				nvps.add(new BasicNameValuePair(param.getKey(), param.getValue().toString()));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			response = client.execute(httpPost);
			int resCode = response.getStatusLine().getStatusCode();
			if (resCode == 200) {
				resContent = EntityUtils.toString(response.getEntity());
			}
		} catch (Exception e) {
			logger.error("", e);
			throw e;
		} finally {
			if (response != null) {
				response.close();
			}
		}
		return resContent;
	}

//	/**
//	 * 将json字符串转换为map
//	 *
//	 * @param data
//	 * @return
//	 */
//	@SuppressWarnings("rawtypes")
//	public static Map<String, Object> parseDataForMap(Object data) {
//		Map<String, Object> params = new HashMap<String, Object>();
//		JSONObject jsonObject = JSONObject.fromObject(data);
//		Iterator it = jsonObject.keys();
//		while (it.hasNext()) {
//			String key = String.valueOf(it.next());
//			Object value = jsonObject.get(key);
//			params.put(key, value);
//		}
//		return params;
//	}

//	/**
//	 * 将json字符串转换为list
//	 *
//	 * @param data
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public static List<Map<String, Object>> parseDataForList(Object data) {
//		List<Map<String, Object>> list = JSONArray.fromObject(data);
//		return list;
//	}

//	/**
//	 * 将json字符串转换为list
//	 * @param data
//	 * @return
//	 * @throws JsonParseException
//	 * @throws JsonMappingException
//	 * @throws IOException
//	 */
//	@SuppressWarnings("unchecked")
//	public static List<Object> parseDataToList(Object data) {
//		List<Object> list = null;
//		if(data instanceof JSONArray)
//		{
//			list = JSONArray.fromObject(data);
//		}
//		else if(data instanceof String)
//		{
//			String [] array = ((String) data).split(",");
//			list = new ArrayList<Object>();
//			if(array!=null)
//			{
//				for(int i=0; i<array.length; i++)
//				{
//					list.add(array[i]);
//				}
//			}
//		}
//		else
//		{
//			list = new ArrayList<Object>();
//		}
//		return list;
//	}
//
//	/**
//	 * 将list字符串转换为json
//	 *
//	 * @param data
//	 * @return
//	 */
//	public static String parseListForJson(List<Map<String, Object>> list) {
//		JSONArray array = JSONArray.fromObject(list);
//		return array.toString();
//	}
//
//	/**
//	 * 将list字符串转换为json
//	 *
//	 * @param data
//	 * @return
//	 */
//	public static String parseListForJson(LinkedList<LinkedHashMap<String, Object>> list) {
//		JSONArray array = JSONArray.fromObject(list);
//		return array.toString();
//	}

	/**
	 * 保留两位小数
	 *
	 * @param f
	 * @return
	 */
	public static Double doubleParse(Double f) {
		BigDecimal bg = new BigDecimal(f);
		double num = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return num;
	}

	public static Double doubleParse(Object temp) {
		try {
			if (temp == null) {
				return 0.0;
			} else {
				return Double.parseDouble(temp.toString());
			}
		} catch (Exception e) {
			return 0.0;
		}
	}

	public static void appendMethodA(String fileName, String content) {
		try {
			// 打开一个随机访问文件流，按读写方式
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。
			randomFile.seek(fileLength);
			randomFile.writeBytes(content);
			randomFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * B方追加文件：使用FileWriter
	 *
	 * @param fileName
	 * @param content
	 */
	public static void appendMethodB(String fileName, String content) {
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	

	/**
	 * 解析RUL形式的参数
	 * @author zg
	 * @version v1.0 (2015年6月1日 下午2:16:42)
	 * @param reqContent
	 * @return TreeMap
	 */
	public static Map<String,Object> parseFormParams(String reqContent){//platenum=沪A00250&sign=1d1b3befd94733a943720f7d3c2fa4a3
		Map<String,Object> map = new TreeMap<>();
		StringTokenizer st = new StringTokenizer(reqContent,"&");
		while(st.hasMoreTokens()){
			String param = st.nextToken();
			int idx = param.indexOf("=");
			String key = param.substring(0,idx);
			String val = param.substring(idx+1);
			map.put(key, val);
		}
		return map;
	}
	

	 public static String SHA1(String decript) throws UnsupportedEncodingException {
	        try {
	            MessageDigest digest = MessageDigest
	                    .getInstance("SHA-1");
	            digest.update(decript.getBytes("UTF-8"));
	            byte messageDigest[] = digest.digest();
	            // Create Hex String
	            StringBuffer hexString = new StringBuffer();
	            // 字节数组转换为 十六进制 数
	            for (int i = 0; i < messageDigest.length; i++) {
	                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
	                if (shaHex.length() < 2) {
	                    hexString.append(0);
	                }
	                hexString.append(shaHex);
	            }
	            return hexString.toString();
	 
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        }
	        return "";
	    }
	 
	 
	
	
	/*public static void main(String[] args){
		System.out.println(parseFormParams("platenum=沪A00250&sign=1d1b3befd94733a943720f7d3c2fa4a3"));
	}*/

}
