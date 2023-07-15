package shop.jbshop.domain.cart;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.jbshop.domain.*;
import shop.jbshop.domain.member.Member;

import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseAuditingListener {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItemList = new ArrayList<>();

//    @OneToMany(mappedBy = "cart")
//    private List<Order> orderList = new ArrayList<>();

    @JoinColumn(name = "delivery_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Delivery delivery;


    public static Cart createCart(Member member) {
        Cart cart = new Cart();
        cart.member = member;
        return cart;
    }

    public void addCart(CartItem cartItem, int quantity) {
        CartItem findCartItem = new CartItem(cartItem.getItem(), quantity, this);
        cartItemList.add(findCartItem);
    }
}
