package com.dt.delivery_tracker.domain.repository;

import com.dt.delivery_tracker.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {}