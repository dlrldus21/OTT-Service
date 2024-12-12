package ott_service.favorite_ott.domain.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
public class Member1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "loginid")
    private String loginId;
    private String password;
    private String name;
    private String grade;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<Answer> answers;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<Question> questions; // 질문 추가
}
