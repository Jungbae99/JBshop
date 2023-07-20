package shop.jbshop.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDto {
    private Long itemId;
    private Long id;
    private Integer quantity;
    private String name;
    private int price;

    public CartItemDto(Long id, Long itemId , String itemName, int itemPrice, Integer quantity) {
        this.quantity = quantity;
        this.price = itemPrice;
        this.name = itemName;
        this.id = id;
        this.itemId = itemId;
    }
}
