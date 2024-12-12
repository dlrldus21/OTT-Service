package ott_service.favorite_ott.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {

    @GetMapping("/index")
    public String index(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object loginMember = session.getAttribute("loginMember");
            if (loginMember != null) {
                model.addAttribute("loginMember", loginMember);
                model.addAttribute("isLoggedIn", true);  // 로그인 상태 추가
                return "index_1010"; // 로그인 상태에서 index_1010 페이지로 이동
            }
        }

        // 로그인 정보가 없으면 access-denied 페이지로 이동
        return "redirect:/access-denied"; // access-denied 페이지로 리다이렉트
    }

    @GetMapping("/index2")
    public String index2(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object loginMember = session.getAttribute("loginMember");
            if (loginMember != null) {
                model.addAttribute("loginMember", loginMember);
            }
        }
        return "movie";
    }

    @GetMapping("/index-j")
    public String indexJ(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object loginMember = session.getAttribute("loginMember");
            if (loginMember != null) {
                model.addAttribute("loginMember", loginMember);
            }
        }
        return "index2";
    }
}
