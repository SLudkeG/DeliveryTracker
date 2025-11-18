package com.dt.delivery_tracker.controller.order;
import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequest(
        @NotBlank(message = "O nome do cliente é obrigatorio")
        String customerName
        ){}

