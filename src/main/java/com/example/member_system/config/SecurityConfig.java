// 보안 설정 및 DB 연동
package com.example.member_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // 1. CSRF 보호 해제
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/members/**").permitAll() // 2. 회원가입 관련 주소는 모두 허용
                        .anyRequest().authenticated() // 3. 그 외의 요청만 인증 필요
                )
                .formLogin(AbstractHttpConfigurer::disable); // 4. 기본 로그인 폼 비활성화

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}