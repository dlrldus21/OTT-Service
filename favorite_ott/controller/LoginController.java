package ott_service.favorite_ott.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ott_service.favorite_ott.domain.dao.Member1;
import ott_service.favorite_ott.domain.dto.LoginForm;
import ott_service.favorite_ott.domain.dto.MemberForm;
import ott_service.favorite_ott.repository.MemberRepository;
import ott_service.favorite_ott.service.LoginService;
import ott_service.favorite_ott.service.MemberService;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;
    private final LoginService loginService;


    // 로그인
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "user/login";
    }

    @PostMapping("/login")
    public String postLogin(@Valid @ModelAttribute("loginForm") LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "user/login";
        }
        Member1 loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "user/login";
        }

        HttpSession session = request.getSession();

        // 회원정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/";
    }
    
    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        // 세션 삭제
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate(); // 세션 제거
        }
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String myInfo(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object loginMember = session.getAttribute(SessionConst.LOGIN_MEMBER);
            if (loginMember != null) {
                model.addAttribute("loginMember", loginMember);
            }
        }
        return "user/profile";
    }

    // 프로필 수정 페이지 이동
    @GetMapping("/profile/update")
    public String updateProfileForm(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        Member1 loginMember = (Member1) session.getAttribute(SessionConst.LOGIN_MEMBER);

        if (loginMember == null) {
            return "redirect:/login"; // 로그인하지 않았다면 로그인 페이지로 리다이렉트
        }

        model.addAttribute("loginMember", loginMember);
        return "user/profileUpdate"; // 프로필 수정 페이지로 이동
    }

    // 프로필 수정 처리
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute MemberForm memberForm, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Member1 loginMember = (Member1) session.getAttribute(SessionConst.LOGIN_MEMBER);

        if (loginMember != null) {
            // 로그인한 회원의 정보를 업데이트
            loginMember.setName(memberForm.getName());
            if (!memberForm.getPassword().isEmpty()) {
                loginMember.setPassword(memberForm.getPassword()); // 새 비밀번호가 있으면 비밀번호도 업데이트
            }
            memberService.save(loginMember); // 업데이트된 회원 정보를 저장
        }
        return "redirect:/profile"; // 업데이트 후 프로필 페이지로 리다이렉트
    }
}
