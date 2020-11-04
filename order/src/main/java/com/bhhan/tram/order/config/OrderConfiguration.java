package com.bhhan.tram.order.config;

import com.bhhan.tram.order.service.CustomerEventConsumer;
import com.bhhan.tram.order.service.OrderService;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration;
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration;
import io.eventuate.tram.spring.optimisticlocking.OptimisticLockingDecoratorConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by hbh5274@gmail.com on 2020-11-04
 * Github : http://github.com/bhhan5274
 */

@Configuration
@Import({TramJdbcKafkaConfiguration.class,
        TramEventsPublisherConfiguration.class,
        TramEventSubscriberConfiguration.class,
        OptimisticLockingDecoratorConfiguration.class})
public class OrderConfiguration {
    @Bean
    public CustomerEventConsumer customerEventConsumer(OrderService orderService){
        return new CustomerEventConsumer(orderService);
    }

    @Bean
    public DomainEventDispatcher domainEventDispatcher(CustomerEventConsumer customerEventConsumer,
                                                       DomainEventDispatcherFactory domainEventDispatcherFactory){
        return domainEventDispatcherFactory.make("customerServiceEvents", customerEventConsumer.domainEventHandlers());
    }
}
