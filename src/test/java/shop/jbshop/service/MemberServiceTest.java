package shop.jbshop.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.jbshop.domain.member.Member;
import shop.jbshop.dto.request.MemberUpdateRequestDto;
import shop.jbshop.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private EntityManager em;



    @Test
    public void memberJoinTest() {
        Member member = Member.builder()
                .password("123")
                .email("admin@admin.com")
                .username("wjdqo")
                .phoneNumber("01012341234")
                .build();
        memberService.joinMember(member);
        em.flush();
        assertThat(memberRepository.findByIdAndDeletedAtNull(member.getId()).get().getUsername()).isEqualTo("wjdqo");

    }

    @Test
    public void FindAllTest() {
        Member member1 = Member.builder().password("123").email("admin1").username("member1").build();
        Member member2 = Member.builder().password("123").email("admin2").username("member2").build();
        Member member3 = Member.builder().password("123").email("admin3").username("member3").build();
        memberService.joinMember(member1);
        memberService.joinMember(member2);
        memberService.joinMember(member3);
        em.flush();

        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList.size()).isEqualTo(3);
        assertThat(memberList).extracting(Member::getEmail)
                .containsExactly("admin1", "admin2", "admin3");

        assertThat(memberList).extracting(Member::getUsername)
                .containsExactly("member1", "member2", "member3");
    }

    @Test
    public void FindAllTest2() {
        Member member1 = Member.builder().password("123").email("admin1").username("member1").build();
        Member member2 = Member.builder().password("123").email("admin2").username("member2").build();
        Member member3 = Member.builder().password("123").email("admin3").username("member3").build();
        memberService.joinMember(member1);
        memberService.joinMember(member2);
        memberService.joinMember(member3);
        em.flush();

        assertThat(memberService.findMembers()).
                extracting("email").containsExactly("admin1", "admin2", "admin3");

    }

    @Test
    public void findByUsernameTest() {
        Member member1 = Member.builder().password("123").email("admin1").username("member1").build();
        Member member2 = Member.builder().password("123").email("admin2").username("member2").build();
        Member member3 = Member.builder().password("123").email("admin3").username("member3").build();
        memberService.joinMember(member1);
        memberService.joinMember(member2);
        memberService.joinMember(member3);
        em.flush();

        List<Member> findMember = memberRepository.findByUsername("email");
        assertThat(findMember.size()).isEqualTo(1);
    }

    @Test
    public void updateMemberTest() {
        Member member1 = Member.builder().password("123").email("admin1").username("member1").build();
        memberRepository.save(member1);

        Long memberId = member1.getId();

        MemberUpdateRequestDto dto = new MemberUpdateRequestDto("1","123","123", "010","123");
        memberService.updateMember("memberId", dto);

        Optional<Member> findMember = memberRepository.findByIdAndDeletedAtNull(memberId);

        assertThat(findMember.get().getUsername()).isEqualTo("admin3");
        assertThat(findMember.get().getPhoneNumber()).isEqualTo("010");

    }


}
