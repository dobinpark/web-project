package dobin.webproject.repository;

import dobin.webproject.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // 1-1. 현재 로그인한 회원의 장바구니(Cart) 엔티티를 찾기 위해 쿼리 메소드 추가
    Cart findByMemberId(Long memberId);
}
