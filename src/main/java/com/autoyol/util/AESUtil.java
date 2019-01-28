package com.autoyol.util;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class AESUtil {
    private static String key = "";

    static {
        Properties config = getProperties();
        key = config.getProperty("key");
    }

    /**
     * @return
     */
    public static Properties getProperties(){
        Properties prop = null;
        try {
            prop = new Properties();
            FileInputStream fis = new FileInputStream(new File("../key.properties"));
            InputStreamReader in = new InputStreamReader (fis,"utf-8");

            prop.load(in);	//加载文件输入流
            in.close();		//关闭输入流
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }

    /**
     * 加密
     * @param content
     * @param strKey
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(String content,String strKey ) throws Exception {
        SecretKeySpec skeySpec = getKey(strKey);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(content.getBytes());
        return  encrypted;
    }

    /**
     * 解密
     * @param strKey
     * @param content
     * @return
     * @throws Exception
     */
    public static String decrypt(byte[] content,String strKey ) throws Exception {
        SecretKeySpec skeySpec = getKey(strKey);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] original = cipher.doFinal(content);
        String originalString = new String(original);
        return originalString;
    }

    private static SecretKeySpec getKey(String strKey) throws Exception {
        byte[] arrBTmp = strKey.getBytes();
        byte[] arrB = new byte[16]; // 创建一个空的16位字节数组（默认值为0）

        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }

        SecretKeySpec skeySpec = new SecretKeySpec(arrB, "AES");

        return skeySpec;
    }


    /**
     * base 64 encode
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes){
        return new BASE64Encoder().encode(bytes);
    }

    /**
     * base 64 decode
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) throws Exception{
        return base64Code.isEmpty() ? null : new BASE64Decoder().decodeBuffer(base64Code);
    }

    /**
     * AES加密为base 64 code
     * @param content 待加密的内容
     * @return 加密后的base 64 code
     * @throws Exception  //加密传String类型，返回String类型
     */
    public static String aesEncrypt(String content) throws Exception {
        return base64Encode(encrypt(content, key));
    }
    /**
     * 将base 64 code AES解密
     * @param encryptStr 待解密的base 64 code
     * @return 解密后的string   //解密传String类型，返回String类型
     * @throws Exception
     */
    public static String aesDecrypt(String encryptStr) throws Exception {
        return encryptStr.isEmpty() ? null : decrypt(base64Decode(encryptStr), key);
    }

    public static void main(String[] args) {
        AESUtil aesUtil = new AESUtil();
        Properties config = aesUtil.getProperties();
        String key = config.getProperty("key");
        System.out.println("key:" + key);
    }
}
