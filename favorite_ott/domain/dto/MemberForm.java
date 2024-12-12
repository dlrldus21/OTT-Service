package ott_service.favorite_ott.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import ott_service.favorite_ott.domain.dao.Member1;

@Getter @Setter
public class MemberForm {

    private Long id;

    @NotEmpty (message = "아이디는 필수입니다.")
    private String loginId;

    @NotEmpty (message = "비밀번호는 필수입니다.")
    private String password;

    @NotEmpty (message = "이름은 필수입니다.")
    private String name;

    private String grade;

    public MemberForm() {}

    // 기존 회원 정보로부터 폼을 채우는 생성자
    public MemberForm(Member1 member1) {
        this.id = member1.getId();
        this.loginId = member1.getLoginId();
        this.name = member1.getName();
    }
}
