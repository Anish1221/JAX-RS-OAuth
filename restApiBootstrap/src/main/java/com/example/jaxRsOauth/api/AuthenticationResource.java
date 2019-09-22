package com.example.jaxRsOauth.api;

import java.sql.SQLException;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.example.jaxRsOauth.auth.JWT;
import com.example.jaxRsOauth.dao.UserDAO;
import com.example.jaxRsOauth.dao.impl.UserDAOImpl;
import com.example.jaxRsOauth.entity.AuthenticateUserModel;
import com.example.jaxRsOauth.entity.User;

@Path("authenticate")
public class AuthenticationResource {

	private UserDAO userDAO = new UserDAOImpl();
	private JWT jwt = new JWT();

	@PermitAll
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getToken(AuthenticateUserModel userCredentials) {
		User user = null;
		System.out.println(userCredentials.toString());
		try {
			user = userDAO.findByUsernamePassword(userCredentials.username, userCredentials.password);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (user != null) {
			String accessToken = jwt.generate(user.getRole());
			return Response.ok(accessToken).build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

	}

}
