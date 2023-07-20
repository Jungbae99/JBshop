package shop.jbshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.jbshop.dto.response.ItemResponseDto;
import shop.jbshop.service.ItemService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {
    //user, admin 모두 접근할 수 있음
    private final ItemService itemService;

    @GetMapping("/detail")
    public String itemDetail(@RequestParam("itemId") Long itemId, Model model) {
        ItemResponseDto dto = itemService.findItem(itemId);
        model.addAttribute("item", dto);
        return "/item/itemDetail";
    }

}
