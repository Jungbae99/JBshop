package shop.jbshop.dto.response;

import shop.jbshop.domain.member.Address;
import shop.jbshop.domain.member.Member;

public class MemberUpdateResponseDto {
    private Address address;
    private String phoneNumber;

    public static MemberUpdateResponseDto fromEntity(Member member) {
        MemberUpdateResponseDto dto = new MemberUpdateResponseDto();
        dto.address = member.getAddress();
        dto.phoneNumber = member.getPhoneNumber();
        return dto;
    }
}
