package ott_service.favorite_ott.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import ott_service.favorite_ott.domain.dao.Answer;
import ott_service.favorite_ott.domain.dao.Member1;
import ott_service.favorite_ott.domain.dao.Question;
import ott_service.favorite_ott.domain.dto.AnswerForm;
import ott_service.favorite_ott.service.AnswerService;
import ott_service.favorite_ott.service.QuestionService;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AnswerController {
    private final AnswerService answerService;
    private final QuestionService questionService;

    // 질문에 대한 답글쓰기
    @PostMapping("/answer/create/{questionId}")
    public String createAnswer(@PathVariable("questionId") Long questionId, @Valid AnswerForm answerForm, BindingResult bindingResult, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member1 loginMember, Model model) {

        Optional<Question> question = questionService.getQuestion(questionId);

        if(bindingResult.hasErrors()) {
            model.addAttribute("question", question.get());
            model.addAttribute("loginMember", loginMember);
            return "user/questionDetail";
        }

        if (question.isPresent()) {

            Answer answer = new Answer();
            answer.setContent(answerForm.getContent());
            answer.setCreateDate(LocalDateTime.now());
            answer.setQuestion(question.get());
            answer.setAuthor(loginMember);
            answerService.create(answer);
        }
        model.addAttribute("loginMember", loginMember);
        return String.format("redirect:/question/detail/%s", questionId);
    }

    // 답변 수정페이지 열기
    @GetMapping("/answer/modify/{answerId}")
    public String answerModify(@PathVariable("answerId") Long answerId, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member1 loginMember, AnswerForm answerForm, Model model) {

        Optional<Answer> answer = answerService.getAnswer(answerId);
        answerForm.setContent(answer.get().getContent());

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("answerForm", answerForm);
        return "user/answerUpdate";
    }

    @PostMapping("/answer/modify/{answerId}")
    public String answerModifyUpdate(@Valid AnswerForm answerForm, BindingResult bindingResult, @PathVariable("answerId") Long answerId, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member1 loginMember, Model model){

        if(bindingResult.hasErrors()) {
            model.addAttribute("loginMember", loginMember);
            return "user/answerUpdate";
        }

        // 아이디에 해당하는 답글 조회
        Optional<Answer> findAnswer = answerService.getAnswer(answerId);
        // 답글에 해당하는 질문 조회
        Optional<Question> findQuestion = questionService.getQuestion(findAnswer.get().getQuestion().getId());

        log.info("찾은 답글 번호 : {}",findAnswer.get().getId());
        log.info("찾은 질문 번호 : {}",findQuestion.get().getId());

        Answer answer = new Answer();
        answer.setId(answerId);
        answer.setContent(answerForm.getContent());
        answer.setCreateDate(findAnswer.get().getCreateDate());
        answer.setModifyDate(LocalDateTime.now());
        answer.setAuthor(loginMember);
        answer.setQuestion(findQuestion.get());

        answerService.create(answer);
        model.addAttribute("loginMember", loginMember);
        return String.format("redirect:/question/detail/%s", findQuestion.get().getId());
    }

    // 삭제로직
    @GetMapping("/answer/delete/{answerId}")
    public String answerDelete(@PathVariable("answerId") Long answerId, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member1 loginMember, Model model) {

        Optional<Answer> deleteAnswer = answerService.getAnswer(answerId);
        model.addAttribute("loginMember", loginMember);
        answerService.delete(deleteAnswer.get());
        return String.format("redirect:/question/detail/%s", deleteAnswer.get().getQuestion().getId());
    }
}
