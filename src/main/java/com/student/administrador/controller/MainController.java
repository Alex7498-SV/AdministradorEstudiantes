package com.student.administrador.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
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
import com.student.administrador.domain.Municipio;
import com.student.administrador.domain.Usuario;
import com.student.administrador.dto.CatalogoEscuelasDTO;
import com.student.administrador.dto.MateriasPorEstudianteDTO;
import com.student.administrador.services.TodoService;

@Controller
public class MainController {

	@Autowired
	private TodoService service;
	
	@RequestMapping("/login")
	public ModelAndView initMain(HttpSession session){
		ModelAndView mav = new ModelAndView();
		//Usuario user = (Usuario) session.getAttribute("usuario");
		if(session.getAttribute("usuario")== null) {
			mav.addObject("usuario", new Usuario());
			mav.setViewName("login");
		} else {
			mav.setViewName("redirect:/menu");
		}
		
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
		Integer flag = 0;
		List<Usuario> users = service.findAll();
		if(!result.hasErrors()) {
			for(Usuario usr: users) {
				System.out.println(user.getUsuario()+usr.getUsuario());
				if(user.getUsuario().equals(usr.getUsuario())) {
					flag++;
				}
			}
			if(flag == 0) {
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
			} else {
				mav.addObject("usuario", new Usuario());
				mav.setViewName("nueva_cuenta");

			}
		
		}
		else {
			mav.setViewName("nueva_cuenta");
		}
		return mav;
	}
	
	@RequestMapping("/redirect")
	public ModelAndView redirect(HttpSession session, @ModelAttribute Usuario user){
		ModelAndView mav = new ModelAndView();
		List<Usuario> users = service.findByUsuarioAndContra(user.getUsuario(), user.getContra());
		Integer flag = null;
		for(Usuario usr: users) {
			if(user.getUsuario().equals(usr.getUsuario()) && user.getContra().equals(usr.getContra())) {
				if(usr.getSesion() == false){
					usr.setSesion(true);
					session.setAttribute("usuario", usr);
					service.insertarOeditarUsuario(usr);
						if(usr.getAdministrador()) {
							flag = 1;
						} else {
							flag = 2;
						}
				} // Debe ir aqui un viewname que mande a que no se puede logear 2 veces. F -El chino 
				
			}
		}
		System.out.print(flag);
		if(flag != null ) {
			if(flag ==1) {
				mav.setViewName("redirect:/menu");
				//mav.setViewName("menu_admin");
			} else if(flag ==2) {
				mav.setViewName("redirect:/buscar_o_agregar_alumnos");
				//mav.setViewName("menu_admin");
			} else {
				mav.setViewName("errorC");
			}
		} else {
			mav.setViewName("login");
		}
		return mav;
	}
	
	
	@RequestMapping("/menu")
	public ModelAndView menuAdmin(HttpSession session){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("menu_admin");
		Usuario usr = (Usuario) session.getAttribute("usuario");
		System.out.println(usr.getSesion());
		/* List<Usuario> users = service.findByUsuarioAndContra(user.getUsuario(), user.getContra());
		Integer flag = null;
		for(Usuario usr: users) {
			if(user.getUsuario().equals(usr.getUsuario()) && user.getContra().equals(usr.getContra())) {
				if(usr.getSesion() == false){
					usr.setSesion(true);
					session.setAttribute("usuario", usr);
					service.insertarOeditarUsuario(usr);
						if(usr.getAdministrador()) {
							flag = 1;
						} else {
							flag = 2;
						}*/
				//} // Debe ir aqui un viewname que mande a que no se puede logear 2 veces. F -El chino 
			//}
		//}
		/*System.out.print(flag);
		if(flag != null ) {
			if(flag ==1) {
				mav.setViewName("menu_admin");
			} else {
				mav.setViewName("menu_admin");
			}
		} else {
			mav.setViewName("login");
		}*/
		return mav;
	}
	
