package com.student.administrador.repositories;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.student.administrador.domain.Estudiante;

public interface EstudianteRepo extends JpaRepository<Estudiante, Integer> {
	
	@Query(nativeQuery=true,
		value="SELECT e.nombres, e.apellidos,  A.Aprobadas, A.Reprobadas, A.Promedio"
		+	"FROM public.estudiante e, public.estudiantemateria me, public.materia mat,"
		+					"(SELECT COUNT(CASE WHEN mee.nota >= 6 THEN 1 END) as Aprobadas, COUNT(CASE WHEN mee.nota < 6 THEN 1 END) as Reprobadas, SUM(mee.nota)/COUNT(mee.idEstudiante) as Promedio, mee.idEstudiante as IDd" 
		+					"FROM public.estudiantemateria mee, public.estudiante ee"
		+					"WHERE mee.idEstudiante=ee.idEstudiante and (ee.nombres LIKE ?1% OR ee.apellidos LIKE ?2%) group by mee.idEstudiante) A"
		+	"WHERE e.idEstudiante = me.idEstudiante and"
		+		"me.idMateria = mat.idMateria and"
		+		"me.idEstudiante = A.IDd and"
		+		"(e.nombres LIKE ?1% OR e.apellidos LIKE ?2%)"
		+	"GROUP BY e.nombres, e.apellidos, A.Aprobadas, A.reprobadas, A.Promedio")
	public List<Object[]> expedientePorNombreApellido(String nombre, String apellido) throws DataAccessException;
}