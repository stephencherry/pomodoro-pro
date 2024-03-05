package com.platform.pomodoropro.service.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@RequiredArgsConstructor
public class AppAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String jwt = tokenProvider.createToken(authentication);
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

        UserMapper userMapper = ModelMapper.INSTANCE.mapUserEntityToUserMapper(userDetails.getUser());
        userMapper.setAddress(ModelMapper.INSTANCE.mapAddressEntityToAddressMapper(userDetails.getUser().getAddress()));
        userMapper.setCurrentAddressMapper(ModelMapper.INSTANCE.mapCurrentAddressEntityToCurrentAddressMapper(userDetails.getUser().getCurrentAddress()));
        userMapper.setCreatedDate(userDetails.getUser().getCreatedDate());
        userMapper.setUpdatedDate(userDetails.getUser().getUpdatedDate());

        if(userDetails.getUser().getBusiness()!=null){
            BusinessMapper businessMapper = ModelMapper.INSTANCE.mapBusinessEntityToBusinessMapper(userDetails.getUser().getBusiness());
            if(userDetails.getUser().getBusiness().getBusinessAddress() != null){
                businessMapper.setBusinessAddress(ModelMapper.INSTANCE.mapBusinessAddressEntityToBusinessAddressMapper(userDetails.getUser().getBusiness().getBusinessAddress()));
            }

            if(userDetails.getUser().getBusiness().getSkillEntity() != null) {
                Set<SkillMapper> skillMappers = ModelMapper.INSTANCE.mapSkillEntitiesToSkillMappers(userDetails.getUser().getBusiness().getSkillEntity());
                userDetails.getUser().getBusiness().getSkillEntity().forEach(skillEntity -> skillMappers.add(objectMapper.convertValue(skillEntity, SkillMapper.class)));
                businessMapper.setSkillMappers(skillMappers);
            }
            userMapper.setBusiness(businessMapper);
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
        clearAuthenticationAttributes(request);

        ResponseModel responseModel = new ResponseModel(userMapper);
        responseModel.setMessage("Login is Successful");
        objectMapper.writeValue(response.getOutputStream(), responseModel);
    }


}
