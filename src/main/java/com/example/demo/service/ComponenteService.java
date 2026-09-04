package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.request.ComponenteRequest;
import com.example.demo.dto.response.ComponenteResponse;

public interface ComponenteService {
    List<ComponenteResponse> listar();
    ComponenteResponse buscarPorId(Long id);
    ComponenteResponse crear(ComponenteRequest request);
    ComponenteResponse actualizar(Long id, ComponenteRequest request);
    void eliminar(Long id);
}
