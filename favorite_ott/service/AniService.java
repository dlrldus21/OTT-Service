package ott_service.favorite_ott.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ott_service.favorite_ott.domain.dao.Ani;
import ott_service.favorite_ott.domain.dao.Question;
import ott_service.favorite_ott.domain.dto.AniForm;
import ott_service.favorite_ott.repository.AniRepository;

@Service
@RequiredArgsConstructor
public class AniService {

    private final AniRepository aniRepository;

    // 콘텐츠 저장
    public void aniCreate(Ani ani) {
        aniRepository.save(ani);
    }

    public void aniCreateAdd(AniForm aniForm) {
        Ani ani = new Ani();
        ani.setImglink(aniForm.getImglink());
        ani.setSubject(aniForm.getSubject());
        ani.setContent(aniForm.getContent());

        aniRepository.save(ani); // 데이터베이스에 저장
    }
}
