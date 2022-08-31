package dobin.webproject.service;

import dobin.webproject.dto.ItemFormDto;
import dobin.webproject.dto.ItemImgDto;
import dobin.webproject.dto.ItemSearchDto;
import dobin.webproject.dto.MainItemDto;
import dobin.webproject.entity.Item;
import dobin.webproject.entity.ItemImg;
import dobin.webproject.repository.ItemImgRepository;
import dobin.webproject.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    // 1-1. 상품 등록
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        // 1-2. itemFormDto를 item entity로 변환
        Item item = itemFormDto.createItem();
        // 1-3. 영속화
        itemRepository.save(item);

        // 1-4. 이미지 등록
        for (int i=0; i< itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            // 1-5. 첫 번쩨 이미지일 경우 대표 상품 이미지 여부 값을 "Y"로 세팅하고 나머지 상품 이미지는 "N"으로 설정
            if (i == 0)
                itemImg.setRepImgYn("Y");
            else
                itemImg.setRepImgYn("N");
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }
        return item.getId();
    }

    // 2-1. 상품 조회
    @Transactional(readOnly = true) /* 2-2. 상품 데이터를 읽어오는 트랜잭션을 읽기 전용을 설정,
                                     *      이럴 경우 JPA가 더티체킹(변경감지)을 수행하지 않아 성능 향상 */
    public ItemFormDto getItemDtl(Long itemId) {

        // 2-3. 해당 상품의 이미지를 조회, 등록순으로 가지고 오기 위해 상품 이미지 아이디 오름차순으로 가지고 옴
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        // 2-4. 2-3에서 조회한 ItemImg 엔티티를 itemImgDto 객체로 변환.
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        for (ItemImg itemImg : itemImgList) {
            // 2-5. modelmapper를 이용하여 만든 메소드로 itemImg를 itemImgDto로 변환합니다.
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            // 2-6. 2-5에서 변환한 itemImgDto를 2-4에다가 추가
            itemImgDtoList.add(itemImgDto);
        }

        // 2-7. 상품의 아이디로 상품 엔티티를 조회
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

        // 2-8. 2-7에서 조회한 item 엔티티를 뷰로 가지고 가기 위해 modelmapper를 이용하여 만든 메소드로 item 엔티티를 itemFormDto로 변환
        ItemFormDto itemFormDto = ItemFormDto.of(item);

        // 2-9. 변환된 itemFormDto에 ItemImgDtoList도 추가
        itemFormDto.setItemImgDtoList(itemImgDtoList);

        return itemFormDto;
    }

    // 3-1. 상품 업데이트
    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        // 3-2. 상품 수정을 위해 전달 받은 itemFormDto에서 id값을 꺼내 item 엔티티 조회
        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        // 3-3. 상품 등록 화면으로부터 전달 받은 ItemFormDto를 통해 상품 엔티티를 업데이트(변경 감지)
        item.updateItem(itemFormDto);

        // 3-4. 상품 이미지 아이디 리스트를 조회
        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        // 3-5. 이미지 업데이트(등록)
        for (int i=0; i<itemImgFileList.size(); i++) {
            /* 3-6. 상품 이미지를 업데이트하기 위해 updateItemImg() 메소드에 상품 이미지 아이디와,
             *      상품 이미지 파일 정보를 파라미터로 전달
             */
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }
        return item.getId();
    }

    // 4-1. 페이지별 상품 관리 화면 select
    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    // 4-2.
    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }
}
