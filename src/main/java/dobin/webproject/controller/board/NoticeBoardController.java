package dobin.webproject.controller.board;

import dobin.webproject.entity.board.NoticeBoard;
import dobin.webproject.repository.NoticeBoardRepository;
import dobin.webproject.service.BoardService;
import dobin.webproject.validator.NoticeBoardValidator;
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
public class NoticeBoardController {

    private final NoticeBoardRepository boardRepository;
    private final NoticeBoardValidator boardValidator;
    private final BoardService boardService;

    @GetMapping("/noticeList")
    public String list(Model model, @PageableDefault(size = 10) Pageable pageable,
                       @RequestParam(required = false, defaultValue = "") String searchText) {
        //Page<Board> boards = boardRepository.findAll(pageable);
        Page<NoticeBoard> noticeBoards = boardRepository.findByTitleContainingOrContentContainingOrderByIdDesc(searchText, searchText, pageable);
        int startPage = Math.max(1, noticeBoards.getPageable().getPageNumber() - 4);
        int endPage = Math.min(noticeBoards.getTotalPages(), noticeBoards.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("noticeBoards", noticeBoards);
        return "board/noticeList";
    }

    @GetMapping("/noticeForm")
    public String form(Model model, @RequestParam(required = false) Long id) {
        if (id == null) {
            model.addAttribute("noticeBoard", new NoticeBoard());
        } else {
            NoticeBoard noticeBoard = boardRepository.findById(id).orElse(null);
            model.addAttribute("noticeBoard", noticeBoard);
        }
        return "board/noticeForm";
    }

    @PostMapping("/noticeForm")
    public String form(@Valid NoticeBoard board, BindingResult bindingResult, Authentication authentication) {
        boardValidator.validate(board, bindingResult);
        if (bindingResult.hasErrors()) {
            return "board/noticeForm";
        }
        String name = authentication.getName();
        boardService.noticeSave(name, board);
        //boardRepository.save(board);
        return "redirect:/board/noticeList";
    }
}