package com.example.demo.dto.response;

import com.example.demo.model.enums.FamiliaAlgoritmo;

public record AlgoritmoResponse (
    Long id,
    String nombre,
    FamiliaAlgoritmo familia,
    boolean quantumSafe,
    AlgoritmoResumen reemplazo
){}
