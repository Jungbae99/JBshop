package shop.jbshop.dto.request;

import lombok.Getter;
import lombok.Setter;
import shop.jbshop.domain.member.Address;

@Getter
@Setter
public class MemberUpdateRequestDto {

    private String userEmail;
    private String city;
    private String street;
    private String zipcode;
    private String phoneNumber;
    private Address address;

    public MemberUpdateRequestDto(String userEmail, String phoneNumber, String city, String street, String zipcode) {
        Address address = new Address(city, street, zipcode);
        this.userEmail = userEmail;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
