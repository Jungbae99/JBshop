package shop.jbshop.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDto {
    private Long id;
    private int quantity;
    private String name;
    private int price;

    public CartItemDto(Long id, String itemName, int itemPrice, int count) {
        this.id = id;
        this.quantity = count;
        this.price = itemPrice;
        this.name = itemName;
    }
}
