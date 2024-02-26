package com.managetask.exception;

import com.managetask.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.managetask.exception.LoginFailedException;
@ControllerAdvice
public class GlobalExceptionHandler {


        @ExceptionHandler(LoginFailedException.class)
        public ResponseEntity<Response> handleLoginFailedException(LoginFailedException ex) {
            Response response = new Response();
            response.setResponse("Login failed: " + ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);


        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Response> handleException(Exception ex) {
            Response response = new Response();
            response.setResponse(ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    @ExceptionHandler(UserDetailsNotFoundException.class)
    public ResponseEntity<String> handleUserDetailsNotFoundException(UserDetailsNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}
