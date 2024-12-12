package ott_service.favorite_ott.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ott_service.favorite_ott.domain.dao.Ani;

import java.util.List;

public interface AniRepository extends JpaRepository<Ani, Long> {

    // 제목으로 검색
    List<Ani> findBySubjectContaining(String subject);

    // 내용으로 검색
    List<Ani> findByContentContaining(String content);

    // 이미지 링크로 검색
    List<Ani> findByImglink(String imglink);
}
