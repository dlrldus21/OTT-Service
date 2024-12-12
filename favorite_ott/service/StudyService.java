package ott_service.favorite_ott.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ott_service.favorite_ott.domain.dao.Sport;
import ott_service.favorite_ott.domain.dao.Study;
import ott_service.favorite_ott.domain.dto.SportForm;
import ott_service.favorite_ott.domain.dto.StudyForm;
import ott_service.favorite_ott.repository.SportRepository;
import ott_service.favorite_ott.repository.StudyRepository;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    // 콘텐츠 저장
    public void studyCreate(Study study) { studyRepository.save(study);
    }

    public void studyCreateAdd(StudyForm studyForm) {
        Study study = new Study();
        study.setImglink(studyForm.getImglink());
        study.setSubject(studyForm.getSubject());
        study.setContent(studyForm.getContent());

        studyRepository.save(study); // 데이터베이스에 저장
    }
}
