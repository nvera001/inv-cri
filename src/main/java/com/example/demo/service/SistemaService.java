package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.SistemaRequest;
import com.example.demo.dto.SistemaResponse;

/**
 * La interfaz define QUÉ hace el service, no CÓMO. El controller depende
 * de esta interfaz, no de SistemaServiceImpl directamente — eso permite,
 * por ejemplo, inyectar una implementación falsa en un test sin tocar el
 * controller.
 */
public interface SistemaService {
    List<SistemaResponse> listar();
    SistemaResponse buscarPorId(Long id);
    SistemaResponse crear(SistemaRequest request);
    SistemaResponse actualizar(Long id, SistemaRequest request);
    void eliminar(Long id);
}
