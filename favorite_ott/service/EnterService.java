package ott_service.favorite_ott.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ott_service.favorite_ott.domain.dao.Drama;
import ott_service.favorite_ott.domain.dao.Enter;
import ott_service.favorite_ott.domain.dto.DramaForm;
import ott_service.favorite_ott.domain.dto.EnterForm;
import ott_service.favorite_ott.repository.DramaRepository;
import ott_service.favorite_ott.repository.EnterRepository;

@Service
@RequiredArgsConstructor
public class EnterService {

    private final EnterRepository enterRepository;

    // 콘텐츠 저장
    public void enterCreate(Enter enter) {
        enterRepository.save(enter);
    }

    public void enterCreateAdd(EnterForm enterForm) {
        Enter enter = new Enter();
        enter.setImglink(enterForm.getImglink());
        enter.setSubject(enterForm.getSubject());
        enter.setContent(enterForm.getContent());

        enterRepository.save(enter); // 데이터베이스에 저장
    }
}
