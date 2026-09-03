package com.example.demo.model;

import java.time.LocalDate;

import com.example.demo.model.enums.Prioridad;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="evaluacion")
@NoArgsConstructor
@Setter
@Getter

public class Evaluacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="componente_id",nullable = false)
    private Componente componente;

    @Column(nullable = false)
    private LocalDate fecha;

    private int margenAnios;

    @Enumerated(EnumType.STRING)
    private Prioridad prioridad;
}
