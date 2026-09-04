package com.example.demo.service;

import com.example.demo.dto.request.EvaluacionRequest;
import com.example.demo.dto.response.EvaluacionResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Componente;
import com.example.demo.model.Evaluacion;
import com.example.demo.model.ParametroRiesgo;
import com.example.demo.model.enums.Prioridad;
import com.example.demo.repository.ComponenteRepository;
import com.example.demo.repository.EvaluacionRepository;
import com.example.demo.repository.ParametroRiesgoRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EvaluacionServiceImpl implements EvaluacionService {

    private static final Long ID_PARAMETRO_UNICO = 1L;

    private final EvaluacionRepository evaluacionRepository;
    private final ComponenteRepository componenteRepository;
    private final ParametroRiesgoRepository parametroRiesgoRepository;

    public EvaluacionServiceImpl(EvaluacionRepository evaluacionRepository,
                                  ComponenteRepository componenteRepository,
                                  ParametroRiesgoRepository parametroRiesgoRepository) {
        this.evaluacionRepository = evaluacionRepository;
        this.componenteRepository = componenteRepository;
        this.parametroRiesgoRepository = parametroRiesgoRepository;
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

    // NUEVO: calcula automáticamente margenAnios y prioridad con la
    // fórmula de Mosca, en vez de recibirlos a mano en el body.
    @Override
    public EvaluacionResponse calcular(Long componenteId) {
        Componente componente = componenteRepository.findById(componenteId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Componente no encontrado con id " + componenteId));

        ParametroRiesgo parametro = parametroRiesgoRepository.findById(ID_PARAMETRO_UNICO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Todavía no hay parámetros de riesgo configurados. Configurá GET/PUT /api/parametros-riesgo primero."));

        boolean quantumSafe = componente.getAlgoritmo().isQuantumSafe();

        CalculadoraRiesgo.ResultadoRiesgo resultado = CalculadoraRiesgo.calcular(
                componente.getVidaUtilDato(),
                parametro.getAniosMigracion(),
                parametro.getAnioEstimadoCRQC(),
                quantumSafe
        );

        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setComponente(componente);
        evaluacion.setFecha(LocalDate.now());
        evaluacion.setMargenAnios(resultado.margenAnios());
        evaluacion.setPrioridad(resultado.prioridad());

        return toResponse(evaluacionRepository.save(evaluacion));
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