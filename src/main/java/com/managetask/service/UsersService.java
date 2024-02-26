package com.managetask.service;

import com.managetask.exception.LoginFailedException;
import com.managetask.exception.UserDetailsNotFoundException;
import com.managetask.model.JwtResponse;
import com.managetask.model.Users;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public interface UsersService {

    List<Users> getUsers(String input, String roleId);
    JwtResponse createAuthenticationToken(String loginId, String password) throws UserDetailsNotFoundException, LoginFailedException;

    Users insertUser(String userName, String password);

    CompletableFuture<List<Users>> getUserByIdAsync();

}
