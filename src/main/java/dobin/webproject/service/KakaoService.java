package dobin.webproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dobin.webproject.dto.kakao.KakaoDto;
import dobin.webproject.entity.kakao.KakaoEntity;
import dobin.webproject.repository.kakao.KakaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class KakaoService {

    private final KakaoRepository kakaoRepository;

    public List<KakaoDto> convertToKakaoDto(String search) throws JsonProcessingException {

        // 카카오 API 호출하여 검색 결과를 받아옴
        String apiUrl = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + search;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK 6440998dba5d9ea71af03fba22c0046b");
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

        // 검색 결과를 리스트로 변환하여 저장
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        List<KakaoDto> kakaoList = new ArrayList<>();
        if (root.has("documents")) {
            JsonNode documents = root.get("documents");
            for (JsonNode document : documents) {
                Long id = Long.valueOf(document.get("id").asText());
                String placeName = document.get("place_name").asText();
                String phone = document.get("phone").asText();
                String roadAddressName = document.get("road_address_name").asText();
                String placeUrl = document.get("place_url").asText();
                String x = document.get("x").asText();
                String y = document.get("y").asText();
                KakaoDto list = new KakaoDto(id, placeName, phone, roadAddressName, placeUrl, x, y);
                kakaoList.add(list);
            }
        }
        return kakaoList;
    }

    public List<KakaoEntity> convertToKakaoEntity(List<KakaoDto> dtos) {
        return dtos.stream()
                .map(dto -> new KakaoEntity(dto.getId(), dto.getPlaceName(), dto.getPhone(),
                        dto.getRoadAddressName(), dto.getPlaceUrl(), dto.getX(), dto.getY()))
                .collect(Collectors.toList());
    }

    public void saveAllPlaces(List<KakaoDto> dtos) {
        List<KakaoEntity> entities = convertToKakaoEntity(dtos);
        kakaoRepository.saveAll(entities);
    }

    public List<KakaoEntity> getAll() {
        return kakaoRepository.findAll();
    }

    public boolean checkIfDataExist() {
        long count = kakaoRepository.count();
        return count > 0;
    }

    public void deleteAllPlaces() {
        kakaoRepository.deleteAll();
    }

    /*public void saveReservation(KakaoReservationDto kakaoReservationDto) {

        KakaoReservationEntity kakaoReservationEntity = new KakaoReservationEntity();
        kakaoReservationEntity.setId(kakaoReservationDto.getId());
        kakaoReservationEntity.setPxName(kakaoReservationDto.getPxName());
        kakaoReservationEntity.setTxtList(kakaoReservationDto.getTxtList());
        kakaoReservationEntity.setSelectedPlaceName(kakaoReservationDto.getSelectedPlaceName());

        // DB에 저장합니다.
        kakaoReservationRepository.save(kakaoReservationEntity);
    }*/
}
