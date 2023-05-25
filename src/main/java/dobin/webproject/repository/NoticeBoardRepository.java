package dobin.webproject.repository;

import dobin.webproject.entity.board.NoticeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long> {

    List<NoticeBoard> findByTitle(String title);
    List<NoticeBoard> findByTitleOrContent(String title, String content);
    Page<NoticeBoard> findByTitleContainingOrContentContainingOrderByIdDesc(String title, String content, Pageable pageable);
}
