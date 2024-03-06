package com.platform.pomodoropro.service;

import com.platform.pomodoropro.entity.model.ResponseModel;
import com.platform.pomodoropro.entity.model.request.RegistrationRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService{
    @Override
    public ResponseModel getUsers(int page) {
        return null;
    }

    @Override
    public String addUser(RegistrationRequestBody registrationRequestBody) {
        return null;
    }
}
