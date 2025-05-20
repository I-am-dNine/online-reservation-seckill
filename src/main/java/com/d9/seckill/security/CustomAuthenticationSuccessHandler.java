package com.d9.seckill.security;

import com.d9.seckill.entity.User;
import com.d9.seckill.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            // This should not happen if Spring Security successfully authenticated the user,
            // but as a fallback
            throw new UsernameNotFoundException("User " + username + " not found after authentication");
        }
        User user = userOptional.get();

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(user);

        // Store token and username in session
        HttpSession session = request.getSession();
        session.setAttribute("jwt", token);
        session.setAttribute("username", username);

        // Determine redirect URL based on role
        String redirectUrl = "/events"; // Default redirect URL
        Optional<? extends GrantedAuthority> adminAuthority = authentication.getAuthorities().stream()
                .filter(a -> a.getAuthority().equals("ROLE_ADMIN"))
                .findFirst();

        if (adminAuthority.isPresent()) {
            redirectUrl = "/admin/reservations";
        } else {
             Optional<? extends GrantedAuthority> userAuthority = authentication.getAuthorities().stream()
                .filter(a -> a.getAuthority().equals("ROLE_USER"))
                .findFirst();
              if (userAuthority.isPresent()) {
                 redirectUrl = "/events";
              }
        }

        // Redirect
        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
} 