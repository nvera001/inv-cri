package com.example.demo.dto.response;

import com.example.demo.model.enums.Ambiente;
import com.example.demo.model.enums.Criticidad;

public record SistemaResponse(
        Long id,
        String nombre,
        String area,
        Criticidad criticidad,
        Ambiente ambiente
) {}