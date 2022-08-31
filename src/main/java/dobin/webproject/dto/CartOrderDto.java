package dobin.webproject.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CartOrderDto {

    private Long cartItemId;

    // 1-1. 장바구니에서 여러 개의 상품을 주문하므로 CartOrderDto 클래스가 자시 자신을 List로 가지고록 하기 위해 생성.
    private List<CartOrderDto> cartOrderDtoList;
}
