package com.autoyol.service.impl;

import com.autoyol.dao.MemberMapper;
import com.autoyol.entity.*;
import com.autoyol.http.HttpResponse;
import com.autoyol.http.HttpUtils;
import com.autoyol.service.MemberService;
import com.autoyol.util.ToolUtil;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.autoyol.util.JsonFormatUtil.formatJson;

@Service
public class MemberServiceImpl implements MemberService {
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
		String str = jedis.set("LgCode:mobile:" + mobile, "111111");
		if (!"OK".equals(str)) {
			result.setStatus(0);
			result.setMsg("success");
			result.setData("Redis配置错误");
			return result;
		}

		List<Member> list = memberMapper.selectMemberInfoByMobile(mobile);
		if (list.size() != 0) {
			result.setStatus(0);
			result.setMsg("success");
			result.setData("手机号：\"" + mobile + "\"已存在 ; 登录验证码：\"111111\"");
			return result;
		}

		String url = pathIP.getServerIP() + "v31/mem/action/login";

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Content-Type", "application/json; charset=utf-8");
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
		Member member = list.get(0);

		Level level = memberMapper.selectWecashLevel(mobile);


		if (list != null && list.size() == 0) {
			result.setStatus(0);
			result.setMsg("success");
			result.setData("无此用户：" + mobile);
			return result;
		} else {
			if (level == null) {
				try {
					memberMapper.insertWecashLevel(member.getReg_no());
				} catch (Exception e) {
					e.getMessage();
				}
			} else {
				memberMapper.updateMemberInfo(mobile);
			}

			memberMapper.updateMemberInfo(mobile);

			List<Member> newMember = memberMapper.selectMemberInfoByMobile(mobile);
			member = newMember.get(0);
			System.out.println("member" + member.toString());

			Level newlevel = memberMapper.selectWecashLevel(mobile);


			if (member.getId_card_auth() == 2 && member.getDri_lic_auth() == 2 && member.getDri_vice_lic_auth() == 2 && member.getRent_flag() == 1 && newlevel != null && "5".equals(newlevel.getLevel())) {
				result.setStatus(0);
				result.setMsg("success");
				result.setData("用户：" + mobile + "，认证成功");
			} else {
				result.setStatus(0);
				result.setMsg("success");
				result.setData("用户：" + mobile + "，认证失败");
			}
		}

		return result;

	}


	public Result getCommUseDrivers(String mobile, String memNo) {
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

	/**
	 * 用token获取手机号或用手机号获取token信息
	 */
	public Result getMobileOrToken(String environment,String value) {

        List<Member> list = memberMapper.selectMemberInfoByMobile(value);

        Result result = new Result();

        PathIP pathIP = ToolUtil.getIP(environment);
        String redisIP = pathIP.getRedisIp();

        Jedis jedis = new Jedis(redisIP);	//连接本地的 Redis 服务

        if(!"PONG".equals(jedis.ping())){	//查看服务是否运行
            result.setStatus(0);
            result.setMsg("success");
            result.setData("\""+environment+"\"环境的Redis服务没在运行，请检查");
            return result;
        }

        if (value == null && value == "") {
			String str = "请检查输入是否正确";
			result.setStatus(0);
			result.setMsg("success");
			result.setData(str);
		} else if (value.length() == 11) {
			if (list != null && list.size() == 0) {
				result.setStatus(0);
				result.setMsg("success");
				result.setData("无此用户：" + value);
			} else {
                String str = "";
                str = "数据库token为：" + list.get(0).getToken();
                String memNo = list.get(0).getReg_no();
                Map<String, String> map;
                map = jedis.hgetAll("user:center:token:" + memNo);
                //使用迭代器，获取key
                Iterator<Map.Entry<String,String>> iter = map.entrySet().iterator();
                while(iter.hasNext()){
					Map.Entry<String,String> entry = iter.next();
					String token = entry.getKey();
					String res = "\r" + "redis用户信息如下：" + jedis.get("user:center:token:"+token);	// raids 通过key 查 value
                    str += res;
                }
                str = formatJson(str);
                result.setStatus(0);
				result.setMsg("success");
                result.setData(str);
            }
		} else {
			if (list != null && list.size() == 0) {

				String str = jedis.get("user:center:token:"+value);	// raids 通过key 查 value
				if(str != null){
					str = formatJson(str);
					result.setStatus(0);
					result.setMsg("success");
					result.setData(str);
				}else{
					result.setStatus(0);
					result.setMsg("success");
					result.setData("无此用户：" + value);
				}

			} else {
				String str;
				str = list.get(0).getMobile();
				result.setStatus(0);
				result.setMsg("success");
				result.setData(str);

            }
		}
        return result;
    }
}
