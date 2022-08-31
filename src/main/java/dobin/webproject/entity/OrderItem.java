package dobin.webproject.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderItem extends BaseEntity {

    @Id
    @Column(name = "order_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문가격

    private int count; // 수량

    // 1-1. 주문할 상품(Item)과 주문 수량(count)을 가지고 주문 상품(OrderItem) 객체를 만드는 메소드
    public static OrderItem createOrderItem(Item item, int count) {
        // 1-1. 주문할 상품(Item)과 주문 수량(count)을 가지고 주문 상품(OrderItem) 객체를 만드는 메소드
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);

        // 1-3. 상품 가격을 주문 가격으로 세팅.
        orderItem.setOrderPrice(item.getPrice());

        // 1-4. 상품에 주문 수량을 전달해 재고를 감소시킨다.(더티체킹)
        item.removeStock(count);

        return orderItem;
    }

    // 2-1. 총 주문 가격 계산 메소드
    public int getTotalPrice() {
        // 2-2. 1-1에서 세팅해 놓은걸로 총 주문 가격 구해서 리턴.
        return orderPrice * count;
    }

    // 3-1. 취소시 주문 아이템에 있는 count를 수를 다시 증가시켜줌.
    public void cancel() {
        this.getItem().addStock(count);
    }
}
