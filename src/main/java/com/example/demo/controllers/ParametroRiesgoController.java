package com.example.demo.controllers;

import com.example.demo.dto.request.ParametroRiesgoRequest;
import com.example.demo.dto.response.ParametroRiesgoResponse;
import com.example.demo.service.ParametroRiesgoService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parametros-riesgo")
public class ParametroRiesgoController {

    private final ParametroRiesgoService parametroRiesgoService;

    public ParametroRiesgoController(ParametroRiesgoService parametroRiesgoService) {
        this.parametroRiesgoService = parametroRiesgoService;
    }

    @GetMapping
    public ParametroRiesgoResponse obtener() {
        return parametroRiesgoService.obtener();
    }

    @PutMapping
    public ParametroRiesgoResponse actualizar(@Valid @RequestBody ParametroRiesgoRequest request) {
        return parametroRiesgoService.actualizar(request);
    }
}