package com.example.jaxRsOauth.dao;

import java.sql.SQLException;
import java.util.List;

import com.example.jaxRsOauth.entity.Task;


public interface TaskDAO {
	public int insertTask(Task task) throws SQLException, Exception;
	
	public List<Task> getAll();
}
