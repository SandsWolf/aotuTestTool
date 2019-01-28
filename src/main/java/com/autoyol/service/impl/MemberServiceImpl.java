package com.autoyol.service.impl;

import com.autoyol.dao.MemberMapper;
import com.autoyol.entity.CommUseDriver;
import com.autoyol.entity.Member;
import com.autoyol.entity.PathIP;
import com.autoyol.entity.Result;
import com.autoyol.http.HttpResponse;
import com.autoyol.http.HttpUtils;
import com.autoyol.service.MemberService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberServiceImpl implements MemberService{
	@Resource
	private MemberMapper memberMapper;
	
	/**
	 * 会员注册
	 * 登录验证码默认设置：111111
	 */
	public Result createMember(String mobile, PathIP pathIP) {
		Result result = new Result();
		
		//设置Redis
		//连接 Redis 服务
        Jedis jedis = new Jedis(pathIP.getRedisIp());
        //设置 redis 登陆验证码,默认：111111
        String str = jedis.set("LgCode:mobile:"+mobile, "111111");
        if(!"OK".equals(str)){
        	result.setStatus(0);
    		result.setMsg("success");
    		result.setData("Redis配置错误");
    		return result;
        }
		
        List<Member> list = memberMapper.selectMemberInfoByMobile(mobile);
		if(list.size()!=0){
			result.setStatus(0);
    		result.setMsg("success");
    		result.setData("手机号：\"" + mobile + "\"已存在 ; 登录验证码：\"111111\"");
    		return result;
		}
		
		String url = pathIP.getServerIP() + "v31/mem/action/login";
		
		Map<String,String> headerMap = new HashMap<String, String>();
		headerMap.put("Content-Type","application/json; charset=utf-8");
		headerMap.put("connection", "Keep-Alive");
		headerMap.put("User-Agent", "AutoyolEs_console");
		headerMap.put("Accept", "application/json;version=3.0;compress=false");
		
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("OsVersion", "25");
		paraMap.put("requestId", "90ADF7D73FA11513755451817");
		paraMap.put("mobile", mobile);
		paraMap.put("validCode", "111111");
		paraMap.put("OS", "ANDROID");
		paraMap.put("mem_no", "166680504");
		paraMap.put("AppChannelId", "meizu");
		paraMap.put("appName", "atzucheApp");
		paraMap.put("publicCityCode", "021");
		paraMap.put("IMEI", "90ADF7D73FA1");
		paraMap.put("AndroidId", "266c1e21acba91f7");
		paraMap.put("publicToken", "0");
		paraMap.put("AppVersion", "69");
		paraMap.put("isNew", "1");
		paraMap.put("PublicLongitude", "0");
		paraMap.put("deviceName", "vivoX9s");
		paraMap.put("PublicLatitude", "0");
		
		HttpResponse httpResult = HttpUtils.post(headerMap, url, paraMap, false, false);
		
		result.setStatus(0);
		result.setMsg("success");
		result.setData("手机号：\"" + mobile + "\"注册成功 ; 登录验证码：\"111111\"<br><br>" + httpResult.getResponseBodyObject().toString());
		
		return result;
	}

	
	/**
	 * 会员认证
	 */
	public Result updateMemberInfo(String mobile) {
		Result result = new Result();
		List<Member> list = memberMapper.selectMemberInfoByMobile(mobile);
		
		if(list.size()==0){
			result.setStatus(0);
			result.setMsg("success");
			result.setData("无此用户：" + mobile);
		}else{
			memberMapper.updateMemberInfo(mobile);
			list = memberMapper.selectMemberInfoByMobile(mobile);
			
			Member member = list.get(0);
			if(member.getId_card_auth()==2 && member.getDri_lic_auth()==2 && member.getDri_vice_lic_auth()==2 && member.getRent_flag()==1){
				result.setStatus(0);
				result.setMsg("success");
				result.setData("用户：" + mobile + "，认证成功");
			}else{
				result.setStatus(0);
				result.setMsg("success");
				result.setData("用户：" + mobile + "，认证失败");
			}
		}
		
		return result;
	}


	public Result getCommUseDrivers (String mobile, String memNo) {
		Result result = new Result();
		List<CommUseDriver> list = memberMapper.selectCommUseDrivers(memNo);

		if (list.size() == 0) {
			result.setStatus(1);
			result.setMsg("success");
			result.setData("手机号：\"" + mobile + "\"名下不存在附加驾驶人");
		} else {
			String str = "";
			for (int i = 0; i < list.size(); i++) {
				str += "<p><input type=\"checkbox\" name=\"category\" />   " + list.get(i).getRealName() + "(" + list.get(i).getMobile() + ") </p>";
			}
			result.setStatus(0);
			result.setMsg("success");
			result.setData(str);
		}
		return result;
	}
}
