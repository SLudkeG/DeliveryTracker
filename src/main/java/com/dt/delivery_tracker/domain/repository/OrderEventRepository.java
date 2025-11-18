package com.dt.delivery_tracker.domain.repository;

import com.dt.delivery_tracker.domain.order.OrderEvent;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderEventRepository extends JpaRepository<OrderEvent, Long> {
    List<OrderEvent> findByOrderIdOrderByEventTimeAsc(Long orderId);
}