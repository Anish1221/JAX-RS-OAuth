package com.example.jaxRsOauth.auth;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;


//@Provider
public class BasicAuthenticationFilter implements ContainerRequestFilter {

	@Context
	private ResourceInfo resourceInfo;

	// the authorization property here allows us to find the appropriate request
	// header to get the authentication request user name and password
	private static final String AUTHORIZATION_PROPERTY = "Authorization";
	// the scheme here is a standard well known for identifying if this is using
	// Basic Authentication
	private static final String AUTHENTICATION_SCHEME = "Basic";

	@Override
	public void filter(ContainerRequestContext requestContext) {
		// get the information of the method being invoked from the web service
		Method method = resourceInfo.getResourceMethod();

		// if the method invoked does not have @PermitAll security annotation
		// check for @DenyAll and @RolesAllowed annotation,
		// to cater for no annotations also equivalent to PermitAll
		if (!method.isAnnotationPresent(PermitAll.class)) {
			// if the method has @DenyAll security annotation - abort with a
			// status forbidden
			if (method.isAnnotationPresent(DenyAll.class)) {
				requestContext.abortWith(
						Response.status(Response.Status.FORBIDDEN).entity("Access is blocked for all").build());
				return;
			}

			// here we handle the authentication where we have to extract the
			// token from the headers

			// Get request headers to check for the authorization header key
			final MultivaluedMap<String, String> headers = requestContext.getHeaders();
			
			System.out.println("Headers: "+headers);

			// fetch authorization header
			final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);
			
			System.out.println("authorization: "+authorization);

			// if no authorization information present; block access
			if (authorization == null || authorization.isEmpty()) {
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
						.entity("You cannot access this resource1").build());
				return;
			}

			// get the access token from the header
			final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");

			//Decode username and password
			String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));
			
			//Split username and password tokens
			final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
			final String username = tokenizer.nextToken();
			final String password = tokenizer.nextToken();
			
			// if the method has roles, check against the claims in the JWT
			if (method.isAnnotationPresent(RolesAllowed.class)) {
				RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
				Set<String> rolesSet = new HashSet<>(Arrays.asList(rolesAnnotation.value()));
				
				boolean flag = false;
				//Is User valid?
				if(flag){
					requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
							.entity("You cannot access this resource").build());
					return;
				}
				
			}

		}

	}

}
