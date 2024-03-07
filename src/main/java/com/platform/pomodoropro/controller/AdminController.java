package com.platform.pomodoropro.controller;

import com.platform.pomodoropro.entity.model.ResponseModel;
import com.platform.pomodoropro.entity.model.request.RegistrationRequestBody;
import com.platform.pomodoropro.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController implements IAdminController{
    private final AdminService adminService;

    @Override
    public ResponseEntity<ResponseModel> addUser(RegistrationRequestBody registrationRequestBody) {
        ResponseModel responseModel = new ResponseModel(null);
        responseModel.setMessage(adminService.addUser(registrationRequestBody));
        responseModel.setResponseCode("00");
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getAllUsers() {
        return getAllUsersOnPage(0);
    }

    @Override
    public ResponseEntity<ResponseModel> getAllUsersOnPage(Integer page) {
        return new ResponseEntity<>(adminService.getUsers(page), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> signup(RegistrationRequestBody registrationRequestBody) {
        ResponseModel responseModel = new ResponseModel(null);
        responseModel.setMessage(adminService.signUp(registrationRequestBody));
        responseModel.setResponseCode("00");
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }
}
