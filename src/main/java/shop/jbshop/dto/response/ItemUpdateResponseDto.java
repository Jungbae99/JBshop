package shop.jbshop.dto.response;

import shop.jbshop.domain.item.Item;

import java.util.Optional;

public class ItemUpdateResponseDto {
    private Long id;
    private String name;
    private String text;
    private int price;
    private int stock;

    public static Long fromEntity(Optional<Item> item) {
        ItemUpdateResponseDto dto = new ItemUpdateResponseDto();
        Item findItem = item.get();
        dto.id = findItem.getId();
        dto.name = findItem.getItemName();
        dto.text = findItem.getItemText();
        dto.price = findItem.getItemPrice();
        dto.stock = findItem.getItemStock();
        return dto.id;
    }
}
