package dobin.webproject.repository.board;

import dobin.webproject.entity.Member;
import dobin.webproject.entity.board.QnaBoard;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnaBoardRepository extends JpaRepository<QnaBoard, Long> {
    List<QnaBoard> findByTitleContaining(String keyword);
    QnaBoard findByUserName(Member userName);
    List<QnaBoard> findAll(Sort sort);
}