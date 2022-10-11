package dobin.webproject.controller.board;

import dobin.webproject.entity.board.NoticeBoard;
import dobin.webproject.repository.NoticeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NoticeApiController {

    private final NoticeBoardRepository repository;

    @GetMapping("/noticeBoards")
    List<NoticeBoard> all(@RequestParam(required = false, defaultValue = "") String title,
                          @RequestParam(required = false, defaultValue = "") String content) {
        if(StringUtils.isEmpty(title) && StringUtils.isEmpty(content)) {
            return repository.findAll();
        } else {
            return repository.findByTitleOrContent(title, content);
        }
    }

    @PostMapping("/noticeBoards")
    NoticeBoard newBoard(@RequestBody NoticeBoard newBoard) {
        return repository.save(newBoard);
    }

    // Single item
    @GetMapping("/noticeBoards/{id}")
    NoticeBoard one(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping("/noticeBoards/{id}")
    NoticeBoard replaceBoard(@RequestBody NoticeBoard newBoard, @PathVariable Long id) {

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

    @DeleteMapping("/noticeBoards/{id}")
    void deleteBoard(@PathVariable Long id) {
        repository.deleteById(id);
    }
}