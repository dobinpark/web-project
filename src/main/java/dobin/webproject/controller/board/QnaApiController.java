package dobin.webproject.controller.board;

import dobin.webproject.entity.board.QnaBoard;
import dobin.webproject.repository.QnaBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QnaApiController {

    private final QnaBoardRepository repository;

    @GetMapping("/qnaBoards")
    List<QnaBoard> all(@RequestParam(required = false, defaultValue = "") String title,
                       @RequestParam(required = false, defaultValue = "") String content) {
        if(StringUtils.isEmpty(title) && StringUtils.isEmpty(content)) {
            return repository.findAll();
        } else {
            return repository.findByTitleOrContent(title, content);
        }
    }

    @PostMapping("/qnaBoards")
    QnaBoard newBoard(@RequestBody QnaBoard newBoard) {
        return repository.save(newBoard);
    }

    // Single item
    @GetMapping("/qnaBoards/{id}")
    QnaBoard one(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping("/qnaBoards/{id}")
    QnaBoard replaceBoard(@RequestBody QnaBoard newBoard, @PathVariable Long id) {

        return repository.findById(id)
                .map(board -> {
                    board.setTitle(newBoard.getTitle());
                    board.setContent(newBoard.getContent());
                    return repository.save(board);
                })
                .orElseGet(() -> {
                    newBoard.setId(id);
                    return repository.save(newBoard);
                });
    }

    @DeleteMapping("/qnaBoards/{id}")
    void deleteBoard(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
