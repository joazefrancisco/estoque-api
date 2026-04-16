package com.joaze.estoqueapi.dto.resposne;

public record ApiResponse<T>(
        String message,
        T data
) {}
