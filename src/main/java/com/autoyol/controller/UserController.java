package com.autoyol.controller;

import com.autoyol.entity.Result;
import com.autoyol.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/user")
public class UserController {
	@Resource
	private UserService userService;
	
	@RequestMapping("/login")
	@ResponseBody
	public Result login(String name, String pwd) throws Exception {
		Result result = userService.checkLogin(name, pwd);
		return result;
	}
	
	
	@RequestMapping("/regist")
	@ResponseBody
	public Result regist(String name, String password, String nickname) throws Exception {
		Result result = userService.regist(name, password, nickname);
		return result;
	}
	
	
}
