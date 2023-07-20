package shop.jbshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.jbshop.domain.Delivery;
import shop.jbshop.domain.Order;
import shop.jbshop.domain.OrderItem;
import shop.jbshop.domain.item.Item;
import shop.jbshop.domain.member.Member;
import shop.jbshop.repository.ItemRepository;
import shop.jbshop.repository.MemberRepository;
import shop.jbshop.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

import static shop.jbshop.domain.Order.createOrder;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        Optional<Member> findMember = memberRepository.findByIdAndDeletedAtNull(memberId);
        Member member = findMember.get();
        Optional<Item> findItem = itemRepository.findByIdAndDeletedAtNull(itemId);
        Item item = findItem.get();

        Delivery delivery = Delivery.setAddress(member.getAddress());

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getItemPrice(), count);
        item.removeStock(count);

        Order order = createOrder(member, delivery, orderItem);
        orderRepository.save(order);
        return order.getId();
    }


    @Transactional
    public void order(Long memberId, List<Long> itemId, List<Integer> count) {
        Optional<Member> findMember = memberRepository.findByIdAndDeletedAtNull(memberId);
        Member member = findMember.get();
        Delivery delivery = Delivery.setAddress(member.getAddress());

        Order order = createOrder(member, delivery);

        for (int i = 0; i < itemId.size(); i++) {
            Long itemIdValue = itemId.get(i);
            Integer countValue = count.get(i);

            Optional<Item> findItem = itemRepository.findByIdAndDeletedAtNull(itemIdValue);
            Item item = findItem.get();

            OrderItem orderItem = OrderItem.createOrderItem(item, item.getItemPrice(), countValue);
            order.setOrderItem(orderItem);
        }
        orderRepository.save(order);
    }
}
