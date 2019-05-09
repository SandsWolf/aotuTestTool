package com.autoyol.controller;

import com.autoyol.dao.MemberMapper;
import com.autoyol.entity.Member;
import com.autoyol.entity.PathIP;
import com.autoyol.entity.Result;
import com.autoyol.service.MemberService;
import com.autoyol.util.SetDateSourceUtil;
import com.autoyol.util.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/member")
public class MemberController {
	@Resource
	private MemberService memberService;
	@Resource
	private MemberMapper memberMapper;

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	/**
	 * 会员注册
	 * @param environment
	 * @param mobile
	 * @return
	 */
	@RequestMapping("/createmember")
	@ResponseBody
	public Result createMember(String environment, String mobile){
		Result result = new Result();
		if("线上".equals(environment)){
			result.setStatus(1);
			result.setMsg("success");
			result.setData("线上环境只能做查询操作");
			return result;
		}

		SetDateSourceUtil.setDataSourceName(environment);
		PathIP pathIP = ToolUtil.getIP(environment);

		if (!ToolUtil.isMobile(mobile)) {
			result = ToolUtil.checkMobile(mobile);
			return result;
		}
		
		result = memberService.createMember(mobile, pathIP);
		return result;
	}
	
	/**
	 * 会员认证
	 * @param environment
	 * @param mobile
	 * @return
	 */
	@RequestMapping("/updateinfo")
	@ResponseBody
	public Result updateMemberInfo(String environment, String mobile){
		Result result = new Result();
		if("线上".equals(environment)){
			result.setStatus(1);
			result.setMsg("success");
			result.setData("线上环境只能做查询操作");
			return result;
		}

		SetDateSourceUtil.setDataSourceName(environment);
		PathIP pathIP = ToolUtil.getIP(environment);

		if (!ToolUtil.isMobile(mobile)) {
			result = ToolUtil.checkMobile(mobile);
			return result;
		}
		 
		result = memberService.updateMemberInfo(mobile);
		return result;
	}


	/**
	 * 获取手机号对应附加驾驶人
	 * @param environment
	 * @param mobile
	 * @return
	 */
	@RequestMapping("/getCommUseDrivers")
	@ResponseBody
	public Result getCommUseDrivers(String environment, String mobile){
		SetDateSourceUtil.setDataSourceName(environment);
		Result result = new Result();
		try {
			List<Member> list = memberMapper.selectMemberInfoByMobile(mobile);
			if (list.size() == 0) {
				result.setStatus(1);
				result.setMsg("success");
				result.setData("手机号：\"" + mobile + "\"不存在 ; 请确认后再试");
				return result;
			}

			result = memberService.getCommUseDrivers(mobile,list.get(0).getReg_no());
		} catch (Exception e) {
			logger.error("获取附加驾驶人异常：",e);
		}
//		List<Member> list = memberMapper.selectMemberInfoByMobile(mobile);
//		if (list.size() == 0) {
//			result.setStatus(1);
//			result.setMsg("success");
//			result.setData("手机号：\"" + mobile + "\"不存在 ; 请确认后再试");
//			return result;
//		}
//
//		result = memberService.getCommUseDrivers(mobile,list.get(0).getReg_no());
		return result;
	}

	/**
	 * 获取手机号对应token/获取token对应手机号
	 * @param environment
	 * @param mobile
	 * @param token
	 * @return
	 */
	@RequestMapping("/getMobileOrToken")
	@ResponseBody
	public Result getMobileOrToken(String environment, String value){
		SetDateSourceUtil.setDataSourceName(environment);
		Result result = new Result();
		try {
			List<Member> list = memberMapper.selectMemberInfoByMobile(value);
			if (list.size() == 0) {
				result.setStatus(1);
				result.setMsg("success");
				result.setData("手机号：\"" + value + "\"不存在 ; 请确认后再试");
				return result;
			}

			result = memberService.getMobileOrToken(value);
		} catch (Exception e) {
		}

		return result;
	}
}
