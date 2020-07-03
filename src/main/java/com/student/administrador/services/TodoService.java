package com.student.administrador.services;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.student.administrador.domain.CentroEscolar;
import com.student.administrador.domain.Departamento;
import com.student.administrador.domain.Estudiante;
import com.student.administrador.domain.EstudianteMateria;
import com.student.administrador.domain.Materia;
import com.student.administrador.domain.Municipio;
import com.student.administrador.domain.Usuario;
import com.student.administrador.dto.CatalogoEscuelasDTO;
import com.student.administrador.dto.ExpedientePorNomApellidoDTO;
import com.student.administrador.dto.MateriasPorEstudianteDTO;

public interface TodoService {
	
	void insertarOeditarMateria(Materia materia) throws DataAccessException;
	void insertarOeditarUsuario(Usuario usuario) throws DataAccessException;
	void insertarOeditarEscuela(CentroEscolar escuela) throws DataAccessException;
	void insertarOeditarEstudiante(Estudiante estudiante) throws DataAccessException;
	
	void agregarOeditarExpediente(Estudiante estudiante) throws DataAccessException;
	void agregarOeditarMateriaCursada(EstudianteMateria estudiante_materia) throws DataAccessException;
	
	public List<Departamento> findAllDepartaments() throws DataAccessException;
	public List<Object[]> escuelasPorMunicipio(Integer idMunicipio) throws DataAccessException;
	public List<Object[]> muncipioPorDep(Integer idDep) throws DataAccessException;
	public Municipio municipioPorId(Integer idMun) throws DataAccessException;
	public Departamento depPorId(Integer idDep) throws DataAccessException;
	public List<Municipio> findAllMunicipios() throws DataAccessException;
	
	//CATALOGOS
	public List<Materia> catalogoMaterias() throws DataAccessException;
	public List<CatalogoEscuelasDTO> catalogoEscuelas() throws DataAccessException;
	public List<Usuario> catalogoUsuarios(int idSesion) throws DataAccessException;
	
	//QUERIES LARGAS
	public List<ExpedientePorNomApellidoDTO> expedientePorNombreOApellido(String nombre, String apellido) throws DataAccessException;
	public List<MateriasPorEstudianteDTO> materiasPorEstudiante(Integer id) throws DataAccessException;

	//LOGIN
	List<Usuario> findByUsuarioAndContra(String usuario, String contra);
	
	//Proposito general
	List<Usuario> findAll();
	
	public Estudiante findByIdEstudiante(Integer id) throws DataAccessException;
}
