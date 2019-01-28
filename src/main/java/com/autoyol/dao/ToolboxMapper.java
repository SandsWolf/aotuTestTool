package com.autoyol.dao;

import com.autoyol.entity.Toolbox;

import java.util.List;

public interface ToolboxMapper {
	public List<Toolbox> selectToolbox();
	public List<Toolbox> selectToolboxByToolboxid(String toolboxids);
}
