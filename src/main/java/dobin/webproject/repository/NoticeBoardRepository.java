package dobin.webproject.repository;

import dobin.webproject.entity.board.NoticeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long> {

    List<NoticeBoard> findByTitle(String title);
    List<NoticeBoard> findByTitleOrContent(String title, String content);
    Page<NoticeBoard> findByTitleContainingOrContentContainingOrderByIdDesc(String title, String content, Pageable pageable);
}