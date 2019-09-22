package com.example.jaxRsOauth.dao;

import java.sql.SQLException;

import com.example.jaxRsOauth.entity.User;

public interface UserDAO {
	public User findByUsernamePassword(String username, String password) throws SQLException, Exception;
}
