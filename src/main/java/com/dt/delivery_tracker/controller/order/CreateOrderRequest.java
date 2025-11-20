package com.dt.delivery_tracker.controller.order;
import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(
        @NotNull(message = "O nome do cliente é obrigatorio")
        String customerName
        ){}

