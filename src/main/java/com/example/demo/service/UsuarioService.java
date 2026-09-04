package com.example.demo.service;

import com.example.demo.dto.request.CrearUsuarioRequest;
import com.example.demo.dto.response.UsuarioResponse;

public interface UsuarioService {
    UsuarioResponse crear(CrearUsuarioRequest request);
}
