package com.student.administrador.repositories;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.student.administrador.domain.Municipio;

public interface MunicipioRepo extends JpaRepository<Municipio, Integer> {

    @Query(nativeQuery=true,
            value="SELECT * FROM public.municipio m WHERE c.idDepartamento = ?1")
    public List<Object[]> municipiosPorDep(Integer idDep) throws DataAccessException; 

}
