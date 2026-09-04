package com.example.demo.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @RestControllerAdvice intercepta las excepciones que tiran los
 * controllers de TODA la app antes de que lleguen al cliente, y las
 * convierte en una respuesta HTTP con el ApiError de arriba. Así los
 * controllers no necesitan un try/catch en cada método.
 */

@RestControllerAdvice
public class GlobalExceptionHandler  {
    // Se dispara cuando un service tira ResourceNotFoundException.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Se dispara automáticamente cuando falla un @Valid sobre un @RequestBody.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fe ->
                fieldErrors.put(fe.getField(), fe.getDefaultMessage())
        );
        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Error de validación",
                fieldErrors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Se dispara cuando un valor no puede convertirse a un enum válido
    // (por ejemplo, una "prioridad" que no es CRITICO/ALTO/MEDIO/OK).
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Se dispara cuando el login falla (usuario inexistente O contraseña
    // incorrecta). A propósito usamos el MISMO mensaje genérico para los
    // dos casos: si le dijéramos al cliente "el usuario no existe" vs
    // "la contraseña está mal", alguien podría usar esa diferencia para
    // averiguar qué usernames existen en el sistema probando uno por uno.
    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ApiError> handleCredencialesInvalidas(CredencialesInvalidasException ex) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
    // Se dispara cuando LoginAttemptService detecta demasiados intentos
    // fallidos de login seguidos para el mismo usuario (fuerza bruta).
    @ExceptionHandler(DemasiadosIntentosException.class)
    public ResponseEntity<ApiError> handleDemasiadosIntentos(DemasiadosIntentosException ex) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.TOO_MANY_REQUESTS.value(),
                HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase(),
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(error);
    }

    // Red de seguridad final: cualquier excepción no contemplada arriba
    // (una NullPointerException, un error de la base de datos, etc.) NO
    // debe devolver el HTML/stacktrace crudo default de Spring. La
    // atajamos acá y devolvemos el mismo ApiError de siempre con 500,
    // sin exponer detalles internos del server en el mensaje.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenerico(Exception ex) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Ocurrió un error inesperado en el servidor",
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
