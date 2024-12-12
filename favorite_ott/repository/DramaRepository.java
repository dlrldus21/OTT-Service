package ott_service.favorite_ott.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ott_service.favorite_ott.domain.dao.Ani;
import ott_service.favorite_ott.domain.dao.Drama;

import java.util.List;

public interface DramaRepository extends JpaRepository<Drama, Long> {

    // 제목으로 검색
    List<Drama> findBySubjectContaining(String subject);

    // 내용으로 검색
    List<Drama> findByContentContaining(String content);

    // 이미지 링크로 검색
    List<Drama> findByImglink(String imglink);
}
