package com.example.demo.dto.response;

import com.example.demo.model.enums.TipoUso;

public record ComponenteResponse(
        Long id,
        SistemaResponse sistema,
        AlgoritmoResponse algoritmo,
        TipoUso tipoUso,
        int tamClave,
        int vidaUtilDato
) {}