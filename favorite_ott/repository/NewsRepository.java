package ott_service.favorite_ott.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ott_service.favorite_ott.domain.dao.Movie;
import ott_service.favorite_ott.domain.dao.News;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {

    // 제목으로 검색
    List<News> findBySubjectContaining(String subject);

    // 내용으로 검색
    List<News> findByContentContaining(String content);

    // 이미지 링크로 검색
    List<News> findByImglink(String imglink);
}
