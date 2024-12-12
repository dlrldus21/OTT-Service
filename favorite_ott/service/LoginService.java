package ott_service.favorite_ott.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ott_service.favorite_ott.domain.dao.Member1;
import ott_service.favorite_ott.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Member1 login(String loginId, String password) {
        Optional<Member1> findMemberOptional = memberRepository.findByLoginId(loginId);

        if (findMemberOptional.isPresent()) {
            Member1 member1 = findMemberOptional.get();
            if (member1.getPassword().equals(password)) {
                return member1;
            }
            else {
                return null;
            }
        }
        return null;
    }
}
