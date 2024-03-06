package com.platform.pomodoropro.entity.model.request;

import lombok.Data;

@Data
public class RegistrationRequestBody {
    public String username;
    public String firstname;
    public String lastname;
    public String phone;
    public String email;
    public String password;
    public String role;
    public String gender;
    public String status;
}
