package com.example.demo.dto.request;

import jakarta.validation.constraints.Positive;

public record ParametroRiesgoRequest(
        @Positive(message = "Los años de migración deben ser un número positivo")
        int aniosMigracion,

        @Positive(message = "El año estimado de CRQC debe ser un número positivo")
        int anioEstimadoCRQC
) {
}