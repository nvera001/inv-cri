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

import com.example.demo.dto.request.ComponenteRequest;
import com.example.demo.dto.response.ComponenteResponse;
import com.example.demo.service.ComponenteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/componentes")
public class ComponenteController {

    private final ComponenteService componenteService;

    public ComponenteController(ComponenteService componenteService) {
        this.componenteService = componenteService;
    }

    @GetMapping
    public List<ComponenteResponse> listar() {
        return componenteService.listar();
    }

    @GetMapping("/{id}")
    public ComponenteResponse buscarPorId(@PathVariable Long id) {
        return componenteService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<ComponenteResponse> crear(@Valid @RequestBody ComponenteRequest request) {
        ComponenteResponse creado = componenteService.crear(request);
        return ResponseEntity.created(URI.create("/api/componentes/" + creado.id())).body(creado);
    }

    @PutMapping("/{id}")
    public ComponenteResponse actualizar(@PathVariable Long id, @Valid @RequestBody ComponenteRequest request) {
        return componenteService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        componenteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}