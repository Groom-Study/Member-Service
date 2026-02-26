package notnull.authservice.service;

import notnull.authservice.dto.LoginDto;
import notnull.authservice.dto.RegisterDto;
import notnull.authservice.entity.Member;
import notnull.authservice.exception.DuplicateUsernameException;
import notnull.authservice.exception.LoginFailedException;
import notnull.authservice.exception.PasswordMismatchException;
import notnull.authservice.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    private LoginDto loginDto(String username, String password) {
        LoginDto dto = new LoginDto();
        dto.setUsername(username);
        dto.setPassword(password);
        return dto;
    }

    private RegisterDto validRegisterDto() {
        RegisterDto dto = new RegisterDto();
        dto.setUsername("testuser");
        dto.setPassword("password123");
        dto.setPasswordConfirm("password123");
        dto.setName("testuser");
        return dto;
    }

    private Member fakeMember(String username, String encodedPassword) {
        return Member.builder()
                .id(1L)
                .username(username)
                .password(encodedPassword)
                .name("testuser")
                .role(Member.Role.USER)
                .build();
    }

    @Nested
    @DisplayName("login()")
    class LoginTest {

        @Test
        @DisplayName("올바른 아이디/비밀번호면 Member를 반환한다")
        void login_success() {
            given(memberRepository.findByUsername("testuser"))
                    .willReturn(Optional.of(fakeMember("testuser", "hashed")));
            given(passwordEncoder.matches("password123", "hashed")).willReturn(true);

            Member result = memberService.login(loginDto("testuser", "password123"));

            assertThat(result.getUsername()).isEqualTo("testuser");
        }

        @Test
        @DisplayName("틀린 비밀번호면 LoginFailedException이 발생한다")
        void login_wrongPassword() {
            given(memberRepository.findByUsername("testuser"))
                    .willReturn(Optional.of(fakeMember("testuser", "hashed")));
            given(passwordEncoder.matches("wrongpass", "hashed")).willReturn(false);

            assertThatThrownBy(() -> memberService.login(loginDto("testuser", "wrongpass")))
                    .isInstanceOf(LoginFailedException.class);
        }

        @Test
        @DisplayName("존재하지 않는 아이디면 LoginFailedException이 발생한다")
        void login_usernameNotFound() {
            given(memberRepository.findByUsername("nobody"))
                    .willReturn(Optional.empty());

            assertThatThrownBy(() -> memberService.login(loginDto("nobody", "password123")))
                    .isInstanceOf(LoginFailedException.class);
        }

        @Test
        @DisplayName("비밀번호가 null이면 LoginFailedException이 발생한다")
        void login_passwordIsNull() {
            given(memberRepository.findByUsername("testuser"))
                    .willReturn(Optional.of(fakeMember("testuser", "hashed")));
            given(passwordEncoder.matches(null, "hashed")).willReturn(false);

            assertThatThrownBy(() -> memberService.login(loginDto("testuser", null)))
                    .isInstanceOf(LoginFailedException.class);
        }
    }

    @Nested
    @DisplayName("register()")
    class RegisterTest {

        @Test
        @DisplayName("정상 입력이면 인코딩된 비밀번호로 save()가 호출된다")
        void register_success() {
            given(memberRepository.existsByUsername("testuser")).willReturn(false);
            given(passwordEncoder.encode("password123")).willReturn("hashed");

            memberService.register(validRegisterDto());

            ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);
            then(memberRepository).should().save(captor.capture());
            assertThat(captor.getValue().getPassword()).isEqualTo("hashed");
        }

        @Test
        @DisplayName("저장되는 회원의 role은 항상 USER다")
        void register_roleIsAlwaysUser() {
            given(memberRepository.existsByUsername("testuser")).willReturn(false);
            given(passwordEncoder.encode(anyString())).willReturn("hashed");

            memberService.register(validRegisterDto());

            ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);
            then(memberRepository).should().save(captor.capture());
            assertThat(captor.getValue().getRole()).isEqualTo(Member.Role.USER);
        }

        @Test
        @DisplayName("비밀번호와 비밀번호 확인이 다르면 PasswordMismatchException이 발생하고 save()는 호출되지 않는다")
        void register_passwordMismatch() {
            RegisterDto dto = validRegisterDto();
            dto.setPasswordConfirm("different!");

            assertThatThrownBy(() -> memberService.register(dto))
                    .isInstanceOf(PasswordMismatchException.class);

            then(memberRepository).should(never()).save(any());
        }

        @Test
        @DisplayName("이미 존재하는 아이디면 DuplicateUsernameException이 발생하고 save()는 호출되지 않는다")
        void register_duplicateUsername() {
            given(memberRepository.existsByUsername("testuser")).willReturn(true);

            assertThatThrownBy(() -> memberService.register(validRegisterDto()))
                    .isInstanceOf(DuplicateUsernameException.class);

            then(memberRepository).should(never()).save(any());
        }

        @Test
        @DisplayName("DuplicateUsernameException 메시지에 중복된 아이디가 포함된다")
        void register_duplicateUsername_messageContainsUsername() {
            given(memberRepository.existsByUsername("testuser")).willReturn(true);

            assertThatThrownBy(() -> memberService.register(validRegisterDto()))
                    .isInstanceOf(DuplicateUsernameException.class)
                    .hasMessageContaining("testuser");
        }

        @Test
        @DisplayName("passwordConfirm이 null이면 PasswordMismatchException이 발생한다")
        void register_passwordConfirmIsNull() {
            RegisterDto dto = validRegisterDto();
            dto.setPasswordConfirm(null);

            assertThatThrownBy(() -> memberService.register(dto))
                    .isInstanceOf(PasswordMismatchException.class);
        }

        @Test
        @DisplayName("password가 null이면 PasswordMismatchException이 발생한다")
        void register_passwordIsNull() {
            RegisterDto dto = validRegisterDto();
            dto.setPassword(null);

            assertThatThrownBy(() -> memberService.register(dto))
                    .isInstanceOf(PasswordMismatchException.class);
        }
    }
}