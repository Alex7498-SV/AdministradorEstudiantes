package com.student.administrador.repositories;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.student.administrador.domain.EstudianteMateria;

public interface EstudianteMateriaRepo extends JpaRepository<EstudianteMateria, Integer> {

    @Query(value = "SELECT * from public.estudianteMateria where idEstudianteMateria = :id", nativeQuery = true)
    public List<Object[]> findEstudianteMateriaById(@Param("id") Integer id) throws DataAccessException;

    @Query(nativeQuery=true,
			value="SELECT m.nombre AS nombreM, e.nombres AS nombreE, me.anio AS anio, me.ciclo_cursado AS ciclo, me.nota AS nota, "
					+ "CASE WHEN me.nota >=6 THEN 'Aprobada' WHEN me.nota <6 THEN 'Reprobada' END AS resultado, me.idEstudianteMateria AS idEM "
					+ "FROM public.estudiante e, public.estudianteMateria me, public.materia m "
					+ "WHERE e.idEstudiante = me.idEstudiante and me.idMateria = m.idMateria and "
					+ "me.idEstudianteMateria = ?1 "
					+ "GROUP BY nombreM, nombreE, anio, ciclo, nota, resultado, idEM")
	public List<Object[]> findDtoById(Integer id) throws DataAccessException;
}
