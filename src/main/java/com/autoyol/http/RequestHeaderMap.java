package com.autoyol.http;

import java.util.HashMap;
import java.util.Map;

public class RequestHeaderMap {
	public static HashMap<String, Map<String, String>> headerMap(){
		
		
		HashMap<String, Map<String, String>> headerMap = new HashMap<String, Map<String, String>>();
		HashMap<String , String> headerMapAutoyolEsweb = new HashMap<String , String>();
		headerMapAutoyolEsweb.put("User-Agent", "AutoyolEs_web");
		headerMapAutoyolEsweb.put("Content-Type","application/json;charset=UTF-8");    	
    	headerMap.put("AutoyolEs_web", headerMapAutoyolEsweb);
    	
    	HashMap<String , String> headerMapAutoyolEsCPIC = new HashMap<String , String>();
    	headerMapAutoyolEsCPIC.put("User-Agent", "AutoyolEs_CPIC");
    	headerMapAutoyolEsCPIC.put("Content-Type","application/json;charset=UTF-8");   	
    	headerMap.put("AutoyolEs_CPIC", headerMapAutoyolEsCPIC); 
 
		return headerMap;
	};
}
