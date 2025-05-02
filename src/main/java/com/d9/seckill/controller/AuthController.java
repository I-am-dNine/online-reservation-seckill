package com.d9.seckill.controller;

import com.d9.seckill.dto.AuthRequest;
import com.d9.seckill.dto.AuthResponse;
import com.d9.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody AuthRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return userService.login(request);
    }

    // TODO ：仅开发期可用
    @PostMapping("/init-admin")
    public String createAdmin(@RequestBody AuthRequest request) {
        return userService.createAdmin(request);
    }
    
}
