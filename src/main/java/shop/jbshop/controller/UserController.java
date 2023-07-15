package shop.jbshop.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.jbshop.dto.request.AddCartRequestDto;
import shop.jbshop.dto.request.DirectOrderRequestDto;
import shop.jbshop.dto.response.CartResponseDto;
import shop.jbshop.dto.response.ItemResponseDto;
import shop.jbshop.service.CartService;
import shop.jbshop.service.ItemService;
import shop.jbshop.service.MemberService;
import shop.jbshop.service.OrderService;

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
}



