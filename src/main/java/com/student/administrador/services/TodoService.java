package com.student.administrador.services;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.student.administrador.domain.CentroEscolar;
import com.student.administrador.domain.Estudiante;
import com.student.administrador.domain.EstudianteMateria;
import com.student.administrador.domain.Materia;
import com.student.administrador.domain.Usuario;

public interface TodoService {
	
	void insertarOeditarMateria(Materia materia) throws DataAccessException;
	void insertarOeditarUsuario(Usuario usuario) throws DataAccessException;
	void insertarOeditarEscuela(CentroEscolar escuela) throws DataAccessException;
	
	void agregarOeditarExpediente(Estudiante estudiante) throws DataAccessException;
	void agregarOeditarMateriaCursada(EstudianteMateria estudiante_materia) throws DataAccessException;
	
	
	
	//CATALOGOS
	public List<Object[]> catalogoMaterias() throws DataAccessException;
	public List<Object[]> catalogoEscuelas() throws DataAccessException;
	public List<Object[]> catalogoUsuarios() throws DataAccessException;
	
	//QUERIES LARGAS
	public List<Object[]> expedientePorNombreOApellido(String cadena) throws DataAccessException;
	public List<Object[]> materiasPorEstudiante(String cadena) throws DataAccessException;

	//LOGIN
	List<Usuario> findByUsuarioAndContra(String usuario, String contra);
}
