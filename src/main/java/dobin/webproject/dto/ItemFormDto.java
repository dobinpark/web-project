package dobin.webproject.dto;

import dobin.webproject.constant.ItemSellStatus;
import dobin.webproject.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "상품 상세는 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    // 1-1. 상품 저장 후 수정할 때 상품 이미지 정보를 저장하는 리스트이다.
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    /**  1-2. 상품의 이미지 ID 값을 저장하는 리스트이다. 상품 등록 시 아직 상품의 이미지를 저장하지 않았기 때문에
     *        아무 값도 들어가 있지 않고 수정 시에 이미지 ID를 담아줄 용도로 사용.
     */
    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    // 1-3. ItemFormDto -> Item entity
    public Item createItem() {
        return modelMapper.map(this, Item.class);
    }

    // 1-4. Item entity -> ItemFormDto
    public static ItemFormDto of(Item item){
        return modelMapper.map(item,ItemFormDto.class);
    }
}
