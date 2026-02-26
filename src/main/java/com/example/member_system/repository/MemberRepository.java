// DB 접근 로직
package com.example.member_system.repository;

import com.example.member_system.entity.Member; // 실제 Member 클래스가 있는 경로로 수정
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}