package dobin.webproject.controller.board;

import dobin.webproject.dto.board.FreeBoardDto;
import dobin.webproject.service.FreeBoardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/board")
public class FreeBoardController {

    private FreeBoardService freeBoardService;

    // 게시글 목록
    // list 경로로 GET 메서드 요청이 들어올 경우 list 메서드와 맵핑시킴
    // list 경로에 요청 파라미터가 있을 경우 (?page=1), 그에 따른 페이지네이션을 수행함.
    @GetMapping("/freeList")
    public String list(Model model, @RequestParam(value="page", defaultValue = "1") Integer pageNum) {
        List<FreeBoardDto> boardList = freeBoardService.getBoardlist(pageNum);
        Integer[] pageList = freeBoardService.getPageList(pageNum);

        model.addAttribute("boardList", boardList);
        model.addAttribute("pageList", pageList);

        return "board/freeList";
    }

    // 글쓰는 페이지
    @GetMapping("/freeForm")
    public String write() {
        return "board/freeForm";
    }

    // 글을 쓴 뒤 POST 메서드로 글 쓴 내용을 DB에 저장
    // 그 후에는 /list 경로로 리디렉션해준다.
    @PostMapping("/freeForm")
    public String write(FreeBoardDto boardDto) {
        freeBoardService.savePost(boardDto);
        return "redirect:/board/freeList";
    }

    // 게시물 상세 페이지이며, {no}로 페이지 넘버를 받는다.
    // PathVariable 애노테이션을 통해 no를 받음
    @GetMapping("/freeForm/{no}")
    public String detail(@PathVariable("no") Long no, Model model) {
        FreeBoardDto boardDTO = freeBoardService.getPost(no);

        model.addAttribute("boardDto", boardDTO);
        return "board/freeView";
    }

    // 게시물 수정 페이지이며, {no}로 페이지 넘버를 받는다.
    @GetMapping("/freeForm/freeEdit/{no}")
    public String edit(@PathVariable("no") Long no, Model model) {
        FreeBoardDto boardDTO = freeBoardService.getPost(no);

        model.addAttribute("boardDto", boardDTO);
        return "board/freeUpdate";
    }

    // 위는 GET 메서드이며, PUT 메서드를 이용해 게시물 수정한 부분에 대해 적용
    @PutMapping("/freeForm/freeEdit/{no}")
    public String update(FreeBoardDto boardDTO) {
        freeBoardService.savePost(boardDTO);

        return "redirect:/board/freeList";
    }

    // 게시물 삭제는 deletePost 메서드를 사용하여 간단하게 삭제할 수 있다.
    @DeleteMapping("/freeForm/{no}")
    public String delete(@PathVariable("no") Long no) {
        freeBoardService.deletePost(no);

        return "redirect:/board/freeList";
    }

    // 검색
    // keyword를 view로부터 전달 받고
    // Service로부터 받은 boardDtoList를 model의 attribute로 전달해준다.
    @GetMapping("/freeSearch")
    public String search(@RequestParam(value="keyword") String keyword, Model model) {
        List<FreeBoardDto> boardDtoList = freeBoardService.searchPosts(keyword);

        model.addAttribute("boardList", boardDtoList);

        return "board/freeList";
    }
}

