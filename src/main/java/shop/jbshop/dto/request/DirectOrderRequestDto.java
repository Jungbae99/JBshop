package shop.jbshop.dto.request;


import lombok.Getter;

@Getter
public class DirectOrderRequestDto {

    private Long itemId;
    private Long memberId;
    private int count;

}
