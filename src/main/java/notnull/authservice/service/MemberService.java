package notnull.authservice.service;

import lombok.RequiredArgsConstructor;
import notnull.authservice.dto.LoginDto;
import notnull.authservice.dto.RegisterDto;
import notnull.authservice.entity.Member;
import notnull.authservice.exception.DuplicateUsernameException;
import notnull.authservice.exception.LoginFailedException;
import notnull.authservice.exception.PasswordMismatchException;
import notnull.authservice.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Member login(LoginDto dto) {
        return memberRepository.findByUsername(dto.getUsername())
                .filter(member -> passwordEncoder.matches(dto.getPassword(), member.getPassword()))
                .orElseThrow(LoginFailedException::new);
    }

    public void register(RegisterDto dto) {
        if (!Objects.equals(dto.getPassword(), dto.getPasswordConfirm())) {
            throw new PasswordMismatchException();
        }
        if (memberRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateUsernameException(dto.getUsername());
        }

        memberRepository.save(Member.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .role(Member.Role.USER)
                .build());
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }
}