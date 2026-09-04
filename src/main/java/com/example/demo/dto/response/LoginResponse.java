package com.example.demo.dto.response;

public record LoginResponse(
        String token,
        String username,
        String rol
) {
}