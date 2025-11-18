package com.dt.delivery_tracker.domain.order;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dt.delivery_tracker.domain.repository.OrderEventRepository;
import com.dt.delivery_tracker.domain.repository.OrderRepository;
import com.dt.delivery_tracker.messaging.OrderEventPublisher;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderEventRepository eventRepository;
    private final OrderEventPublisher publisher;

    public OrderService(OrderRepository orderRepository, OrderEventRepository eventRepository, OrderEventPublisher publisher) {
        this.orderRepository = orderRepository;
        this.eventRepository = eventRepository;
        this.publisher = publisher;
    }

    /**
     * Cria um novo pedido e registra um evento associado.
     *
     * @param customerName Nome do cliente
     * @return Pedido salvo no banco com ID gerado
     */
    @Transactional
    public Order createOrder(String customerName) {

        // Cria o pedido inicial
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setStatus("CREATED");

        Order saved = orderRepository.save(order);

        // Registra o evento de criação
        OrderEvent event = new OrderEvent();
        event.setOrder(saved);
        event.setEventType("ORDER_CREATED");
        event.setDescription("Pedido criado com sucesso.");

        eventRepository.save(event);
        publisher.publish(event);

        return saved;
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    /**
     * Atualiza o status do pedido e registra isso no histórico.
     *
     * @param orderId   ID do pedido
     * @param newStatus Novo status (ex: IN_TRANSIT, DELIVERED)
     */
    @Transactional
    public Order updateStatus(Long orderId, String newStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        order.setStatus(newStatus);
        order.setUpdatedAt(java.time.LocalDateTime.now());

        Order updated = orderRepository.save(order);

        OrderEvent event = new OrderEvent();
        event.setOrder(updated);
        event.setEventType("STATUS_CHANGED");
        event.setDescription("Status atualizado para: " + newStatus);

        eventRepository.save(event);
        publisher.publish(event);

        return updated;
    }
    }
