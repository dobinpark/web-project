package dobin.webproject.repository.kakao;

import dobin.webproject.entity.kakao.KakaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KakaoRepository extends JpaRepository<KakaoEntity, Long> {
}
