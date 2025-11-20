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
import com.dt.delivery_tracker.domain.order.OrderStatus;

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
        Order mockSaved = new Order();
        mockSaved.setCustomerName("João");
        mockSaved.setStatus(OrderStatus.CREATED);

        when(orderRepository.save(any(Order.class))).thenReturn(mockSaved);

        Order created = service.createOrder("João");

        assertNotNull(created);
        assertEquals("João", created.getCustomerName());
        assertEquals(OrderStatus.CREATED, created.getStatus());

        verify(orderRepository).save(any(Order.class));
        verify(eventRepository).save(any(OrderEvent.class));
        verify(publisher).publish(any(OrderEvent.class));
    }

    @Test
    void testUpdateStatus_OrderNotFound() {
        when(orderRepository.findById(999L)).thenReturn(java.util.Optional.empty());

        assertThrows(com.dt.delivery_tracker.exception.ResourceNotFoundException.class,
                () -> service.updateStatus(999L, OrderStatus.IN_TRANSIT));
    }

    @Test
    void testFindById_OrderExists() {

        Order mockOrder = new Order();
        mockOrder.setCustomerName("Maria");

        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(mockOrder));

        Order result = service.findById(1L);

        assertNotNull(result);
        assertEquals("Maria", result.getCustomerName());
    }

    @Test
    void testFindById_OrderNotFound() {

        when(orderRepository.findById(999L)).thenReturn(java.util.Optional.empty());

        assertThrows(com.dt.delivery_tracker.exception.ResourceNotFoundException.class,
                () -> service.findById(999L));
    }

    @Test
    void testUpdateStatus() {
        Order existing = new Order();
        existing.setCustomerName("João");
        existing.setStatus(OrderStatus.CREATED);

        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(existing));
        when(orderRepository.save(any(Order.class))).thenReturn(existing);

        Order updated = service.updateStatus(1L, OrderStatus.IN_TRANSIT);

        assertEquals(OrderStatus.IN_TRANSIT, updated.getStatus());

        verify(orderRepository).findById(1L);
        verify(orderRepository).save(existing);
        verify(eventRepository).save(any(OrderEvent.class));
        verify(publisher).publish(any(OrderEvent.class));
    }
}
