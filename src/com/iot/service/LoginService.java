package com.iot.service;

import java.sql.SQLException;

import com.iot.DO.Login;

public interface LoginService {
	public String login(Login l)throws SQLException;
}
