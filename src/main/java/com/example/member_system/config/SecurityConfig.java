package com.example.member_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // 1. CSRF 해제 (테스트 및 API 서버용)
                .formLogin(AbstractHttpConfigurer::disable) // 2. 기본 로그인 창 안 뜨게 설정
                .httpBasic(AbstractHttpConfigurer::disable) // 3. HTTP 기본 인증 비활성화
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) // 4. H2 Console 등 프레임 허용
                .authorizeHttpRequests(auth -> auth
                        // 5. 누구나 접근 가능한 경로 설정 (매우 중요!)
                        .requestMatchers("/", "/index.html", "/static/**", "/css/**", "/js/**", "/favicon.ico").permitAll()
                        .requestMatchers("/api/members/**").permitAll() // 회원가입, 로그인 API 허용
                        .requestMatchers("/h2-console/**").permitAll() // DB 확인용 콘솔 허용

                        // 6. 나머지는 로그인한 사용자만 (현재는 위에서 다 열어줘서 웬만하면 통과)
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}