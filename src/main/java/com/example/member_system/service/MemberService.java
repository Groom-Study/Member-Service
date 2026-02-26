package com.example.member_system.service;

import com.example.member_system.config.JwtTokenProvider;
import com.example.member_system.dto.MemberLoginRequest;
import com.example.member_system.entity.Member;
import com.example.member_system.dto.MemberJoinRequest;
import com.example.member_system.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider; //(의존성 주입)

    public Long join(MemberJoinRequest request) {
        memberRepository.findByEmail(request.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 가입된 이메일입니다.");
                });

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member member = new Member(
                request.getEmail(),
                encodedPassword,
                request.getName()
        );

        return memberRepository.save(member).getId();
    }

    @Transactional(readOnly = true)
    public String login(MemberLoginRequest request) { // 반환 타입을 기존 Long에서 String으로 변경함
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 잘못되었습니다.");
        }

        // 로그인 성공 시 이메일을 기반으로 JWT 토큰을 생성하여 반환합니다.
        return jwtTokenProvider.createToken(member.getEmail());
    }
}