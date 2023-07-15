package shop.jbshop.dto.response;

import lombok.Getter;
import shop.jbshop.domain.item.Category;
import shop.jbshop.domain.item.Item;

import java.util.Optional;

@Getter
public class ItemResponseDto {
    private Long id;
    private String itemName;
    private String itemText;
    private int itemPrice;
    private int itemCount;
    private int itemStock;
    private String itemImg;
    private Category category;

    public static ItemResponseDto fromEntity(Optional<Item> item) {
        ItemResponseDto dto = new ItemResponseDto();
        Item findItem = item.get();
        dto.id = findItem.getId();
        dto.itemName = findItem.getItemName();
        dto.itemText = findItem.getItemText();
        dto.itemPrice = findItem.getItemPrice();
        dto.itemCount = findItem.getItemCount();
        dto.itemStock = findItem.getItemStock();
        dto.itemImg = findItem.getImageAsBase64();
        dto.category = findItem.getItemCategory();
        return dto;
    }

}
