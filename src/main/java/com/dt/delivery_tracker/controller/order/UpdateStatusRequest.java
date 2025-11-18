package com.dt.delivery_tracker.controller.order;

import jakarta.validation.constraints.NotBlank;

public record UpdateStatusRequest(
        @NotBlank(message = "O novo status é obrigatório.")
        String newStatus
) {}
 