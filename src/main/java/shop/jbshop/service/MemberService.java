package shop.jbshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.jbshop.domain.member.Address;
import shop.jbshop.domain.member.Author;
import shop.jbshop.domain.member.Grade;
import shop.jbshop.domain.member.Member;
import shop.jbshop.dto.request.MemberDirectCreateRequestDto;
import shop.jbshop.dto.request.MemberUpdateRequestDto;
import shop.jbshop.dto.response.MemberResponseDto;
import shop.jbshop.dto.response.MemberUpdateResponseDto;
import shop.jbshop.repository.MemberRepository;


import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
//    private final PasswordEncoder passwordEncoder;

    /**
     * TODO: 인코딩
     */
    public Long joinMember(MemberDirectCreateRequestDto dto) {
        Address address = new Address(dto.getCity(), dto.getStreet(), dto.getZipcode());

        String password = dto.getPassword();
        Member member = Member.builder()
                .email(dto.getEmail())
                .username(dto.getUsername())
                .phoneNumber(dto.getPhoneNumber())
                .password(password)
                .author(Author.USER)
                .grade(Grade.BASIC)
                .note(dto.getNote())
                .address(address)
                .build();
        System.out.println(member.getAuthor());
        return memberRepository.save(member);
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public MemberResponseDto findMember(Long id) {
        Optional<Member> findMember = memberRepository.findByIdAndDeletedAtNull(id);
        Member member = findMember.get();

        return MemberResponseDto.fromEntity(member);

    }


    @Transactional
    public MemberUpdateResponseDto updateMember(String userEmail, MemberUpdateRequestDto dto) {
        Optional<Member> member = memberRepository.findByEmailAndDeletedAtNull(userEmail);
        Member findMember = member.get();
        findMember.updatePhoneNumber(dto.getPhoneNumber());
        findMember.updateAddress(dto.getCity(), dto.getStreet(), dto.getZipcode());

        return MemberUpdateResponseDto.fromEntity(findMember);
    }

    @Transactional
    public Long deleteMember(String userEmail) {
        Optional<Member> member = memberRepository.findByEmailAndDeletedAtNull(userEmail);
        Member findMember = member.get();
        findMember.deleteMember();
        return findMember.getId();
    }

    public MemberResponseDto findMemberByEmail(String userEmail) {
        return MemberResponseDto.fromEntity(memberRepository.findByEmailAndDeletedAtNull(userEmail).get());
    }

    /**
     * TODO : 테스트
     */
    public void joinMember(Member member) {
        memberRepository.save(member);
    }

}
