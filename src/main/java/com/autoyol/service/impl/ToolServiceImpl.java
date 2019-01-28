package com.autoyol.service.impl;

import com.autoyol.dao.ToolMapper;
import com.autoyol.entity.Result;
import com.autoyol.entity.Tool;
import com.autoyol.service.ToolService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ToolServiceImpl implements ToolService{
	@Resource
	private ToolMapper toolMapper;
	
	/**
	 * 测试环境用
	 */
	public Result pathTest(String mobile) {
		Result result = new Result();
		try {
			String realName = toolMapper.pathTest(mobile);
			result.setStatus(0);
			result.setMsg("success");
			result.setData(realName);
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(1);
			result.setMsg("error");
		}
		
		return result;
	}
	
	/**
	 * 加载tool列表
	 */
	public Result findToolsByBoxid(String toolboxId) {
		Result result = new Result();
		try {
			List<Tool> list = toolMapper.selectToolsByBoxid(toolboxId);
			
			result.setStatus(0);
			result.setMsg("success");
			result.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(1);
			result.setMsg("error");
		}
		
		return result;
	}

	/**
	 * 加载tool
	 */
	public Result findTooldescById(String toolId) {
		Result result = new Result();
		try {
			String toolDesc = toolMapper.selectTooldescById(toolId);
			result.setStatus(0);
			result.setMsg("success");
			result.setData(toolDesc);
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(1);
			result.setMsg("error");
		}
		
		return result;
	}

	
	
	
	


}
