package com.d9.seckill.controller;

import com.d9.seckill.dto.AuthRequest;
import com.d9.seckill.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session,
                          Model model) {
        try {
            String token = authService.login(new AuthRequest(username, password));
            session.setAttribute("jwt", token); // 暂存 JWT
            session.setAttribute("username", username); // 暂存用户名
            return "redirect:/events";
        } catch (Exception e) {
            model.addAttribute("error", "登录失败：" + e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}