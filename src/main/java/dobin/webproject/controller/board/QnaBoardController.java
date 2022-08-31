package dobin.webproject.controller.board;

import dobin.webproject.entity.board.QnaBoard;
import dobin.webproject.repository.QnaBoardRepository;
import dobin.webproject.service.BoardService;
import dobin.webproject.validator.QnaBoardValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class QnaBoardController {

    private final QnaBoardRepository boardRepository;
    private final QnaBoardValidator boardValidator;
    private final BoardService boardService;

    @GetMapping("/qnaList")
    public String list(Model model, @PageableDefault(size = 10) Pageable pageable,
                       @RequestParam(required = false, defaultValue = "") String searchText) {
        //Page<Board> boards = boardRepository.findAll(pageable);
        Page<QnaBoard> qunBoards = boardRepository.findByTitleContainingOrContentContainingOrderByIdDesc(searchText, searchText, pageable);
        int startPage = Math.max(1, qunBoards.getPageable().getPageNumber() - 4);
        int endPage = Math.min(qunBoards.getTotalPages(), qunBoards.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("qunBoards", qunBoards);
        return "board/qnaList";
    }

    @GetMapping("/qnaForm")
    public String form(Model model, @RequestParam(required = false) Long id) {
        if (id == null) {
            model.addAttribute("qnaBoard", new QnaBoard());
        } else {
            QnaBoard qnaBoard = boardRepository.findById(id).orElse(null);
            model.addAttribute("qnaBoard", qnaBoard);
        }
        return "board/qnaForm";
    }

    @PostMapping("/qnaForm")
    public String form(@Valid QnaBoard board, BindingResult bindingResult, Authentication authentication) {
        boardValidator.validate(board, bindingResult);
        if (bindingResult.hasErrors()) {
            return "board/qnaForm";
        }
        String name = authentication.getName();
        boardService.qnaSave(name, board);
        boardRepository.save(board);
        return "redirect:/board/qnaList";
    }
}
