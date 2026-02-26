// 회원가입 이메일, 비밀번호, 이름 Request
package com.example.member_system.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberJoinRequest {
    private String email;
    private String password;
    private String name;
}