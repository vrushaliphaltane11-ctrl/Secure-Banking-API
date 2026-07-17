package com.vrushali.securebankingapi.dto;

import com.vrushali.securebankingapi.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtAuthResponse {

    private String token;
    private String tokenType;
    private Long userId;
    private String email;
    private Role role;
    private String message;
}