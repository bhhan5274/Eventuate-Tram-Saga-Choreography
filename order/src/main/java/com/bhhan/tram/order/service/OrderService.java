package com.bhhan.tram.order.service;

import com.bhhan.tram.common.*;
import com.bhhan.tram.order.domain.Order;
import com.bhhan.tram.order.domain.OrderRepository;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static java.util.Collections.singletonList;

/**
 * Created by hbh5274@gmail.com on 2020-11-04
 * Github : http://github.com/bhhan5274
 */

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final DomainEventPublisher domainEventPublisher;
    private final OrderRepository orderRepository;

    public Order createOrder(OrderDetails orderDetails){
        ResultWithEvents<Order> orderWithEvents = Order.createOrder(orderDetails);
        Order order = orderWithEvents.result;
        orderRepository.save(order);
        domainEventPublisher.publish(Order.class, order.getId(), orderWithEvents.events);

        return order;
    }

    public void approveOrder(Long orderId){
        Order order = getOrder(orderId);
        order.noteCreditReserved();
        domainEventPublisher.publish(Order.class, orderId, singletonList(new OrderApprovedEvent(order.getOrderDetails())));
    }

    public void rejectOrder(Long orderId){
        Order order = getOrder(orderId);
        order.noteCreditReservationFailed();
        domainEventPublisher.publish(Order.class,
                orderId,
                singletonList(new OrderRejectedEvent(order.getOrderDetails())));
    }

    public Order cancelOrder(Long orderId){
        Order order = getOrder(orderId);
        order.cancel();
        domainEventPublisher.publish(Order.class,
                orderId,
                singletonList(new OrderCancelledEvent(order.getOrderDetails())));
        return order;
    }

    public Optional<Order> getOrderOptional(Long orderId){
        return orderRepository.findById(orderId);
    }

    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("order with id %s not found", orderId)));
    }
}
