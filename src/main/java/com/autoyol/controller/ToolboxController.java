package com.autoyol.controller;

import com.autoyol.entity.Result;
import com.autoyol.service.ToolboxService;
import com.autoyol.util.SetDateSourceUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/toolbox")
public class ToolboxController {
	@Resource
	private ToolboxService toolboxService;
	
	@RequestMapping("/loadlist")
	@ResponseBody
	public Result loadToolboxs(){
		SetDateSourceUtil.setDataSourceName("baseDB");
		
		Result result = toolboxService.findToolBoxes();
		return result;
	}
	
}
