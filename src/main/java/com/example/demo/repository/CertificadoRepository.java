package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Certificado;

public interface CertificadoRepository extends JpaRepository<Certificado, Long> {
    
}
