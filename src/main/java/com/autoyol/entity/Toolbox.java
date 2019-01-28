package com.autoyol.entity;

public class Toolbox {
	private String toolbox_id;
	private String toolbox_type_id;
	private String toolbox_name;
	private String toolbox_desc;
	private String toolbox_createtime;
	private String toolbox_lastmodifytime;
	private String status;
	public String getToolbox_id() {
		return toolbox_id;
	}
	public void setToolbox_id(String toolbox_id) {
		this.toolbox_id = toolbox_id;
	}
	public String getToolbox_type_id() {
		return toolbox_type_id;
	}
	public void setToolbox_type_id(String toolbox_type_id) {
		this.toolbox_type_id = toolbox_type_id;
	}
	public String getToolbox_name() {
		return toolbox_name;
	}
	public void setToolbox_name(String toolbox_name) {
		this.toolbox_name = toolbox_name;
	}
	public String getToolbox_desc() {
		return toolbox_desc;
	}
	public void setToolbox_desc(String toolbox_desc) {
		this.toolbox_desc = toolbox_desc;
	}
	public String getToolbox_createtime() {
		return toolbox_createtime;
	}
	public void setToolbox_createtime(String toolbox_createtime) {
		this.toolbox_createtime = toolbox_createtime;
	}
	public String getToolbox_lastmodifytime() {
		return toolbox_lastmodifytime;
	}
	public void setToolbox_lastmodifytime(String toolbox_lastmodifytime) {
		this.toolbox_lastmodifytime = toolbox_lastmodifytime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Toolbox{" +
				"toolbox_id='" + toolbox_id + '\'' +
				", toolbox_type_id='" + toolbox_type_id + '\'' +
				", toolbox_name='" + toolbox_name + '\'' +
				", toolbox_desc='" + toolbox_desc + '\'' +
				", toolbox_createtime='" + toolbox_createtime + '\'' +
				", toolbox_lastmodifytime='" + toolbox_lastmodifytime + '\'' +
				", status='" + status + '\'' +
				'}';
	}
}
