package com.platform.pomodoropro.security.jwt;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.platform.pomodoropro.service.AppUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AppUserDetailsService appUserDetailsService;
    private final AppAuthenticationFilter appAuthenticationFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(appUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public TokenProvider tokenProvider() {
        return new TokenProvider(objectMapper());
    }


    @Bean
    public AppAuthenticationFailureHandler appAuthenticationFailureHandler(){
        return new AppAuthenticationFailureHandler(objectMapper());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AppAuthenticationSuccessHandler successHandler() {
        return new AppAuthenticationSuccessHandler(objectMapper(), tokenProvider());
    }

    @Bean
    public AppAuthenticationFilter appAuthenticationFilter() throws Exception {
        AppAuthenticationFilter filter = new AppAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationFailureHandler(appAuthenticationFailureHandler());
        filter.setAuthenticationSuccessHandler(successHandler());
        filter.setPostOnly(true);
        filter.setFilterProcessesUrl("/**/signin");
        return filter;
    }

    private JWTConfigurer securityConfigAdapter() {
        return new JWTConfigurer(tokenProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilterBefore(new CORSFilter(), AbstractPreAuthenticatedProcessingFilter.class)
///             .addFilterBefore(new WhitelistFilter(), SecurityContextHolderAwareRequestFilter.class)
                .addFilterAt(appAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/signin", "/signup", "/signup/verify", "/admin/signup", "/skills").permitAll() // Whitelisted endpoints
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .apply(securityConfigAdapter());
    }
}