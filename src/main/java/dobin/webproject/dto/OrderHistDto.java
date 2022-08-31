package dobin.webproject.dto;

import dobin.webproject.constant.OrderStatus;
import dobin.webproject.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class OrderHistDto {

    // 1-1. 주문 아이디
    private Long orderId;

    // 1-2. 주문 날짜
    private String orderDate;

    // 1-3. 주문 상태
    private OrderStatus orderStatus;

    // 1-4. 주문 상품 리스트
    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    // 1-5. 생성자
    public OrderHistDto(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    // 1-6. orderItemDto 객체를 주문 상품 리스트(1-4)에 추가하는 메소드
    public void addOrderItemDto(OrderItemDto orderItemDto) {
        orderItemDtoList.add(orderItemDto);
    }
}
