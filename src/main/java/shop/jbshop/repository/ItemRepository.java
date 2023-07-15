package shop.jbshop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.jbshop.domain.item.Item;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public Long save(Item item) {
        em.persist(item);
        em.flush();
        return item.getId();
    }


    public Optional<Item> findByIdAndDeletedAtNull(Long itemId) {
        return Optional.ofNullable(em.createQuery("select i from Item i where i.id = :itemId and i.deletedAt is null", Item.class)
                .setParameter("itemId", itemId)
                .getSingleResult());
    }

    public Optional<List<Item>> findByIdsAndDeletedAtNull(List<Long> itemIds) {
        return Optional.ofNullable(em.createQuery("select i from Item i where i.id IN :itemIds")
                .setParameter("itemIds", itemIds)
                .getResultList());

    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i where i.deletedAt is null", Item.class)
                .getResultList();
    }
}
