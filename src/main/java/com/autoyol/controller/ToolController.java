package com.autoyol.controller;

import com.autoyol.entity.Result;
import com.autoyol.service.ToolService;
import com.autoyol.util.SetDateSourceUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/tool")
public class ToolController {
	@Resource
	private ToolService toolService;
	
	/**
	 * 加载tool列表
	 * @param toolboxId
	 * @return
	 */
	@RequestMapping("/loadlist")
	@ResponseBody
	public Result loadTools(String toolboxId){
		SetDateSourceUtil.setDataSourceName("baseDB");
		
		Result result = toolService.findToolsByBoxid(toolboxId);
		return result;
	}
	
	/**
	 * 加载tool
	 * @param toolId
	 * @return
	 */
	@RequestMapping("/loadtool")
	@ResponseBody
	public Result loadToolDesc(String toolId){
		SetDateSourceUtil.setDataSourceName("baseDB");
		
		Result result = toolService.findTooldescById(toolId);
		return result;
	}
	
	
	
	
	/**
	 * 环境测试用
	 * @param mobile
	 * @return
	 */
	@RequestMapping("/pathtest")
	@ResponseBody
	public Result pathTest(String mobile, String environment){
		SetDateSourceUtil.setDataSourceName(environment);
		
		Result result = toolService.pathTest(mobile);
		return result;
	}
	


}
