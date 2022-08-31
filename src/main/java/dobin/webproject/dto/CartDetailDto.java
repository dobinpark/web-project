package dobin.webproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartDetailDto {

    // 1-1. 장바구니 상품 아이디
    private Long cartItemId;

    // 1-2. 상품명
    private String itemNm;

    // 1-3. 상품 금액
    private int price;

    // 1-4. 수량
    private int count;

    // 1-5. 상품 이미지 경로
    private String imgUrl;

    // 1-6. 생성자, 장바구니 페이지에 전달할 데이터를 생성자의 파라미터로 만들어줌
    public CartDetailDto(Long cartItemId, String itemNm, int price, int count, String imgUrl) {
        this.cartItemId = cartItemId;
        this.itemNm = itemNm;
        this.price = price;
        this.count = count;
        this.imgUrl = imgUrl;
    }
}
