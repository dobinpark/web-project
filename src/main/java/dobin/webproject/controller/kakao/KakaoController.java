package dobin.webproject.controller.kakao;

import com.fasterxml.jackson.core.JsonProcessingException;
import dobin.webproject.dto.kakao.KakaoDto;
import dobin.webproject.entity.kakao.KakaoEntity;
import dobin.webproject.repository.kakao.KakaoRepository;
import dobin.webproject.service.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;
    private final KakaoRepository kakaoRepository;

    @GetMapping("/search")
    public String searchPlace() {
        return "food/search";
    }

    @GetMapping("/places")
    public String placeList(@RequestParam String search, Model model) throws JsonProcessingException {
        List<KakaoDto> palces = kakaoService.convertToKakaoDto(search);
        boolean isDataExist = kakaoService.checkIfDataExist();

        if (isDataExist) {
            kakaoService.deleteAllPlaces();
        }

        kakaoService.saveAllPlaces(palces);

        List<KakaoEntity> entities = kakaoService.convertToKakaoEntity(palces);

        model.addAttribute("places", entities);
        return "food/wishList";
    }

    @GetMapping("/map")
    public String map(Model model) {
        List<KakaoEntity> entities = kakaoService.getAll();
        model.addAttribute("places", entities);
        return "food/maps";
    }
}
