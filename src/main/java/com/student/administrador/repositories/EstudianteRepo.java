package com.student.administrador.repositories;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.student.administrador.domain.Estudiante;

public interface EstudianteRepo extends JpaRepository<Estudiante, Integer> {
	
	@Query(nativeQuery=true,
			value="SELECT e.nombres, e.apellidos, COUNT(me.nota>=6) as Aprobadas, COUNT(me.nota<6) as Reprobadas, SUM(me.nota)/COUNT(me.nota) as Promedio"
					+ "FROM public.estudiante e, public.materiaEstudiante me, public.materia m "
					+ "WHERE e.idEstudiante = me.idEstudiante and  "
					+ "me.idMateria = m.idMateria and"
					+ "(e.nombres = ?1 or e.apellidos = ?1)"
					+ "GROUP BY e.nombres, e.apellidos")
	public List<Object[]> expedientePorNombreApellidos(String cadena) throws DataAccessException;
	
}
