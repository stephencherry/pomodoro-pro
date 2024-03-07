package com.platform.pomodoropro.controller;

import com.platform.pomodoropro.entity.model.ResponseModel;
import com.platform.pomodoropro.entity.model.request.RegistrationRequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface IUserController {
    @PostMapping("signup")
    ResponseEntity<ResponseModel> signup(@RequestBody RegistrationRequestBody registrationRequestBody);

    @GetMapping("signup/verify")
    ResponseEntity<ResponseModel> verify(@RequestParam(name = "code") Integer code, @RequestParam(name = "email") String email);

    @GetMapping("user")
    ResponseEntity<ResponseModel> getUser(@RequestParam(name = "userId") long userId);
}