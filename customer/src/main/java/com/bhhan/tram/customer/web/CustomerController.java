package com.bhhan.tram.customer.web;

import com.bhhan.tram.customer.domain.Customer;
import com.bhhan.tram.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hbh5274@gmail.com on 2020-11-04
 * Github : http://github.com/bhhan5274
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/customers")
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public CreateCustomerResponse createCustomer(@RequestBody CreateCustomerRequest createCustomerRequest) {
        Customer customer = customerService.createCustomer(createCustomerRequest.getName(), createCustomerRequest.getCreditLimit());
        return new CreateCustomerResponse(customer.getId());
    }

    @GetMapping("/{customerId}")
    public Customer getCustomer(@PathVariable Long customerId){
        Customer customer = customerService.getCustomer(customerId);
        log.info("================================");
        log.info(customer.toString());
        log.info("================================");
        return customer;
    }
}