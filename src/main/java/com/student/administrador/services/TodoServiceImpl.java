package com.student.administrador.services;

import java.text.ParseException;
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
import com.student.administrador.dto.ExpedientePorNomApellidoDTO;
import com.student.administrador.dto.MateriasPorEstudianteDTO;
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
	public List<Materia> catalogoMaterias() throws DataAccessException {
		List<Materia> mat = matR.catalogoMaterias().stream().map(ce->{
			Materia materia = new Materia();
			materia.setIdMateria(Integer.parseInt(ce[0].toString()));
			materia.setNombre(ce[1].toString());
			materia.setEstado(Boolean.parseBoolean(ce[2].toString()));
			return materia;
		}).collect(Collectors.toList());
		return mat;
	}

	@Override
	public List<Materia> materiasActivas() throws DataAccessException {
		List<Materia> mat = matR.materiasActivas().stream().map(ce->{
			Materia materia = new Materia();
			materia.setIdMateria(Integer.parseInt(ce[0].toString()));
			materia.setNombre(ce[1].toString());
			materia.setEstado(Boolean.parseBoolean(ce[2].toString()));
			return materia;
		}).collect(Collectors.toList());
		return mat;
	}
	
	@Override
	public List<CatalogoEscuelasDTO> catalogoEscuelas() throws DataAccessException {
		List<CatalogoEscuelasDTO> catesc = escR.catalogoEscuelas().stream().map(ce->{
			CatalogoEscuelasDTO dto = new CatalogoEscuelasDTO();
			dto.setCodigo(Integer.parseInt(ce[0].toString()));
			dto.setDescripcion(ce[1].toString());
			dto.setEstado(Boolean.valueOf(ce[2].toString()));
			dto.setMunicipio(ce[3].toString());
			return dto;
		}).collect(Collectors.toList());
		return catesc;
		//return escR.catalogoEscuelas();
	}
	@Override
	public List<Usuario> catalogoUsuarios() throws DataAccessException {
		List<Usuario> catus = usR.catalogoUsuarios().stream().map(f->{
			Usuario dto = new Usuario();
			dto.setIdUsuario(Integer.parseInt(f[0].toString()));
			dto.setNombre(f[1].toString());
			dto.setApellido(f[2].toString());
			dto.setDireccion(f[3].toString());
			dto.setUsuario(f[4].toString());
			dto.setEstado(Boolean.valueOf(f[5].toString()));
			return dto;
		}).collect(Collectors.toList());
		return catus;
	}
	@Override
	public List<ExpedientePorNomApellidoDTO> expedientePorNombreOApellido(String nombre, String apellido) throws DataAccessException {
		List<ExpedientePorNomApellidoDTO> exp = estR.expedientePorNombreApellido(nombre, apellido).stream().map(f->{
			ExpedientePorNomApellidoDTO dto = new ExpedientePorNomApellidoDTO();
			dto.setNombres(f[0].toString());
			dto.setApellidos(f[1].toString());
			dto.setIdEstudiante(Integer.parseInt(f[5].toString()));
			if(f[2] != null) {
				dto.setAprobadas(Integer.parseInt(f[2].toString()));
				dto.setReprobadas(Integer.parseInt(f[3].toString()));
				dto.setPromedio(Float.parseFloat(f[4].toString()));
			}else {
				dto.setAprobadas(0);
				dto.setReprobadas(0);
				dto.setPromedio(Float.parseFloat("0"));
			}
			return dto;
		}).collect(Collectors.toList());
		return exp;
	}
	@Override
	public List<MateriasPorEstudianteDTO> materiasPorEstudiante(Integer id) throws DataAccessException {
		List<MateriasPorEstudianteDTO> matporest = matR.materiasCursadasPorEstudiante(id).stream().map(ce->{
			MateriasPorEstudianteDTO dto = new MateriasPorEstudianteDTO();
			dto.setNombreMateria(ce[0].toString());
			dto.setNombreEstudiante(ce[1].toString());
			dto.setAnio(Integer.parseInt(ce[2].toString()));
			dto.setCiclo(Integer.parseInt(ce[3].toString()));
			dto.setNota(Float.parseFloat(ce[4].toString()));
			dto.setResultado(ce[5].toString());
			dto.setIdEstudianteMateria(Integer.parseInt(ce[6].toString()));
			dto.setIdMateria(Integer.parseInt(ce[7].toString()));
			return dto;
		}).collect(Collectors.toList());
		return matporest;
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
		List<Departamento> deps = depR.findAllDepartaments().stream().map(ce->{
			Departamento dep = new Departamento();
			dep.setIdDepartamento(Integer.parseInt(ce[0].toString()));
			dep.setNombre(ce[1].toString());
			return dep;
		}).collect(Collectors.toList());
		return deps;
	}

	@Override
	public List<CentroEscolar> escuelasPorMunicipio(Integer idMunicipio){
		List<CentroEscolar> ces = escR.escuelasPorMunicipio(idMunicipio).stream().map(ce->{
			CentroEscolar mun = new CentroEscolar();
			mun.setIdEscolar(Integer.parseInt(ce[0].toString()));
			mun.setNombre(ce[1].toString());
			return mun;
		}).collect(Collectors.toList());
		return ces;
	}

	@Override
	public List<Municipio> muncipioPorDep(Integer idDep){
		List<Municipio> muni = munR.municipiosPorDep(idDep).stream().map(ce->{
			Municipio mun = new Municipio();
			mun.setIdMunicipio(Integer.parseInt(ce[1].toString()));
			mun.setNombre(ce[0].toString());
			return mun;
		}).collect(Collectors.toList());
		return muni;
	}

	@Override
	public Municipio municipioPorId(Integer idMun){
		return munR.municipioPorId(idMun);
	}

	@Override
	public Departamento depPorId(Integer idDep){
		return depR.depPorId(idDep);
	}
	
	@Override
	public List<Municipio> findAllMunicipios(){
		return munR.allMunicipios();
	}

	@Override
	public void insertarOeditarEstudiante(Estudiante estudiante) throws DataAccessException {
		estR.save(estudiante);
		
	}

	@Override
	public Estudiante findByIdEstudiante(Integer id) throws DataAccessException {
		List<Estudiante> ests = estR.findByIdEstudiante(id).stream().map(ce->{
			Estudiante est = new Estudiante();
			est.setIdEstudiante(Integer.parseInt(ce[0].toString()));
			est.setNombres(ce[1].toString());
			est.setApellidos(ce[2].toString());
			est.setCarnet(ce[3].toString());
			try {
				est.setFechaNac(ce[4].toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			est.setEdad(Integer.parseInt(ce[5].toString()));
			est.setDireccion(ce[6].toString());
			est.setTel_fijo(ce[7].toString());
			est.setTel_movil(ce[8].toString());
			est.setNombrePadre(ce[9].toString());
			est.setNombreMadre(ce[10].toString());
			return est;
		}).collect(Collectors.toList());
		return ests.get(0);
	}

	@Override
	public EstudianteMateria findEstudianteMateriaById(Integer id) throws DataAccessException {
		List<EstudianteMateria> estMats = estMatR.findEstudianteMateriaById(id).stream().map(ce->{
			EstudianteMateria estMat = new EstudianteMateria();
			estMat.setIdEstudianteMateria(Integer.parseInt(ce[0].toString()));
			estMat.setIdMateria(Integer.parseInt(ce[1].toString()));
			estMat.setIdEstudiante(Integer.parseInt(ce[2].toString()));
			estMat.setNota(Float.parseFloat(ce[3].toString()));
			estMat.setAnio(Integer.parseInt(ce[4].toString()));
			estMat.setCicloCursado(Integer.parseInt(ce[5].toString()));
			return estMat;
		}).collect(Collectors.toList());
		return estMats.get(0);
	}

	@Override
	public Materia findByIdMateria(Integer id) throws DataAccessException {
		return matR.findByIdMateria(id);
	}

	@Override
	public MateriasPorEstudianteDTO findMateriaEstudianteDTOById(Integer id) throws DataAccessException{
		List<MateriasPorEstudianteDTO> matporest = estMatR.findDtoById(id).stream().map(ce->{
			MateriasPorEstudianteDTO dto = new MateriasPorEstudianteDTO();
			dto.setNombreMateria(ce[0].toString());
			dto.setNombreEstudiante(ce[1].toString());
			dto.setAnio(Integer.parseInt(ce[2].toString()));
			dto.setCiclo(Integer.parseInt(ce[3].toString()));
			dto.setNota(Float.parseFloat(ce[4].toString()));
			dto.setResultado(ce[5].toString());
			dto.setIdEstudianteMateria(Integer.parseInt(ce[6].toString()));
			return dto;
		}).collect(Collectors.toList());
		return matporest.get(0);
	}

	@Override
	public Usuario findByIdUsuario(Integer id) throws DataAccessException {
		// TODO Auto-generated method stub
		return usR.findByIdUsuario(id);
	}

	@Override
	public CentroEscolar findByIdEscolar(Integer id) throws DataAccessException {
		// TODO Auto-generated method stub
		return escR.findByIdEscolar(id);
	}
}


