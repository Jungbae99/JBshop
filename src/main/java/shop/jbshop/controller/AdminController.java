package shop.jbshop.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.jbshop.domain.item.Category;
import shop.jbshop.dto.request.ItemSaveRequestDto;
import shop.jbshop.dto.request.ItemUpdateRequestDto;
import shop.jbshop.dto.response.AllItemResponseDto;
import shop.jbshop.dto.response.ItemResponseDto;
import shop.jbshop.dto.response.ItemUpdateResponseDto;
import shop.jbshop.service.ItemService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final ItemService itemService;

    @GetMapping("/addItem")
    public String addItem() {
        return "/member/addItem";
    }

    @PostMapping("/addItem")
    public String addItem(@Validated ItemSaveRequestDto dto) {
        String category = dto.getCategory();
        System.out.println("category = " + category);
        Category cate = Category.valueOf(dto.getCategory());
        System.out.println("cate = " + cate);
        itemService.saveItem(dto);
        return "redirect:/main";
    }

    @GetMapping("/updateItem")
    public String updateItem(@RequestParam("itemId") Long itemId, Model model) {
        ItemResponseDto findItem = itemService.findItem(itemId);
        model.addAttribute("item", findItem);
        return "/item/updateItem";
    }

    @PostMapping("/updateItem")
    public String updateItem(ItemUpdateRequestDto requestDto, Model model) {
        Long id = itemService.updateItem(requestDto);
        ItemResponseDto dto = itemService.findItem(id);
        model.addAttribute("item", dto);
        return "item/itemDetail";
    }

    @GetMapping("/deleteItem")
    public String deleteItem(@RequestParam("itemId") Long itemId) {
        System.out.println("id = " + itemId);
        itemService.deleteItem(itemId);
        return "redirect:/main";
    }

}

