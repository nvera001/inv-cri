package com.example.demo.service;

import com.example.demo.dto.request.ParametroRiesgoRequest;
import com.example.demo.dto.response.ParametroRiesgoResponse;
import com.example.demo.model.ParametroRiesgo;
import com.example.demo.repository.ParametroRiesgoRepository;

import org.springframework.stereotype.Service;

@Service
public class ParametroRiesgoServiceImpl implements ParametroRiesgoService {

    private static final Long ID_UNICO = 1L;

    private final ParametroRiesgoRepository parametroRiesgoRepository;

    public ParametroRiesgoServiceImpl(ParametroRiesgoRepository parametroRiesgoRepository) {
        this.parametroRiesgoRepository = parametroRiesgoRepository;
    }

    @Override
    public ParametroRiesgoResponse obtener() {
        return toResponse(buscarOCrearPorDefecto());
    }

    @Override
    public ParametroRiesgoResponse actualizar(ParametroRiesgoRequest request) {
        ParametroRiesgo parametro = buscarOCrearPorDefecto();
        parametro.setAniosMigracion(request.aniosMigracion());
        parametro.setAnioEstimadoCRQC(request.anioEstimadoCRQC());
        return toResponse(parametroRiesgoRepository.save(parametro));
    }

    // Si todavía no existe la fila de configuración, la crea con valores
    // por defecto razonables (3 años de migración, CRQC estimado en 2035),
    // así el endpoint GET nunca devuelve un 404 por esto.
    private ParametroRiesgo buscarOCrearPorDefecto() {
        return parametroRiesgoRepository.findById(ID_UNICO)
                .orElseGet(() -> {
                    ParametroRiesgo nuevo = new ParametroRiesgo();
                    nuevo.setId(ID_UNICO);
                    nuevo.setAniosMigracion(3);
                    nuevo.setAnioEstimadoCRQC(2035);
                    return parametroRiesgoRepository.save(nuevo);
                });
    }

    private ParametroRiesgoResponse toResponse(ParametroRiesgo parametro) {
        return new ParametroRiesgoResponse(parametro.getAniosMigracion(), parametro.getAnioEstimadoCRQC());
    }
}