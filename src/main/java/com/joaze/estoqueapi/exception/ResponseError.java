package com.joaze.estoqueapi.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ResponseError(
        LocalDateTime timestamp,
        Integer status,
        String error,
        String message,
        String path,
        List<FieldErrorResponse> errors
) {
}
