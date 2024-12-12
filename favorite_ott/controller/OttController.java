package ott_service.favorite_ott.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import ott_service.favorite_ott.domain.dao.Member1;
import ott_service.favorite_ott.domain.dto.AniForm;
import ott_service.favorite_ott.domain.dto.DaqForm;
import ott_service.favorite_ott.service.*;

@Controller
@RequiredArgsConstructor
public class OttController {

    private final AniService aniService;
    private final DaqService daqService;
    private final DramaService dramaService;
    private final EnterService enterService;
    private final MovieService movieService;
    private final NewsService newsService;
    private final StudyService studyService;

    @GetMapping("/admin-page")
    public String adminPage(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member1 loginMember, Model model) {
        // 로그인한 사용자가 없거나, 관리자가 아닌 경우 접근 차단
        if (loginMember == null || !loginMember.getGrade().equals("admin")) {
            return "redirect:/access-denied"; // 접근 거부 페이지로 리다이렉트
        }

        // 로그인 정보를 모델에 추가
        model.addAttribute("loginMember", loginMember);

        // 관리자인 경우에만 페이지로 이동
        model.addAttribute("pageTitle", "관리자 페이지");
        return "admin/adminPage"; // admin-page.html 템플릿을 반환
    }

    @GetMapping("/ani-add")
    public String aniAdd(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member1 loginMember , Model model, AniForm aniForm) {
        if (loginMember == null || !loginMember.getGrade().equals("admin")) {
            return "redirect:/access-denied"; // 접근 거부 페이지로 리다이렉트
        }

        model.addAttribute("AniForm", new AniForm());
        model.addAttribute("loginMember", loginMember);
        return "admin/AniAddForm";
    }

    // 등록 처리
    @PostMapping("/ani-add")
    public String createAni(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member1 loginMember,
                            @ModelAttribute AniForm aniForm,
                            Model model) {
        // 로그인 사용자 체크
        if (loginMember == null || !loginMember.getGrade().equals("admin")) {
            return "redirect:/access-denied"; // 접근 거부 페이지로 리다이렉트
        }

        // 등록 로직 (예: 데이터베이스에 저장)
        aniService.aniCreateAdd(aniForm); // 저장 서비스 호출

        // 성공 메시지 추가
        model.addAttribute("successMessage", "저장이 성공적으로 등록되었습니다."); // 성공 메시지

        // 질문 목록 페이지로 리다이렉트 (원하는 페이지로 변경)
        return "redirect:/admin-page"; // 관리자 페이지로 리다이렉트
    }

    @GetMapping("/daq-add")
    public String daqAdd(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member1 loginMember , Model model, DaqForm daqForm) {
        if (loginMember == null || !loginMember.getGrade().equals("admin")) {
            return "redirect:/access-denied"; // 접근 거부 페이지로 리다이렉트
        }

        model.addAttribute("DaqForm", new DaqForm());
        model.addAttribute("loginMember", loginMember);
        return "admin/DaqAddForm";
    }

    // 등록 처리
    @PostMapping("/daq-add")
    public String createDaq(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member1 loginMember,
                            @ModelAttribute DaqForm daqForm,
                            Model model) {
        // 로그인 사용자 체크
        if (loginMember == null || !loginMember.getGrade().equals("admin")) {
            return "redirect:/access-denied"; // 접근 거부 페이지로 리다이렉트
        }

        // 등록 로직 (예: 데이터베이스에 저장)
        daqService.daqCreateAdd(daqForm); // 저장 서비스 호출

        // 성공 메시지 추가
        model.addAttribute("successMessage", "저장이 성공적으로 등록되었습니다."); // 성공 메시지

        // 질문 목록 페이지로 리다이렉트 (원하는 페이지로 변경)
        return "redirect:/admin-page"; // 관리자 페이지로 리다이렉트
    }

}
