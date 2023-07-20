package shop.jbshop.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.jbshop.dto.request.AddCartRequestDto;
import shop.jbshop.dto.request.DirectOrderRequestDto;
import shop.jbshop.dto.response.AllItemResponseDto;
import shop.jbshop.dto.response.CartResponseDto;
import shop.jbshop.dto.response.ItemResponseDto;
import shop.jbshop.service.CartService;
import shop.jbshop.service.ItemService;
import shop.jbshop.service.MemberService;
import shop.jbshop.service.OrderService;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final OrderService orderService;
    private final ItemService itemService;
    private final MemberService memberService;
    private final CartService cartService;

    @GetMapping("/directOrder")
    public String directOrder(@RequestParam("itemId") Long itemId, Model model) {
        ItemResponseDto dto = itemService.findItem(itemId);
        model.addAttribute("item", dto);
        return "/item/directOrderForm";
    }

    @PostMapping("/directOrder")
    public String directOrder(@RequestParam("itemId") Long itemId,
                              @RequestParam("itemCount") int itemCount,
                              HttpSession session) {
        Long id = (Long) session.getAttribute("memberId");
        orderService.order(id, itemId, itemCount);
        return "redirect:/main";
    }

    //장바구니 담기 폼으로 이동
    @GetMapping("/cartOrder")
    public String cartOrder(@RequestParam("itemId") Long itemId, Model model) {
        ItemResponseDto dto = itemService.findItem(itemId);
        model.addAttribute("item", dto);
        return "/item/cartForm";
    }

    //장바구니 담기
    @PostMapping("/cartOrder")
    public String cartOrder(HttpSession session, AddCartRequestDto dto) {
        System.out.println(dto.getItemId());
        System.out.println(dto.getQuantity());
        Long memberId = (Long) session.getAttribute("memberId");
        cartService.addCart(dto, memberId);
        return "redirect:/main";
    }

    //장바구니 보기
    @GetMapping("/cart")
    public String cart(Model model, HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");
        CartResponseDto cartItems = cartService.getCartItems(memberId);
        model.addAttribute("cartItems", cartItems);
        return "/item/cartListForm";
    }

    //카테고리별 이동
    @GetMapping("/{category}")
    public String categorySearch(@PathVariable String category,
                                 HttpSession session,
                                 Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "9") int size) {
        System.out.println(category);
        Page<AllItemResponseDto> itemsPage = itemService.findItems(PageRequest.of(page, size), category);
        model.addAttribute("items", itemsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", itemsPage.getTotalPages());
        String message = (String) session.getAttribute("message");
        if (message != null) {
            model.addAttribute("message", message);
            session.removeAttribute("message");
        }
        return "/item/category";
    }


    // 체크한 상품 삭제 처리
    @PostMapping("/cartAction")
    public String removeItems(@RequestParam(value = "itemIds", required = false) List<Long> itemIds) {
        for (Long itemId : itemIds) {
            System.out.println("itemId = " + itemId);
        }
        cartService.removeCartItems(itemIds);
        return "redirect:/user/cart";
    }


    @PostMapping("/placeOrder")
    public String placeOrder(@RequestParam(value = "itemIds") List<Long> itemIds,
                             @RequestParam(value = "itemQuantity") List<Integer> itemQuantity,
                             HttpSession session) {
        for (Long itemId : itemIds) {
            System.out.println("itemId = " + itemId);
        }
        for (Integer integer : itemQuantity) {
            System.out.println("integer = " + integer);
        }
        Long memberId = (Long) session.getAttribute("memberId");
        orderService.order(memberId, itemIds, itemQuantity);
        cartService.removeCartItems(itemIds);
        return "redirect:/user/cart";
    }


}



