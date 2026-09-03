package com.example.demo.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.SistemaRequest;
import com.example.demo.dto.SistemaResponse;
import com.example.demo.service.SistemaService;

import jakarta.validation.Valid;

// @RestController = @Controller + @ResponseBody: cada método devuelve
// directamente el objeto serializado a JSON, no el nombre de una vista.
@RestController
@RequestMapping("/api/sistemas")
public class SistemaController {

    private final SistemaService sistemaService;

    public SistemaController(SistemaService sistemaService) {
        this.sistemaService = sistemaService;
    }

    @GetMapping
    public List<SistemaResponse> listar() {
        return sistemaService.listar();
    }

    @GetMapping("/{id}")
    public SistemaResponse buscarPorId(@PathVariable Long id) {
        return sistemaService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<SistemaResponse> crear(@Valid @RequestBody SistemaRequest request) {
        // @Valid dispara las anotaciones de SistemaRequest (@NotBlank, @NotNull);
        // si algo falla, Spring tira MethodArgumentNotValidException antes de
        // que este método se ejecute, y el GlobalExceptionHandler la atrapa.
        SistemaResponse creado = sistemaService.crear(request);
        // 201 Created + header Location con la URL del recurso nuevo, en vez de un 200 plano.
        return ResponseEntity.created(URI.create("/api/sistemas/" + creado.id())).body(creado);
    }

    @PutMapping("/{id}")
    public SistemaResponse actualizar(@PathVariable Long id, @Valid @RequestBody SistemaRequest request) {
        return sistemaService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        sistemaService.eliminar(id);
        return ResponseEntity.noContent().build(); // 204: éxito, sin cuerpo.
    }
}