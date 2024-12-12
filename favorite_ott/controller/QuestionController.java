package ott_service.favorite_ott.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ott_service.favorite_ott.domain.dao.Member1;
import ott_service.favorite_ott.domain.dao.Question;
import ott_service.favorite_ott.domain.dto.AnswerForm;
import ott_service.favorite_ott.domain.dto.QuestionForm;
import ott_service.favorite_ott.service.QuestionService;
import jakarta.persistence.NoResultException;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    // 질문 등록폼 열기
    @GetMapping("/question/create")
    public String questionCreateForm(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member1 loginMember, QuestionForm questionForm, Model model) {
        // 로그인 정보가 없으면 access-denied로 리다이렉트
        if (loginMember == null) {
            return "redirect:/access-denied";
        }

        model.addAttribute("questionForm", new QuestionForm());
        model.addAttribute("loginMember", loginMember);
        return "user/questionForm";
    }

    @PostMapping("/question/create")
    public String questionCreate(@Valid @ModelAttribute("questionForm") QuestionForm questionForm, BindingResult bindingResult, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member1 loginMember, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("loginMember", loginMember);
            return "user/questionForm";
        }
        Question question = new Question();
        question.setSubject(questionForm.getSubject());
        question.setContent(questionForm.getContent());
        question.setCreateDate(LocalDateTime.now());
        question.setAuthor(loginMember);
        questionService.create(question);
        model.addAttribute("loginMember", loginMember);
        return "redirect:/question/list";
    }


    // 목록 보기
    @GetMapping("/question/list")
    public String list(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member1 loginMember , Model model) {
        Page<Question> paging = questionService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        model.addAttribute("loginMember", loginMember);
        
        return "user/questionList";
    }

    // 추가된 부분: NoResultException 예외 처리**
    @ExceptionHandler(NoResultException.class)
    public ModelAndView handleNoResultException(NoResultException ex) {
        ModelAndView mav = new ModelAndView("errorPage");
        mav.addObject("message", "검색 결과가 없습니다.");
        return mav;
    }

    // 질문 상세페이지
    @GetMapping("/question/detail/{id}")
    public String detail(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member1 loginMember , @PathVariable("id") Long id, AnswerForm answerForm, Model model) {
        Optional<Question> question = questionService.getQuestion(id);

        // 로그인 정보가 없으면 access-denied로 리다이렉트
        if (loginMember == null) {
            return "redirect:/access-denied";
        }

        if (question.isPresent()) {
            model.addAttribute("question", question.get());
            model.addAttribute("answerForm", answerForm);
            model.addAttribute("loginMember", loginMember);
            return "user/questionDetail";
        }
        model.addAttribute("loginMember", loginMember);
        return "user/questionList";
    }

    // 수정 페이지열기
    @GetMapping("/qustion/modify/{questionId}")
    public String questionModify(@PathVariable("questionId") Long questionId, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member1 loginMember, QuestionForm questionForm, Model model) {

        Optional<Question> question = questionService.getQuestion(questionId);
        questionForm.setSubject(question.get().getSubject());
        questionForm.setContent(question.get().getContent());
        model.addAttribute("questionForm", questionForm);
        model.addAttribute("loginMember", loginMember);
        return "user/questionForm";
    }

    // 질문 수정
    @PostMapping("/qustion/modify/{questionId}")
    public String questionModifyUpdate(@Valid @ModelAttribute("questionForm") QuestionForm questionForm, BindingResult bindingResult, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member1 loginMember, @PathVariable("questionId") Long questionId, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("loginMember", loginMember);
            return "user/questionForm";
        }

        // 세션 회원데이터가 없으면 home
        if(loginMember == null) {
            return "home";
        }

        Optional<Question> findquestion = questionService.getQuestion(questionId);

        Question question = new Question();
        question.setId(questionId);
        question.setSubject(questionForm.getSubject());
        question.setContent(questionForm.getContent());
        question.setModifyDate(LocalDateTime.now());
        question.setCreateDate(findquestion.get().getCreateDate());
        question.setAuthor(loginMember);
        questionService.create(question);
        model.addAttribute("loginMember", loginMember);

        return String.format("redirect:/question/detail/%s", questionId);
    }

    // 삭제로직 추가
    @GetMapping("/qustion/delete/{questionId}")
    public String questionDelete(@PathVariable("questionId") Long questionId, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member1 loginMember) {

        Optional<Question> deleteQuestion = questionService.getQuestion(questionId);
        if (deleteQuestion.isPresent()) {
            questionService.delete(deleteQuestion.get());
        }
        return "redirect:/question/list";
    }

    @GetMapping("/motd")
    public String motd(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member1 loginMember, Model model) {
        model.addAttribute("loginMember", loginMember); // 로그인 정보 추가
        return "user/motdList";
    }
}
