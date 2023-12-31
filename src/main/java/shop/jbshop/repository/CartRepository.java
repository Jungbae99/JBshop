package shop.jbshop.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.jbshop.domain.cart.Cart;
import shop.jbshop.domain.cart.CartItem;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CartRepository {

    private final EntityManager em;

    public Long save(Cart cart) {
        em.persist(cart);
        return cart.getId();
    }


    public Optional<List<CartItem>> findAll() {
        return Optional.ofNullable(em.createQuery("select c from CartItem c")
                .getResultList());

    }

    public Optional<List<Cart>> findAllByMemberId(Long memberId) {
        return Optional.ofNullable(em.createQuery("select c from Cart c join fetch c.cartItemList ci " +
                        "where c.member.id =:memberId ")
                .setParameter("memberId", memberId)
                .getResultList());

    }


    public Optional<Cart> findByMemberId(Long memberId) {
        List<Cart> carts = em.createQuery(
                        "SELECT c FROM Cart c " +
                                "WHERE c.member.id = :memberId", Cart.class)
                .setParameter("memberId", memberId)
                .getResultList();

        return carts.stream().findFirst();
    }
    
    //단건 삭제
//    public void deleteCartItem(Long cartItemId) {
//        em.createQuery("delete from CartItem ci WHERE ci.id = :cartItemId")
//                .setParameter("cartItemId", cartItemId)
//                .executeUpdate();
//
//    }

    //한번에 삭제
    public void deleteCartItems(List<Long> itemIds) {
        em.createQuery("delete from CartItem ci where ci.item.id in :itemIds")
                .setParameter("itemIds", itemIds)
                .executeUpdate();
        em.flush();
    }



    
}
