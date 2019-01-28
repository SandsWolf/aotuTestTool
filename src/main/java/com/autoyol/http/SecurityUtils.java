package com.autoyol.http;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map.Entry;
import java.util.TreeMap;


public class SecurityUtils {
    public static final String ALGORITHM_MD5 = "MD5";
    public static final String ALGORITHM_3DES = "DESede";
    private static final String ENCODING_UTF8 = "UTF-8";

    public static final String ALGORITHM_SHA512 = "SHA-512";

    /**
     * 对上送到服务端请求报文加密
     * @param key
     * @param content
     * @return
     * @throws Exception
     * @throws UnsupportedEncodingException
     */
    public static String cryption(String key, String content) throws UnsupportedEncodingException, Exception {
        return byteArray2HexString(encryptMode(hexString2ByteArray(Md5(key)), content.getBytes()));
    }
    
    /***
     * sign签名加密算法
     * @param key
     * @param content
     * @return
     * @throws UnsupportedEncodingException
     * @throws Exception
     */
    public static String signCryption(String key, String content) throws UnsupportedEncodingException, Exception {
    	
    	SecretKey secretKey = new SecretKeySpec(key.getBytes(),"HmacMD5");

        Mac mac = Mac.getInstance(secretKey.getAlgorithm());

        mac.init(secretKey);
        return byteArray2HexString(mac.doFinal(content.getBytes("UTF-8")));
    }
    
    
    
    public static String getSign(String key, TreeMap<String,Object> map, int type, Object... fieldsName) throws UnsupportedEncodingException, Exception {
    	StringBuilder keysandvalues = new StringBuilder();
        if(type==1){//适用appServer登录
    		for (Entry<String, Object> entry : map.entrySet()) {
                keysandvalues.append(entry.getValue());
            }
    		return signCryption(key,keysandvalues.toString());
    	}else if(type==2){//适用仁云对外接口加密
    		for (Entry<String, Object> entry : map.entrySet()) {
    			keysandvalues.append(entry.getKey()).append(entry.getValue());
            }
    		return md5Encode(keysandvalues.toString().toUpperCase()+key);
		}else if(type==3){//适用app快注和登录，不定字段加密规则
    		for (Object obj : fieldsName){
            	keysandvalues.append(obj);
            }
    		System.out.println("zifuchuan1++++++++++++++++"+keysandvalues.toString());
    		return signCryption(key,keysandvalues.toString());
    	}else {
			throw new Exception("未定义的读取map数据方式");//主动抛出一个异常
		}        
    }
    
    /**
     * MD5签名算法 返回 Hex string(32位大写)
     * @param plainText
     * @return
     * @throws Exception
     */
    public static String Md5(String plainText) throws Exception {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM_MD5);
        md.update(plainText.getBytes());
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
        return buf.toString().toUpperCase();

    }
    
    /***
     * MD5签名算法 返回 Hex string(32位小写)
     * @param plainText
     * @return
     * @throws Exception
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
    
    public static String SHA512(String plainText) throws Exception {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM_SHA512);
        byte[] byteArr = md.digest(plainText.getBytes(ENCODING_UTF8));
        return byteArray2HexString(byteArr);
    }

    //---------------------------------------------------------- private methods ------------------------------------------------------------
    private static byte[] encryptMode(byte[] keybyte, byte[] src) {
        try {
            // 生成密钥
            if (keybyte.length < 24) {
                keybyte = SecurityUtils.convertKey(keybyte);
            }
            SecretKey deskey = new SecretKeySpec(keybyte, ALGORITHM_3DES);
            // 加密
            Cipher c1 = Cipher.getInstance(ALGORITHM_3DES);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    // keybyte为加密密钥，长度为24字节
    // src为加密后的缓冲区

    private static byte[] decryptMode(byte[] keybyte, byte[] src) {
        try {
            //System.out.println("key length: " + keybyte.length);
            if (keybyte.length < 24) {
                keybyte = SecurityUtils.convertKey(keybyte);
            }
            SecretKey deskey = new SecretKeySpec(keybyte, ALGORITHM_3DES);
            Cipher c1 = Cipher.getInstance("DESede/ECB/NoPadding");
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        System.out.println("----------xxxxxxxxxxxxxxx--------");
        return null;
    }

    // 将16字节的密钥转换成24字节
    private static byte[] convertKey(byte[] srcKey) {
        byte[] destKey = null;
        byte[] keyFirst = new byte[8];
        ByteBuffer buffer = ByteBuffer.wrap(srcKey);
        buffer.get(keyFirst);
        buffer.clear();
        buffer = ByteBuffer.allocate(24);
        buffer.put(srcKey);
        buffer.put(keyFirst);
        buffer.flip();
        destKey = buffer.array();
        buffer.clear();
        return destKey;
    }

	/*private static String byteArray2HexString(byte[] arr) {
		StringBuilder sbd = new StringBuilder();
		for (byte b : arr) {
			String tmp = Integer.toHexString(0xFF & b);
			if (tmp.length() < 2)
				tmp = "0" + tmp;
			sbd.append(tmp);
		}
		return sbd.toString();
	}*/

    private static String byteArray2HexString(byte[] byteArr) {
        if(byteArr == null || byteArr.length == 0){
            return null;
        }
        StringBuilder sbd = new StringBuilder("");
        for (byte b : byteArr) {
            String tmp = Integer.toHexString(0xFF & b);
            if (tmp.length() < 2){
                sbd.append(0);
            }
            sbd.append(tmp);
        }
        return sbd.toString();
    }

    private static byte[] hexString2ByteArray(String hexStr) {
        if (hexStr == null)
            return null;
        if (hexStr.length() % 2 != 0) {
            return null;
        }
        byte[] data = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            char hc = hexStr.charAt(2 * i);
            char lc = hexStr.charAt(2 * i + 1);
            byte hb = hexChar2Byte(hc);
            byte lb = hexChar2Byte(lc);
            if (hb < 0 || lb < 0) {
                return null;
            }
            int n = hb << 4;
            data[i] = (byte) (n + lb);
        }
        return data;
    }

    private static byte hexChar2Byte(char c) {
        if (c >= '0' && c <= '9')
            return (byte) (c - '0');
        if (c >= 'a' && c <= 'f')
            return (byte) (c - 'a' + 10);
        if (c >= 'A' && c <= 'F')
            return (byte) (c - 'A' + 10);
        return -1;
    }

//	public static String getSign(String key, TreeMap<String, Object> reqMap,int i) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
