package com.example.demo.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EvaluacionRequest(
        @NotNull(message = "El componenteId es obligatorio")
        Long componenteId,

        @NotNull(message = "La fecha es obligatoria")
        LocalDate fecha,

        int margenAnios,

        @NotNull(message = "La prioridad es obligatoria")
        String prioridad
) {
}