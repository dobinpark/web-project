package dobin.webproject.entity.board;

import dobin.webproject.entity.BaseEntity;
import dobin.webproject.entity.Member;
import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.Size;

@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 외부에서의 생성을 열어 둘 필요가 없을 때 / 보안적으로 권장된다.
@Getter
@Entity
@Table(name = "qna_board")  // 이거 보고 테이블 생성
public class QnaBoard extends BaseEntity {

    @Id // PK Field
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // PK의 생성 규칙
    @Column(name = "qna_id")
    private Long id;

    @Column(name = "qna_writer", length = 10, nullable = false)
    private String writer;

    @Column(name = "qna_title", length = 100, nullable = false)
    @Size(min=2, max=30, message = "제목은 2자이상 30자 이하입니다.")
    private String title;

    @Column(name = "qna_content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    private Member userName;

    // Java 디자인 패턴, 생성 시점에 값을 채워줌
    @Builder
    public QnaBoard(Long id, String title, String content, String writer, Member userName) {
        // Assert 구문으로 안전한 객체 생성 패턴을 구현
        Assert.hasText(writer, "writer must not be empty");
        Assert.hasText(title, "title must not be empty");
        Assert.hasText(content, "content must not be empty");

        this.id = id;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.userName = userName;
    }
}
