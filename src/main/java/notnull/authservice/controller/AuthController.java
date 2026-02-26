package notnull.authservice.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import notnull.authservice.dto.LoginDto;
import notnull.authservice.dto.RegisterDto;
import notnull.authservice.entity.Member;
import notnull.authservice.exception.DuplicateUsernameException;
import notnull.authservice.exception.LoginFailedException;
import notnull.authservice.exception.PasswordMismatchException;
import notnull.authservice.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private static final String SESSION_MEMBER_ID = "loginMemberId";

    private final MemberService memberService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginDto", new LoginDto());
        return "login";
    }

    @PostMapping("/login")
    public String loginProcess(@Valid @ModelAttribute LoginDto loginDto,
                               BindingResult bindingResult,
                               HttpSession session,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMsg", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "login";
        }

        try {
            Member member = memberService.login(loginDto);
            session.setAttribute(SESSION_MEMBER_ID, member.getId());
            return member.getRole() == Member.Role.ADMIN ? "redirect:/admin/" : "redirect:/";
        } catch (LoginFailedException e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerProcess(@Valid @ModelAttribute RegisterDto registerDto,
                                  BindingResult bindingResult,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMsg", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "register";
        }

        try {
            memberService.register(registerDto);
            return "redirect:/login?registered=true";
        } catch (PasswordMismatchException | DuplicateUsernameException e) {
            model.addAttribute("errorMsg", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        Member member = getLoginMember(session);
        if (member == null) return "redirect:/login";
        model.addAttribute("member", member);
        return "home";
    }

    @GetMapping("/admin/")
    public String adminHome(HttpSession session, Model model) {
        Member member = getLoginMember(session);
        if (member == null) return "redirect:/login";
        if (member.getRole() != Member.Role.ADMIN) {
            model.addAttribute("loginDto", new LoginDto());
            model.addAttribute("errorMsg", "관리자만 접근할 수 있습니다.");
            return "login";
        }
        model.addAttribute("member", member);
        return "admin/home";
    }

    @GetMapping("/admin/members")
    public String memberList(HttpSession session, Model model) {
        Member member = getLoginMember(session);
        if (member == null) return "redirect:/login";
        if (member.getRole() != Member.Role.ADMIN) {
            model.addAttribute("loginDto", new LoginDto());
            model.addAttribute("errorMsg", "관리자만 접근할 수 있습니다.");
            return "login";
        }
        model.addAttribute("members", memberService.findAllMembers());
        return "admin/members";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    private Member getLoginMember(HttpSession session) {
        Long memberId = (Long) session.getAttribute(SESSION_MEMBER_ID);
        if (memberId == null) return null;
        return memberService.findById(memberId);
    }
}