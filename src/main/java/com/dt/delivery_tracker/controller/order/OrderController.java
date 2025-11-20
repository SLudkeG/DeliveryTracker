package com.dt.delivery_tracker.controller.order;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.dt.delivery_tracker.domain.order.Order;
import com.dt.delivery_tracker.domain.order.OrderEvent;
import com.dt.delivery_tracker.domain.order.OrderService;
import com.dt.delivery_tracker.domain.repository.OrderEventResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public OrderResponse create(@RequestBody @Valid CreateOrderRequest request) {
        Order order = service.createOrder(request.customerName());
        return toResponse(order);
    }

    @PutMapping("/{id}/status")
    public OrderResponse updateStatus(
            @PathVariable Long id,
            @RequestBody @Valid UpdateStatusRequest request) {

        Order updated = service.updateStatus(id, request.newStatus());
        return toResponse(updated);
    }

    @GetMapping("/{id}")
    public OrderResponse findById(@PathVariable Long id) {
        return toResponse(service.findById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/{id}/events")
    public List<OrderEventResponse> events(@PathVariable Long id) {
        return service.getOrderEvents(id);
    }

    @GetMapping
    public List<OrderResponse> list() {
        return service.list()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/events")
    public List<OrderEventResponse> listAllEvents() {
        return service.findAllEvents()
                .stream()
                .map(this::toEventResponse)
                .toList();
    }   

    private OrderEventResponse toEventResponse(OrderEvent event) {
        return new OrderEventResponse(
                        event.getOrder().getId(),
                        event.getEventType(),
                        event.getEventTime(),
                        event.getDescription());
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomerName(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getUpdatedAt());
    }
}
