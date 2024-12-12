package ott_service.favorite_ott.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ott_service.favorite_ott.domain.dao.Member1;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member1, Long> {
    // 사용자 아이디 조회
    Optional<Member1> findByLoginId(String loginId);
}
