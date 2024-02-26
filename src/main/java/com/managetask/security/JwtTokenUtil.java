package com.managetask.security;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.managetask.model.Users;
import com.managetask.serviceimpl.UsersServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtTokenUtil implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -2475257440476470762L;

	public static final long JWT_TOKEN_VALIDITY = 60 * 60;

	@Autowired
	UsersServiceImpl usersServiceImpl;

//
//
//	public static SecretKey generateSecureKey() {
//		// Generate a secure key for HS512
//		return Keys.secretKeyFor(SignatureAlgorithm.HS512);
//	}
//
//	SecretKey secret = generateSecureKey();

	String secretKeyString = "thisisthetestsecrettogeneratejwttokentoaceessapisthisisthetestsecrettogeneratejwttokentoaceessapis";

	// Generate a SecretKey from the string using HS512 algorithm
	SecretKey secret = Keys.hmacShaKeyFor(secretKeyString.getBytes());


	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Boolean ignoreTokenExpiration(String token) {
		// here you specify tokens, for that the expiration is ignored
		return false;
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("userData", findUserData(userDetails.getUsername(),""));
		return doGenerateToken(claims, userDetails.getUsername());
	}

	private Users findUserData(String userName,String role) {
		List<Users> userData = usersServiceImpl.getUsers(userName,role);
		Users user = new Users();
		Users userDto = new Users();
		for (Users usr : userData) {
			user = usr;
		}
		return user;

	}


	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();

	}

	public Boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
