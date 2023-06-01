package dobin.webproject.repository.board;

import dobin.webproject.entity.Member;
import dobin.webproject.entity.board.NoticeBoard;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long> {
    List<NoticeBoard> findByTitleContaining(String keyword);
    NoticeBoard findByUserName(Member userName);
    List<NoticeBoard> findAll(Sort sort);
}
