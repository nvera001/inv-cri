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

import com.example.demo.dto.response.AlgoritmoResponse;
import com.example.demo.dto.request.AlgoritmoRequest;
import com.example.demo.service.AlgoritmoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/algoritmos")
public class AlgoritmoController {

    private final AlgoritmoService algoritmoService;

    public AlgoritmoController(AlgoritmoService algoritmoService) {
        this.algoritmoService = algoritmoService;
    }

    @GetMapping
    public List<AlgoritmoResponse> listar() {
        return algoritmoService.listar();
    }

    @GetMapping("/{id}")
    public AlgoritmoResponse buscarPorId(@PathVariable Long id) {
        return algoritmoService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<AlgoritmoResponse> crear(@Valid @RequestBody AlgoritmoRequest request) {
        AlgoritmoResponse creado = algoritmoService.crear(request);
        return ResponseEntity.created(URI.create("/api/algoritmos/" + creado.id())).body(creado);
    }

    @PutMapping("/{id}")
    public AlgoritmoResponse actualizar(@PathVariable Long id, @Valid @RequestBody AlgoritmoRequest request) {
        return algoritmoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        algoritmoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}