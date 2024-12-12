package ott_service.favorite_ott.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ott_service.favorite_ott.domain.dao.Enter;
import ott_service.favorite_ott.domain.dao.Movie;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    // 제목으로 검색
    List<Movie> findBySubjectContaining(String subject);

    // 내용으로 검색
    List<Movie> findByContentContaining(String content);

    // 이미지 링크로 검색
    List<Movie> findByImglink(String imglink);
}
