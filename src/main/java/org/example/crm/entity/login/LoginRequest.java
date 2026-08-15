package org.example.crm.entity.login;

import lombok.Data;

@Data
public class LoginRequest {
    private String phone;
    private String password;
}
