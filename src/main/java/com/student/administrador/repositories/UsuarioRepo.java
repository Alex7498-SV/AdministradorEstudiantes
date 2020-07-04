package com.student.administrador.repositories;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.student.administrador.domain.Usuario;

public interface UsuarioRepo extends JpaRepository<Usuario, Integer> {

	@Query(nativeQuery=true,
			value="SELECT u.idUsuario AS Codigo, u.nombre as Nombre, u.apellido as Apellido,  CONCAT('Dirección: ', u.direccion, '.') as Descripcion, "
					+ "u.usuario as Usuario, u.estado as Estado "
					+ "FROM public.usuario u where u.usuario != 'admin' ")
	public List<Object[]> catalogoUsuarios() throws DataAccessException;
	
	/*@Query(nativeQuery=true,
			value="SELECT * FROM public.usuario u WHERE u.usuario = ?1 AND u.contra =?2 ")*/
	List<Usuario> findByUsuarioAndContra(String usuario, String contra);
}
