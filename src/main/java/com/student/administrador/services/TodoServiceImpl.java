package com.student.administrador.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.student.administrador.domain.CentroEscolar;
import com.student.administrador.domain.Estudiante;
import com.student.administrador.domain.EstudianteMateria;
import com.student.administrador.domain.Materia;
import com.student.administrador.domain.Usuario;
import com.student.administrador.repositories.DepartamentoRepo;
import com.student.administrador.repositories.EscuelaRepo;
import com.student.administrador.repositories.EstudianteMateriaRepo;
import com.student.administrador.repositories.EstudianteRepo;
import com.student.administrador.repositories.MateriaRepo;
import com.student.administrador.repositories.MunicipioRepo;
import com.student.administrador.repositories.UsuarioRepo;

@Service
public class TodoServiceImpl implements TodoService{

	@Autowired
	DepartamentoRepo depR;
	@Autowired
	EscuelaRepo escR;
	@Autowired
	EstudianteMateriaRepo estMatR;
	@Autowired
	EstudianteRepo estR;
	@Autowired
	MateriaRepo matR;
	@Autowired
	MunicipioRepo munR;
	@Autowired
	UsuarioRepo usR;
	
	@Override
	public List<Object[]> catalogoMaterias() throws DataAccessException {
		return matR.catalogoMaterias();
	}
	@Override
	public List<Object[]> catalogoEscuelas() throws DataAccessException {
		return escR.catalogoEscuelas();
	}
	@Override
	public List<Object[]> catalogoUsuarios() throws DataAccessException {
		return usR.catalogoUsuarios();
	}
	@Override
	public List<Object[]> expedientePorNombreOApellido(String nombre, String apellido) throws DataAccessException {
		return estR.expedientePorNombreApellido(nombre, apellido);
	}
	@Override
	public List<Object[]> materiasPorEstudiante(String cadena) throws DataAccessException {
		return matR.materiasCursadasPorEstudiante(cadena);
	}
	@Override
	@Transactional
	public void insertarOeditarMateria(Materia materia) throws DataAccessException {
		matR.save(materia);
	}
	@Override
	@Transactional
	public void insertarOeditarUsuario(Usuario usuario) throws DataAccessException {
		usR.save(usuario);
	}
	@Override
	@Transactional
	public void insertarOeditarEscuela(CentroEscolar escuela) throws DataAccessException {
		escR.save(escuela);
	}
	@Override
	@Transactional
	public void agregarOeditarExpediente(Estudiante estudiante) throws DataAccessException {
		estR.save(estudiante);
	}
	@Override
	@Transactional
	public void agregarOeditarMateriaCursada(EstudianteMateria estudiante_materia) throws DataAccessException {
		estMatR.save(estudiante_materia);
	}
	@Override
	public List<Usuario> findByUsuarioAndContra(String usuario, String contra) {
		return usR.findByUsuarioAndContra(usuario, contra);
	}
}
