package com.managetask.service;

import java.util.ArrayList;
import java.util.List;

import com.managetask.model.Users;
import com.managetask.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class JwtUserDetailsService implements UserDetailsService {
	@Autowired
	private UsersRepository usersRepository;

	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

		List<Users> userData = getUsers(loginId);
		Users user = new Users();
		for (Users usr : userData) {
			user = usr;
		}
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + loginId);
		}

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				new ArrayList<>());
	}

	@Cacheable(cacheNames = "users")
	private List<Users> getUsers(String loginId) {
//		List<Users> userData = usersRepository.findAll();
		List<Users> userData = usersRepository.findByUsername(loginId);
		return userData;
	}


}