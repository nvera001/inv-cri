package com.example.demo.exception;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Forma estándar de error que va a devolver la API, sea un 404 (recurso
 * no encontrado) o un 400 (datos inválidos). Es un record porque es un
 * simple portador de datos inmutable, no necesita comportamiento propio.
 *
 * fieldErrors queda null en un 404; en un 400 de validación trae
 * campo -> mensaje (ej. "nombre" -> "El nombre es obligatorio").
 */

public record ApiError (
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    Map<String, String> fieldErrors
)
{}
