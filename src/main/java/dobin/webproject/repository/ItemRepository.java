package dobin.webproject.repository;

import dobin.webproject.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

//1-7. QuerydslPredicateExecutor -> Predict란 '이 조건이 맞다'고 판단하는 근거를 함수로 제공하는 것이다. Repository에 Predicate를 파라미터로 전달하기 위해서 QueryDslPredicateExecutor 인터페이스를 상속 받는다.
public interface ItemRepository extends JpaRepository<Item, Long>,
        QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {

    // 1-1. 쿼리 메소드 -> find + (엔티티 명) + By + 변수이름
    List<Item> findByItemNm(String itemNm);

    // 1-2. 쿼리 메소드 OR 조건 처리하기
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    // 1-3. 쿼리 메소드 LessThan 조건 처리하기
    List<Item> findByPriceLessThan(Integer price);

    // 1-4. 쿼리 메소드 OrderBy로 정렬 처리하기
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    // 1-5. @Query를 이용한 검색 처리하기(JPQL)
    @Query("select i from Item i where i.itemDetail like " +
            "%:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    // 1-6. @Query - nativeQuery(기존의 DB에서 사용하던 쿼리를 그대로 사용하고 싶을 때)
    @Query(value="select * from item i where i.item_detail like " +
            "%:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);
}
