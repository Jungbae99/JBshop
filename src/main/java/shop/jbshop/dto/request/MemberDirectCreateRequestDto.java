package shop.jbshop.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDirectCreateRequestDto {

    @Email(message = "INVALID_EMAIL")
    @NotBlank(message="EMAIL_IS_MANDATORY")
    private String email;
    @NotBlank(message = "PASSWORD_IS_MANDATORY")
    private String password;
    @NotBlank(message="USERNAME_IS_MANDATORY")
    private String username;
    private String phoneNumber;


    private String city;
    private String street;
    private String zipcode;
    private String note;
}
