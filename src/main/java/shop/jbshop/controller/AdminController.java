package shop.jbshop.controller;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shop.jbshop.domain.item.Category;
import shop.jbshop.dto.request.ItemSaveRequestDto;
import shop.jbshop.dto.request.ItemUpdateRequestDto;
import shop.jbshop.dto.response.AllItemResponseDto;
import shop.jbshop.dto.response.ItemResponseDto;
import shop.jbshop.dto.response.ItemUpdateResponseDto;
import shop.jbshop.service.ItemService;
import shop.jbshop.service.KakaoService;

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

