package com.autoyol.dao;

import com.autoyol.entity.Tool;

import java.util.List;


public interface ToolMapper {
	public List<Tool> selectToolsByBoxid(String toolboxId);
	public String selectTooldescById(String toolId);

	
	public String pathTest(String mobile);
}
