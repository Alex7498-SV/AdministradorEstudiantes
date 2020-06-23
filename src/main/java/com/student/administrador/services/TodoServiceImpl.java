package com.student.administrador.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

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
	public List<Object[]> expedientePorNombreOApellido(String cadena) throws DataAccessException {
		return estR.expedientePorNombreApellido(cadena);
	}
	@Override
	public List<Object[]> materiasPorEstudiante(String cadena) throws DataAccessException {
		return matR.materiasCursadasPorEstudiante(cadena);
	}
	@Override
	public void insertarOeditarMateria(Materia materia) throws DataAccessException {
		matR.save(materia);
	}
	@Override
	public void insertarOeditarUsuario(Usuario usuario) throws DataAccessException {
		usR.save(usuario);
	}
	@Override
	public void insertarOeditarEscuela(CentroEscolar escuela) throws DataAccessException {
		escR.save(escuela);
	}
	@Override
	public void agregarNuevoExpediente(Estudiante estudiante) throws DataAccessException {
		estR.save(estudiante);
	}
	@Override
	public void agregarNuevaMateriaCursada(EstudianteMateria estudiante_materia) throws DataAccessException {
		estMatR.save(estudiante_materia);
	}
}
