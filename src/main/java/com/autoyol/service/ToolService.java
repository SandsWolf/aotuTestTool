package com.autoyol.service;

import com.autoyol.entity.Result;

public interface ToolService {
	public Result findToolsByBoxid(String toolboxId);
	public Result findTooldescById(String toolId);

	
	public Result pathTest(String mobile);
}
