package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Evaluacion;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long>{
    
}
