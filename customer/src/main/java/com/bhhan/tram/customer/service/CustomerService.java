package com.bhhan.tram.customer.service;

import com.bhhan.tram.common.Money;
import com.bhhan.tram.customer.domain.Customer;
import com.bhhan.tram.customer.domain.CustomerRepository;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by hbh5274@gmail.com on 2020-11-04
 * Github : http://github.com/bhhan5274
 */

@Transactional
@RequiredArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final DomainEventPublisher domainEventPublisher;

    public Customer createCustomer(String name, Money creditLimit) {
        ResultWithEvents<Customer> customerWithEvents = Customer.create(name, creditLimit);
        Customer customer = customerRepository.save(customerWithEvents.result);
        domainEventPublisher.publish(Customer.class, customer.getId(), customerWithEvents.events);
        return customer;
    }

    public Customer getCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("not found customer!!!"));
    }
}
