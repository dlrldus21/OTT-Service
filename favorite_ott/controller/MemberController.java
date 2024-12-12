package ott_service.favorite_ott.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ott_service.favorite_ott.domain.dao.Member1;
import ott_service.favorite_ott.domain.dto.MemberForm;
import ott_service.favorite_ott.repository.MemberRepository;
import ott_service.favorite_ott.service.MemberService;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    // 회원가입
    @GetMapping("/accountCreate")
    public String createAccount(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "user/addAccount";
    }

    // 회원가입 포스트
    @PostMapping("/accountCreate")
    public String create(@Validated @ModelAttribute("memberForm") MemberForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/addAccount";
        }
        // 중복 회원 검사
        boolean isMemberExists = memberRepository.findByLoginId(form.getLoginId()).isPresent();
        if (isMemberExists) {
            // 로그인 ID가 이미 존재하면 에러 메시지 추가
            bindingResult.rejectValue("loginId", "duplicate", "이미 존재하는 로그인 ID입니다.");
            return "user/addAccount"; // 다시 회원 가입 폼으로 돌아감
        }

        Member1 member1 = new Member1();
        member1.setLoginId(form.getLoginId());
        member1.setPassword(form.getPassword());
        member1.setName(form.getName());
        member1.setGrade("user");

        memberService.join(member1);
        return "redirect:/login";
    }

    // 리스트
    @GetMapping("/members")
    public String listMembers(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member1 loginMember, Model model) {
        List<Member1> members = memberService.findMember();
        model.addAttribute("members", members);
        model.addAttribute("loginMember", loginMember);
        return "admin/memberList";
    }

    // 삭제
    @GetMapping("/members/{id}/delete")
    public String deleteMember(@PathVariable("id") Long id) {
        Optional<Member1> findMember = memberService.findOne(id);

        if (findMember.isPresent()) {
            memberService.delete(findMember.get());
        }
        return "redirect:/members";
    }

    // 멤버 수정폼 보여주기
    @GetMapping("/members/{memberid}/edit")
    public String updateMemberForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member1 loginMember, @PathVariable("memberid") Long memberid, Model model) {
        Optional<Member1> findMember = memberService.findOne(memberid);
        if (findMember.isPresent()) {
            Member1 findMemberGet = findMember.get(); // 존재하는 회원 엔티티 가져오기
            MemberForm memberForm = new MemberForm();
            memberForm.setId(findMemberGet.getId());
            memberForm.setLoginId(findMemberGet.getLoginId());
            memberForm.setPassword(findMemberGet.getPassword());
            memberForm.setName(findMemberGet.getName());
            memberForm.setGrade(findMemberGet.getGrade());

            model.addAttribute("memberForm", memberForm);
            model.addAttribute("loginMember", loginMember);
            return "admin/adminProfileUpdate";
        }
        model.addAttribute("loginMember", loginMember);
        return "admin/memberList"; // 회원이 존재하지 않으면 리스트로 이동
    }

    // 멤버 수정 저장
    @PostMapping("/members/{memberid}/edit")
    public String updateMember(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member1 loginMember, @Validated @ModelAttribute("memberForm") MemberForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("loginMember", loginMember);
            return "admin/adminProfileUpdate"; // 오류가 있을 경우 수정폼으로 돌아감
        }

        // 기존 회원 엔티티 조회
        Optional<Member1> existingMemberOpt = memberService.findOne(form.getId());
        if (existingMemberOpt.isPresent()) {
            Member1 existingMember = existingMemberOpt.get(); // 존재하는 회원 엔티티 가져오기
            existingMember.setLoginId(form.getLoginId());
            existingMember.setName(form.getName());
            existingMember.setPassword(form.getPassword());
            existingMember.setGrade(form.getGrade());

            memberService.save(existingMember); // 기존 엔티티를 저장
        }

        return "redirect:/members"; // 수정 완료 후 회원 목록으로 리다이렉트
    }


}
