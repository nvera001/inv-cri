package com.example.demo.service;

import com.example.demo.dto.request.CertificadoRequest;
import com.example.demo.dto.response.CertificadoResponse;

import java.util.List;

public interface CertificadoService {
    List<CertificadoResponse> listar();
    CertificadoResponse buscarPorId(Long id);
    CertificadoResponse crear(CertificadoRequest request);
    CertificadoResponse actualizar(Long id, CertificadoRequest request);
    void eliminar(Long id);
}