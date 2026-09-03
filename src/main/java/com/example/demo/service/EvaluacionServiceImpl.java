package com.example.demo.service;

import com.example.demo.dto.request.EvaluacionRequest;
import com.example.demo.dto.response.EvaluacionResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Componente;
import com.example.demo.model.Evaluacion;
import com.example.demo.model.enums.Prioridad;
import com.example.demo.repository.ComponenteRepository;
import com.example.demo.repository.EvaluacionRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluacionServiceImpl implements EvaluacionService {

    private final EvaluacionRepository evaluacionRepository;
    private final ComponenteRepository componenteRepository;

    public EvaluacionServiceImpl(EvaluacionRepository evaluacionRepository,
                                  ComponenteRepository componenteRepository) {
        this.evaluacionRepository = evaluacionRepository;
        this.componenteRepository = componenteRepository;
    }

    @Override
    public List<EvaluacionResponse> listar() {
        return evaluacionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public EvaluacionResponse buscarPorId(Long id) {
        return toResponse(buscarEntidad(id));
    }

    @Override
    public EvaluacionResponse crear(EvaluacionRequest request) {
        Evaluacion evaluacion = new Evaluacion();
        aplicarDatos(evaluacion, request);
        return toResponse(evaluacionRepository.save(evaluacion));
    }

    @Override
    public EvaluacionResponse actualizar(Long id, EvaluacionRequest request) {
        Evaluacion evaluacion = buscarEntidad(id);
        aplicarDatos(evaluacion, request);
        return toResponse(evaluacionRepository.save(evaluacion));
    }

    @Override
    public void eliminar(Long id) {
        Evaluacion evaluacion = buscarEntidad(id);
        evaluacionRepository.delete(evaluacion);
    }

    private Evaluacion buscarEntidad(Long id) {
        return evaluacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluacion no encontrada con id " + id));
    }

    private void aplicarDatos(Evaluacion evaluacion, EvaluacionRequest request) {
        Componente componente = componenteRepository.findById(request.componenteId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Componente no encontrado con id " + request.componenteId()));

        Prioridad prioridad = parsearPrioridad(request.prioridad());

        evaluacion.setComponente(componente);
        evaluacion.setFecha(request.fecha());
        evaluacion.setMargenAnios(request.margenAnios());
        evaluacion.setPrioridad(prioridad);
    }

    private Prioridad parsearPrioridad(String valor) {
        try {
            return Prioridad.valueOf(valor.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Prioridad inválida: '" + valor + "'. Valores permitidos: CRITICO, ALTO, MEDIO, OK");
        }
    }

    private EvaluacionResponse toResponse(Evaluacion evaluacion) {
        return new EvaluacionResponse(
                evaluacion.getId(),
                evaluacion.getComponente().getId(),
                evaluacion.getFecha(),
                evaluacion.getMargenAnios(),
                evaluacion.getPrioridad().name()
        );
    }
}