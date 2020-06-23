package com.student.administrador.repositories;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.student.administrador.domain.Usuario;

public interface UsuarioRepo extends JpaRepository<Usuario, Integer> {

	@Query(nativeQuery=true,
			value="SELECT u.idUsuario AS Codigo, u.nombre as Descripcion, u.estado as Estado"
					+ "FROM public.usuario u ")
	public List<Object[]> catalogoUsuarios() throws DataAccessException;
}
