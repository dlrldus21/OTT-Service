package ott_service.favorite_ott.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ott_service.favorite_ott.domain.dao.Daq;
import ott_service.favorite_ott.domain.dao.Drama;
import ott_service.favorite_ott.domain.dto.DaqForm;
import ott_service.favorite_ott.domain.dto.DramaForm;
import ott_service.favorite_ott.repository.DaqRepository;
import ott_service.favorite_ott.repository.DramaRepository;

@Service
@RequiredArgsConstructor
public class DramaService {

    private final DramaRepository dramaRepository;

    // 콘텐츠 저장
    public void dramaCreate(Drama drama) {
        dramaRepository.save(drama);
    }

    public void dramaCreateAdd(DramaForm dramaForm) {
        Drama drama = new Drama();
        drama.setImglink(dramaForm.getImglink());
        drama.setSubject(dramaForm.getSubject());
        drama.setContent(dramaForm.getContent());

        dramaRepository.save(drama); // 데이터베이스에 저장
    }
}
