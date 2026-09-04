package com.example.demo.service;

import com.example.demo.model.enums.Prioridad;
import com.example.demo.service.CalculadoraRiesgo.ResultadoRiesgo;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculadoraRiesgoTest {

    // Uso el año actual como base en vez de un año fijo (ej. 2026),
    // así el test sigue siendo válido sin importar cuándo se corra.
    private static final int ANIO_ACTUAL = LocalDate.now().getYear();

    @Test
    void margenNegativoDaCritico() {
        // Z = anioEstimadoCRQC - anioActual = 5. Y = 3 → Z - Y = 2. X = 10 → margen = 2 - 10 = -8.
        ResultadoRiesgo resultado = CalculadoraRiesgo.calcular(10, 3, ANIO_ACTUAL + 5, false);

        assertEquals(-8, resultado.margenAnios());
        assertEquals(Prioridad.CRITICO, resultado.prioridad());
    }

    @Test
    void margenCeroDaAlto() {
        // Z = 10. Y = 3 → Z - Y = 7. X = 7 → margen = 0 (límite inferior de la franja ALTO).
        ResultadoRiesgo resultado = CalculadoraRiesgo.calcular(7, 3, ANIO_ACTUAL + 10, false);

        assertEquals(0, resultado.margenAnios());
        assertEquals(Prioridad.ALTO, resultado.prioridad());
    }

    @Test
    void margenTresDaAlto() {
        // Mismo Z-Y = 7. X = 4 → margen = 3 (límite superior de la franja ALTO).
        ResultadoRiesgo resultado = CalculadoraRiesgo.calcular(4, 3, ANIO_ACTUAL + 10, false);

        assertEquals(3, resultado.margenAnios());
        assertEquals(Prioridad.ALTO, resultado.prioridad());
    }

    @Test
    void margenCuatroDaMedio() {
        // Mismo Z-Y = 7. X = 3 → margen = 4 (recién ahí pasa a MEDIO).
        ResultadoRiesgo resultado = CalculadoraRiesgo.calcular(3, 3, ANIO_ACTUAL + 10, false);

        assertEquals(4, resultado.margenAnios());
        assertEquals(Prioridad.MEDIO, resultado.prioridad());
    }

    @Test
    void quantumSafeSiempreDaOkAunqueElMargenSeaNegativo() {
        // Mismo caso que margenNegativoDaCritico (margen -8), pero con quantumSafe = true:
        // la prioridad tiene que ser OK igual, aunque el numero de margen no cambie.
        ResultadoRiesgo resultado = CalculadoraRiesgo.calcular(10, 3, ANIO_ACTUAL + 5, true);

        assertEquals(-8, resultado.margenAnios());
        assertEquals(Prioridad.OK, resultado.prioridad());
    }
}