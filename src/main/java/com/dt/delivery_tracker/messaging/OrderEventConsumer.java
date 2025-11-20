package com.dt.delivery_tracker.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.dt.delivery_tracker.config.RabbitMQConfig;
import com.dt.delivery_tracker.domain.order.OrderEvent;

@Component
public class OrderEventConsumer {

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void receive(OrderEvent event) {
        System.out.println("Evento recebido: " + event);
    }
}