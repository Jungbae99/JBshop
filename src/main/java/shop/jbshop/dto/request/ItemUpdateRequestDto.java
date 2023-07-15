package shop.jbshop.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import shop.jbshop.domain.item.Category;

@Getter
@Setter
public class ItemUpdateRequestDto {
    private Long itemId;
    private String itemName;
    private String itemText;
    private int itemPrice;
    private int itemStock;
    private MultipartFile imageFile;
    private Category category;
}

