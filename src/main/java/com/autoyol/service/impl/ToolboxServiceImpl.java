package com.autoyol.service.impl;

import com.autoyol.dao.ToolboxMapper;
import com.autoyol.entity.Result;
import com.autoyol.entity.Toolbox;
import com.autoyol.service.ToolboxService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class ToolboxServiceImpl implements ToolboxService{
	@Resource
	private ToolboxMapper toolboxMapper;

	/**
	 * 加载工具箱列表
	 */
	public Result findToolBoxes() {
		Result result = new Result();
		try {
			List<Toolbox> list = toolboxMapper.selectToolbox();

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

}
