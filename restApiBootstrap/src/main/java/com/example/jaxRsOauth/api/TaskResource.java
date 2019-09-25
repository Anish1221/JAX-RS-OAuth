package com.example.jaxRsOauth.api;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.example.jaxRsOauth.dao.TaskDAO;
import com.example.jaxRsOauth.dao.impl.TaskDAOImpl;
import com.example.jaxRsOauth.entity.Task;

@Path("tasks")
public class TaskResource {

	TaskDAO taskDAO = new TaskDAOImpl();

	@GET
	@RolesAllowed(value={"user"})
	@Produces(value = MediaType.APPLICATION_JSON)
	public List<Task> getAllTask() {
		return taskDAO.getAll();
	}

}
