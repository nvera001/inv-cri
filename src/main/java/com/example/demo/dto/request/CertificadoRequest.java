package com.example.demo.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CertificadoRequest (
    @NotNull(message = "El componenteId es obligatorio")
    Long componenteId,

    @NotBlank(message = "El CN es obligatorio")
    String cn,

    LocalDate venceEl
){}
