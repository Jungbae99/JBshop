package shop.jbshop.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponseDto {

    private String status;
    private String message;
    private String debugMessage;

    @Builder
    ErrorResponseDto(String status, String message, String debugMessage) {
        this.status = status;
        this.message = message;
        this.debugMessage = debugMessage;
    }
}