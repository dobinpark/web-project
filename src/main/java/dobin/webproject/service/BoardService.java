package dobin.webproject.service;

import dobin.webproject.entity.Member;
import dobin.webproject.entity.board.FreeBoard;
import dobin.webproject.entity.board.NoticeBoard;
import dobin.webproject.entity.board.QnaBoard;
import dobin.webproject.repository.FreeBoardRepository;
import dobin.webproject.repository.MemberRepository;
import dobin.webproject.repository.NoticeBoardRepository;
import dobin.webproject.repository.QnaBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final FreeBoardRepository freeBoardRepository;
    private final NoticeBoardRepository noticeBoardRepository;
    private final QnaBoardRepository qnaBoardRepository;

    private final MemberRepository memberRepository;

    public NoticeBoard noticeSave(String name, NoticeBoard board) {
        Member member = memberRepository.findByName(name);
        board.setMember(member);
        return noticeBoardRepository.save(board);
    }

    public FreeBoard freeSave(String name, FreeBoard board) {
        Member member = memberRepository.findByName(name);
        board.setMember(member);
        return freeBoardRepository.save(board);
    }

    public QnaBoard qnaSave(String name, QnaBoard board) {
        Member member = memberRepository.findByName(name);
        board.setMember(member);
        return qnaBoardRepository.save(board);
    }
}
