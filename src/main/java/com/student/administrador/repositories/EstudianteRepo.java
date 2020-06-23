package com.student.administrador.repositories;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.student.administrador.domain.Estudiante;

public interface EstudianteRepo extends JpaRepository<Estudiante, Integer> {

	List<Estudiante>findByNombre(String nombre) throws DataAccessException;
	List<Estudiante>findByApellido(String apellido) throws DataAccessException;
	
	@Query(nativeQuery=true,
			value="SELECT e.nombres, e.apellidos, COUNT(me.nota>=6) as Aprobadas, COUNT(me.nota<6) as Reprobadas, SUM(me.nota)/COUNT(me.nota) as Promedio"
					+ "FROM public.estudiante e, public.materiaEstudiante me, public.materias m "
					+ "WHERE e.idEstudiante = me.idEstudiante and  "
					+ "me.idMateria = m.idMateria and"
					+ "(e.nombre = ?1 or e.apellido = ?1)"
					+ "GROUP BY e.nombres, e.apellidos")
	public List<Object[]> expedientePorNombreApellido(String cadena) throws DataAccessException;
	
}
