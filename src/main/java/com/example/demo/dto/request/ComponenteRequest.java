package com.example.demo.dto.request;

import com.example.demo.model.enums.TipoUso;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ComponenteRequest (
    @NotNull(message = "El sistema es obligatorio")
    Long sistemaId,

    @NotNull(message = "El algoritmo es obligatorio")
    Long algoritmoId,

    @NotNull(message = "El tipo de uso es obligatorio")
    TipoUso tipoUso,

    @Positive(message = "El tamaño de clave debe ser mayor a 0")
    int tamClave,

    @Positive(message = "La vida útil del dato debe ser mayor a 0")
    int vidaUtilDato
) {}