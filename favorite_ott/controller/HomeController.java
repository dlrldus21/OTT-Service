package ott_service.favorite_ott.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
        // 세션이 없으면 home
        HttpSession session = request.getSession(false);

        if (session == null) {
            return "home";
        }
        // 로그인 세션에 보관한 회원 객체 찾기
        Object loginMember = session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 회원 데이터가 없으면 home 이동
        if (loginMember == null) {
            return "home";
        }

        // 세션이 유지되면 로그인홈으로 이동
        model.addAttribute("loginMember", loginMember);
        return "loginHome";
    }
}
