package dobin.webproject.entity;

import dobin.webproject.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // 1-1. DB 명령어 중 정렬할 때 사용하는 "order"라는 키워드가 있기 때문에 Order 엔티티에 매핑되는 테이블로 orders를 만든다.
@Getter @Setter
public class Order extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; // 주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // 주문상태

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL
            ,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    // 1-1. 주문 상품 정보를 담아주는 메소드
    public void addOrderItem(OrderItem orderItem) {
        // 1-2. orderItems에 주문 상품 정보를 담는다.
        orderItems.add(orderItem);
        // 1-3. Order 엔티티와 OrderItem 엔티니는 양방향 관계이므로 orderItem 객체에서도 order를 참조할 수 있게 order 객체를 세팅
        orderItem.setOrder(this);
    }

    // 2-1. 주문 생성 메소드
    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order();

        // 2-2. 상품을 주문한 회원 정보를 세팅
        order.setMember(member);

        // 2-3. 여러개를 주문 할 수도 있으므로 리스트 형태로 값을 받아서 세팅
        for (OrderItem orderItem : orderItemList) {
            // 2-4. 주문 엔티티에 OrderItem(주문 상품) 세팅
            order.addOrderItem(orderItem);
        }

        // 2-5. 주문 상태 ORDER로 세팅
        order.setOrderStatus(OrderStatus.ORDER);

        // 2-6. 주문 시간을 현재 시간으로 세팅
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    // 3-1. 총 주문 금액을 구하는 메소드
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    // 4-1. 주문 취소 메소드
    public void cancelOrder() {

        // 4-2. Status
        this.orderStatus = OrderStatus.CANCEL;

        for (OrderItem orderItem : orderItems) {
            // 4-3. 상품이 여러개 일 경우 각각 취소
            orderItem.cancel();
        }
    }
}
