package com.platform.pomodoropro.service;

import com.platform.pomodoropro.entity.model.UserMapper;
import com.platform.pomodoropro.entity.model.request.RegistrationRequestBody;

public interface IUserService {
    String signUp(RegistrationRequestBody registrationRequestBody);
    String verifySignUp(int code, String email);
    UserMapper getUser(long userId);
}
