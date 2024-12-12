package ott_service.favorite_ott.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ott_service.favorite_ott.domain.dao.Ani;
import ott_service.favorite_ott.domain.dao.Daq;

import java.util.List;

public interface DaqRepository extends JpaRepository<Daq, Long> {

    // 제목으로 검색
    List<Daq> findBySubjectContaining(String subject);

    // 내용으로 검색
    List<Daq> findByContentContaining(String content);

    // 이미지 링크로 검색
    List<Daq> findByImglink(String imglink);
}
