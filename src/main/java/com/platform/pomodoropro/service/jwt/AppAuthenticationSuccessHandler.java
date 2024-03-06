package com.platform.pomodoropro.service.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.pomodoropro.entity.model.ModelMapper;
import com.platform.pomodoropro.entity.model.ResponseModel;
import com.platform.pomodoropro.entity.model.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class AppAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {

        String jwt = tokenProvider.createToken(authentication);
        AppUserDetails appUserDetails = (AppUserDetails) authentication.getPrincipal();

        UserMapper userMapper = ModelMapper.INSTANCE.mapUserEntityToUserMapper(appUserDetails.getUser());
        userMapper.setCreatedDate(appUserDetails.getUser().getCreatedDate());
        userMapper.setUpdatedDate(appUserDetails.getUser().getUpdatedDate());

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
        clearAuthenticationAttributes(httpServletRequest);

        ResponseModel responseModel = new ResponseModel(userMapper);
        responseModel.setMessage("Login is Successful");
        objectMapper.writeValue(httpServletResponse.getOutputStream(), responseModel);
    }


}
