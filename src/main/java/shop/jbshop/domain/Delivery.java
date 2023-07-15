package shop.jbshop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import shop.jbshop.domain.member.Address;

@Entity
@Getter
public class Delivery {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "delivery")
    private Order order;

    private Address address;

    public void addOrder(Order order) {
        this.order = order;
    }

    public static Delivery setAddress(Address address) {
        Delivery delivery = new Delivery();
        delivery.address = address;
        return delivery;
    }


}
