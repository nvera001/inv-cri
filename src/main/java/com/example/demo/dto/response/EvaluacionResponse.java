package com.example.demo.dto.response;

import java.time.LocalDate;

public record EvaluacionResponse(
        Long id,
        Long componenteId,
        LocalDate fecha,
        int margenAnios,
        String prioridad
) {
}