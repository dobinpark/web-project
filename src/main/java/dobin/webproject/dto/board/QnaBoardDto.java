package dobin.webproject.dto.board;

import dobin.webproject.entity.board.QnaBoard;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString   // 객체가 가지고 있는 정보나 값들을 문자열로 만들어 리턴하는 메서드
@NoArgsConstructor  // 인자 없이 객체 생성 가능
public class QnaBoardDto {

    private Long id;
    private String writer;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public QnaBoard toEntity(){
        QnaBoard qnaBoardDto = QnaBoard.builder()
                .id(id)
                .writer(writer)
                .title(title)
                .content(content)
                .build();
        return qnaBoardDto;
    }

    @Builder
    public QnaBoardDto(Long id, String title, String content, String writer, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
