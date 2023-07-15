package shop.jbshop.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.jbshop.domain.item.ItemImg;

import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemImgRepository {

    private final EntityManager em;

    public Long save(ItemImg itemImg) {
        em.persist(itemImg);
        return itemImg.getId();
    }
}
