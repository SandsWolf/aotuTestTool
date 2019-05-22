package com.autoyol.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@SuppressWarnings("deprecation")
public class HttpRequest {
    
    public static String sendPostTestServer(String strURL, String param,String userAgent) {  
        try {  
            URL url = new URL(strURL);// 创建连接  
            HttpURLConnection connection = (HttpURLConnection) url  
                    .openConnection();  
            connection.setDoOutput(true);  
            connection.setDoInput(true);  
            connection.setUseCaches(false);  
            connection.setInstanceFollowRedirects(true);  
            connection.setRequestMethod("POST"); // 设置请求方式  
            connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式  
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");  
            connection.setRequestProperty("user-agent", userAgent);
            //connection.setReadTimeout(30000);
            
            connection.connect();  
            OutputStreamWriter out = new OutputStreamWriter(  
                    connection.getOutputStream(), "UTF-8"); // utf-8编码  
            ObjectMapper om = new ObjectMapper();
//            //String paramString = om.writeValueAsString(param);
            if (param != null) {
            	out.append(param);  
            }
            
            out.flush();  
            out.close();
            
            //读取响应 ，进行GZIP解压
            BufferedReader in = new BufferedReader(
                   new InputStreamReader(connection.getInputStream()));
            String line;
            String result = "";
            while ((line = in.readLine()) != null) {
               result += line;
            }
            return result;
           
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } 
        
        return "error"; // 自定义错误信息  
    }
}
