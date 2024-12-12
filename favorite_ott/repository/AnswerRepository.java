package ott_service.favorite_ott.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ott_service.favorite_ott.domain.dao.Answer;
import ott_service.favorite_ott.domain.dao.Question;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    // 특정 질문에 대한 답변 조회 메서드 추가
    List<Answer> findByQuestion(Question question);
}
