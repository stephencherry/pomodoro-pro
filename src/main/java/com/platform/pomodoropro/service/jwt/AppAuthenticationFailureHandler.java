package com.platform.pomodoropro.service.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.pomodoropro.entity.model.ResponseModel;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AppAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper  objectMapper;

    static final String ERROR_MESSAGE = "User could not be Authenticated, please try again or contact administrator";
    static final String BAD_CREDENTIALS = "Username or password is incorrect";
    static final String ACTIVATE_ACCOUNT = "Please activate your profile by clicking on the link sent to your email";

    public AppAuthenticationFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException authenticationException) throws IOException, ServletException {
        String message;
        if(authenticationException instanceof BadCredentialsException){
            message = BAD_CREDENTIALS;
        } else if (authenticationException instanceof DisabledException) {
            message = ACTIVATE_ACCOUNT;
        }else {
            message = ERROR_MESSAGE;
        }

        ResponseModel errorDTO = new ResponseModel(null);
        errorDTO.setMessage(message);
        objectMapper.writeValue(httpServletResponse.getOutputStream(), errorDTO);
    }
}
