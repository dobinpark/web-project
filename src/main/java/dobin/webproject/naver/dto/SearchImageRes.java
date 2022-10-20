package dobin.webproject.naver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchImageRes {

    // 지역 검색 출력 결과를 변수화
    private String lastBuildDate;        // 검색 결과를 생성한 시간이다.
    private int total;                   //  검색 결과 문서의 총 개수를 의미한다.
    private int start;                   // 검색 결과 문서 중, 문서의 시작점을 의미한다.
    private int display;                 // 검색된 검색 결과의 개수이다.
    private List<SearchLocalItem> items; // XML 포멧에서는 item 태그로, JSON 포멧에서는 items 속성으로 표현된다. 개별 검색 결과이며 title, link, description, address, mapx, mapy를 포함한다.

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchLocalItem {
        private String title;      // 검색 결과 업체, 기관명을 나타낸다.
        private String link;       // 검색 결과 업체, 기관의 상세 정보가 제공되는 네이버 페이지의 하이퍼텍스트 link를 나타낸다.
        private String thumbnail;  // 검색 결과 이미지의 썸네일 link를 나타낸다.
        private String sizeheight; // 검색 결과 이미지의 썸네일 높이를 나타낸다.
        private String sizewidth;  // 검색 결과 이미지의 너비를 나타낸다. 단위는 pixel이다.
    }
}
