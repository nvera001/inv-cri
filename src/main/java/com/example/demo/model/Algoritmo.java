package com.example.demo.model;

import com.example.demo.model.enums.FamiliaAlgoritmo;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="algoritmo")
@NoArgsConstructor
@Getter
@Setter
public class Algoritmo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    private FamiliaAlgoritmo familia;

    private boolean quantumSafe;

    @ManyToOne
    @JoinColumn(name = "reemplazo_id")
    private Algoritmo reemplazo;
}
