package org.notnull.member.login;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.notnull.member.login.domain.item.Item;
import org.notnull.member.login.domain.item.ItemRepository;
import org.notnull.member.login.domain.member.Member;
import org.notnull.member.login.domain.member.MemberRepository;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        // 테이블 초기화
        memberRepository.initTable();

        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));

        Member member = new Member();
        member.setLoginId("test");
        member.setPassword("test!");
        member.setName("테스터");

        memberRepository.save(member);

    }

}