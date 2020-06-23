package com.student.administrador.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.administrador.domain.Municipio;

public interface MunicipioRepo extends JpaRepository<Municipio, Integer> {

}
