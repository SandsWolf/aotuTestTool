package com.autoyol.http;

import org.apache.commons.codec.binary.Base64;

import java.net.URLEncoder;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map.Entry;
import java.util.TreeMap;

public class SecurityApiUtils {
	
	private static final String ENCODEING = "UTF-8";
	private static final String ALGORITHM = "RSA";
	private static final String SIGNATURE_ALGORITHM = "MD5withRSA";
	
	//
	private static final String PRIVATE_KEY_STR = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAxvL/FQTBTHtuRRvWBktttNjoE/4YiMQwrzbgm3cZuYiEJF0aSWpxSmJWdPRhPDRcbYTrP9W7b7hWEJ7jJfP1uQIDAQABAkBFJMTkkC+CHk/XsvHyZBqvxMb7087BEbZ93su3HHCTWzHvK2x8vawLLB3N2B5wQfGNqzMaQguK+XkZDQxZnEiBAiEA/U8tmY37PVPfSWLQyzec9aeoaNpm5OTH/UNUexnaCdECIQDJD/9SyJrtPC7m4TnZHOuht3pKbhjNeOkhX9+VJMO/aQIhAJ1gbPdDA/3VNxuz/f7T3XuuH26NinHZRfsusrUMma+RAiA2wZyPNwK6SQGc7wmKD048pHMxgfpPOvaCmFGTlIeawQIgMP7fq6HFLr2ItaLQ9XUnDqP7se3HBahvAbNw/pgK2xA=";
	private static final byte[] PRIVATE_KEY = Base64.decodeBase64(PRIVATE_KEY_STR);
			
	private static final int KEY_SIZE = 512;//秘钥长度 （64的整数倍，512-65536位之间，默认为1024）
	
	private static final PKCS8EncodedKeySpec pkcs8EncodeKeySpec = new PKCS8EncodedKeySpec(PRIVATE_KEY);//
	
	private static KeyFactory priKeyFac = null;
	private static KeyFactory pubKeyFac = null;
	
	static {
		try {
			priKeyFac = KeyFactory.getInstance(ALGORITHM);
			pubKeyFac = KeyFactory.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 对字符串内容进行签名
	 * @param fieldVals （要进行签名的数据，需按文档中的字段顺序传入）
	 * @return
	 * @throws Exception
	 */
	public static String makeSign(String...fieldVals) throws Exception {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fieldVals.length; i++) {
			sb.append(fieldVals[i]);
		}
		byte[] data = sb.toString().getBytes(ENCODEING);
		PrivateKey priKey = priKeyFac.generatePrivate(pkcs8EncodeKeySpec);//获取私钥对象
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);//获取Signature对象
		signature.initSign(priKey);
		signature.update(data);
		String reqContent = Base64.encodeBase64String(signature.sign());
		//编码放置在公共方法中。
		reqContent = URLEncoder.encode(reqContent, ENCODEING);//需要做encode,防止POST提交时丢失“+”
		return reqContent;
	}
	
	/***对传入的map进行签名
	 * 
	 * @param treeMap
	 * @return
	 * @throws Exception
	 */
	public static String makeSign(TreeMap<String, Object> treeMap) throws Exception {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Object> entry : treeMap.entrySet()) {
			sb.append(entry.getValue());
        }
		byte[] data = sb.toString().getBytes(ENCODEING);
		PrivateKey priKey = priKeyFac.generatePrivate(pkcs8EncodeKeySpec);//获取私钥对象
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);//获取Signature对象
		signature.initSign(priKey);
		signature.update(data);
		String reqContent = Base64.encodeBase64String(signature.sign());
		//编码放置在公共方法中。
		reqContent = URLEncoder.encode(reqContent, ENCODEING);//需要做encode,防止POST提交时丢失“+”
		return reqContent;
	}
	
	/**
	 * 验证签名
	 * @param data
	 * @param publicKey
	 * @param sign （length必须大于64）
	 * @param pubKey 公钥
	 * @return
	 * @throws Exception
	 */
	public static boolean verifySign(String content, String sign, String pubKey)throws Exception {
		byte[] pkByteArr =  Base64.decodeBase64(pubKey);
		X509EncodedKeySpec x509EncodeKeySpec = new X509EncodedKeySpec(pkByteArr);
		PublicKey pk = pubKeyFac.generatePublic(x509EncodeKeySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(pk);
		signature.update(content.getBytes(ENCODEING));
		return signature.verify(Base64.decodeBase64(sign));
	}
	
	/**
	 * 生成秘钥对
	 * @throws NoSuchAlgorithmException
	 */
	@SuppressWarnings("unused")
	private static void generateKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);//获得秘钥对生成器
		kpg.initialize(KEY_SIZE);
		KeyPair keyPair = kpg.generateKeyPair();
		RSAPublicKey pubKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey priKey = (RSAPrivateKey) keyPair.getPrivate();
		
		System.out.println("公钥："+Base64.encodeBase64String(pubKey.getEncoded()));//"MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAMby/xUEwUx7bkUb1gZLbbTY6BP+GIjEMK824Jt3GbmIhCRdGklqcUpiVnT0YTw0XG2E6z/Vu2+4VhCe4yXz9bkCAwEAAQ=="
		System.out.println("私钥："+Base64.encodeBase64String(priKey.getEncoded()));//"MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAxvL/FQTBTHtuRRvWBktttNjoE/4YiMQwrzbgm3cZuYiEJF0aSWpxSmJWdPRhPDRcbYTrP9W7b7hWEJ7jJfP1uQIDAQABAkBFJMTkkC+CHk/XsvHyZBqvxMb7087BEbZ93su3HHCTWzHvK2x8vawLLB3N2B5wQfGNqzMaQguK+XkZDQxZnEiBAiEA/U8tmY37PVPfSWLQyzec9aeoaNpm5OTH/UNUexnaCdECIQDJD/9SyJrtPC7m4TnZHOuht3pKbhjNeOkhX9+VJMO/aQIhAJ1gbPdDA/3VNxuz/f7T3XuuH26NinHZRfsusrUMma+RAiA2wZyPNwK6SQGc7wmKD048pHMxgfpPOvaCmFGTlIeawQIgMP7fq6HFLr2ItaLQ9XUnDqP7se3HBahvAbNw/pgK2xA=";
		System.out.println("时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	}
	
	public static void main(String[] args) throws Exception {
		//generateKeyPair();
		
		String pubKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAMby/xUEwUx7bkUb1gZLbbTY6BP+GIjEMK824Jt3GbmIhCRdGklqcUpiVnT0YTw0XG2E6z/Vu2+4VhCe4yXz9bkCAwEAAQ==";
		String str = "sf格格热热热热热访问服务范围范文芳二二二二热热热";
		System.out.println("原文："+str);
		String sign = makeSign(str);//签名
		System.out.println("生成签名："+sign);
		boolean result = verifySign(str, sign, pubKey);//验签
		System.out.println("验签："+result);
		
		
	}

}
