package dobin.webproject.entity.kakao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Builder
@Table(name = "kakao")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoEntity {

    @Id
    @Column(name = "id", length = 15, nullable = false)
    private Long id;

    @Column(name = "place_name", length = 100, nullable = false)
    private String placeName;

    @Column(name = "phone", length = 20, nullable = false)
    private String phone;

    @Column(name = "road_address_name", length = 200, nullable = false)
    private String roadAddressName;

    @Column(name = "place_url", length = 100, nullable = false)
    private String placeUrl;

    @Column(name = "x", length = 100, nullable = false)
    private String x;

    @Column(name = "y", length = 100, nullable = false)
    private String y;
}
