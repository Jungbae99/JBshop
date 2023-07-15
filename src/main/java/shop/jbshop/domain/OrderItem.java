package shop.jbshop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import shop.jbshop.domain.item.Item;

@Entity
@Getter
public class OrderItem extends BaseAuditingListener {

    @Id @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int price;

    private int count;

    public static OrderItem createOrderItem(Item item, int price, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.item = item;
        orderItem.price = price;
        orderItem.count = count;
        item.removeStock(count);
        return orderItem;
    }

    public void addOrder(Order order) {
        this.order = order;
    }




}
