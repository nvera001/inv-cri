package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.response.AlgoritmoResponse;
import com.example.demo.dto.response.AlgoritmoResumen;
import com.example.demo.dto.request.ComponenteRequest;
import com.example.demo.dto.response.ComponenteResponse;
import com.example.demo.dto.response.SistemaResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Algoritmo;
import com.example.demo.model.Componente;
import com.example.demo.model.Sistema;
import com.example.demo.repository.AlgoritmoRepository;
import com.example.demo.repository.ComponenteRepository;
import com.example.demo.repository.SistemaRepository;

@Service
public class ComponenteServiceImpl implements ComponenteService {

    private final ComponenteRepository componenteRepository;
    private final SistemaRepository sistemaRepository;
    private final AlgoritmoRepository algoritmoRepository;

    public ComponenteServiceImpl(ComponenteRepository componenteRepository,
                                  SistemaRepository sistemaRepository,
                                  AlgoritmoRepository algoritmoRepository) {
        this.componenteRepository = componenteRepository;
        this.sistemaRepository = sistemaRepository;
        this.algoritmoRepository = algoritmoRepository;
    }

    @Override
    public List<ComponenteResponse> listar() {
        return componenteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ComponenteResponse buscarPorId(Long id) {
        return toResponse(buscarEntidad(id));
    }

    @Override
    public ComponenteResponse crear(ComponenteRequest request) {
        Componente componente = new Componente();
        aplicarDatos(componente, request);
        return toResponse(componenteRepository.save(componente));
    }

    @Override
    public ComponenteResponse actualizar(Long id, ComponenteRequest request) {
        Componente componente = buscarEntidad(id);
        aplicarDatos(componente, request);
        return toResponse(componenteRepository.save(componente));
    }

    @Override
    public void eliminar(Long id) {
        if (!componenteRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe un componente con id " + id);
        }
        componenteRepository.deleteById(id);
    }

    private Componente buscarEntidad(Long id) {
        return componenteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un componente con id " + id));
    }

    private void aplicarDatos(Componente componente, ComponenteRequest request) {
        // Si el sistemaId o algoritmoId no existen en la base, esto tira
        // ResourceNotFoundException ANTES de guardar nada.
        Sistema sistema = sistemaRepository.findById(request.sistemaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un sistema con id " + request.sistemaId()));

        Algoritmo algoritmo = algoritmoRepository.findById(request.algoritmoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un algoritmo con id " + request.algoritmoId()));

        componente.setSistema(sistema);
        componente.setAlgoritmo(algoritmo);
        componente.setTipoUso(request.tipoUso());
        componente.setTamClave(request.tamClave());
        componente.setVidaUtilDato(request.vidaUtilDato());
    }

    // Nota: este mapeo repite lógica que ya está en SistemaServiceImpl.toResponse()
    // y AlgoritmoServiceImpl.toResponse(). Queda duplicado a propósito por ahora
    // (no vale la pena meter una capa de "mapper" compartido antes de que haga falta);
    // si el mapeo crece o se repite en más lugares, ese es el momento de extraerlo.
    private ComponenteResponse toResponse(Componente componente) {
        Sistema sistema = componente.getSistema();
        Algoritmo algoritmo = componente.getAlgoritmo();

        SistemaResponse sistemaResponse = new SistemaResponse(
                sistema.getId(), sistema.getNombre(), sistema.getArea(),
                sistema.getCriticidad(), sistema.getAmbiente()
        );

        AlgoritmoResumen reemplazoResumen = null;
        if (algoritmo.getReemplazo() != null) {
            reemplazoResumen = new AlgoritmoResumen(
                    algoritmo.getReemplazo().getId(),
                    algoritmo.getReemplazo().getNombre()
            );
        }
        AlgoritmoResponse algoritmoResponse = new AlgoritmoResponse(
                algoritmo.getId(), algoritmo.getNombre(), algoritmo.getFamilia(),
                algoritmo.isQuantumSafe(), reemplazoResumen
        );

        return new ComponenteResponse(
                componente.getId(),
                sistemaResponse,
                algoritmoResponse,
                componente.getTipoUso(),
                componente.getTamClave(),
                componente.getVidaUtilDato()
        );
    }
}