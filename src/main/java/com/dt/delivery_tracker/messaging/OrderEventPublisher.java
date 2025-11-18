package com.dt.delivery_tracker.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.dt.delivery_tracker.config.RabbitMQConfig;
import com.dt.delivery_tracker.domain.order.OrderEvent;

@Component
public class OrderEventPublisher {

    private RabbitTemplate rabbitTemplate;

    public OrderEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(OrderEvent event) {
        rabbitTemplate.convertAndSend
        (RabbitMQConfig.EXCHANGE, "orders.events.created", event);
    }
}
