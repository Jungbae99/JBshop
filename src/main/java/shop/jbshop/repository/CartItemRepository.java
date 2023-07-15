package shop.jbshop.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.thymeleaf.engine.ElementName;
import shop.jbshop.domain.cart.CartItem;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CartItemRepository {

    private final EntityManager em;

    public Optional<CartItem> getCartItemByItemId(Long itemId) {
        return (Optional<CartItem>) em.createQuery("select ci from CartItem ci where ci.item.id =:itemId ")
                .setParameter("itemId", itemId)
                .getSingleResult();
    }
}
