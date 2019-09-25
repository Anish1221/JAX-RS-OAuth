package com.example.jaxRsOauth.auth;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWT {
	
	/*
	 * Generate a token based on the user roles after the username and password matches 
	 */
	public String generate(String roles){
		String token = "";
		
		//variables required for access token
		String subject = "appuser";
		String secret = getSecret();
		String id = getId();
		String issuer = getIssuer();
		long ttlMillis = getTimeToLive();
		//set the signature algorithm
		
		//The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm =SignatureAlgorithm.HS256;
		
		//get the current time in milliseconds for setting the expiry time later
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		
		// We will sign our JWT with our ApiKey secret
		//generate a byte array or file data from our secret
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
		//create a key using the file data and the algorithm
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		
		/*
		 * build the JWT using the id(app id), issued time, subject or name
		 * insert the claims - there can be multiple claims e.g. roles a user has claims to
		 * set the issuer information
		 * Sign the token with the key generated earlier = 
		 * this ensures that the token cannot be altered and the receiving
		 * server can prove that the token originated from the server
		 */
		
		JwtBuilder builder = Jwts.builder().setId(id)
									.setIssuedAt(now)
									.setSubject(subject)
									.claim("roles", String.join(",", roles))
									.setIssuer(issuer)
									.signWith(signatureAlgorithm, signingKey);
		
		//if it has been specified, let's add the expiration
		if(ttlMillis > 0){
			long expMillis = nowMillis + ttlMillis;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}
		
		token = builder.compact(); // this encodes the data into the final access token
		
		//create the token here
		return token;
	}
	
	

	public Boolean isValid(String accessToken, Set<String> rolesSet){
		Boolean result = false;
		//check access token validity against roleSet of the method
		try{
			System.out.println("checking validity");
			//This line will throw an exception if it is not a signed JWS (as expected)
			// JWT will parse the access token using our secret to get the list of claims to amtch the roles set
			Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(getSecret()))
					.parseClaimsJws(accessToken).getBody();
			
			//extract roles claim from claims
			String roles = claims.get("roles", String.class);
			//to check similarities between 2 sets, we use the intersect to find out of at least 1 similarity exists between 2 object
			Set<String> claimsRolesSet = new HashSet<>(Arrays.asList(roles.split(",")));
			rolesSet.retainAll(claimsRolesSet);
					
			System.out.println(claims.getId()+" "+claims.getId());
			System.out.println(claims.getIssuer()+" "+claims.getIssuer());
			System.out.println(rolesSet.size()>0);
			if(claims.getId().equals(getId()) && claims.getIssuer().equals(getIssuer()) && rolesSet.size()>0){
				result = true;
			}
		}catch(Exception ex){
			//token validation failed
			ex.printStackTrace();
		}
		
		return result;
	}
	
	//Issuer is the name of the token issuer
	private String getIssuer(){
		return "issuer1";
	}
	
	//Id for the token 
	private String getId(){
		return "jaxRsOauth";
	}
	
	private String getSecret(){
		return "mysecretphrase";
	}
	
	/*
	 * Set the time to live - or token lifetime in milliseconds, 1000ms = 1s
	 */
	private long getTimeToLive() {
		return 3600000; // 1 hour = 60 mins x 60 secs x 1000 = 3600000
	}
}
