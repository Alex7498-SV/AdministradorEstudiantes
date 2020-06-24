package com.student.administrador.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.student.administrador.domain.CentroEscolar;
import com.student.administrador.domain.Estudiante;
import com.student.administrador.domain.EstudianteMateria;
import com.student.administrador.domain.Materia;
import com.student.administrador.domain.Usuario;
import com.student.administrador.services.TodoService;


@Controller
public class MainController {

	@Autowired
	private TodoService service;
	
	@RequestMapping("/login")
	public ModelAndView initMain(){
		ModelAndView mav = new ModelAndView();
		mav.addObject("usuario", new Usuario());
		mav.setViewName("login");
		return mav;
	}
	
	@RequestMapping("/register")
	public ModelAndView nCuenta(){
		ModelAndView mav = new ModelAndView();
		mav.addObject("usuario", new Usuario());
		mav.setViewName("nueva_cuenta");
		return mav;
	}
	
	@RequestMapping("/welcome")
	public ModelAndView welcome(@Valid @ModelAttribute Usuario user, BindingResult result){
		ModelAndView mav = new ModelAndView();
		if(!result.hasErrors()) {
			user.setEstado(false);
			user.setSesion(false);
			if(user.getAdministrador() == null) {
				user.setAdministrador(false);
			}
			try {
				service.insertarOeditarUsuario(user);
			}catch(Exception e){
				e.printStackTrace();
			}
			mav.setViewName("welcome");
		}
		else {
			mav.setViewName("nueva_cuenta");
		}
		return mav;
	}
	
	@RequestMapping("/menu")
	public ModelAndView menuAdmin(@ModelAttribute Usuario user){
		ModelAndView mav = new ModelAndView();
		List<Usuario> users = service.findByUsuarioAndContra(user.getUsuario(), user.getContra());
		Integer flag = 0;
		for(Usuario usr: users) {
			if(user.getUsuario().equals(usr.getUsuario()) && user.getContra().equals(usr.getContra())) {
				if(usr.getAdministrador()) {
					flag = 1;
				} else {
					flag = 2;
				}
			}
		}
		System.out.print(flag);
		if(flag ==1 ) {
			mav.setViewName("menu_admin");
		} else {
			mav.setViewName("login");
		}
		return mav;
	}
	
	@RequestMapping("/catalogo_escuela")
	public ModelAndView catalogoEscuelas(){
		ModelAndView mav = new ModelAndView();
		List<Object[]> escuelas = null;
		try{
			escuelas = service.catalogoEscuelas();
		}catch(Exception e){
			e.printStackTrace();
		}
		mav.addObject("escuelas", escuelas);
		mav.setViewName("catalogo_escuela");
		return mav;
	}
	
