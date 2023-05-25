package dobin.webproject.repository;

import dobin.webproject.entity.board.FreeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long> {

    List<FreeBoard> findByTitle(String title);
    List<FreeBoard> findByTitleOrContent(String title, String content);
    Page<FreeBoard> findByTitleContainingOrContentContainingOrderByIdDesc(String title, String content, Pageable pageable);
}