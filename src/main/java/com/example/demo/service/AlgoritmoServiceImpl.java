package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.response.AlgoritmoResponse;
import com.example.demo.dto.response.AlgoritmoResumen;
import com.example.demo.dto.request.AlgoritmoRequest;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Algoritmo;
import com.example.demo.repository.AlgoritmoRepository;

@Service
public class AlgoritmoServiceImpl implements AlgoritmoService{

    private final AlgoritmoRepository algoritmoRepository;

    public AlgoritmoServiceImpl(AlgoritmoRepository algoritmoRepository) {
        this.algoritmoRepository = algoritmoRepository;
    }

    @Override
    public List<AlgoritmoResponse> listar() {
        return algoritmoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public AlgoritmoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidad(id));
    }

    @Override
    public AlgoritmoResponse crear(AlgoritmoRequest request) {
        Algoritmo algoritmo = new Algoritmo();
        aplicarDatos(algoritmo, request);
        return toResponse(algoritmoRepository.save(algoritmo));
    }

    @Override
    public AlgoritmoResponse actualizar(Long id, AlgoritmoRequest request) {
        Algoritmo algoritmo = buscarEntidad(id);
        aplicarDatos(algoritmo, request);
        return toResponse(algoritmoRepository.save(algoritmo));
    }

    @Override
    public void eliminar(Long id) {
        if (!algoritmoRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe un algoritmo con id " + id);
        }
        algoritmoRepository.deleteById(id);
    }

    private Algoritmo buscarEntidad(Long id) {
        return algoritmoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un algoritmo con id " + id));
    }

    private void aplicarDatos(Algoritmo algoritmo, AlgoritmoRequest request) {
        algoritmo.setNombre(request.nombre());
        algoritmo.setFamilia(request.familia());
        algoritmo.setQuantumSafe(request.quantumSafe());

        // El reemplazo es opcional: si viene un id, lo buscamos y lo linkeamos;
        // si no, dejamos explícito que no tiene (por si se está "desvinculando" en un update).
        if (request.reemplazoId() != null) {
            Algoritmo reemplazo = algoritmoRepository.findById(request.reemplazoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No existe un algoritmo de reemplazo con id " + request.reemplazoId()));
            algoritmo.setReemplazo(reemplazo);
        } else {
            algoritmo.setReemplazo(null);
        }
    }

    private AlgoritmoResponse toResponse(Algoritmo algoritmo) {
        AlgoritmoResumen reemplazoResumen = null;
        if (algoritmo.getReemplazo() != null) {
            reemplazoResumen = new AlgoritmoResumen(
                    algoritmo.getReemplazo().getId(),
                    algoritmo.getReemplazo().getNombre()
            );
        }
        return new AlgoritmoResponse(
                algoritmo.getId(),
                algoritmo.getNombre(),
                algoritmo.getFamilia(),
                algoritmo.isQuantumSafe(), // boolean primitivo -> Lombok genera isQuantumSafe(), no getQuantumSafe()
                reemplazoResumen
        );
    }
}