package dobin.webproject.naver.wishlist.repository;

import dobin.webproject.naver.db.MemoryDbRepositoryAbstract;
import dobin.webproject.naver.wishlist.entity.WishListEntity;
import org.springframework.stereotype.Repository;

@Repository // DB를 저장하는 곳이다라고 지정
public class WishListRepository extends MemoryDbRepositoryAbstract<WishListEntity> {
}
