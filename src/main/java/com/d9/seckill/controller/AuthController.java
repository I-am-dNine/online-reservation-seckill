package com.d9.seckill.controller;

import com.d9.seckill.dto.AuthRequest;
import com.d9.seckill.dto.AuthResponse;
import com.d9.seckill.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证管理", description = "用户注册、登录和管理员初始化接口")
public class AuthController {
    
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "注册新用户", description = "创建新的普通用户账号")
    public String register(@RequestBody AuthRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录并获取JWT令牌")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return userService.login(request);
    }

    // TODO ：仅开发期可用
    @PostMapping("/init-admin")
    @Operation(summary = "初始化管理员", description = "仅开发环境可用，创建管理员账号")
    public String createAdmin(@RequestBody AuthRequest request) {
        return userService.createAdmin(request);
    }
    
}
