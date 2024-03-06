package com.platform.pomodoropro.entity.model;
import lombok.*;

@Getter
@Setter
public class UserMapper extends BaseEntity{
    public long id;
    public String email;
    public String username;
    public String firstname;
    public String lastname;
    public String gender;
    public String role;
    public String status;
}
