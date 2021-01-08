package com.spring.insist.dao;

import com.spring.insist.po.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

	List<User> queryUserList(Map<String, Object> param);
}
