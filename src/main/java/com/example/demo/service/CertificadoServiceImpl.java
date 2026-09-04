package com.example.demo.service;

import com.example.demo.dto.request.CertificadoRequest;
import com.example.demo.dto.response.CertificadoResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Certificado;
import com.example.demo.model.Componente;
import com.example.demo.repository.CertificadoRepository;
import com.example.demo.repository.ComponenteRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificadoServiceImpl implements CertificadoService {

    private final CertificadoRepository certificadoRepository;
    private final ComponenteRepository componenteRepository;

    public CertificadoServiceImpl(CertificadoRepository certificadoRepository,
                                   ComponenteRepository componenteRepository) {
        this.certificadoRepository = certificadoRepository;
        this.componenteRepository = componenteRepository;
    }

    @Override
    public List<CertificadoResponse> listar() {
        return certificadoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CertificadoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidad(id));
    }

    @Override
    public CertificadoResponse crear(CertificadoRequest request) {
        Certificado certificado = new Certificado();
        aplicarDatos(certificado, request);
        return toResponse(certificadoRepository.save(certificado));
    }

    @Override
    public CertificadoResponse actualizar(Long id, CertificadoRequest request) {
        Certificado certificado = buscarEntidad(id);
        aplicarDatos(certificado, request);
        return toResponse(certificadoRepository.save(certificado));
    }

    @Override
    public void eliminar(Long id) {
        Certificado certificado = buscarEntidad(id);
        certificadoRepository.delete(certificado);
    }

    private Certificado buscarEntidad(Long id) {
        return certificadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificado no encontrado con id " + id));
    }

    private void aplicarDatos(Certificado certificado, CertificadoRequest request) {
        Componente componente = componenteRepository.findById(request.componenteId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Componente no encontrado con id " + request.componenteId()));

        certificado.setComponente(componente);
        certificado.setCn(request.cn());
        certificado.setVenceEl(request.venceEl());
    }

    private CertificadoResponse toResponse(Certificado certificado) {
        return new CertificadoResponse(
                certificado.getId(),
                certificado.getComponente().getId(),
                certificado.getCn(),
                certificado.getVenceEl()
        );
    }
}