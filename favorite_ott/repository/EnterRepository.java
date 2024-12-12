package ott_service.favorite_ott.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ott_service.favorite_ott.domain.dao.Ani;
import ott_service.favorite_ott.domain.dao.Drama;
import ott_service.favorite_ott.domain.dao.Enter;

import java.util.List;

public interface EnterRepository extends JpaRepository<Enter, Long> {

    // 제목으로 검색
    List<Enter> findBySubjectContaining(String subject);

    // 내용으로 검색
    List<Enter> findByContentContaining(String content);

    // 이미지 링크로 검색
    List<Enter> findByImglink(String imglink);
}
