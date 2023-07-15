package shop.jbshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberLoginRequestDto {

    @NotBlank(message = "EMAIL IS MANDATORY")
    private String email;
    @NotBlank(message = "PASSWORD IS MANDATORY")
    private String password;

}
