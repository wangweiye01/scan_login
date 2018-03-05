package cn.epaylinks.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.epaylinks.model.User;

@Service
public interface IUserService
{

	List<User> getUserList();

}
