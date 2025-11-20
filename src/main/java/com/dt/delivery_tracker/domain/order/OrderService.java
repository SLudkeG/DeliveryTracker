package com.dt.delivery_tracker.domain.order;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dt.delivery_tracker.domain.repository.OrderEventRepository;
import com.dt.delivery_tracker.domain.repository.OrderEventResponse;
import com.dt.delivery_tracker.domain.repository.OrderRepository;
import com.dt.delivery_tracker.messaging.OrderEventPublisher;
import com.dt.delivery_tracker.exception.ResourceNotFoundException;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderEventRepository eventRepository;
    private final OrderEventPublisher publisher;

    public OrderService(OrderRepository orderRepository, OrderEventRepository eventRepository,
            OrderEventPublisher publisher) {
        this.orderRepository = orderRepository;
        this.eventRepository = eventRepository;
        this.publisher = publisher;
    }

    public Order findById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));
    }
    
    /**
     * Cria um novo pedido e registra um evento associado.
     *
     * @param customerName Nome do cliente
     * @return Pedido salvo no banco com ID gerado
     */
    @Transactional
    public Order createOrder(String customerName) {

        Order order = new Order();
        order.setCustomerName(customerName);
        order.setStatus(OrderStatus.CREATED);

        Order saved = orderRepository.save(order);

        OrderEvent event = new OrderEvent();
        event.setOrder(saved);
        event.setEventType("ORDER_CREATED");
        event.setEventTime(LocalDateTime.now());
        event.setDescription("Pedido criado com sucesso.");

        eventRepository.save(event);
        publisher.publish(event);

        return saved;
    }

    public List<OrderEvent> findAllEvents() {
        return eventRepository.findAllByOrderByEventTimeAsc();
    }

    public List<OrderEventResponse> getOrderEvents(Long orderId) {

        findById(orderId);

        List<OrderEvent> events = eventRepository.findByOrderIdOrderByEventTimeAsc(orderId);

        return events.stream()
                .map(e -> new OrderEventResponse(
                        e.getId(),
                        e.getEventType(),
                        e.getEventTime(),
                        e.getDescription()))
                .toList();
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        Order order = findById(id);

        orderRepository.delete(order);
    }

    /**
     * Atualiza o status do pedido e registra isso no histórico.
     *
     * @param orderId   ID do pedido
     * @param newStatus Novo status (ex: IN_TRANSIT, DELIVERED)
     */
    @Transactional
    public Order updateStatus(Long orderId, OrderStatus newStatus) {
        
        Order order = findById(orderId);

        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());

        Order updated = orderRepository.save(order);

        OrderEvent event = createEvent(updated, "STATUS_CHANGED", "Status atualizado para: " + newStatus);
        eventRepository.save(event);
        publisher.publish(event);

        return updated;
    }

        private OrderEvent createEvent(Order order, String eventType, String description) {
        OrderEvent event = new OrderEvent();
        event.setOrder(order);
        event.setEventType(eventType);
        event.setEventTime(LocalDateTime.now());
        event.setDescription(description);
        return event;
    }
}
