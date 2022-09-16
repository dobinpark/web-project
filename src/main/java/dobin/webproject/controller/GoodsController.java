package dobin.webproject.controller;

import dobin.webproject.dto.ItemSearchDto;
import dobin.webproject.dto.MainItemDto;
import dobin.webproject.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {

    private final ItemService itemService;

    @GetMapping(value = "/items")
    public String Main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);

        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);

        model.addAttribute("itemSearchDto", itemSearchDto);

        model.addAttribute("maxPage", 5);

        return "item/itemGoods";
    }
}
