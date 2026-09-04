package com.example.demo.service;

import com.example.demo.model.enums.Prioridad;

import java.time.LocalDate;

// Clase de cálculo puro: sin @Service, sin dependencias de Spring, sin
// tocar la base. Recibe números, devuelve un resultado. Se hace así
// a propósito para que sea trivial de testear con JUnit más adelante
// (paso 3 del roadmap: "motor de scoring CON TESTS").
public class CalculadoraRiesgo {

    public static ResultadoRiesgo calcular(int vidaUtilDato, int aniosMigracion, int anioEstimadoCRQC, boolean quantumSafe) {
        int anioActual = LocalDate.now().getYear();
        int z = anioEstimadoCRQC - anioActual;
        int margen = z - (vidaUtilDato + aniosMigracion);

        Prioridad prioridad;
        if (quantumSafe) {
            prioridad = Prioridad.OK;
        } else if (margen < 0) {
            prioridad = Prioridad.CRITICO;
        } else if (margen <= 3) {
            prioridad = Prioridad.ALTO;
        } else {
            prioridad = Prioridad.MEDIO;
        }

        return new ResultadoRiesgo(margen, prioridad);
    }

    public record ResultadoRiesgo(int margenAnios, Prioridad prioridad) {
    }
}