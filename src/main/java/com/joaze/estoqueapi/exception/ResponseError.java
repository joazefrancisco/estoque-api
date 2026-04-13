package com.joaze.estoqueapi.exception;

import java.time.LocalDateTime;

public record ResponseError(
        LocalDateTime timestamp,
        Integer status,
        String message,
        String path
) {
}
