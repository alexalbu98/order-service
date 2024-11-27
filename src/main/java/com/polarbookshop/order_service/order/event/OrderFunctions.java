package com.polarbookshop.order_service.order.event;

import com.polarbookshop.order_service.domain.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

//RabbitMQ guarantees at least once delivery, so duplicates can appear.
@Configuration
public class OrderFunctions {
    private static final Logger log = LoggerFactory.getLogger(OrderFunctions.class);

    @Bean
    public Consumer<Flux<OrderDispatchedMessage>> dispatchOrder(OrderService orderService) {
        return orderDispatchedMessageFlux ->
                orderService.consumeOrderDispatchedEvent(orderDispatchedMessageFlux)
                        .doOnNext(order -> log.info("The order with id {} is dispatched", order.id()))
                        .subscribe();
    }
}
