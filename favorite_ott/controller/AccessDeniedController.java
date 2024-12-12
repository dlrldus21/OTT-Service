package ott_service.favorite_ott.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import ott_service.favorite_ott.domain.dao.Member1;

@Controller
@RequiredArgsConstructor
public class AccessDeniedController {
    
    
    // 접근 권한 없음
    @GetMapping("/access-denied")
    public String accessDenied(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member1 loginMember, Model model) {

        model.addAttribute("loginMember", loginMember);

        return "accessDenied"; // accessDenied.html
    }
}
