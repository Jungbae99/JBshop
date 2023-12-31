package shop.jbshop.dto.request;

import lombok.Getter;

@Getter
public class KakaoDto {
    private String email;
    private String username;

    public static KakaoDto fromEntity(String email) {
        KakaoDto dto = new KakaoDto();
        dto.email = email;
        return dto;
    }
}
