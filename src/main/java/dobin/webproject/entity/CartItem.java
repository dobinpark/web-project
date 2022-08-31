package dobin.webproject.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "cart_item")
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;

    // 1-1. 장바구니(Cart)에 상품(Item)을 담을 장바구니 상품(CartItem) 엔티티를 생성하는 메소드
    public static CartItem createCartItem(Cart cart, Item item, int count) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;
    }

    // 1-2. 장바구니에 기존에 담겨 있는 상품이였는데, 해당 상품의 수량을 변경해 장바구니에 담을 때 사용될 메소드(더티 체킹)
    public void addCount(int count) {
        this.count += count;
    }

    // 1-3. 장바구니 상품 수량 변경하기
    public void updateCount(int count){
        this.count = count;
    }
}
