package shop.jbshop.dto.response;


import lombok.Getter;
import shop.jbshop.domain.member.Address;
import shop.jbshop.domain.member.Grade;
import shop.jbshop.domain.member.Member;

import java.time.LocalDateTime;

@Getter
public class MemberResponseDto {

    private Long id;
    private String email;
    private Address address;
    private String username;
    private String phoneNumber;
    private Grade grade;
    private String password;
    private LocalDateTime createdAt;

    public static MemberResponseDto fromEntity(Member member) {
        MemberResponseDto dto = new MemberResponseDto();
        dto.id = member.getId();
        dto.email = member.getEmail();
        dto.address = member.getAddress();
        dto.phoneNumber = member.getPhoneNumber();
        dto.username = member.getUsername();
        dto.grade = member.getGrade();
        dto.password = member.getPassword();
        dto.createdAt = member.getCreatedAt();
        return dto;
    }

}
