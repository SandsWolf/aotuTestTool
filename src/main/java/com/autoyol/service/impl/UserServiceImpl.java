package com.autoyol.service.impl;

import com.autoyol.dao.UserMapper;
import com.autoyol.entity.Result;
import com.autoyol.entity.User;
import com.autoyol.service.UserService;
import com.autoyol.util.NoteUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService{
	@Resource
	private UserMapper userMapper;
	
	static{
//		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_MYSQL);
	}
	
	/*
	 * 登录检查
	 */
	public Result checkLogin(String name, String pwd) throws Exception {
		Result result = new Result();
		User user = userMapper.findByName(name);

		if(user == null){
			result.setStatus(1);
			result.setMsg("用户名不存在");
			return result;
		}
		
		if(!user.getUser_password().equals(NoteUtil.md5(pwd))){
			result.setStatus(2);
			result.setMsg("密码不正确");
			return result;
		}
		
		result.setStatus(0);
		result.setMsg("用户和密码正确");
		result.setData(user.getUser_id());	//登陆成功服务器返回用户id值
		return result;
	}

	
	/*
	 * 注册
	 */
	public Result regist(String name, String password, String nickname) throws Exception {
		Result result = new Result();
		
		User has_user = userMapper.findByName(name);
		if(has_user != null){
			result.setStatus(1);
			result.setMsg("用户名已被占用");
			return result;
		}
		
		User user = new User();
		user.setUser_name(name);
		user.setUser_desc(nickname);
		user.setUser_password(NoteUtil.md5(password));
		user.setUser_id(NoteUtil.createId());
		userMapper.save(user);

		result.setStatus(0);
		result.setMsg("注册成功");
		return result;
	}
}
