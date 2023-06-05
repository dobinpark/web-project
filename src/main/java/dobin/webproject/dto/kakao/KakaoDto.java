package dobin.webproject.dto.kakao;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class KakaoDto {

    private Long id;
    private String placeName;
    private String phone;
    private String roadAddressName;
    private String placeUrl;
    private String x;
    private String y;

    public KakaoDto(Long id, String placeName, String phone, String roadAddressName, String placeUrl, String x, String y) {
        this.id = id;
        this.placeName = placeName;
        this.phone = phone;
        this.roadAddressName = roadAddressName;
        this.placeUrl = placeUrl;
        this.x = x;
        this.y = y;
    }
}
