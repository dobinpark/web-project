package dobin.webproject.repository;

import dobin.webproject.dto.CartDetailDto;
import dobin.webproject.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    @Query("select new dobin.webproject.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count) " +
            "from CartItem ci " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "order by ci.regTime desc"
    )
    List<CartDetailDto> findCartDetailDtoList(@Param("cartId") Long cartId);
}