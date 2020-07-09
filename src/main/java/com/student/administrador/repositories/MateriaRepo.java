package com.student.administrador.repositories;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.student.administrador.domain.Materia;

public interface MateriaRepo extends JpaRepository<Materia, Integer> {

	@Query(nativeQuery=true,
			value="SELECT m.nombre AS nombreM, e.nombres AS nombreE, me.anio AS anio, me.ciclo_cursado AS ciclo, me.nota AS nota, "
					+ "CASE WHEN me.nota >=6 THEN 'Aprobada' WHEN me.nota <6 THEN 'Reprobada' END AS resultado, me.idEstudianteMateria AS idEM, m.idMateria AS idM "
					+ "FROM public.estudiante e, public.estudianteMateria me, public.materia m "
					+ "WHERE e.idEstudiante = me.idEstudiante and me.idMateria = m.idMateria and "
					+ "e.idEstudiante = ?1 "
					+ "GROUP BY nombreM, nombreE, anio, ciclo, nota, resultado, idEM, idM")
	public List<Object[]> materiasCursadasPorEstudiante(Integer id) throws DataAccessException;
	
	@Query(nativeQuery=true,
			value="SELECT m.idMateria AS Codigo, m.nombre as Descripcion, m.estado as Estado "
					+ "FROM public.materia m ")
	public List<Object[]> catalogoMaterias() throws DataAccessException;

	@Query(nativeQuery=true,
			value="SELECT m.idMateria AS Codigo, m.nombre as Descripcion, m.estado as Estado "
					+ "FROM public.materia m WHERE m.estado = true")
	public List<Object[]> materiasActivas() throws DataAccessException;
 /*
	@Query(nativeQuery = true,
		value="SELECT * FROM public.materia WHERE idMateria = ?1")*/
	public Materia findByIdMateria(Integer id) throws DataAccessException;
}