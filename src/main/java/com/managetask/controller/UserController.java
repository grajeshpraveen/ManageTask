package com.managetask.controller;

import com.managetask.dto.Response;
import com.managetask.exception.LoginFailedException;
import com.managetask.model.JwtResponse;
import com.managetask.model.Users;
import com.managetask.service.UsersService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@RestController
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    UsersService usersService;

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam String loginId, @RequestParam String password) throws LoginFailedException, Exception {
        logger.info("login method started");
        String response = null;
        Response respons = new Response();
        JwtResponse result = null;
        try {
            result = usersService.createAuthenticationToken(loginId, password);
            if (result != null) {
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                response = "Invalid password";
                throw new LoginFailedException(response); // Throwing specific exception for login failure
            }
        } catch (LoginFailedException e) {
            // Handle the LoginFailedException if needed
            throw e; // rethrow the exception if no additional handling is required
        } catch (Exception e) {
            // Handle any other exceptions if needed
            throw e; // rethrow the exception if no additional handling is required
        }
    }


    @GetMapping("/insertuser")
    public ResponseEntity<?> addUser(@RequestParam String insertUser, @RequestParam String password) {
        String response = null;
        Response respons = new Response();
        Users result = null;
        try {
            result = usersService.insertUser(insertUser, password);
        } catch (InternalAuthenticationServiceException e) {
            response = "Invalid username";
            respons.setResponse(response);
            return new ResponseEntity<>(respons, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response = "Invalid password";
            respons.setResponse(response);
            return new ResponseEntity<>(respons, HttpStatus.BAD_REQUEST);
        }
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(respons, HttpStatus.CREATED);
        }
    }

    @GetMapping("/getuser")
    public ResponseEntity<?> getUser() {
        String response = null;
        Response respons = new Response();
       List<Users> result = null;
        try {
            CompletableFuture<List<Users>> results = usersService.getUserByIdAsync();

            result = results.get();
            Executors.newCachedThreadPool().shutdown();

        } catch (Exception e) {
            response = "Invalid username";
            respons.setResponse(response);
            return new ResponseEntity<>(respons, HttpStatus.BAD_REQUEST);
        }
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(respons, HttpStatus.OK);
        }
    }

}
