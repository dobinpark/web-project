package dobin.webproject.controller.board;

import dobin.webproject.entity.board.FreeBoard;
import dobin.webproject.repository.FreeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FreeBoardApiController {

    private final FreeBoardRepository repository;

    @GetMapping("/freeBoards")
    List<FreeBoard> all(@RequestParam(required = false, defaultValue = "") String title,
                        @RequestParam(required = false, defaultValue = "") String content) {
        if(StringUtils.isEmpty(title) && StringUtils.isEmpty(content)) {
            return repository.findAll();
        } else {
            return repository.findByTitleOrContent(title, content);
        }
    }

    @PostMapping("/freeBoards")
    FreeBoard newBoard(@RequestBody FreeBoard newBoard) {
        return repository.save(newBoard);
    }

    // Single item
    @GetMapping("/freeBoards/{id}")
    FreeBoard one(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping("/freeBoards/{id}")
    FreeBoard replaceBoard(@RequestBody FreeBoard newBoard, @PathVariable Long id) {

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

    @DeleteMapping("/freeBoards/{id}")
    void deleteBoard(@PathVariable Long id) {
        repository.deleteById(id);
    }
}