package com.example.demo.service;

import com.example.demo.dto.request.EvaluacionRequest;
import com.example.demo.dto.response.EvaluacionResponse;

import java.util.List;

public interface EvaluacionService {
    List<EvaluacionResponse> listar();
    EvaluacionResponse buscarPorId(Long id);
    EvaluacionResponse crear(EvaluacionRequest request);
    EvaluacionResponse actualizar(Long id, EvaluacionRequest request);
    void eliminar(Long id);
    EvaluacionResponse calcular(Long componenteId);
}