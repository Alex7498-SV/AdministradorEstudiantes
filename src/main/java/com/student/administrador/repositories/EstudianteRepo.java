package com.student.administrador.repositories;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.student.administrador.domain.Estudiante;

public interface EstudianteRepo extends JpaRepository<Estudiante, Integer> {
	
	@Query(nativeQuery=true,
			value="SELECT e.nombres, e.apellidos, "
					+ 						"(SELECT COUNT(mee.nota) FROM public.materiaEstudiante mee, public.estudiante ee "
					+ 						"WHERE mee.nota>=6 and mee.idEstudiante=ee.idEstudiante and (ee.nombres LIKE ?1% OR ee.apellidos LIKE ?2%)) as Aprobadas," 
					+ 						"(SELECT COUNT(mee.nota) FROM public.materiaEstudiante mee, public.estudiante ee "
					+ 						"WHERE mee.nota<6 and mee.idEstudiante=ee.idEstudiante and (ee.nombres LIKE ?1% OR ee.apellidos LIKE ?2%)) as Reprobadas," 
					+ 						"(SELECT SUM(mee.nota)/COUNT(mee.nota) FROM public.materiaEstudiante mee, public.estudiante ee"
					+ 						"WHERE mee.idEstudiante=ee.idEstudiante and (ee.nombres LIKE ?1% OR ee.apellidos LIKE ?2%)) as Promedio,"
					+ "FROM public.estudiante e, public.materiaEstudiante me"
					+ "WHERE e.idEstudiante = me.idEstudiante and"
					+ 			"(e.nombres LIKE ?1% OR e.apellidos LIKE ?2%)"
					+ "GROUP BY e.nombres, e.apellidos")
	public List<Object[]> expedientePorNombreApellido(String nombre, String apellido) throws DataAccessException;
	//por la gran puta pacheco me cagas, ponele nombre en singular
}
