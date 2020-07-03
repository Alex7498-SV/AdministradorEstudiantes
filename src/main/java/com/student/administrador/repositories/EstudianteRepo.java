package com.student.administrador.repositories;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.student.administrador.domain.Estudiante;

public interface EstudianteRepo extends JpaRepository<Estudiante, Integer> {
	
	@Query(nativeQuery=true,
		value="SELECT e.nombres, e.apellidos,  A.Aprobadas, A.Reprobadas, A.Promedio " + 
				"FROM public.estudiante e\r\n" + 
				"	LEFT JOIN public.estudiantemateria em ON e.idEstudiante=em.idEstudiante " + 
				"	LEFT JOIN public.materia mat ON em.idMateria=mat.idMateria " + 
				"	LEFT JOIN " + 
				"	(SELECT sum(CASE WHEN mee.nota>=6 THEN 1 END) as Aprobadas, COUNT(CASE WHEN mee.nota < 6 THEN 1 END) as Reprobadas, SUM(mee.nota)/COUNT(mee.idEstudiante) as Promedio, mee.idEstudiante as IDd " + 
				"		FROM public.estudiante ee " + 
				"			LEFT JOIN public.estudiantemateria mee ON ee.idEstudiante=mee.idEstudiante " + 
				"	 		LEFT JOIN public.materia matt ON mee.idMateria=matt.idMateria " + 
				"	 	WHERE\r\n" + 
				"		 	(e.nombres LIKE ?1% OR e.apellidos LIKE '?2%') " + 
				"	 	GROUP BY mee.idEstudiante) A ON em.idEstudiante = A.IDd " + 
				"WHERE " + 
				"	(e.nombres LIKE ?1% OR e.apellidos LIKE '?2%') " + 
				"GROUP BY e.nombres, e.apellidos, A.Aprobadas, A.reprobadas, A.Promedio")
	public List<Object[]> expedientePorNombreApellido(String nombre, String apellido) throws DataAccessException;

	public Estudiante findOneEstudiante(Integer code) throws DataAccessException;
}