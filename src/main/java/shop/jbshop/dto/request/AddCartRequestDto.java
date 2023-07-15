package shop.jbshop.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCartRequestDto {
    private Long itemId;
    private int quantity;
}
