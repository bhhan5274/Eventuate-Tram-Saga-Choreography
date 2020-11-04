package com.bhhan.tram.customer.service;

import com.bhhan.tram.common.*;
import com.bhhan.tram.customer.domain.Customer;
import com.bhhan.tram.customer.domain.CustomerCreditLimitExceededException;
import com.bhhan.tram.customer.domain.CustomerRepository;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Optional;

/**
 * Created by hbh5274@gmail.com on 2020-11-04
 * Github : http://github.com/bhhan5274
 */

@Slf4j
@RequiredArgsConstructor
public class OrderEventConsumer {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    public DomainEventHandlers domainEventHandlers(){
        return DomainEventHandlersBuilder
                .forAggregateType("com.bhhan.tram.order.domain.Order")
                .onEvent(OrderCreatedEvent.class, this::handleOrderCreatedEventHandler)
                .onEvent(OrderCancelledEvent.class, this::handleOrderCancelledEvent)
                .build();
    }

    private void handleOrderCreatedEventHandler(DomainEventEnvelope<OrderCreatedEvent> domainEventEnvelope){
        long orderId = Long.parseLong(domainEventEnvelope.getAggregateId());
        OrderCreatedEvent orderCreatedEvent = domainEventEnvelope.getEvent();
        Long customerId = orderCreatedEvent.getOrderDetails().getCustomerId();

        Optional<Customer> possibleCustomer = customerRepository.findById(customerId);

        if(!possibleCustomer.isPresent()){
            log.info("Non-existent customer: {}", customerId);
            domainEventPublisher.publish(Customer.class,
                    customerId,
                    Collections.singletonList(new CustomerValidationFailedEvent(orderId)));
            return;
        }

        Customer customer = possibleCustomer.get();

        try{
            customer.reserveCredit(orderId, orderCreatedEvent.getOrderDetails().getOrderTotal());

            CustomerCreditReservedEvent customerCreditReservedEvent = new CustomerCreditReservedEvent(orderId);
            domainEventPublisher.publish(Customer.class,
                    customer.getId(),
                    Collections.singletonList(customerCreditReservedEvent));
        }catch(CustomerCreditLimitExceededException e){
            CustomerCreditReservationFailedEvent customerCreditReservationFailedEvent = new CustomerCreditReservationFailedEvent(orderId);
            domainEventPublisher.publish(Customer.class,
                    customer.getId(),
                    Collections.singletonList(customerCreditReservationFailedEvent));
        }
    }

    private void handleOrderCancelledEvent(DomainEventEnvelope<OrderCancelledEvent> domainEventEnvelope){
        long orderId = Long.parseLong(domainEventEnvelope.getAggregateId());
        OrderCancelledEvent orderCancelledEvent = domainEventEnvelope.getEvent();
        Long customerId = orderCancelledEvent.getOrderDetails().getCustomerId();

        Customer customer = customerRepository.findById(customerId).get();
        customer.unreserveCredit(orderId);
    }
}
