package com.bhhan.tram.common;

public class OrderCancelConfirmedEvent implements OrderEvent {

  private OrderDetails orderDetails;

  public OrderCancelConfirmedEvent() {
  }

  public OrderCancelConfirmedEvent(OrderDetails orderDetails) {
    this.orderDetails = orderDetails;
  }

  public OrderDetails getOrderDetails() {
    return orderDetails;
  }
}
