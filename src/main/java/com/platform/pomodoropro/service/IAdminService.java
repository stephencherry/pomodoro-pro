package com.platform.pomodoropro.service;

import com.platform.pomodoropro.entity.model.ResponseModel;
import com.platform.pomodoropro.entity.model.request.RegistrationRequestBody;

public interface IAdminService {
    ResponseModel getUsers(int page);
    String addUser(RegistrationRequestBody registrationRequestBody);
}
