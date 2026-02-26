// 로그인 이메일 비밀번호 Request
package com.example.member_system.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberLoginRequest {
    private String email;
    private String password;
}