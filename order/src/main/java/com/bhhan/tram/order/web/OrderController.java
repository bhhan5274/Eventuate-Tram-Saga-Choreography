package com.bhhan.tram.order.web;

import com.bhhan.tram.common.OrderDetails;
import com.bhhan.tram.order.domain.Order;
import com.bhhan.tram.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hbh5274@gmail.com on 2020-11-04
 * Github : http://github.com/bhhan5274
 */

@RestController
@RequestMapping(path = "/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest createOrderRequest){
        Order order = orderService.createOrder(new OrderDetails(createOrderRequest.getCustomerId(), createOrderRequest.getOrderTotal()));
        return new CreateOrderResponse(order.getId());
    }

    @GetMapping(value = "/{orderId}")
    public ResponseEntity<GetOrderResponse> getOrder(@PathVariable Long orderId){
        return orderService.getOrderOptional(orderId)
                .map(order -> makeSuccessfulResponse(order))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/{orderId}/cancel")
    public ResponseEntity<GetOrderResponse> cancelOrder(@PathVariable Long orderId){
        Order order = orderService.cancelOrder(orderId);
        return makeSuccessfulResponse(order);
    }

    private ResponseEntity<GetOrderResponse> makeSuccessfulResponse(Order order){
        return new ResponseEntity<>(new GetOrderResponse(order.getId(), order.getState()), HttpStatus.OK);
    }
}
