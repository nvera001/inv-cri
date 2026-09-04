package com.example.demo.dto.request;

import com.example.demo.model.enums.FamiliaAlgoritmo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AlgoritmoRequest(
    @NotBlank(message="Nombre es obligatorio")
    String nombre,

    @NotNull(message="Familia es obligatoria")
    FamiliaAlgoritmo familia,

    boolean quantumSafe,

    // Opcional: id de otro Algoritmo que lo reemplaza. Null si no tiene reemplazo todavía.
    Long reemplazoId
)
{}
