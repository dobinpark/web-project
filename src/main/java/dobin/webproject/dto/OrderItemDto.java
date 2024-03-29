package dobin.webproject.dto;

import dobin.webproject.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemDto {

    private String itemNm;

    private int count;

    private int orderPrice;

    public OrderItemDto(OrderItem orderItem) {
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
    }
}