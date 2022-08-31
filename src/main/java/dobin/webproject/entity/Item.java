package dobin.webproject.entity;

import dobin.webproject.constant.ItemSellStatus;
import dobin.webproject.dto.ItemFormDto;
import dobin.webproject.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@Getter @Setter
@ToString
public class Item extends BaseEntity {

    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // 1-1. 상품 코드(PK)

    @Column(nullable = false, length = 50)
    private String itemNm; // 1-2. 상품 명

    @Column(name="price", nullable = false)
    private int price; // 1-3. 가격

    @Column(nullable = false)
    private int stockNumber; // 1-4. 재고 수량

    @Lob // BLOB, COLOB 타입 매핑
    @Column(nullable = false)
    private String itemDetail; // 1-5. 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 1-6. 상품 판매 상태(SELL, SOLD_OUT)

    // 1-7. 상품 업데이트(더티 체킹 이용)
    public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    // 8-1. 상품 주문시 재고 감소시키는 기능
    public void removeStock(int stockNumber) {
        // 8-2. 남은 재고 = 현재 재고 - 주문 수량
        int restStock = this.stockNumber - stockNumber;
        // 8-3. 남은 재고가 0보다 작을 시
        if (restStock < 0) {
            //상품의 재고가 주문 수량보다 작을 경우 재고 부족 예외를 발생시킵니다.
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량 : " + this.stockNumber + ")");
        }
        //주문후 남은 재고 수량을 상품의 현재 재고 값으로 할당.
        this.stockNumber = restStock;
    }

    // 9-1. 상품의 재고를 증가시키는 메소드
    public void addStock(int stockNumber) {
        // 9-2. 주문했던 수량만큼 다시 증가시켜준다.
        this.stockNumber += stockNumber;
    }
}
