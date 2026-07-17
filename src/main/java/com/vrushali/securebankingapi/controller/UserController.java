package com.vrushali.securebankingapi.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/profile")
    public Map<String, Object> getProfile(Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        response.put("email", authentication.getName());
        response.put("authorities", authentication.getAuthorities());
        response.put("message", "Protected endpoint accessed successfully");

        return response;
    }
}