package ott_service.favorite_ott.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import ott_service.favorite_ott.domain.dao.Member1;
import ott_service.favorite_ott.domain.dao.Question;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    //findAll 메서드
    Page<Question> findAll(Specification<Question> spec, Pageable pageable);

    // 특정 회원이 작성한 질문을 조회하는 메서드 추가
    List<Question> findByAuthor(Member1 author);
}
