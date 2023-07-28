package shop.jbshop.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.jbshop.domain.member.Address;
import shop.jbshop.domain.member.Author;
import shop.jbshop.domain.member.Grade;
import shop.jbshop.domain.member.Member;
import shop.jbshop.dto.request.KakaoDto;
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
     * direct 회원가입
     */
    public Long joinMember(MemberDirectCreateRequestDto dto) {
        Address address = new Address(dto.getCity(), dto.getStreet(), dto.getZipcode());

        validateEmailDuplication(dto.getEmail());

        validateNicknameDuplication(dto.getUsername());

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

    /**
     * kakao 회원가입
     */
    public Long joinMemberByOauth(KakaoDto dto) {
        String randomUsername = RandomStringUtils.randomAlphanumeric(10);
        Member member = Member.builder().
                email(dto.getEmail()).
                username(randomUsername).
                oauthType("KAKAO").
                author(Author.USER).
                grade(Grade.BASIC).
                password("123").
                build();
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
        Optional<Member> optionalMember = memberRepository.findByEmailAndDeletedAtNull(userEmail);
        if (optionalMember.isPresent()) {
            return MemberResponseDto.fromEntity(optionalMember.get());
        } else {
            return null; // 또는 필요에 따라 다른 값을 반환하거나, 예외를 던지도록 처리
        }
    }


    /**
     * 중복체크 유틸
     */

    private void validateEmailDuplication(String email) throws DuplicateKeyException {
        memberRepository.findByEmailAndDeletedAtNull(email)
                .ifPresent(m -> {
                    throw new DuplicateKeyException("이미 같은 이메일이 존재합니다.");
                });
    }

    private void validateNicknameDuplication(String nickname) throws DuplicateKeyException {
        memberRepository.findByUsernameAndDeletedAtNull(nickname)
                .ifPresent(m -> {
                    throw new DuplicateKeyException("이미 같은 닉네임이 존재합니다.");
                });
    }

    /**
     * TODO : 테스트
     */
    public void joinMember(Member member) {
        memberRepository.save(member);
    }


    public Long findCartCount(Long memberId) {
        return memberRepository.findCartCountByIdAndDeletedAtNull(memberId);
    }
}
