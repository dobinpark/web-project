package dobin.webproject.controller;

import dobin.webproject.dto.MemberFormDto;
import dobin.webproject.entity.Member;
import dobin.webproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String memberForm(Model model) {
        // 1-1. 빈 껍데기 MemberFormDto를 view로 넘겨준다.(유효성 검사를 위해)
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String newMember(@Valid MemberFormDto memberFormDto,
                            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "member/memberForm";
        }

        // 1-5. try로 감싸준다.
        try {
            // 1-2. view에서 넘어온 MemberFormDto에서 password는 passwordEncoder로 인코딩해서 Member Entity를 반환한다.
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            // 1-3. 회원 가입
            memberService.saveMember(member);

        } catch (IllegalStateException e) {
            // 1-6. 에러 발생시
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember() {
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인하여 주십시오.");
        return "/member/memberLoginForm";
    }
}


