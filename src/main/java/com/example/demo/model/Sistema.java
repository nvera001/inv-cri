package com.example.demo.model;

import com.example.demo.model.enums.Ambiente;
import com.example.demo.model.enums.Criticidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table (name = "sistema")
@NoArgsConstructor
@Getter
@Setter
public class Sistema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String nombre;

    private String area;

    @Enumerated (EnumType.STRING)
    private Criticidad criticidad;

    @Enumerated (EnumType.STRING)
    private Ambiente ambiente;

}
