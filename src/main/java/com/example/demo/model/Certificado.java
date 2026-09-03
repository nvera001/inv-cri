package com.example.demo.model;

import java.time.LocalDate;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="certificado")
@NoArgsConstructor
@Getter
@Setter

public class Certificado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="componente_id", nullable = false)
    private Componente componente;

    @Column(nullable = false)
    private String cn;

    private LocalDate venceEl;
}
