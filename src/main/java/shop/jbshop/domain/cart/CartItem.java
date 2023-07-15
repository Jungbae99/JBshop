package shop.jbshop.domain.cart;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.jbshop.domain.item.Item;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem {

    @Id
    @GeneratedValue
    @Column(name="cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int quantity;

    public CartItem(Item item, int quantity, Cart cart) {
        this.item = item;
        this.quantity = quantity;
        this.cart = cart;
    }

    public static CartItem createCartItem(Item item, int quantity) {
        CartItem cartItem = new CartItem();
        cartItem.item = item;
        cartItem.quantity = quantity;
        return cartItem;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }
}
