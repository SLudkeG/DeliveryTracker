package com.dt.delivery_tracker.repository;

import com.dt.delivery_tracker.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {}

