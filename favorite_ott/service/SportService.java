package ott_service.favorite_ott.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ott_service.favorite_ott.domain.dao.News;
import ott_service.favorite_ott.domain.dao.Sport;
import ott_service.favorite_ott.domain.dto.NewsForm;
import ott_service.favorite_ott.domain.dto.SportForm;
import ott_service.favorite_ott.repository.NewsRepository;
import ott_service.favorite_ott.repository.SportRepository;

@Service
@RequiredArgsConstructor
public class SportService {

    private final SportRepository sportRepository;

    // 콘텐츠 저장
    public void sportCreate(Sport sport) { sportRepository.save(sport);
    }

    public void sportCreateAdd(SportForm sportForm) {
        Sport sport = new Sport();
        sport.setImglink(sportForm.getImglink());
        sport.setSubject(sportForm.getSubject());
        sport.setContent(sportForm.getContent());

        sportRepository.save(sport); // 데이터베이스에 저장
    }
}
