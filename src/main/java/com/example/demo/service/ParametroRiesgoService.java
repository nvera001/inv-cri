package com.example.demo.service;

import com.example.demo.dto.request.ParametroRiesgoRequest;
import com.example.demo.dto.response.ParametroRiesgoResponse;

public interface ParametroRiesgoService {
    ParametroRiesgoResponse obtener();
    ParametroRiesgoResponse actualizar(ParametroRiesgoRequest request);
}