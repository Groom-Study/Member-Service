// DB 연동
package com.example.member_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity // 이 클래스는 DB 테이블과 1:1로 매핑
@Getter
@NoArgsConstructor // 파라미터가 없는 기본 생성자를 자동으로 생성
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL의 Auto Increment 사용
    private Long id;

    @Column(nullable = false, unique = true) // 중복 가입 방지를 위해 unique 설정
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    // 데이터를 담을 때 쓸 생성자
    public Member(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}