package dobin.webproject.service;

import dobin.webproject.entity.ItemImg;
import dobin.webproject.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    /* 1-1. @Value 어노테이션(org.springframework.beans.factory.annotation.Value) ->
     *      application.properties에 설정한 "itemImgLocation" 프로퍼티 값을 읽어옴
     */
    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    // 1-2. 상품 이미지 저장
    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        // 1-3. 실제 파일 업로드
        if (!StringUtils.isEmpty(oriImgName)) {
            /* 1-4. itemName : 실제 로컬에 저장된 상품 이미지 파일 이름
             *    oriImgName : 업로드 했던 상품 이미지 파일의 원래 이름
             */
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());

            // 1-5. imgUrl : 업로드 결과 로컬에 저장된 상품 이미지 파일을 불러오는 요청 경로
            imgUrl = "/images/item/" + imgName;
        }

        // 1-4. 상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }

    // 2-1. 상품 이미지 수정
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {

        // 2-2. 상품 이미지를 수정한 경우 상품 이미지를 업데이트
        if (!itemImgFile.isEmpty()) {
            // 2-3. 상품 이미지 아이디를 이용하여 기존에 저장했던 상품 이미지 엔티티를 조회
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);

            // 2-4. 기존에 등록된 상품 이미지 파일이 있을 경우 해당 파일을 삭제
            if (!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation + "/" +
                        savedItemImg.getImgName());
            }

            // 2-5. 2-4가 아니라면 전달받은 MultipartFile itemImgFile의 원본 파일명을 가지고 온다.
            String oriImgName = itemImgFile.getOriginalFilename();
            // 2-6. 업데이트한 상품 이미지 파일을 업로드
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            // 2-7. 변경된 상품 이미지 경로를 만들어준다.
            String imgUrl = "/images/item/" + imgName;
            /* 2-8. 변경된 상품 이미지 정보를 세팅, 엔티티에 만들어 놓은 updateItemImg 메소드를 이용해
             *      변경감지(더티체킹)를 일으켜 update 쿼리를 실행시킨다.
             */
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
        }
    }
}
