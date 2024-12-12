package ott_service.favorite_ott.service;

import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ott_service.favorite_ott.domain.dao.Answer;
import ott_service.favorite_ott.domain.dao.Member1;
import ott_service.favorite_ott.domain.dao.Question;
import ott_service.favorite_ott.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    // 질문 생성 시 저장하기
    public void create(Question question) {
        questionRepository.save(question);
    }

    // 전체 질문 조회
    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        PageRequest pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Question> spec = search(kw);
        Page<Question> result = questionRepository.findAll(spec, pageable);

        if (result.hasContent()) {
            return result;
        } else {
            throw new NoResultException("검색결과가 없습니다.");
//            return null;
        }
    }

    // 질문조회
    public Optional<Question> getQuestion(Long id) {
        return questionRepository.findById(id);
    }

    // 질문 삭제하기
    public void delete(Question question) {
        questionRepository.delete(question);
    }

    // 검색기능추가하기

    private Specification<Question> search(String kw) {
        return new Specification<Question>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                // 중복 제거
                query.distinct(true);
                Join<Question, Member1> m1 = q.join("author", JoinType.LEFT);
                Join<Question, Member1> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, Member1> m2 = q.join("author", JoinType.LEFT);
                return criteriaBuilder.or(
                        criteriaBuilder.like(q.get("subject"), "%" + kw + "%"), // 제목
                        criteriaBuilder.like(q.get("content"), "%" + kw + "%"), // 질문내용
                        criteriaBuilder.like(m1.get("name"), "%" + kw + "%"), // 질문 작성자
                        criteriaBuilder.like(a.get("content"), "%" + kw + "%"), // 답변내용
                        criteriaBuilder.like(m2.get("name"), "%" + kw + "%") // 답변작성자
                );
            }
        };
    }
}
