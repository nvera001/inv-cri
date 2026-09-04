package com.example.demo.dto.response;

// A propósito NUNCA incluye el password (ni el hash): no hay ningún
// motivo para que el hash de la contraseña salga de la base de datos.
public record UsuarioResponse(
        Long id,
        String username,
        String rol
) {
}
