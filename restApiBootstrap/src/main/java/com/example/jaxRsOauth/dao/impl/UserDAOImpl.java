package com.example.jaxRsOauth.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.jaxRsOauth.dao.UserDAO;
import com.example.jaxRsOauth.dbutil.DbConnection;
import com.example.jaxRsOauth.entity.User;

public class UserDAOImpl implements UserDAO {

	private DbConnection db = new DbConnection();

	@Override
	public User findByUsernamePassword(String username, String password) throws SQLException, Exception {
		User user = null;
		try {
			db.open();
			String sql = "SELECT username, role from tbl_user where username=? and password=?";
			PreparedStatement stmt = db.initStatement(sql);
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs = db.executeQuery();

			if (rs.next()) {
				user = new User();
				user.setUsername(rs.getString("username"));
				user.setRole(rs.getString("role"));

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		db.close();
		return user;
	}
	
	public static void main(String[] args) throws SQLException, Exception {
		UserDAOImpl userDAOImpl = new UserDAOImpl();
		User user = userDAOImpl.findByUsernamePassword("anish", "anish");
		System.out.println(user.toString());
				
	}

}
