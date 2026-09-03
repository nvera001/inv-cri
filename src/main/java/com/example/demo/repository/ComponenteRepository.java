package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Componente;

public interface ComponenteRepository extends JpaRepository<Componente, Long>{
    
}
