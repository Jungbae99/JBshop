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



}
