package com.autoyol.service;

import com.autoyol.entity.Result;


public interface UserService {
	public Result checkLogin(String name, String pwd) throws Exception;
	public Result regist(String name, String password, String nickname) throws Exception;
}
