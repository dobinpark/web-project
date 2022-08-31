package dobin.webproject.entity.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dobin.webproject.entity.BaseEntity;
import dobin.webproject.entity.Member;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@Table(name = "qna_board")
public class QnaBoard extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title")
    @Size(min=2, max=30, message = "제목은 2자이상 30자 이하입니다.")
    private String title;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;
}
