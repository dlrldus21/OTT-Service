package ott_service.favorite_ott.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ott_service.favorite_ott.domain.dao.Enter;
import ott_service.favorite_ott.domain.dao.Movie;
import ott_service.favorite_ott.domain.dto.EnterForm;
import ott_service.favorite_ott.domain.dto.MovieForm;
import ott_service.favorite_ott.repository.EnterRepository;
import ott_service.favorite_ott.repository.MovieRepository;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    // 콘텐츠 저장
    public void movieCreate(Movie movie) {
        movieRepository.save(movie);
    }

    public void movieCreateAdd(MovieForm movieForm) {
        Movie movie = new Movie();
        movie.setImglink(movieForm.getImglink());
        movie.setSubject(movieForm.getSubject());
        movie.setContent(movieForm.getContent());

        movieRepository.save(movie); // 데이터베이스에 저장
    }
}
