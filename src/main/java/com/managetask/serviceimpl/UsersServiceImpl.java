package com.managetask.serviceimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.managetask.exception.LoginFailedException;
import com.managetask.exception.UserDetailsNotFoundException;
import com.managetask.model.JwtResponse;
import com.managetask.model.Users;
import com.managetask.repository.UsersRepository;
import com.managetask.security.JwtTokenUtil;
import com.managetask.service.JwtUserDetailsService;
import com.managetask.service.UsersService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@CacheConfig(cacheNames = "usersCache")
public class UsersServiceImpl implements UsersService {
    private static final Logger logger = LogManager.getLogger(UsersServiceImpl.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * get all users info from database
     */
    @Cacheable("usersCache")
    public List<Users> getUsers(String input, String role) {
        logger.info("....getUsers method started.....");
        Boolean status = true ;
        String query = "SELECT id,username FROM USERS WHERE  1=1 ";
        if (!input.equals("")) {

            query = query + "AND ( username LIKE  " + "'%" + input + "%')";
        }
            query = query + " AND ENABLED =" + status;

        List<Users> usersList = null;

        usersList = jdbcTemplate.query(query, new UsersRowMapper());
        logger.info("....getUsers method ended.....");
        return usersList;
    }

    @Override
    // Simulate fetching user data asynchronously
    public CompletableFuture<List<Users>> getUserByIdAsync() {
        return CompletableFuture.supplyAsync(() -> {
            // Simulate fetching user data from the database
            logger.info("Fetching user data asynchronously...");
            return usersRepository.findAll();
        }, Executors.newCachedThreadPool());
//        future.exceptionally(ex -> "Handled error: " + ex.getMessage());


    }

    @Override
    public JwtResponse createAuthenticationToken(String loginId, String password) throws UserDetailsNotFoundException, LoginFailedException {
        logger.info("....createAuthenticationToken method started.....");

        try {
            authenticate(loginId, password);

            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginId);

            String token = jwtTokenUtil.generateToken(userDetails);

            return new JwtResponse(token);
        } catch (AuthenticationException e) {
            // Handle authentication exception
            // For example, you can log the exception
            logger.error("Authentication failed: " + e.getMessage());
            throw new LoginFailedException("Authentication failed");
        } catch (Exception e) {
            // Handle any other general exceptions
            // For example, you can log the exception
            logger.error("An unexpected error occurred: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    @CacheEvict(cacheNames = "users", allEntries = true)
    @Transactional
    public Users insertUser(String userName, String password) {
        try {
            Users user = new Users();
            user.setUsername(userName);
            user.setPassword(bcryptEncoder.encode(password));
            user.setEnabled(true);

            // Save to the database
            usersRepository.save(user);

            // Log the successful save
            logger.info("User saved successfully: {}", user);

            return user;
        } catch (Exception e) {
            // Log the exception and rethrow
            logger.error("Error saving user: {}", e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred");
        }
    }


    public Authentication authenticate(String username, String password) {

            // Create an authentication token with the provided username and password
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

            // Authenticate the user
            Authentication result = authenticationManager.authenticate(authentication);

            // If authentication is successful, return the authenticated user
            return result;

    }
    private static final class UsersRowMapper implements RowMapper<Users> {
        public Users mapRow(ResultSet rs, int rowNum) throws SQLException {
            logger.info("....mapRow method started.....");
            Users users = new Users();
            users.setUsername(rs.getString("username") );
            users.setId(rs.getLong("id"));
            logger.info("....mapRow method ended.....");
            return users;
        }
    }

}