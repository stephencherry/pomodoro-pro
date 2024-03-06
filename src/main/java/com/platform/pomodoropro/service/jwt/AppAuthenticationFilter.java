package com.platform.pomodoropro.service.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AppAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException {

        LoginRequest loginRequest;
        try {
            loginRequest = objectMapper.readValue(httpServletRequest.getInputStream(), LoginRequest.class);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Authentication request failed: " + e.getMessage(), e);
        }

        String email = loginRequest.getEmail();
        String credential = loginRequest.getPassword();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, credential);
        authenticationToken.setDetails(new WebAuthenticationDetails(httpServletRequest));
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    @Getter
    public static class LoginRequest {
        private String email;
        private String password;
    }

}
