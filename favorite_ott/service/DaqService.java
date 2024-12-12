package ott_service.favorite_ott.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ott_service.favorite_ott.domain.dao.Ani;
import ott_service.favorite_ott.domain.dao.Daq;
import ott_service.favorite_ott.domain.dto.AniForm;
import ott_service.favorite_ott.domain.dto.DaqForm;
import ott_service.favorite_ott.repository.AniRepository;
import ott_service.favorite_ott.repository.DaqRepository;

@Service
@RequiredArgsConstructor
public class DaqService {

    private final DaqRepository daqRepository;

    // 콘텐츠 저장
    public void daqCreate(Daq daq) {
        daqRepository.save(daq);
    }

    public void daqCreateAdd(DaqForm daqForm) {
       Daq daq = new Daq();
        daq.setImglink(daqForm.getImglink());
        daq.setSubject(daqForm.getSubject());
        daq.setContent(daqForm.getContent());

        daqRepository.save(daq); // 데이터베이스에 저장
    }
}
