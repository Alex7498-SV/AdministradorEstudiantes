package com.student.administrador.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.administrador.domain.EstudianteMateria;

public interface EstudianteMateriaRepo extends JpaRepository<EstudianteMateria, Integer> {

}
