package dobin.webproject.dto;

import dobin.webproject.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemSearchDto {

    /* 1-1. 검색 타입(현재 시간과 상품 등록일 비교)으로 상품 데이터를 조회
     *  - all : 상품 등록일 전체
     *  - 1d : 최근 하루 동안 등록된 상품
     *  - 1w : 최근 일주일 동안 등록된 상품
     *  - 1m : 최근 한달 동안 등록된 상품
     *  - 6m : 최근 6개월 동안 등록된 상품
     */
    private String searchDateType;

    // 1-2. 상품의 판매 상태를 기준으로 상품 에디터 조회
    private ItemSellStatus searchSellStatus;

    /* 1-3. 상품을 조회할 때 어떤 유형으로 조회할지 선택.
            - itemNm : 상품명
            - createBy : 상품 등록자 아이디
    */
    private String searchBy;

    // 1-4. 조회할 검색어를 저장할 변수, searchBy가 itemNm일 경우 상품명을 기준으로 검색하고, createdBy일 경우 상품 등록자 ID 기준으로 검색합니다.
    private String searchQuery = "";
}
