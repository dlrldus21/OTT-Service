package ott_service.favorite_ott.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ott_service.favorite_ott.domain.dao.Sport;
import ott_service.favorite_ott.domain.dao.Study;

import java.util.List;

public interface StudyRepository extends JpaRepository<Study, Long> {

    // 제목으로 검색
    List<Study> findBySubjectContaining(String subject);

    // 내용으로 검색
    List<Study> findByContentContaining(String content);

    // 이미지 링크로 검색
    List<Study> findByImglink(String imglink);
}
