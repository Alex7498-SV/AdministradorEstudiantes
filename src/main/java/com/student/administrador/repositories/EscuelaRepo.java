package com.student.administrador.repositories;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.student.administrador.domain.CentroEscolar;

public interface EscuelaRepo extends JpaRepository<CentroEscolar, Integer> {

	@Query(nativeQuery=true,
			value="SELECT c.idEscolar AS Codigo, c.nombre as Descripcion, c.estado as Estado"
					+ "FROM public.centroEscolar c")
	public List<Object[]> catalogoEscuelas() throws DataAccessException;
}
