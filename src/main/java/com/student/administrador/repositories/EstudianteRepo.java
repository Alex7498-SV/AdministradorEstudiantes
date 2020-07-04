package com.student.administrador.repositories;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.student.administrador.domain.Estudiante;

public interface EstudianteRepo extends JpaRepository<Estudiante, Integer> {
	
	@Query(nativeQuery=true,
		value="SELECT e.nombres, e.apellidos,  A.Aprobadas, A.Reprobadas, A.Promedio\r\n" + 
				"FROM public.estudiante e\r\n" + 
				"	LEFT JOIN public.estudiantemateria em ON e.idEstudiante=em.idEstudiante\r\n" + 
				"	LEFT JOIN public.materia mat ON em.idMateria=mat.idMateria\r\n" + 
				"	LEFT JOIN \r\n" + 
				"	(SELECT COUNT(CASE WHEN mee.nota>=6 THEN 1 END) as Aprobadas, COUNT(CASE WHEN mee.nota < 6 THEN 1 END) as Reprobadas, SUM(mee.nota)/COUNT(mee.idEstudiante) as Promedio, mee.idEstudiante as IDd \r\n" + 
				"		FROM public.estudiante ee\r\n" + 
				"			LEFT JOIN public.estudiantemateria mee ON ee.idEstudiante=mee.idEstudiante\r\n" + 
				"	 		LEFT JOIN public.materia matT ON mee.idMateria=matt.idMateria \r\n" + 
				"	 	WHERE\r\n" + 
				"		 	ee.nombres LIKE ?1% OR ee.apellidos LIKE ?2%\r\n" + 
				"	 	GROUP BY mee.idEstudiante) A ON em.idEstudiante = A.IDd \r\n" + 
				"WHERE\r\n" + 
				"	e.nombres LIKE ?1% OR e.apellidos LIKE ?2%\r\n" + 
				"GROUP BY e.nombres, e.apellidos, A.Aprobadas, A.reprobadas, A.Promedio")
	public List<Object[]> expedientePorNombreApellido(String nombre, String apellido) throws DataAccessException;

	@Query(nativeQuery=true,
		value="SELECT es.idEstudiante, es.nombres FROM public.estudiante es WHERE idEstudiante = ?1")
	public List<Object[]> findByIdEstudiante(Integer id) throws DataAccessException;
}