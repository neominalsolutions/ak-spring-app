package com.akbank.spring_app.request.auth;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;
    private String password;

}
