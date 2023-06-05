package dobin.webproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartDetailDto {

    private Long cartItemId;

    private String itemNm;

    private int price;

    private int count;


    public CartDetailDto(Long cartItemId, String itemNm, int price, int count) {
        this.cartItemId = cartItemId;
        this.itemNm = itemNm;
        this.price = price;
        this.count = count;
    }
}