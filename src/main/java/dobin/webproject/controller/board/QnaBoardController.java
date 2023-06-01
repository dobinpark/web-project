package dobin.webproject.controller.board;

import dobin.webproject.dto.board.QnaBoardDto;
import dobin.webproject.service.QnaBoardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/board")
public class QnaBoardController {

    private QnaBoardService qnaBoardService;

    // 게시글 목록
    // list 경로로 GET 메서드 요청이 들어올 경우 list 메서드와 맵핑시킴
    // list 경로에 요청 파라미터가 있을 경우 (?page=1), 그에 따른 페이지네이션을 수행함.
    @GetMapping("/qnaList")
    public String list(Model model, @RequestParam(value="page", defaultValue = "1") Integer pageNum) {
        List<QnaBoardDto> boardList = qnaBoardService.getBoardlist(pageNum);
        Integer[] pageList = qnaBoardService.getPageList(pageNum);

        model.addAttribute("boardList", boardList);
        model.addAttribute("pageList", pageList);

        return "board/qnaList";
    }

    // 글쓰는 페이지
    @GetMapping("/qnaForm")
    public String write() {
        return "board/qnaForm";
    }

    // 글을 쓴 뒤 POST 메서드로 글 쓴 내용을 DB에 저장
    // 그 후에는 /list 경로로 리디렉션해준다.
    @PostMapping("/qnaForm")
    public String write(QnaBoardDto boardDto) {
        qnaBoardService.savePost(boardDto);
        return "redirect:/board/qnaList";
    }

    // 게시물 상세 페이지이며, {no}로 페이지 넘버를 받는다.
    // PathVariable 애노테이션을 통해 no를 받음
    @GetMapping("/qnaForm/{no}")
    public String detail(@PathVariable("no") Long no, Model model) {
        QnaBoardDto boardDTO = qnaBoardService.getPost(no);

        model.addAttribute("boardDto", boardDTO);
        return "board/qnaView";
    }

    // 게시물 수정 페이지이며, {no}로 페이지 넘버를 받는다.
    @GetMapping("/qnaForm/qnaEdit/{no}")
    public String edit(@PathVariable("no") Long no, Model model) {
        QnaBoardDto boardDTO = qnaBoardService.getPost(no);

        model.addAttribute("boardDto", boardDTO);
        return "board/qnaUpdate";
    }

    // 위는 GET 메서드이며, PUT 메서드를 이용해 게시물 수정한 부분에 대해 적용
    @PutMapping("/qnaForm/qnaEdit/{no}")
    public String update(QnaBoardDto boardDTO) {
        qnaBoardService.savePost(boardDTO);

        return "redirect:/board/qnaList";
    }

    // 게시물 삭제는 deletePost 메서드를 사용하여 간단하게 삭제할 수 있다.
    @DeleteMapping("/qnaForm/{no}")
    public String delete(@PathVariable("no") Long no) {
        qnaBoardService.deletePost(no);

        return "redirect:/board/qnaList";
    }

    // 검색
    // keyword를 view로부터 전달 받고
    // Service로부터 받은 boardDtoList를 model의 attribute로 전달해준다.
    @GetMapping("/qnaSearch")
    public String search(@RequestParam(value="keyword") String keyword, Model model) {
        List<QnaBoardDto> boardDtoList = qnaBoardService.searchPosts(keyword);

        model.addAttribute("boardList", boardDtoList);

        return "board/qnaList";
    }
}