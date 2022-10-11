package dobin.webproject.controller.board;

import dobin.webproject.entity.board.FreeBoard;
import dobin.webproject.repository.FreeBoardRepository;
import dobin.webproject.service.BoardService;
import dobin.webproject.validator.FreeBoardValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class FreeBoardController {

    private final FreeBoardRepository boardRepository;
    private final FreeBoardValidator boardValidator;
    private final BoardService boardService;

    @GetMapping("/freeList")
    public String list(Model model, @PageableDefault(size = 10) Pageable pageable,
                       @RequestParam(required = false, defaultValue = "") String searchText) {
        //Page<Board> boards = boardRepository.findAll(pageable);
        Page<FreeBoard> freeBoards = boardRepository.findByTitleContainingOrContentContainingOrderByIdDesc(searchText, searchText, pageable);
        int startPage = Math.max(1, freeBoards.getPageable().getPageNumber() - 4);
        int endPage = Math.min(freeBoards.getTotalPages(), freeBoards.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("freeBoards", freeBoards);
        return "board/freeList";
    }

    @GetMapping("/freeForm")
    public String form(Model model, @RequestParam(required = false) Long id) {
        if (id == null) {
            model.addAttribute("freeBoard", new FreeBoard());
        } else {
            FreeBoard freeBoard = boardRepository.findById(id).orElse(null);
            model.addAttribute("freeBoard", freeBoard);
        }
        return "board/freeForm";
    }

    @PostMapping("/freeForm")
    public String form(@Valid FreeBoard board, BindingResult bindingResult, Authentication authentication) {
        boardValidator.validate(board, bindingResult);
        if (bindingResult.hasErrors()) {
            return "board/freeForm";
        }
        String name = authentication.getName();
        boardService.freeSave(name, board);
        //boardRepository.save(board);
        return "redirect:/board/freeList";
    }
}