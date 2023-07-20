package shop.jbshop.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import shop.jbshop.domain.item.Category;
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
        return Optional.ofNullable(em.createQuery("select i from Item i where i.id IN :itemIds", Item.class)
                .setParameter("itemIds", itemIds)
                .getResultList());

    }

    public Page<Item> findAll(Pageable pageable) {
        Long totalItemCount = em.createQuery("select count(i) from Item i where i.deletedAt is null", Long.class)
                .getSingleResult();

        List<Item> items = em.createQuery("select i from Item i where i.deletedAt is null order by i.id desc ", Item.class)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        return new PageImpl<>(items, pageable, totalItemCount);
    }

    public Page<Item> findAllByCate(Pageable pageable, String category) {
        Category findCate = Category.valueOf(category);
        Long totalItemCount = em.createQuery("select count(i) from Item i where i.deletedAt is null and " +
                        "i.itemCategory =:category", Long.class)
                .setParameter("category", findCate)
                .getSingleResult();
        List<Item> items = em.createQuery("select i from Item i where i.deletedAt is null " +
                        " and i.itemCategory =:category order by i.id desc ", Item.class)
                .setParameter("category", findCate)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        return new PageImpl<>(items, pageable, totalItemCount);
    }


    public Page<Item> findAllByText(Pageable pageable, String text) {
        String searchText = "%" + text + "%";
        Long totalItemCount = em.createQuery("select count(i) from Item i where i.deletedAt is null " +
                        "and (i.itemName like :searchText or i.itemText like :searchText)", Long.class)
                .setParameter("searchText", searchText)
                .getSingleResult();

        List<Item> items = em.createQuery("select i from Item i where i.deletedAt is null " +
                        "and (i.itemName like :searchText or i.itemText like :searchText)", Item.class)
                .setParameter("searchText", searchText)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        return new PageImpl(items, pageable, totalItemCount);
    }
}