	@RequestMapping("/logout")
	public ModelAndView logout(HttpSession session){
		ModelAndView mav = new ModelAndView();
		Usuario usr = (Usuario) session.getAttribute("usuario");
		usr.setSesion(false);
		service.insertarOeditarUsuario(usr);
		session.removeAttribute("usuario");
		mav.setViewName("Logout");
		return mav;
	}
	
	@RequestMapping("/catalogo_escuela")
	public ModelAndView catalogoEscuelas(){
		ModelAndView mav = new ModelAndView();
		List<CatalogoEscuelasDTO> escuelas = null;
		try{
			escuelas = service.catalogoEscuelas();
		}catch(Exception e){
			e.printStackTrace();
		}
		mav.addObject("escuelas", escuelas);
		mav.setViewName("catalogo_escuela");
		return mav;
	}
	

	@RequestMapping("/catalogo_usuario")
	public ModelAndView catalogoUsuarios(){
		ModelAndView mav = new ModelAndView();
		List<Usuario> usuarios = null;
		try{
			usuarios = service.catalogoUsuarios();
		}catch(Exception e){
			e.printStackTrace();
		}
		mav.addObject("usuarios", usuarios);
		mav.setViewName("catalogo_usuario");
		return mav;
	}
	
	@RequestMapping("/catalogo_materia")
	public ModelAndView catalogoMaterias(){
		ModelAndView mav = new ModelAndView();
		List<Materia> materias = null;
		try{
			materias = service.catalogoMaterias();
		}catch(Exception e){
			e.printStackTrace();
		}
		mav.addObject("materias", materias);
		mav.setViewName("catalogo_materia");
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
            List<CatalogoEscuelasDTO> escuelas = null;
    		try{
    			escuelas = service.catalogoEscuelas();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    		mav.addObject("escuelas", escuelas);
            mav.setViewName("catalogo_escuela");
        }
        else {
        	mav.setViewName("nuevo_catalogo_escuela");
        }
        return mav;
    }
	
	@RequestMapping("/nuevo_catalogo_materia")
	public ModelAndView nuevoCatalogoMaterias(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("nuevo_catalogo_materia");
		return mav;
	}

	@RequestMapping("/nuevo_catalogo_usuario")
	public ModelAndView nuevoCatalogoUsuario(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("nuevo_catalogo_usuario");
		return mav;
	}

	@RequestMapping("/nuevo_catalogo_escuela")
	public ModelAndView nuevoCatalogoEscuela(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("nuevo_catalogo_escuela");
		return mav;
	}

	
	@RequestMapping("/find_municipios")
	public ModelAndView municipios(){
		ModelAndView mav = new ModelAndView();
		List<Municipio> municipios = null;
		try{
			municipios = service.findAllMunicipios();
		}catch(Exception e){
			e.printStackTrace();
		}
		mav.addObject("municipios", municipios);
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
            List<Materia> materias = null;
    		try{
    			materias = service.catalogoMaterias();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    		mav.addObject("materias", materias);
            mav.setViewName("catalogo_materia");
        }
        else {
        	mav.setViewName("nuevo_catalogo_materia");
        }
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
            List<Usuario> usuarios = null;
    		try{
    			usuarios = service.catalogoUsuarios();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    		mav.addObject("usuarios", usuarios);
            mav.setViewName("catalogo_usuario");
        }
        else {
        	mav.setViewName("nuevo_catalogo_usuario");
        }
        return mav;
    }
	
	@RequestMapping("/buscar_o_agregar_alumnos" )
	public ModelAndView buscarAgregarAlumnos(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("../templates_coordinador/buscar_o_agregar_alumnos");
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
	} //Que estas haciendo???
	
	@RequestMapping("/materias_cursadas" )
	public ModelAndView materiasCursadas(@RequestParam Integer idEstudiante){
		ModelAndView mav = new ModelAndView();
		List<MateriasPorEstudianteDTO> matCursadas = null;
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
