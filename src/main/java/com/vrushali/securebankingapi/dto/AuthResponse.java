package com.vrushali.securebankingapi.dto;

import com.vrushali.securebankingapi.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private Long userId;
    private String fullName;
    private String email;
    private Role role;
    private String message;
}