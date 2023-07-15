package shop.jbshop.service;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.jbshop.domain.item.Item;
import shop.jbshop.repository.ItemRepository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private EntityManager em;

    public void registItemTest() {
        Item item = Item.builder()
                .text("good")
                .price(1000)
                .stock(10)
                .name("nike")
                .build();
        itemService.registItemTest(item);


    }


}