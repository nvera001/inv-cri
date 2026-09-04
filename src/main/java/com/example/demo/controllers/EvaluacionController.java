package com.example.demo.controllers;

import com.example.demo.dto.request.EvaluacionRequest;
import com.example.demo.dto.response.EvaluacionResponse;
import com.example.demo.service.EvaluacionService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/evaluaciones")
public class EvaluacionController {

    private final EvaluacionService evaluacionService;

    public EvaluacionController(EvaluacionService evaluacionService) {
        this.evaluacionService = evaluacionService;
    }

    @GetMapping
    public List<EvaluacionResponse> listar() {
        return evaluacionService.listar();
    }

    @GetMapping("/{id}")
    public EvaluacionResponse buscarPorId(@PathVariable Long id) {
        return evaluacionService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<EvaluacionResponse> crear(@Valid @RequestBody EvaluacionRequest request) {
        EvaluacionResponse creada = evaluacionService.crear(request);
        return ResponseEntity.created(URI.create("/api/evaluaciones/" + creada.id())).body(creada);
    }

    @PostMapping("/calcular/{componenteId}")
    public ResponseEntity<EvaluacionResponse> calcular(@PathVariable Long componenteId) {
        EvaluacionResponse creada = evaluacionService.calcular(componenteId);
        return ResponseEntity.created(URI.create("/api/evaluaciones/" + creada.id())).body(creada);
    }

    @PutMapping("/{id}")
    public EvaluacionResponse actualizar(@PathVariable Long id, @Valid @RequestBody EvaluacionRequest request) {
        return evaluacionService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        evaluacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}