package dobin.webproject.controller.board;

import dobin.webproject.dto.board.NoticeBoardDto;
import dobin.webproject.service.NoticeBoardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/board")
public class NoticeBoardController {

    private NoticeBoardService noticeBoardService;

    // 게시글 목록
    // list 경로로 GET 메서드 요청이 들어올 경우 list 메서드와 맵핑시킴
    // list 경로에 요청 파라미터가 있을 경우 (?page=1), 그에 따른 페이지네이션을 수행함.
    @GetMapping("/noticeList")
    public String list(Model model, @RequestParam(value="page", defaultValue = "1") Integer pageNum) {
        List<NoticeBoardDto> boardList = noticeBoardService.getBoardlist(pageNum);
        Integer[] pageList = noticeBoardService.getPageList(pageNum);

        model.addAttribute("boardList", boardList);
        model.addAttribute("pageList", pageList);

        return "board/noticeList";
    }

    // 글쓰는 페이지
    @GetMapping("/noticeForm")
    public String write() {
        return "board/noticeForm";
    }

    // 글을 쓴 뒤 POST 메서드로 글 쓴 내용을 DB에 저장
    // 그 후에는 /list 경로로 리디렉션해준다.
    @PostMapping("/noticeForm")
    public String write(NoticeBoardDto boardDto) {
        noticeBoardService.savePost(boardDto);
        return "redirect:/board/noticeList";
    }

    // 게시물 상세 페이지이며, {no}로 페이지 넘버를 받는다.
    // PathVariable 애노테이션을 통해 no를 받음
    @GetMapping("/noticeForm/{no}")
    public String detail(@PathVariable("no") Long no, Model model) {
        NoticeBoardDto boardDTO = noticeBoardService.getPost(no);

        model.addAttribute("boardDto", boardDTO);
        return "board/noticeView";
    }

    // 게시물 수정 페이지이며, {no}로 페이지 넘버를 받는다.
    @GetMapping("/noticeForm/noticeEdit/{no}")
    public String edit(@PathVariable("no") Long no, Model model) {
        NoticeBoardDto boardDTO = noticeBoardService.getPost(no);

        model.addAttribute("boardDto", boardDTO);
        return "board/noticeUpdate";
    }

    // 위는 GET 메서드이며, PUT 메서드를 이용해 게시물 수정한 부분에 대해 적용
    @PutMapping("/noticeForm/noticeEdit/{no}")
    public String update(NoticeBoardDto boardDTO) {
        noticeBoardService.savePost(boardDTO);

        return "redirect:/board/noticeList";
    }

    // 게시물 삭제는 deletePost 메서드를 사용하여 간단하게 삭제할 수 있다.
    @DeleteMapping("/noticeForm/{no}")
    public String delete(@PathVariable("no") Long no) {
        noticeBoardService.deletePost(no);

        return "redirect:/board/noticeList";
    }

    // 검색
    // keyword를 view로부터 전달 받고
    // Service로부터 받은 boardDtoList를 model의 attribute로 전달해준다.
    @GetMapping("/noticeSearch")
    public String search(@RequestParam(value="keyword") String keyword, Model model) {
        List<NoticeBoardDto> boardDtoList = noticeBoardService.searchPosts(keyword);

        model.addAttribute("boardList", boardDtoList);

        return "board/noticeList";
    }
}