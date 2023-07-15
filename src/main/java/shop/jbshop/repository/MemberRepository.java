package shop.jbshop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.jbshop.domain.member.Member;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

//    public Member findById(Long memberId) {
//        return em.find(Member.class, memberId);
//    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public Optional<Member> findByIdAndDeletedAtNull(Long memberId) {
        Member findMember = em.createQuery("select m from Member m where m.id = :memberId and m.deletedAt is null", Member.class)
                .setParameter("memberId", memberId)
                .getSingleResult();
        return Optional.ofNullable(findMember);

    }

    public List<Member> findByUsername(String username) {
        return em.createQuery("select m from Member m where m.username =:username", Member.class)
                .setParameter("username", username)
                .getResultList();
    }


    public Optional<Member> findByEmailAndDeletedAtNull(String email) {
        try {
            Member member = em.createQuery("select m from Member m where m.email = :email and m.deletedAt is null", Member.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(member);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
