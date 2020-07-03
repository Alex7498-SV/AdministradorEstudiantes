package com.student.administrador.repositories;

import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.student.administrador.domain.Departamento;

public interface DepartamentoRepo extends JpaRepository<Departamento, Integer> {

    @Query(nativeQuery=true, 
            value="SELECT d.idDepartamento, d.nombre FROM public.departamento d")
    public List<Object[]> findAllDepartaments() throws DataAccessException;


    @Query(nativeQuery=true,
            value="SELECT * FROM public.departamento d WHERE d.idDepartamento = ?1")
    public Departamento depPorId(Integer idDep) throws DataAccessException;

}
