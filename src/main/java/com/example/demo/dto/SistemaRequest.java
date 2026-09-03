package com.example.demo.dto;

import com.example.demo.model.enums.Ambiente;
import com.example.demo.model.enums.Criticidad;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SistemaRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        String area,

        @NotNull(message = "La criticidad es obligatoria")
        Criticidad criticidad,

        @NotNull(message = "El ambiente es obligatorio")
        Ambiente ambiente
) {}