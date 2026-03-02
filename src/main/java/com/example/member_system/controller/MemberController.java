// API 엔드포인트 설정 파일
package com.example.member_system.controller;

import com.example.member_system.config.JwtTokenProvider;
import com.example.member_system.dto.LoginResponse;
import com.example.member_system.dto.MemberJoinRequest;
import com.example.member_system.dto.MemberLoginRequest;
import com.example.member_system.entity.Member;
import com.example.member_system.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*") // 모든 도메인 접속 하용
@RestController // 결과를 JSON 형태로 반환하는 컨트롤러
@RequestMapping("/api/members") // 이 컨트롤러의 모든 주소는 /api/members로 시작
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/join") // 주소: POST http://localhost:8080/api/members/join
    public String join(@RequestBody MemberJoinRequest request) {
        memberService.join(request);
        return "회원가입 성공!";
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody MemberLoginRequest request) {
        // 2. 로그인 수행 및 토큰 생성
        String token = memberService.login(request);

        // 3. 이메일로 회원 정보를 찾아 이름을 가져옵니다.
        // (서비스에 findByEmail 메서드가 구현되어 있어야 합니다)
        Member member = memberService.findByEmail(request.getEmail());

        // 4. JSON 객체 형태로 응답 ({ "token": "...", "name": "..." })
        return ResponseEntity.ok(new LoginResponse(token, member.getName()));
    }

    @GetMapping("/me")
    public String getMyInfo(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            if (jwtTokenProvider.validateToken(jwtToken)) {
                String email = jwtTokenProvider.getEmail(jwtToken);
                return "현재 로그인한 사용자: " + email;
            }
        }
        return "로그인이 필요합니다.";
    }
}