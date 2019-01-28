package com.autoyol.dao;

import com.autoyol.entity.User;


public interface UserMapper {
	public User findByName(String name);
	public void save(User user);
}
