package com.spring.insist.service;

import com.spring.insist.po.User;

import java.util.List;
import java.util.Map;

public interface UserService {
	List<User> queryUsers(Map<String, Object> param);
}
