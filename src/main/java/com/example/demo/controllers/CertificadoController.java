package com.example.demo.controllers;

import com.example.demo.dto.request.CertificadoRequest;
import com.example.demo.dto.response.CertificadoResponse;
import com.example.demo.service.CertificadoService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/certificados")
public class CertificadoController {

    private final CertificadoService certificadoService;

    public CertificadoController(CertificadoService certificadoService) {
        this.certificadoService = certificadoService;
    }

    @GetMapping
    public List<CertificadoResponse> listar() {
        return certificadoService.listar();
    }

    @GetMapping("/{id}")
    public CertificadoResponse buscarPorId(@PathVariable Long id) {
        return certificadoService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<CertificadoResponse> crear(@Valid @RequestBody CertificadoRequest request) {
        CertificadoResponse creado = certificadoService.crear(request);
        return ResponseEntity.created(URI.create("/api/certificados/" + creado.id())).body(creado);
    }

    @PutMapping("/{id}")
    public CertificadoResponse actualizar(@PathVariable Long id, @Valid @RequestBody CertificadoRequest request) {
        return certificadoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        certificadoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}