package cn.epaylinks.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.epaylinks.mapper.UserMapper;
import cn.epaylinks.model.User;
import cn.epaylinks.service.IUserService;

@Service
public class UserServiceImpl implements IUserService
{

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public List<User> getUserList()
	{
		// TODO Auto-generated method stub
		List<User> list = userMapper.findUserInfo();
		return list;
	}
	
}
