package shop.jbshop.dto.response;

import lombok.Getter;
import lombok.Setter;
import shop.jbshop.domain.item.Item;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AllItemResponseDto {

        private Long id;
        private int count;
        private String itemName;
        private int itemPrice;
        private int itemStock;
        private String itemText;
        private String itemImg;

//    public static List<AllItemResponseDto> fromEntity(List<Item> findItems) {
//        List<AllItemResponseDto> lists = new ArrayList<>();
//
//        for (Item findItem : findItems) {
//            AllItemResponseDto dto = new AllItemResponseDto();
//            dto.id = findItem.getId();
//            dto.itemName = findItem.getItemName();
//            dto.count = findItem.getItemCount();
//            dto.itemPrice = findItem.getItemPrice();
//            dto.itemStock = findItem.getItemStock();
//            dto.itemText = findItem.getItemText();
//            dto.itemImg = findItem.getImageAsBase64();
//            lists.add(dto);
//        }
//        return lists;
//    }

    public static AllItemResponseDto fromEntity(Item item) {
        AllItemResponseDto dto = new AllItemResponseDto();
        dto.id = item.getId();
        dto.itemName = item.getItemName();
        dto.count = item.getItemCount();
        dto.itemPrice = item.getItemPrice();
        dto.itemStock = item.getItemStock();
        dto.itemText = item.getItemText();
        dto.itemImg = item.getImageAsBase64();
        return dto;
    }
}

