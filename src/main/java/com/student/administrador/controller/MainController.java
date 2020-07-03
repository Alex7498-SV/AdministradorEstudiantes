package com.student.administrador.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
import com.student.administrador.services.TodoService;

@Controller
public class MainController {

	@Autowired
	private TodoService service;
	
	@RequestMapping("/login")
	public ModelAndView initMain(HttpSession session){
		ModelAndView mav = new ModelAndView();
		Usuario user = (Usuario) session.getAttribute("usuario");
		if(session.getAttribute("usuario")== null) {
			mav.addObject("usuario", new Usuario());
			mav.setViewName("login");
		} 
		else {
			mav.setViewName("redirect:/menu");
		}
		
		return mav;
	}
	
	@RequestMapping("/register")
	public ModelAndView nCuenta(){
		ModelAndView mav = new ModelAndView();
		List<Departamento> deps = null;
		try {
			deps = service.findAllDepartaments();
		} catch(Exception e){
			e.printStackTrace();
		}
		mav.addObject("dep", deps);
		mav.addObject("usuario", new Usuario());
		mav.setViewName("nueva_cuenta");
		return mav;
	}

	@RequestMapping("/municipios")
	public @ResponseBody List<String[]> buscarMunicipios(@RequestParam Integer idDep){
		List<Municipio> municipios = null;
		try{
			municipios = service.muncipioPorDep(idDep);
		} catch(Exception er) {
			er.printStackTrace();
		}
		List<String[]> data =  new ArrayList<>();
		for(Municipio mun : municipios){
			data.add(new String[]{mun.getIdMunicipio().toString(),mun.getNombre()});
		}
		return data;
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
		Usuario u = new Usuario();
		Integer flag = null;
		List<Usuario> users = service.findByUsuarioAndContra(user.getUsuario(), user.getContra());
		
		for(Usuario usr: users) {
			u = usr;
			if(user.getUsuario().equals(usr.getUsuario()) && user.getContra().equals(usr.getContra())) {
				if(usr.getSesion() == false && usr.getEstado()){
					usr.setSesion(true);
					session.setAttribute("usuario", usr);
					service.insertarOeditarUsuario(usr);
						if(usr.getAdministrador()) {
							flag = 1;
						} else {
							flag = 2;
						}
				} // Debe ir aqui un viewname que mande a que no se puede logear 2 veces. F -El chino 
				if(usr.getEstado() == false) {
					flag = 3;
				}
			}
		}
		//System.out.print(flag);
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
			//System.out.println(u.getSesion() + " alv");
			u.setSesion(false);
			service.insertarOeditarUsuario(u);
			session.removeAttribute("usuario");
			mav.setViewName("dobleinicio");
		}
		return mav;
	}
	
	
	@RequestMapping("/menu")
	public ModelAndView menuAdmin(HttpSession session){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("menu_admin");
		Usuario usr = (Usuario) session.getAttribute("usuario");
		if(session.getAttribute("usuario") == null) {
			mav.setViewName("redirect:/login");
		}
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
	public ModelAndView catalogoUsuarios(HttpSession request){
		Usuario usr = (Usuario)request.getAttribute("usuario");
		ModelAndView mav = new ModelAndView();
		List<Usuario> usuarios = null;
		try{
			usuarios = service.catalogoUsuarios(usr.getIdUsuario());
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
    public ModelAndView editCatUsuario(@Valid @ModelAttribute Usuario usuario ,BindingResult result, HttpSession request) {
		Usuario usr = (Usuario)request.getAttribute("usuario");
        ModelAndView mav = new ModelAndView();
        if(!result.hasErrors()) {
            try {
                service.insertarOeditarUsuario(usuario);
            }catch(Exception e) {
                e.printStackTrace();
            }
            List<Usuario> usuarios = null;
    		try{
    			usuarios = service.catalogoUsuarios(usr.getIdUsuario());
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
	
	@RequestMapping("/nuevo_catalogo_materia")
	public ModelAndView nuevoCatalogoMaterias(){
		ModelAndView mav = new ModelAndView();
		mav.addObject("catalogoMateria", new Materia());
		mav.setViewName("nuevo_catalogo_materia");
		return mav;
	}
	
	@RequestMapping("/nuevo_catalogo_usuario")
	public ModelAndView nuevoCatalogoUsuario(){
		ModelAndView mav = new ModelAndView();
		mav.addObject("catalogoUsuario", new Usuario());
		mav.setViewName("nuevo_catalogo_usuario");
		return mav;
	}

	@RequestMapping("/nuevo_catalogo_escuela")
	public ModelAndView nuevoCatalogoEscuela(){
		ModelAndView mav = new ModelAndView();
		mav.addObject("catalogoEscuela", new CentroEscolar());
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
	
	@RequestMapping("/buscar_o_agregar_alumnos" )
	public ModelAndView buscarAgregarAlumnos(){
		ModelAndView mav = new ModelAndView();
		mav.addObject("objeto", new ExpedientePorNomApellidoDTO());
		mav.setViewName("../templates_coordinador/buscar_o_agregar_alumnos");
		return mav;
	}
	
	
	@RequestMapping("/filtrar_por_nom_ape")
	public ModelAndView filtrar(@ModelAttribute ExpedientePorNomApellidoDTO pclave) {
		ModelAndView mav = new ModelAndView();
		List<ExpedientePorNomApellidoDTO> estudiantes = null;
		System.out.println(pclave.getNombres()+pclave.getApellidos());
		try {
			if(pclave.getNombres().isEmpty()) {/*por la gran puta*/
				System.out.println("Dese puta");
			}
			else if(pclave.getApellidos().equals("apellido")) {
				estudiantes = service.expedientePorNombreOApellido(" ", pclave.getNombres());
			}
			else if (pclave.getApellidos().equals("nombre")){
				estudiantes = service.expedientePorNombreOApellido(pclave.getNombres(), " ");
			}
		}catch(Exception e ) {
			e.printStackTrace();
		}
		
		mav.addObject("estudiantes", estudiantes);
		mav.setViewName("../templates_coordinador/tabla_estudiantes");
		return mav;
	}

	@RequestMapping("/editar_expediente_existente" )
	public ModelAndView editarExpediente(@RequestParam Integer idEstudiante){
		ModelAndView mav = new ModelAndView();
		Estudiante est = new Estudiante();
		est = service.findByIdEstudiante(idEstudiante);
		mav.addObject("estudianteNuevo", est);
		mav.setViewName("../templates_coordinador/agregar_estudiante");
		return mav;
	}
	
	@RequestMapping("/agregar_expediente_nuevo" )
	public ModelAndView nuevoExpediente(){
		ModelAndView mav = new ModelAndView();
		mav.addObject("estudianteNuevo", new Estudiante());
		mav.setViewName("../templates_coordinador/agregar_estudiante");
		return mav;
	}
	
	@RequestMapping("/expediente_guardado" )
	public ModelAndView nuevoExpedienteGuardado(@Valid @ModelAttribute Estudiante estudiante ,BindingResult result){
		ModelAndView mav = new ModelAndView();
		if(!result.hasErrors()) {
            try {
                service.insertarOeditarEstudiante(estudiante);
            }catch(Exception e) {
                e.printStackTrace();
            }
            mav.setViewName("../templates_coordinador/buscar_o_agregar_alumnos");
        }
        else {
        	mav.setViewName("../templates_coordinador/agregar_estudiante");
        }
        return mav;
	}
	
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
		mav.setViewName("../templates_coordinador/materias_cursadas");
		return mav;
	}
	
	@RequestMapping("/agregar_nueva_materia_cursada")
	public ModelAndView nuevaMatCursada(){
		ModelAndView mav = new ModelAndView();
		mav.addObject("nuevaMateriaCursada", new EstudianteMateria());
		mav.setViewName("../templates_coordinador/agregar_editar_materia");
		return mav;
	}
	
	@RequestMapping("/materia_cursada_guardada")
	public ModelAndView nuevaMatGuardada(@Valid @ModelAttribute Materia materia ,BindingResult result){
        ModelAndView mav = new ModelAndView();
		if(!result.hasErrors()) {
            try {
                service.insertarOeditarMateria(materia);
            }catch(Exception e) {
                e.printStackTrace();
            }
            mav.setViewName("../templates_coordinador/buscar_o_agregar_alumnos");
        }
        else {
        	mav.setViewName("../templates_coordinador/agregar_editar_materia");
        }
        return mav;
	}
}
