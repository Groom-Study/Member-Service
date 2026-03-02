package com.example.member_system;

import com.example.member_system.dto.MemberJoinRequest;
import com.example.member_system.entity.Member;
import com.example.member_system.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberServiceTest {

	@Autowired
	MemberService memberService;

	@Test
	@DisplayName("회원가입이 정상적으로 성공하고 데이터가 일치해야 한다")
	void join_success() {
		// given: 회원 정보 준비
		MemberJoinRequest request = new MemberJoinRequest();
		request.setEmail("test_user@kangnam.ac.kr");
		request.setPassword("password123!");
		request.setName("테스트유저");

		// when: 가입 실행
		Long savedId = memberService.join(request);

		// then: DB에서 다시 조회하여 이름이 일치하는지 검증
		Member foundMember = memberService.findByEmail("test_user@kangnam.ac.kr");

		assertThat(savedId).isNotNull();
		assertThat(foundMember.getName()).isEqualTo("테스트유저");
		assertThat(foundMember.getEmail()).isEqualTo("test_user@kangnam.ac.kr");
	}

	@Test
	@DisplayName("중복된 이메일로 가입하면 예외가 발생해야 한다")
	void join_duplicate_email() {
		// given: 동일한 정보로 두 번 가입 시도 준비
		MemberJoinRequest request = new MemberJoinRequest();
		request.setEmail("duplicate@kangnam.ac.kr");
		request.setName("유저1");
		request.setPassword("pwd1");
		memberService.join(request);

		// when & then: 예외 발생 검증
		assertThatThrownBy(() -> memberService.join(request))
				.isInstanceOf(IllegalStateException.class)
				.hasMessage("이미 가입된 이메일입니다.");
	}
}