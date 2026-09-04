package com.example.demo.dto.response;

/**
 * Versión "chica" de un Algoritmo, solo id + nombre. La usamos para el
 * campo "reemplazo" dentro de AlgoritmoResponse: si ahí pusiéramos un
 * AlgoritmoResponse completo, y ese reemplazo tuviera a su vez otro
 * reemplazo, el JSON podría anidarse en cadena sin necesidad. Con este
 * resumen cortamos la anidación en un nivel.
 */
public record AlgoritmoResumen(
        Long id,
        String nombre
) {}