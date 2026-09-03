package com.example.demo.exception;

/**
 * Se lanza cuando se busca por id una entidad que no existe.
 * El GlobalExceptionHandler la traduce a un 404 con un cuerpo JSON prolijo,
 * en vez de que el cliente reciba una NullPointerException fea de Spring.
 */

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException (String message) {
        super(message);
    }
}
