package com.d9.seckill.service;

import com.d9.seckill.dto.AuthRequest;
import com.d9.seckill.dto.AuthResponse;
import com.d9.seckill.entity.User;
import com.d9.seckill.repository.UserRepository;
import com.d9.seckill.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("用户注册成功")
    void testRegisterSuccess() {
        AuthRequest request = new AuthRequest("testuser", "password123");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        String result = userService.register(request);

        assertEquals("注册成功", result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("用户名重复注册失败")
    void testRegisterFail_UserExists() {
        AuthRequest request = new AuthRequest("existing", "pass");
        when(userRepository.existsByUsername("existing")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.register(request));
        assertEquals("用户名已存在", ex.getMessage());
    }

    @Test
    @DisplayName("用户登录成功")
    void testLoginSuccess() {
        AuthRequest request = new AuthRequest("testuser", "password123");
        User user = User.builder().username("testuser").password("hashed").role("USER").build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "hashed")).thenReturn(true);
        when(jwtTokenProvider.generateToken(user)).thenReturn("mocked-jwt-token");

        AuthResponse response = userService.login(request);

        assertEquals("mocked-jwt-token", response.getToken());
    }

    @Test
    @DisplayName("用户不存在登录失败")
    void testLoginFail_UserNotFound() {
        AuthRequest request = new AuthRequest("missing", "pass");
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.login(request));
        assertEquals("用户不存在", ex.getMessage());
    }

    @Test
    @DisplayName("密码错误登录失败")
    void testLoginFail_BadPassword() {
        AuthRequest request = new AuthRequest("testuser", "wrongpass");
        User user = User.builder().username("testuser").password("correctHash").role("USER").build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpass", "correctHash")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.login(request));
        assertEquals("密码错误", ex.getMessage());
    }

    @Test
    @DisplayName("管理员创建成功")
    void testCreateAdminSuccess() {
        AuthRequest request = new AuthRequest("admin", "securepass");

        when(userRepository.existsByUsername("admin")).thenReturn(false);
        when(passwordEncoder.encode("securepass")).thenReturn("encodedAdminPass");

        String result = userService.createAdmin(request);

        assertEquals("管理员创建成功", result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("管理员用户名已存在")
    void testCreateAdminFail_UserExists() {
        AuthRequest request = new AuthRequest("admin", "pass");
        when(userRepository.existsByUsername("admin")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.createAdmin(request));
        assertEquals("用户名已存在", ex.getMessage());
    }
}
