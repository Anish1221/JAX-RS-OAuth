package com.example.jaxRsOauth.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.jaxRsOauth.dao.TaskDAO;
import com.example.jaxRsOauth.dbutil.DbConnection;
import com.example.jaxRsOauth.entity.Task;

public class TaskDAOImpl implements TaskDAO{

	private DbConnection db = new DbConnection();
	
	@Override
	public int insertTask(Task task) throws SQLException, Exception {
		int result = 0;
		try{
			db.open();
			String sql = "INSERT INTO tbl_task(task_name, task_description) VALUES(?,?)";
			PreparedStatement stmt = db.initStatement(sql);
			stmt.setString(1, task.getTaskName());
			stmt.setString(2, task.getTaskDescription());
			result = db.executeUpdate();
			db.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}
	
	@Override
	public List<Task> getAll(){
		List<Task> taskList = null;
		try{
			db.open();
			String sql = "SELECT task_name, task_description FROM tbl_task";
			PreparedStatement stmt = db.initStatement(sql);
			ResultSet rs = db.executeQuery();
			taskList = new ArrayList<>();
			while(rs.next()){
				Task task = new Task();
				task.setTaskName(rs.getString("task_name"));
				task.setTaskDescription(rs.getString("task_description"));
				taskList.add(task);
			}
			db.close();
		}catch(Exception ex){
			System.out.println("Exception on getting list of task: "+ex);
			ex.printStackTrace();
		}
		return taskList;
	}

}
