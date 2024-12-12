package ott_service.favorite_ott.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ott_service.favorite_ott.domain.dao.News;
import ott_service.favorite_ott.domain.dao.Sport;

import java.util.List;

public interface SportRepository extends JpaRepository<Sport, Long> {

    // 제목으로 검색
    List<Sport> findBySubjectContaining(String subject);

    // 내용으로 검색
    List<Sport> findByContentContaining(String content);

    // 이미지 링크로 검색
    List<Sport> findByImglink(String imglink);
}
