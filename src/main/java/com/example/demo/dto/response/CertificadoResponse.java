package com.example.demo.dto.response;

import java.time.LocalDate;

public record CertificadoResponse(
        Long id,
        Long componenteId,
        String cn,
        LocalDate venceEl
) {
}