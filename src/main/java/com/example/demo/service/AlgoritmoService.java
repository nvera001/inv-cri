package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.response.AlgoritmoResponse;
import com.example.demo.dto.request.AlgoritmoRequest;

public interface  AlgoritmoService {
    List<AlgoritmoResponse> listar();
    AlgoritmoResponse buscarPorId(Long id);
    AlgoritmoResponse crear(AlgoritmoRequest request);
    AlgoritmoResponse actualizar(Long id, AlgoritmoRequest request);
    void eliminar(Long id);
}
