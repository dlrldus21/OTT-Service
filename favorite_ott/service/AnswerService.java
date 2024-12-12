package ott_service.favorite_ott.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ott_service.favorite_ott.domain.dao.Answer;
import ott_service.favorite_ott.repository.AnswerRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    public void create(Answer answer) {
        answerRepository.save(answer);
    }

    // 답글 조회
    public Optional<Answer> getAnswer(Long id) {
        return answerRepository.findById(id);
    }

    // 답글 삭제
    public void delete(Answer answer) {
        answerRepository.delete(answer);
    }
}