	@RequestMapping("/editar_catalogo_escuela")
    public ModelAndView editCatEscuela(@Valid @ModelAttribute CentroEscolar escuela ,BindingResult result) {
        ModelAndView mav = new ModelAndView();
        if(!result.hasErrors()) {
            try {
                service.insertarOeditarEscuela(escuela);
            }catch(Exception e) {
                e.printStackTrace();
            }
            List<Object[]> escuelas = null;
    		try{
    			escuelas = service.catalogoEscuelas();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    		mav.addObject("escuelas", escuelas);
            mav.setViewName("catalogo_escuela");
        }
        else {
        	mav.setViewName("editar_catalogo_escuela");
        }
        return mav;
    }
	
	@RequestMapping("/catalogo_materia")
	public ModelAndView catalogoMaterias(){
		ModelAndView mav = new ModelAndView();
		List<Object[]> materias = null;
		try{
			materias = service.catalogoMaterias();
		}catch(Exception e){
			e.printStackTrace();
		}
		mav.addObject("materias", materias);
		mav.setViewName("catalogo_materia");
		return mav;
	}
	
	@RequestMapping("/editar_catalogo_materia")
    public ModelAndView editCatMateria(@Valid @ModelAttribute Materia materia ,BindingResult result) {
        ModelAndView mav = new ModelAndView();
        if(!result.hasErrors()) {
            try {
                service.insertarOeditarMateria(materia);
            }catch(Exception e) {
                e.printStackTrace();
            }
            List<Object[]> materias = null;
    		try{
    			materias = service.catalogoMaterias();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    		mav.addObject("materias", materias);
            mav.setViewName("catalogo_materia");
        }
        else {
        	mav.setViewName("editar_catalogo_materia");
        }
        return mav;
    }
	
	@RequestMapping("/catalogo_usuario")
	public ModelAndView catalogoUsuarios(){
		ModelAndView mav = new ModelAndView();
		List<Object[]> usuarios = null;
		try{
			usuarios = service.catalogoUsuarios();
		}catch(Exception e){
			e.printStackTrace();
		}
		mav.addObject("usuarios", usuarios);
		mav.setViewName("catalogo_usuario");
		return mav;
	}
	
	@RequestMapping("/editar_catalogo_usuario")
    public ModelAndView editCatUsuario(@Valid @ModelAttribute Usuario usuario ,BindingResult result) {
        ModelAndView mav = new ModelAndView();
        if(!result.hasErrors()) {
            try {
                service.insertarOeditarUsuario(usuario);
            }catch(Exception e) {
                e.printStackTrace();
            }
            List<Object[]> usuarios = null;
    		try{
    			usuarios = service.catalogoUsuarios();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    		mav.addObject("usuarios", usuarios);
            mav.setViewName("catalogo_usuario");
        }
        else {
        	mav.setViewName("editar_catalogo_usuario");
        }
        return mav;
    }

	@RequestMapping("/buscar_o_agregar_alumnos" )
	public ModelAndView buscarAgregarAlumnos(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("buscar_o_agregar_alumnos");
		return mav;
	}
	
	@RequestMapping("/filtrar_por_nombreApellido")
	public ModelAndView filtrar(@RequestParam(value="nombreApellido") String cadena, @RequestParam(value="comboBoxNumber") Integer comboNumber) {
		ModelAndView mav = new ModelAndView();
		List<Object[]> estudiantes = null;
		if(comboNumber == 1) {
			try {
				estudiantes = service.expedientePorNombreOApellido(cadena,"");
			}catch(Exception e ) {
				e.printStackTrace();
			}
		}else {
			try {
				estudiantes = service.expedientePorNombreOApellido("",cadena);
			}catch(Exception e ) {
				e.printStackTrace();
			}
		}
		mav.addObject("estudiantes", estudiantes);
		mav.setViewName("tabla_estudiantes");
		return mav;
	}

	@RequestMapping("/agregar_expediente_nuevo" )
	public ModelAndView nuevoExpediente(){
		ModelAndView mav = new ModelAndView();
		mav.addObject("estudianteNuevo", new Estudiante());
		mav.setViewName("tabla_estudiantes");
		return mav;
	}
	
	@RequestMapping("/guardar_expediente_nuevo" )
	public ModelAndView nuevoExpedienteGuardado(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("buscar_o_agregar_alumnos");
		return mav;
	}
	
	@RequestMapping("/materias_cursadas" )
	public ModelAndView materiasCursadas(@RequestParam Integer idEstudiante){
		ModelAndView mav = new ModelAndView();
		List<Object[]> matCursadas = null;
		try {
			matCursadas = service.materiasPorEstudiante(idEstudiante);
		}catch(Exception e ) {
			e.printStackTrace();
		}
		mav.addObject("matCursadas", matCursadas);
		mav.setViewName("materias_cursadas");
		return mav;
	}
	
	@RequestMapping("/agregar_nueva_materia_cursada")
	public ModelAndView nuevaMatCursada(){
		ModelAndView mav = new ModelAndView();
		mav.addObject("nuevaMateriaCursada", new EstudianteMateria());
		mav.setViewName("agregar_editar_materia");
		return mav;
	}
	
	@RequestMapping("/guardar_nueva_materia_cursada")
	public ModelAndView nuevaMatGuardada(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("materias_cursadas");
		return mav;
	}
}
