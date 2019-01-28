package com.autoyol.util;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;


public class NoteUtil {
	//生成cn_user_id
	public static String createId(){
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	
	//给密码加密
	public static String md5(String msg) throws NoSuchAlgorithmException {
		//利用md5对msg处理
		MessageDigest md = MessageDigest.getInstance("MD5");
		
		byte[] input = msg.getBytes();
		byte[] output = md.digest(input); //MD5是处理字节信息，所以需要将字符串先转成字节数组
		
		//将MD5处理的output结果转成字符串(先要导入commons-codec-1.9.jar包)
		String result = Base64.encodeBase64String(output);
		return result;
	}



}
