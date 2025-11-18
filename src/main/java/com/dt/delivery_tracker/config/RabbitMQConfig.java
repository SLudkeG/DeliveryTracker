package com.dt.delivery_tracker.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

        public static final Strng EXCHANGE = "orders.exchange";
        public static final String QUEUE = "orders.queue";
        public static final String ROUTING_KEY = "orders.routingkey";

        @Bean
        public TopicExchange exchange() {
                return new TopicExchange(EXCHANGE);
        }

        @Bean
        public Queue queue() { 
                return new Queue(QUEUE, true);
        }

        @Bean
        public Binding binding(Queue queue, TopicExchange exchange) {
                return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(ROUTING_KEY);
        }
}
