package com.student.administrador.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.student.administrador.domain.CentroEscolar;
import com.student.administrador.domain.Estudiante;
import com.student.administrador.domain.EstudianteMateria;
import com.student.administrador.domain.Materia;
import com.student.administrador.domain.Usuario;
import com.student.administrador.dto.CatalogoEscuelasDTO;
import com.student.administrador.domain.Municipio;
import com.student.administrador.domain.Departamento;
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
	public List<CatalogoEscuelasDTO> catalogoEscuelas() throws DataAccessException {
		List<CatalogoEscuelasDTO> catesc = escR.catalogoEscuelas().stream().map(ce->{
			CatalogoEscuelasDTO dto = new CatalogoEscuelasDTO();
			dto.setCodigo(Integer.parseInt(ce[0].toString()));
			dto.setDescripcion(ce[1].toString());
			dto.setEstado(Boolean.valueOf(ce[2].toString()));
			return dto;
		}).collect(Collectors.toList());
		return catesc;
		//return escR.catalogoEscuelas();
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
	public List<Object[]> materiasPorEstudiante(Integer id) throws DataAccessException {
		return matR.materiasCursadasPorEstudiante(id);
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
	@Override
	public List<Usuario> findAll(){
		return usR.findAll();
	}

	@Override
	public List<Departamento> findAllDepartaments(){
		return depR.findAllDepartaments();
	}

	@Override
	public List<Object[]> escuelasPorMunicipio(Integer idMunicipio){
		return escR.escuelasPorMunicipio(idMunicipio);
	}

	@Override
	public List<Object[]> muncipioPorDep(Integer idDep){
		return munR.municipiosPorDep(idDep);
	}

	@Override
	public Municipio municipioPorId(Integer idMun){
		return munR.municipioPorId(idMun);
	}

	@Override
	public Departamento depPorId(Integer idDep){
		return depR.depPorId(idDep);
	}
}
