package dobin.webproject.repository;

import dobin.webproject.dto.ItemSearchDto;
import dobin.webproject.dto.MainItemDto;
import dobin.webproject.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
