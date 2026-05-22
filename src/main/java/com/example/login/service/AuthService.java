package com.example.login.service;

import com.example.login.config.JwtTokenProvider;
import com.example.login.dto.LoginRequest;
import com.example.login.dto.LoginResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        // 1) 이메일과 비밀번호로 인증 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        // 2) 실제로 인증 수행 (CustomUserDetailsService + BCryptPasswordEncoder 사용)
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 3) 인증 정보를 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 4) 인증 성공 시 JWT 토큰 발급
        String accessToken = jwtTokenProvider.generateToken(authentication);

        return new LoginResponse(accessToken, "Bearer");
    }
}
