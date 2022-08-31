package dobin.webproject.dto;

import dobin.webproject.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemDto {

    // 1-1. 상품명
    private String itemNm;

    // 1-2. 주문 수량
    private int count;

    // 1-3. 주문 금액
    private int orderPrice;

    // 1-4. 상품 이미지 경로
    private String imgUrl;

    // 1-5. OrderItemDto 클래스의 생성자로 orderItem 객체와 이미지 경로를 파라미터로 받아서 멤버 변수 값을 세팅
    public OrderItemDto(OrderItem orderItem, String imgUrl) {
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }
}
