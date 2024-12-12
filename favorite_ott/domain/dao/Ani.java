package ott_service.favorite_ott.domain.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Ani {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ani_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String imglink;

    @Column(length = 200)
    private String subject;

    // columnDefinition = "TEXT" : 텍스트를 열 데이터로 넣을 수 있음을 의미하고 글자 수를 제한할 수 없는 경우에 사용한다.
    @Column(columnDefinition = "TEXT")
    private String content;
}
