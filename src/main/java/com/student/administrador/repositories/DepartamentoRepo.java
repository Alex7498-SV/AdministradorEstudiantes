package com.student.administrador.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.administrador.domain.Departamento;

public interface DepartamentoRepo extends JpaRepository<Departamento, Integer> {

}
