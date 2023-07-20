package shop.jbshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.jbshop.domain.cart.Cart;
import shop.jbshop.domain.cart.CartItem;
import shop.jbshop.domain.item.Item;
import shop.jbshop.domain.member.Member;
import shop.jbshop.dto.request.AddCartRequestDto;
import shop.jbshop.dto.response.CartItemDto;
import shop.jbshop.dto.response.CartResponseDto;
import shop.jbshop.repository.CartItemRepository;
import shop.jbshop.repository.CartRepository;
import shop.jbshop.repository.ItemRepository;
import shop.jbshop.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
//    private final CartItemRepository cartItemRepository;

    
    //장바구니 생성
    @Transactional
    public Long addCart(AddCartRequestDto dto, Long memberId) {
        Optional<Member> findMember = memberRepository.findByIdAndDeletedAtNull(memberId);
        Member member = findMember.get();

        // 이미 해당 memberId로 생성된 Cart 엔티티가 있는지 확인
        Optional<Cart> existingCart = cartRepository.findByMemberId(memberId);
        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            System.out.println("이미 있음");
            // 기존의 Cart 엔티티를 사용하여 추가 작업 수행
            Optional<Item> item = itemRepository.findByIdAndDeletedAtNull(dto.getItemId());
            Item findItem = item.get();

            Optional<CartItem> findCartItem = cart.getCartItemList().stream()
                    .filter(ci -> ci.getItem().getId().equals(findItem.getId()))
                    .findFirst();

            if (findCartItem.isPresent()) {
                CartItem cartItem = findCartItem.get();
                cartItem.updateQuantity(dto.getQuantity());
            } else {
                CartItem cartItem = CartItem.createCartItem(findItem, dto.getQuantity());
                cart.addCart(cartItem, dto.getQuantity());
            }

        } else {
            // 새로운 Cart 엔티티 생성
            Optional<Item> item = itemRepository.findByIdAndDeletedAtNull(dto.getItemId());
            Item findItem = item.get();

            Cart cart = Cart.createCart(member);
            CartItem cartItem = CartItem.createCartItem(findItem, dto.getQuantity());
            cart.addCart(cartItem, dto.getQuantity());
            cartRepository.save(cart);
            return cart.getId();
        }

        return existingCart.get().getId();
    }



    public CartResponseDto getCartItems(Long memberId) {
        Optional<List<Cart>> findCartList = cartRepository.findAllByMemberId(memberId);

        List<CartItemDto> cartItemDtos = new ArrayList<>();

        for (Cart cart : findCartList.get()) {
            for (CartItem cartItem : cart.getCartItemList()) {
                Optional<Item> findItem = itemRepository.findByIdAndDeletedAtNull(cartItem.getItem().getId());
                Item item = findItem.get();

                CartItemDto cartItemDto = new CartItemDto(
                        cartItem.getId(),
                        item.getId(),
                        item.getItemName(),
                        item.getItemPrice(),
                        cartItem.getQuantity()
                );
                cartItemDtos.add(cartItemDto);
            }
        }
        CartResponseDto responseDto = new CartResponseDto();
        responseDto.setCartItems(cartItemDtos);
        return responseDto;

    }

//    @Transactional
//    public void removeCartItem(Long itemId) {
//        cartRepository.deleteCartItem(itemId);
//    }

    @Transactional
    public void removeCartItems(List<Long> itemIds) {
        cartRepository.deleteCartItems(itemIds);
    }
}

