package com.platform.pomodoropro.controller;

import com.platform.pomodoropro.entity.model.ResponseModel;
import com.platform.pomodoropro.entity.model.request.RegistrationRequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface IAdminController {
    @PostMapping("user")
    ResponseEntity<ResponseModel> addUser(@RequestBody RegistrationRequestBody registrationRequestBody);

    @GetMapping("users")
    ResponseEntity<ResponseModel> getAllUsers();

    @GetMapping("users/{page}")
    ResponseEntity<ResponseModel> getAllUsersOnPage(@PathVariable Integer page);

    @PostMapping("signup")
    ResponseEntity<ResponseModel> signup(@RequestBody RegistrationRequestBody registrationRequestBody);
}
