package com.iot.serviceImpl;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.DO.Login;
import com.iot.dao.LoginDao;
import com.iot.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService{

	@Autowired
	private LoginDao loginDao;
	
	@Override
	public String login(Login objLogin) throws SQLException {
		String jsonResponse = loginDao.login(objLogin);
		return jsonResponse;
	}
	
}
