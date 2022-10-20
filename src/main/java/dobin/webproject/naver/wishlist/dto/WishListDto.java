package dobin.webproject.naver.wishlist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// DB의 엔티티가  변경이 되면 프론트엔드까지도 변수 명에도 영향을 끼치기 때문에
// 변화하는 과정만 있으면 되기에 위험성을 없애기 위해서 Entity랑 분리해서 만든다
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WishListDto {

    private int index;
    private String title;                   // 음식명, 장소명
    private String category;                // 카테고리
    private String address;                 // 주소
    private String roadAddress;             // 도로명
    private String homePageLink;            // 홈페이지 주소
    private String imageLink;               // 음식, 가게 이미지 주소
    private boolean isVisit;                // 방문 여부
    private int visitCount;                 // 방문 횟수
    private LocalDateTime lastVisitDate;    // 마지막 방문 일자
}
