package com.student.administrador.repositories;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.student.administrador.domain.Estudiante;

public interface EstudianteRepo extends JpaRepository<Estudiante, Integer> {
	
	@Query(nativeQuery=true,
		value="SELECT e.nombres, e.apellidos,  A.Aprobadas, A.Reprobadas, A.Promedio, e.idEstudiante " 
				+ 	"FROM public.estudiante e " 
				+ 	"LEFT JOIN public.estudiantemateria em ON e.idEstudiante = em.idEstudiante " 
				+ 	"LEFT JOIN public.materia mat ON em.idMateria = mat.idMateria " 
				+ 	"LEFT JOIN " 
				+ 	"	(SELECT COUNT(CASE WHEN mee.nota>=6 THEN 1 END) as Aprobadas, COUNT(CASE WHEN mee.nota < 6 THEN 1 END) as Reprobadas, SUM(mee.nota)/COUNT(mee.idEstudiante) as Promedio, mee.idEstudiante as IDd " 
				+ 	"		FROM public.estudiante ee " 
				+ 	"		LEFT JOIN public.estudiantemateria mee ON ee.idEstudiante = mee.idEstudiante " 
				+ 	"	 	LEFT JOIN public.materia matT ON mee.idMateria=matt.idMateria " 
				+ 	"	 	WHERE (LOWER(ee.nombres) LIKE ?1) OR (LOWER(SPLIT_PART(ee.nombres, ' ', 2)) LIKE ?1) OR " 
				+ 	"			(LOWER(ee.apellidos) LIKE ?2) OR (LOWER(SPLIT_PART(ee.apellidos, ' ', 2)) LIKE ?2) " 
				+ 	"	 	GROUP BY mee.idEstudiante) A ON em.idEstudiante = A.IDd " 
				+ 	"WHERE (LOWER(e.nombres) LIKE ?1) OR (LOWER(SPLIT_PART(e.nombres, ' ', 2)) LIKE ?1) OR " 
				+ 	"	(LOWER(e.apellidos) LIKE ?2) OR (LOWER(SPLIT_PART(e.apellidos, ' ', 2)) LIKE ?2) " 
				+ 	"GROUP BY e.nombres, e.apellidos, A.Aprobadas, A.reprobadas, A.Promedio, e.idEstudiante")
	public List<Object[]> expedientePorNombreApellido(String nombre, String apellido) throws DataAccessException;

	@Query(nativeQuery=true,
		value="SELECT es.idEstudiante, es.nombres, es.apellidos, es.carnet, es.fecha_nacimiento, es.edad, es.direccion, es.telefono_fijo, telefono_movil, es.nombre_padre, es.nombre_madre FROM public.estudiante es WHERE idEstudiante = ?1")
	public List<Object[]> findByIdEstudiante(Integer id) throws DataAccessException;
}