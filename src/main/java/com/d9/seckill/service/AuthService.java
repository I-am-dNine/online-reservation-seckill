package com.d9.seckill.service;

import com.d9.seckill.dto.AuthRequest;
import com.d9.seckill.entity.User;
import com.d9.seckill.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public String login(AuthRequest request) {
        try {
            logger.debug("Authenticating user: {}", request.getUsername());
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

            String token = jwtService.generateToken(user);
            logger.debug("Generated JWT token for user: {}", request.getUsername());
            return token;
        } catch (AuthenticationException e) {
            logger.error("Authentication failed for user: " + request.getUsername(), e);
            throw new RuntimeException("用户名或密码错误");
        } catch (Exception e) {
            logger.error("Login error for user: " + request.getUsername(), e);
            throw new RuntimeException("登录失败: " + e.getMessage());
        }
    }
}