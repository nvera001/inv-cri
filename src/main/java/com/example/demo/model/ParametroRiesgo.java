package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "parametro_riesgo")
@NoArgsConstructor
@Getter
@Setter
public class ParametroRiesgo {
    @Id
    private Long id;

    private int aniosMigracion; // Y

    private int anioEstimadoCRQC; // año estimado en que existirá una computadora cuántica peligrosa (base para calcular Z)
}