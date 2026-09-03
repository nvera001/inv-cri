package com.example.demo.model;

import com.example.demo.model.enums.TipoUso;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="componente")
@NoArgsConstructor
@Getter
@Setter
public class Componente {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="sistema_id",nullable=false)
    private Sistema sistema;

    @ManyToOne 
    @JoinColumn(name="algoritmo_id",nullable=false)
    private Algoritmo algoritmo;

    @Enumerated(EnumType.STRING)
    private TipoUso tipoUso;

    private int tamClave;

    private int vidaUtilDato;

}
