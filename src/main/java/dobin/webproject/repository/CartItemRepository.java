package dobin.webproject.repository;

import dobin.webproject.dto.CartDetailDto;
import dobin.webproject.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // 1-1. 장바구니 아이디와 상품 아이디를 이용해서 상품이 장바구니에 담겨있는지 조회하는 메소드
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    // 1-2. 장바구니 페이지에 전달할 CartDetailDto 리스트를 쿼리 하나로 조회하는 JPQL
    @Query("select new dobin.webproject.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +
            "and im.repImgYn = 'Y' " +
            "order by ci.regTime desc"
    )
    List<CartDetailDto> findCartDetailDtoList(@Param("cartId") Long cartId);
}
