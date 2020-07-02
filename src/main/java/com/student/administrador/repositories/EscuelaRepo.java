package com.student.administrador.repositories;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.student.administrador.domain.CentroEscolar;

public interface EscuelaRepo extends JpaRepository<CentroEscolar, Integer> {

	/*@Query(nativeQuery=true,
			value="SELECT c.idEscolar AS Codigo, c.nombre as Descripcion, c.estado as Estado"
					+ "FROM public.centroEscolar c")*/
	//Por alguna razon las queries no sirven en lineas diferentes
	@Query(nativeQuery=true,
			value="SELECT c.idEscolar AS Codigo, c.nombre AS Descripcion, c.estado AS Estado, m.nombre AS municipio "
					+ "FROM public.centroEscolar c INNER JOIN public.municipio m ON m.idMunicipio = c.idMunicipio")
	public List<Object[]> catalogoEscuelas() throws DataAccessException;

	@Query(nativeQuery=true,
			value="SELECT c.idMunicipio, c.nombre FROM public.centroEscolar c WHERE c.idMunicipio = ?1 AND c.estado = true")
	public List<Object[]> escuelasPorMunicipio(Integer idMunicipio) throws DataAccessException;

}
