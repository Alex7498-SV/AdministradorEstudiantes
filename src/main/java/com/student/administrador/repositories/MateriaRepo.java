package com.student.administrador.repositories;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.student.administrador.domain.Materia;

public interface MateriaRepo extends JpaRepository<Materia, Integer> {

	@Query(nativeQuery=true,
			value="SELECT m.nombre AS Materia, me.anio, me.ciclo, me.nota, CASE WHEN me.nota >=6 THEN 'Aprobada' WHEN me.nota <6 THEN 'Reprobada' END AS Resultado"
					+ "FROM public.estudiante e, public.materiaEstudiante me, public.materias m "
					+ "WHERE e.idEstudiante = me.idEstudiante and  me.idMateria = m.idMateria and "
					+ "e.nombre = ?1"
					+ "GROUP BY e.nombres, e.apellidos")
	public List<Object[]> materiasCursadasPorEstudiante(String cadena) throws DataAccessException;
	
	@Query(nativeQuery=true,
			value="SELECT m.idMateria AS Codigo, m.nombre as Descripcion, m.estado as Estado"
					+ "FROM public.materias m ")
	public List<Object[]> catalogoMaterias() throws DataAccessException;
}
