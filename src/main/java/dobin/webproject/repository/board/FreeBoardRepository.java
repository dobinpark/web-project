package dobin.webproject.repository.board;

import dobin.webproject.entity.Member;
import dobin.webproject.entity.board.FreeBoard;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long> {

    List<FreeBoard> findByTitleContaining(String keyword);
    FreeBoard findByUserName(Member userName);
    List<FreeBoard> findAll(Sort sort);
}