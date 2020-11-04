package com.bhhan.tram.order.service;

import com.bhhan.tram.common.CustomerCreditReservationFailedEvent;
import com.bhhan.tram.common.CustomerCreditReservedEvent;
import com.bhhan.tram.common.CustomerValidationFailedEvent;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import lombok.RequiredArgsConstructor;

/**
 * Created by hbh5274@gmail.com on 2020-11-04
 * Github : http://github.com/bhhan5274
 */

@RequiredArgsConstructor
public class CustomerEventConsumer {
    private final OrderService orderService;

    public DomainEventHandlers domainEventHandlers(){
        return DomainEventHandlersBuilder
                .forAggregateType("com.bhhan.tram.customer.domain.Customer")
                .onEvent(CustomerCreditReservedEvent.class, this::handleCustomerCreditReservedEvent)
                .onEvent(CustomerCreditReservationFailedEvent.class, this::handleCustomerCreditReservationFailedEvent)
                .onEvent(CustomerValidationFailedEvent.class, this::handleCustomerValidationFailedEvent)
                .build();
    }

    private void handleCustomerCreditReservedEvent(DomainEventEnvelope<CustomerCreditReservedEvent> domainEventEnvelope){
        orderService.approveOrder(domainEventEnvelope.getEvent().getOrderId());
    }

    private void handleCustomerCreditReservationFailedEvent(DomainEventEnvelope<CustomerCreditReservationFailedEvent> domainEventEnvelope){
        orderService.rejectOrder(domainEventEnvelope.getEvent().getOrderId());
    }

    private void handleCustomerValidationFailedEvent(DomainEventEnvelope<CustomerValidationFailedEvent> domainEventEnvelope){
        orderService.rejectOrder(domainEventEnvelope.getEvent().getOrderId());
    }
}
