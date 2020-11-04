package com.bhhan.tram.customer.config;

import com.bhhan.tram.customer.service.OrderEventConsumer;
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
        TramEventSubscriberConfiguration.class,
        TramEventsPublisherConfiguration.class,
        OptimisticLockingDecoratorConfiguration.class})
public class CustomerConfiguration {
    @Bean
    public OrderEventConsumer orderEventConsumer(){
        return new OrderEventConsumer();
    }

    @Bean
    public DomainEventDispatcher domainEventDispatcher(OrderEventConsumer orderEventConsumer,
                                                       DomainEventDispatcherFactory domainEventDispatcherFactory){
        return domainEventDispatcherFactory.make("orderServiceEvents", orderEventConsumer.domainEventHandlers());
    }
}
