package com.example.member_system;

import com.example.member_system.dto.MemberJoinRequest;
import com.example.member_system.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // 테스트 후 DB를 자동으로 롤백해줍니다.
class MemberServiceTest {

	@Autowired
	MemberService memberService;

	@Test
	@DisplayName("회원가입이 정상적으로 성공해야 한다")
	void join_success() {
		// given (준비)
		MemberJoinRequest request = new MemberJoinRequest();
		request.setEmail("test_user@kangnam.ac.kr");
		request.setPassword("password123!");
		request.setName("테스트유저");

		// when (실행)
		Long savedId = memberService.join(request);

		// then (검증)
		assertThat(savedId).isNotNull();
	}
}