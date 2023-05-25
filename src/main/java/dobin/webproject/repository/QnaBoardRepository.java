package dobin.webproject.repository;

import dobin.webproject.entity.board.QnaBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnaBoardRepository extends JpaRepository<QnaBoard, Long> {

    List<QnaBoard> findByTitle(String title);
    List<QnaBoard> findByTitleOrContent(String title, String content);
    Page<QnaBoard> findByTitleContainingOrContentContainingOrderByIdDesc(String title, String content, Pageable pageable);
}