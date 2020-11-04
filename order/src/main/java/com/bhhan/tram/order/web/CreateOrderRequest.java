package com.bhhan.tram.order.web;

import com.bhhan.tram.common.Money;

public class CreateOrderRequest {
  private Money orderTotal;
  private Long customerId;

  public CreateOrderRequest() {
  }

  public CreateOrderRequest(Long customerId, Money orderTotal) {
    this.customerId = customerId;
    this.orderTotal = orderTotal;
  }

  public Money getOrderTotal() {
    return orderTotal;
  }

  public Long getCustomerId() {
    return customerId;
  }
}
