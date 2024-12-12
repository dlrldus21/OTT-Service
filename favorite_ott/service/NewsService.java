package ott_service.favorite_ott.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ott_service.favorite_ott.domain.dao.Movie;
import ott_service.favorite_ott.domain.dao.News;
import ott_service.favorite_ott.domain.dto.MovieForm;
import ott_service.favorite_ott.domain.dto.NewsForm;
import ott_service.favorite_ott.repository.MovieRepository;
import ott_service.favorite_ott.repository.NewsRepository;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    // 콘텐츠 저장
    public void newsCreate(News news) {
        newsRepository.save(news);
    }

    public void newsCreateAdd(NewsForm newsForm) {
        News news = new News();
        news.setImglink(newsForm.getImglink());
        news.setSubject(newsForm.getSubject());
        news.setContent(newsForm.getContent());

        newsRepository.save(news); // 데이터베이스에 저장
    }
}
