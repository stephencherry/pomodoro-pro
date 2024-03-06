package com.platform.pomodoropro.security.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JWTConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final TokenProvider tokenProvider;

    public JWTConfigurer(TokenProvider tokenProvider){
        this.tokenProvider = tokenProvider;
    }

    /**
     * Token comes before the UsernamePasswordAuthenticationFilter authenticationFilter
     */
    @Override
    public void configure(HttpSecurity builder) {
        JWTFilter jwtFilter = new JWTFilter(tokenProvider);
        builder.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
