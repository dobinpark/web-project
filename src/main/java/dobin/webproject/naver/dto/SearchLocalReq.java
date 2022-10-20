package dobin.webproject.naver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchLocalReq {

    // 지역 검색 요청 변수에 대한 변수 생성
    private String query = "";       // 검색을 원하는 문자열로서 UTF-8로 인코딩한다.
    private int display = 1;         // 검색 결과 출력 건수 지정(1 ~ 5)
    private int  start = 1;          // 검색 시작 위치로 1만 가능
    private String sort = "random";  // 정렬 옵션: random(유사도순), comment(카페/블로그 리뷰 개수 순)

    public MultiValueMap<String, String> toMultiValueMap() {

        var map = new LinkedMultiValueMap<String, String>();

        map.add("query", query);
        map.add("display", String.valueOf(display));
        map.add("start", String.valueOf(start));
        map.add("sort", sort);

        return map;
    }
}
