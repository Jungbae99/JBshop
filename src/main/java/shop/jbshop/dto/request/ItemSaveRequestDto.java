package shop.jbshop.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import shop.jbshop.domain.item.Category;

@Getter
@Setter
public class ItemSaveRequestDto {
    private String name;
    private String text;
    private int price;
    private int stock;
    private MultipartFile imageFile;
    private String category;
}

