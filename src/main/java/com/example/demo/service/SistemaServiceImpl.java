package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.response.SistemaResponse;
import com.example.demo.dto.request.SistemaRequest;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Sistema;
import com.example.demo.repository.SistemaRepository;

// @Service registra la clase como bean de Spring, para que se pueda
// inyectar (acá, en el constructor de SistemaController).
@Service
public class SistemaServiceImpl implements SistemaService {

    // final + inyección por constructor (no @Autowired en el campo): el objeto
    // queda inmutable una vez construido y es fácil de testear pasando un mock.
    private final SistemaRepository sistemaRepository;

    public SistemaServiceImpl(SistemaRepository sistemaRepository) {
        this.sistemaRepository = sistemaRepository;
    }

    @Override
    public List<SistemaResponse> listar() {
        return sistemaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public SistemaResponse buscarPorId(Long id) {
        return toResponse(buscarEntidad(id));
    }

    @Override
    public SistemaResponse crear(SistemaRequest request) {
        Sistema sistema = new Sistema();
        aplicarDatos(sistema, request);
        return toResponse(sistemaRepository.save(sistema));
    }

    @Override
    public SistemaResponse actualizar(Long id, SistemaRequest request) {
        Sistema sistema = buscarEntidad(id);
        aplicarDatos(sistema, request);
        return toResponse(sistemaRepository.save(sistema));
    }

    @Override
    public void eliminar(Long id) {
        if (!sistemaRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe un sistema con id " + id);
        }
        sistemaRepository.deleteById(id);
    }

    private Sistema buscarEntidad(Long id) {
        return sistemaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un sistema con id " + id));
    }

    private void aplicarDatos(Sistema sistema, SistemaRequest request) {
        sistema.setNombre(request.nombre());
        sistema.setArea(request.area());
        sistema.setCriticidad(request.criticidad());
        sistema.setAmbiente(request.ambiente());
    }

    private SistemaResponse toResponse(Sistema sistema) {
        return new SistemaResponse(
                sistema.getId(),
                sistema.getNombre(),
                sistema.getArea(),
                sistema.getCriticidad(),
                sistema.getAmbiente()
        );
    }
}