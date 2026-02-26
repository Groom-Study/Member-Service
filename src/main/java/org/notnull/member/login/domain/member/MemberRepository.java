package org.notnull.member.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    private final JdbcTemplate template;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        template = jdbcTemplate;
    }

    public void initTable() {
        template.execute("drop table if exists member");
        template.execute("create table member(" +
                "id bigint auto_increment primary key, " +
                "login_id varchar(20) not null, " +
                "name varchar(20) not null, " +
                "password varchar(20) not null)");
    }

    public Member save(Member member) {
        String sql = "insert into member(login_id, name, password) values (?, ?, ?)";
        template.update(sql, member.getLoginId(), member.getName(), member.getPassword());
        
        // ID 조회를 위해 마지막 insert id를 가져오거나, findByLoginId 등을 활용할 수 있음
        // 여기서는 간단히 loginId로 다시 조회하여 ID를 채워줌
        Member savedMember = findByLoginId(member.getLoginId()).orElseThrow();
        member.setId(savedMember.getId());
        
        log.info("save: member={}", member);
        return member;
    }

    public Member findById(Long id) {
        String sql = "select id, login_id, name, password from member where id = ?";
        return template.queryForObject(sql, memberRowMapper(), id);
    }

    public Optional<Member> findByLoginId(String loginId) {
        String sql = "select id, login_id, name, password from member where login_id = ?";
        return template.query(sql, memberRowMapper(), loginId).stream().findFirst();
    }

    public List<Member> findAll() {
        String sql = "select id, login_id, name, password from member";
        return template.query(sql, memberRowMapper());
    }

    public void clearStore() {
        template.execute("delete from member");
    }

    private RowMapper<Member> memberRowMapper() {
        return BeanPropertyRowMapper.newInstance(Member.class);
    }
}
