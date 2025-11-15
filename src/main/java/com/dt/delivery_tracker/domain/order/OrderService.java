package com.dt.delivery_tracker.repository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dt.delivery_tracker.domain.Order;
import com.dt.delivery_tracker.domain.OrderEvent;

@Service
public class OrderService {

     private final OrderRepository orderRepository;
     private final OrderEventRepository eventRepository;

     public OrderService(OrderRepository orderRepository, OrderEventRepository eventRepository) {
          this.orderRepository = orderRepository;
          this.eventRepository = eventRepository;
     }

     @Transactional
     public Order createOrder(String customerName) {
          // cria pedido e salva
          Order order = new Order();
          order.setCustomerName(customerName);
          order.setStatus("CREATED");

          Order saved = orderRepository.save(order);

          //registrar o evento
          OrderEvent event = new OrderEvent();
          event.setOrder(saved);
          event.setEventType("ORDER_CREATED");
          event.setDescription("Pedido criaddo com sucesso");

          eventRepository.save(event);

          return saved;
     }

     @Transactional
     public Order updateStatus(Long orderId, String newStatus) {
          Order order = orderRepository.findById(orderId)
          .orElseThrow(() -> new RuntimeException("Pedido nao encontrado"));

          order.setStatus(newStatus);
          order.setUpdatedAt(java.time.LocalDateTime.now());

          Order updated = orderRepository.save(order);

          // registrar o evento
          OrderEvent event = new OrderEvent();
          event.setOrder(updated);
          event.setEventType("STATUS_CHANGED");
          event.setDescription("Status atualizado para" + newStatus);

          eventRepository.save(event);

          return updated;

     }
}
