package com.platform.pomodoropro.controller;

import com.platform.pomodoropro.entity.model.ResponseModel;
import com.platform.pomodoropro.entity.model.request.RegistrationRequestBody;
import com.platform.pomodoropro.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/")
public class UserController implements IUserController{
    private final UserService userService;

    @Override
    public ResponseEntity<ResponseModel> signup(RegistrationRequestBody registrationRequestBody) {
        ResponseModel responseModel = new ResponseModel(null);
        responseModel.setMessage(userService.signUp(registrationRequestBody));
        responseModel.setResponseCode("00");

        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> verify(Integer code, String email) {
        ResponseModel responseModel = new ResponseModel(null);
        responseModel.setMessage(userService.verifySignUp(code, email));
        responseModel.setResponseCode("00");

        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseModel> getUser(long userId) {
        ResponseModel responseModel = new ResponseModel(userService.getUser(userId));
        responseModel.setMessage("Success");
        responseModel.setResponseCode("00");

        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }
}