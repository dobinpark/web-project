package dobin.webproject.service;

import dobin.webproject.dto.board.FreeBoardDto;
import dobin.webproject.dto.board.NoticeBoardDto;
import dobin.webproject.entity.board.FreeBoard;
import dobin.webproject.entity.board.NoticeBoard;
import dobin.webproject.repository.board.FreeBoardRepository;
import dobin.webproject.repository.board.NoticeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeBoardService {

    private final NoticeBoardRepository noticeBoardRepository;

    private static final int BLOCK_PAGE_NUM_COUNT = 5; // 블럭에 존재하는 페이지 번호 수
    private static final int PAGE_POST_COUNT = 4; // 한 페이지에 존재하는 게시글 수

    // Entity -> Dto로 변환
    private NoticeBoardDto convertEntityToDto(NoticeBoard board) {
        return NoticeBoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .createdDate(board.getRegTime())
                .modifiedDate(board.getUpdateTime())
                .build();
    }

    @Transactional(readOnly = true)
    public List<NoticeBoardDto> getBoardlist(Integer pageNum) {

        PageRequest pageRequest = PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by("regTime").descending());
        Page<NoticeBoard> page = noticeBoardRepository.findAll(pageRequest);
        List<NoticeBoardDto> boardDtoList = page.map(this::convertEntityToDto).getContent();

        /*List<Board> boardEntities = page.getContent();
        List<BoardDto> boardDtoList = new ArrayList<>();

        for (Board board : boardEntities) {
            boardDtoList.add(this.convertEntityToDto(board));
        }*/

        return boardDtoList;
    }

    @Transactional
    public NoticeBoardDto getPost(Long id) {
        // Optional : NPE(NullPointerException) 방지
        Optional<NoticeBoard> boardWrapper = noticeBoardRepository.findById(id);
        NoticeBoard board = boardWrapper.get();

        NoticeBoardDto boardDTO = NoticeBoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .createdDate(board.getRegTime())
                .modifiedDate(board.getUpdateTime())
                .build();

        return boardDTO;
    }

    @Transactional
    public Long savePost(NoticeBoardDto freeBoardDto) {
        return noticeBoardRepository.save(freeBoardDto.toEntity()).getId();
    }

    @Transactional
    public void deletePost(Long id) {
        noticeBoardRepository.deleteById(id);
    }

    // 검색 API
    @Transactional
    public List<NoticeBoardDto> searchPosts(String keyword) {
        List<NoticeBoard> boardEntities = noticeBoardRepository.findByTitleContaining(keyword);
        List<NoticeBoardDto> boardDtoList = new ArrayList<>();

        if (boardEntities.isEmpty()) return boardDtoList;

        for (NoticeBoard board : boardEntities) {
            boardDtoList.add(this.convertEntityToDto(board));
        }

        return boardDtoList;
    }

    // 페이징
    @Transactional
    public Long getBoardCount() {
        return noticeBoardRepository.count();
    }

    public Integer[] getPageList(Integer curPageNum) {
        Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];

        // 총 게시글 갯수
        Double postsTotalCount = Double.valueOf(this.getBoardCount());

        // 총 게시글 기준으로 계산한 마지막 페이지 번호 계산 (올림으로 계산)
        Integer totalLastPageNum = (int)(Math.ceil((postsTotalCount/PAGE_POST_COUNT)));

        // 현재 페이지를 기준으로 블럭의 마지막 페이지 번호 계산
        Integer blockLastPageNum = (totalLastPageNum > curPageNum + BLOCK_PAGE_NUM_COUNT)
                ? curPageNum + BLOCK_PAGE_NUM_COUNT
                : totalLastPageNum;

        // 페이지 시작 번호 조정
        curPageNum = (curPageNum <= 3) ? 1 : curPageNum - 2;

        // 페이지 번호 할당
        for (int val = curPageNum, idx = 0; val <= blockLastPageNum; val++, idx++) {
            pageList[idx] = val;
        }

        return pageList;
    }
}
