package com.autoyol.entity;

import java.io.Serializable;

public class NoteBook implements Serializable {
	private String notebook_id;
	private String notebook_type_id;
	private String notebook_name;
	private String notebook_desc;
	private String notebook_createtime;
	private String notebook_lastmodifytime;
	private int status;
	public String getNotebook_id() {
		return notebook_id;
	}
	public void setNotebook_id(String notebook_id) {
		this.notebook_id = notebook_id;
	}
	public String getNotebook_type_id() {
		return notebook_type_id;
	}
	public void setNotebook_type_id(String notebook_type_id) {
		this.notebook_type_id = notebook_type_id;
	}
	public String getNotebook_name() {
		return notebook_name;
	}
	public void setNotebook_name(String notebook_name) {
		this.notebook_name = notebook_name;
	}
	public String getNotebook_desc() {
		return notebook_desc;
	}
	public void setNotebook_desc(String notebook_desc) {
		this.notebook_desc = notebook_desc;
	}
	public String getNotebook_createtime() {
		return notebook_createtime;
	}
	public void setNotebook_createtime(String notebook_createtime) {
		this.notebook_createtime = notebook_createtime;
	}
	public String getNotebook_lastmodifytime() {
		return notebook_lastmodifytime;
	}
	public void setNotebook_lastmodifytime(String notebook_lastmodifytime) {
		this.notebook_lastmodifytime = notebook_lastmodifytime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
