package ott_service.favorite_ott.domain.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @Column(length = 200)
    private String subject;

    // columnDefinition = "TEXT" : 텍스트를 열 데이터로 넣을 수 있음을 의미하고 글자 수를 제한할 수 없는 경우에 사용한다.
    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;
    // 하나의 질문에 답변이 여러개일수 있다.
    // cascade = CascadeType.REMOVE : 질문을 삭제하면 그에 달린 답변들도 모두 삭제된다.

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member1 author; // 로그인 사용자


    private LocalDateTime modifyDate;

}
