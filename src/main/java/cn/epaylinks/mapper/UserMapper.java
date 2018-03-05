package cn.epaylinks.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.epaylinks.model.User;

@Repository
public interface UserMapper
{
	public List<User> findUserInfo();
}
