package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CrearUsuarioRequest(
        @NotBlank(message = "El username es obligatorio")
        String username,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        String password,

        // Se recibe como String (no como el enum Rol directamente) para
        // poder convertir un valor inválido en un 400 claro en vez de un
        // error crudo de deserialización de Jackson. Mismo patrón que
        // Evaluacion.prioridad.
        @NotBlank(message = "El rol es obligatorio")
        String rol
) {
}
