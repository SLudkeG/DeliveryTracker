package domain.order;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.dt.delivery_tracker.domain.repository.OrderEventRepository;
import com.dt.delivery_tracker.domain.repository.OrderRepository;
import com.dt.delivery_tracker.messaging.OrderEventPublisher;
import com.dt.delivery_tracker.domain.order.Order;
import com.dt.delivery_tracker.domain.order.OrderEvent;
import com.dt.delivery_tracker.domain.order.OrderService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderServiceTest {

    private OrderRepository orderRepository;
    private OrderEventRepository eventRepository;
    private OrderEventPublisher publisher;
    private OrderService service;

    @BeforeEach
    void setup() {
        orderRepository = mock(OrderRepository.class);
        eventRepository = mock(OrderEventRepository.class);
        publisher = mock(OrderEventPublisher.class);

        service = new OrderService(orderRepository, eventRepository, publisher);
    }
    
    @Test
    void testCreateOrder() {
        Order mockOrder = new Order();
        mockOrder.setCustomerName("João");
        mockOrder.setStatus("CREATED");

        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        Order created = service.createOrder("João");

        assertNotNull(created);
        assertEquals("João", created.getCustomerName());
        assertEquals("CREATED", created.getStatus());

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(eventRepository, times(1)).save(any(OrderEvent.class));
        verify(publisher, times(1)).publish(any(OrderEvent.class));
    }
    @Test
void testUpdateStatus() {
    Order existing = new Order();
    existing.setCustomerName("João");
    existing.setStatus("CREATED");

    when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(existing));
    when(orderRepository.save(any(Order.class))).thenReturn(existing);

    Order updated = service.updateStatus(1L, "IN_TRANSIT");

    assertEquals("IN_TRANSIT", updated.getStatus());

    verify(orderRepository, times(1)).findById(1L);
    verify(orderRepository, times(1)).save(existing);
    verify(eventRepository, times(1)).save(any(OrderEvent.class));
    verify(publisher, times(1)).publish(any(OrderEvent.class));
    }
}
    