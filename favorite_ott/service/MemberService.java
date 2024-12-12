package ott_service.favorite_ott.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ott_service.favorite_ott.domain.dao.Answer;
import ott_service.favorite_ott.domain.dao.Member1;
import ott_service.favorite_ott.domain.dao.Question;
import ott_service.favorite_ott.repository.AnswerRepository;
import ott_service.favorite_ott.repository.MemberRepository;
import ott_service.favorite_ott.repository.QuestionRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    // 회원가입
    public Long join(Member1 member1) {
        // 중복 회원
        validateDuplicateMember(member1);
        this.memberRepository.save(member1);
        return member1.getId();
    }
    
    // 중복 회원 메서드
    private void validateDuplicateMember(Member1 member1) {
        Optional<Member1> findMember = memberRepository.findByLoginId(member1.getLoginId());
        if (findMember.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 전체 조회
    public List<Member1> findMember() {
        return memberRepository.findAll();
    }

    // 한명 조회
    public Optional<Member1> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

    // 회원 삭제
    public void delete(Member1 member1) {
        // 해당 회원이 작성한 질문을 가져와서 삭제
        List<Question> questions = questionRepository.findByAuthor(member1);
        for (Question question : questions) {
            // 질문에 대한 답변도 삭제
            List<Answer> answers = answerRepository.findByQuestion(question);
            answerRepository.deleteAll(answers); // 답변 삭제
            questionRepository.delete(question); // 질문 삭제
        }

        // 최종적으로 회원 삭제
        this.memberRepository.delete(member1);
    }

    // 회원 수정
    public void save(Member1 member1) {
        memberRepository.save(member1); // JPA를 사용하는 경우
    }



}
