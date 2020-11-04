package com.bhhan.tram.common;

public class CustomerCreditReleasedEvent extends AbstractCustomerOrderEvent {

  public CustomerCreditReleasedEvent() {
  }

  public CustomerCreditReleasedEvent(Long orderId) {
    super(orderId);
  }
}
