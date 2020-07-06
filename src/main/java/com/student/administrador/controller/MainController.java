package com.student.administrador.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.student.administrador.domain.CentroEscolar;
import com.student.administrador.domain.Departamento;
import com.student.administrador.domain.Estudiante;
import com.student.administrador.domain.EstudianteMateria;
import com.student.administrador.domain.Login;
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
	
	
	private ModelAndView verifyAdmin(HttpSession session, ModelAndView mav) {
		Usuario user = (Usuario) session.getAttribute("usuario");
		if(!user.getAdministrador()){
			mav.clear();
			mav.setViewName("redirect:/buscar_o_agregar_alumnos");
		} 
		return mav;
		
	}
	
	private ModelAndView verifyCoord(HttpSession session, ModelAndView mav) {
		Usuario user = (Usuario) session.getAttribute("usuario");
		if(user.getAdministrador()){
			mav.clear();
			mav.setViewName("redirect:/menu");
		} 
		return mav;
		
	}
	
	@RequestMapping("/login")
	public ModelAndView initMain(HttpSession session){
		ModelAndView mav = new ModelAndView();
		Usuario user = (Usuario) session.getAttribute("usuario");
		if(session.getAttribute("usuario")== null) {
			mav.addObject("usuario", new Login());
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

	@RequestMapping("/cEscolares")
	public @ResponseBody List<String[]> buscarEscuelas(@RequestParam Integer idMun){
		List<CentroEscolar> escuelas = null;
		try{
			escuelas = service.escuelasPorMunicipio(idMun);
		} catch(Exception er) {
			er.printStackTrace();
		}
		List<String[]> data =  new ArrayList<>();
		for(CentroEscolar esc : escuelas){
			data.add(new String[]{esc.getIdEscolar().toString(),esc.getNombre()});
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
				List<Departamento> deps = null;
				try {
					deps = service.findAllDepartaments();
				} catch(Exception e){
					e.printStackTrace();
				}
				mav.addObject("dep", deps);
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
	public ModelAndView redirect(HttpSession session, @ModelAttribute Login user){
		ModelAndView mav = new ModelAndView();
		Usuario u = new Usuario();
		Integer flag = null;
		List<Usuario> users = service.findByUsuarioAndContra(user.getUsuario(), user.getContra());
		if(session.getAttribute("usuario") != null) {
			u = (Usuario) session.getAttribute("usuario");
			if(u.getAdministrador()) {
				mav.setViewName("redirect:/menu");
			} else {
				mav.setViewName("redirect:/buscar_o_agregar_alumnos");
			}
		}
		else if(users.size() == 0) {
			flag = 4;
			
		} else {
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
					} // Verificando que este activada 
					if(usr.getEstado() == false) {
						flag = 3;
					}
				}
			}
		}
		//System.out.print(flag);
		if(flag != null ) {
			if(flag ==1) {
				mav.setViewName("redirect:/menu");
			} else if(flag ==2) {
				mav.setViewName("redirect:/buscar_o_agregar_alumnos");
			} else if(flag == 3){
				mav.setViewName("errorC");
			} else {
				mav.setViewName("login");
			}
		} else {
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
		
		Usuario usr = (Usuario) session.getAttribute("usuario");
		if(session.getAttribute("usuario") == null) {
			mav.setViewName("redirect:/login");
		}
		System.out.println(usr.getSesion());
		mav.addObject("username", usr.getUsuario());
		mav.setViewName("menu_admin");
		verifyAdmin(session, mav);
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
	public ModelAndView catalogoEscuelas(HttpSession session){
		ModelAndView mav = new ModelAndView();
		List<CatalogoEscuelasDTO> escuelas = null;
		try{
			escuelas = service.catalogoEscuelas();
		}catch(Exception e){
			e.printStackTrace();
		}
		mav.addObject("escuelas", escuelas);
		mav.setViewName("catalogo_escuela");
		verifyAdmin(session, mav);
		return mav;
	}
	

	@RequestMapping("/nuevo_catalogo_escuela")
	public ModelAndView nuevoCatalogoEscuela(HttpSession session){
		ModelAndView mav = new ModelAndView();
		List<Departamento> deps = null;
		try {
			deps = service.findAllDepartaments();
		} catch(Exception e){
			e.printStackTrace();
		}
		mav.addObject("dep", deps);
		mav.addObject("catalogoEscuela", new CentroEscolar());
		mav.setViewName("nuevo_catalogo_escuela");
		verifyAdmin(session, mav);
		return mav;
	}
	
		@RequestMapping("/editar_catalogo_escuela/{id}")
    public ModelAndView editCatEscuela(@PathVariable int id, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        CentroEscolar ce = null;
        try {
        	ce = service.findByIdEscolar(id);
        } catch(Exception e) {
        	e.printStackTrace();
        }
        List<Departamento> deps = null;
        try {
        	deps = service.findAllDepartaments();
        } catch(Exception e) {
        	e.printStackTrace();
        }
        mav.addObject("dep", deps);
        mav.addObject("catalogoEscuela", ce);
        mav.setViewName("nuevo_catalogo_escuela");
        verifyAdmin(session, mav);
        return mav;
    }
		
	@RequestMapping("/catalogo_escuela_guardado")
    public ModelAndView CatEscuelaGuardado(@Valid @ModelAttribute CentroEscolar ce ,BindingResult result, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        if(!result.hasErrors()) {
            try {
                service.insertarOeditarEscuela(ce);
            }catch(Exception e) {
                e.printStackTrace();
            }
            mav.setViewName("redirect:/catalogo_escuela");
        }
        else {
        	mav.setViewName("nuevo_catalogo_escuela");
        }
        verifyAdmin(session, mav);
        return mav;
    }
		
	@RequestMapping("/catalogo_materia")
	public ModelAndView catalogoMaterias(HttpSession session){
		ModelAndView mav = new ModelAndView();
		List<Materia> materias = null;
		try{
			materias = service.catalogoMaterias();
		}catch(Exception e){
			e.printStackTrace();
		}
		mav.addObject("materias", materias);
		mav.setViewName("catalogo_materia");
		verifyAdmin(session, mav);
		return mav;
	}
	

	
	@RequestMapping("/editar_catalogo_materia/{id}")
    public ModelAndView editCatMateria(HttpSession session, @PathVariable int id) {
		ModelAndView mav = new ModelAndView();
		Materia mat = null;
		try{
			mat = service.findByIdMateria(id);
		}catch(Exception e){
			e.printStackTrace();
		}
		mav.addObject("catalogoMateria", mat);
		mav.setViewName("nuevo_catalogo_materia");
        verifyAdmin(session, mav);
        return mav;
    }
	

	
	@RequestMapping("/nuevo_catalogo_materia")
	public ModelAndView nuevoCatalogoMaterias(HttpSession session){
		ModelAndView mav = new ModelAndView();
		mav.addObject("catalogoMateria", new Materia());
		mav.setViewName("nuevo_catalogo_materia");
		return mav;
	}
	
		@RequestMapping("/catalogo_usuario")
	public ModelAndView catalogoUsuarios(HttpSession session){
		ModelAndView mav = new ModelAndView();
		List<Usuario> usuarios = null;
		try{
			usuarios = service.catalogoUsuarios();
		}catch(Exception e){
			e.printStackTrace();
		}
		mav.addObject("usuarios", usuarios);
		mav.setViewName("catalogo_usuario");
		verifyAdmin(session, mav);
		return mav;
	}
		
	@RequestMapping("/nuevo_catalogo_usuario")
	public ModelAndView nuevoCatalogoUsuario(HttpSession session){
		ModelAndView mav = new ModelAndView();
		List<Departamento> deps = null;
		try {
			deps = service.findAllDepartaments();
		} catch(Exception e){
			e.printStackTrace();
		}
		mav.addObject("dep", deps);
		mav.addObject("catalogoUsuario", new Usuario());
		mav.setViewName("nuevo_catalogo_usuario");
		verifyAdmin(session, mav);
		return mav;
	}
	
	@RequestMapping("/editar_catalogo_usuario/{id}")
    public ModelAndView editCatUsuario(@PathVariable int id, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        Usuario user = null;
        try {
        	user = service.findByIdUsuario(id);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    	List<Departamento> deps = null;
		try {
			deps = service.findAllDepartaments();
		} catch(Exception e){
			e.printStackTrace();
		}
		mav.addObject("dep", deps);
		mav.addObject("catalogoUsuario", user);
    	mav.setViewName("nuevo_catalogo_usuario");
        verifyAdmin(session, mav);
        return mav;
    }
	
	@RequestMapping("/catalogo_usuario_guardado")
    public ModelAndView CatUsuarioGuardado(@Valid @ModelAttribute Usuario usuario ,BindingResult result, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        if(!result.hasErrors()) {
            try {
                service.insertarOeditarUsuario(usuario);;
            }catch(Exception e) {
                e.printStackTrace();
            }
            mav.setViewName("redirect:/catalogo_usuario");
        }
        else {
        	mav.setViewName("nuevo_catalogo_usuario");
        }
        verifyAdmin(session, mav);
        return mav;
    }
	
	@RequestMapping("/catalogo_materia_guardado")
    public ModelAndView catMateriaGuardado(@Valid @ModelAttribute Materia mat ,BindingResult result, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        if(!result.hasErrors()) {
            try {
                service.insertarOeditarMateria(mat);;
            }catch(Exception e) {
                e.printStackTrace();
            }
            mav.setViewName("redirect:/catalogo_materia");
        }
        else {
        	mav.setViewName("nuevo_catalogo_materia");
        }
        verifyAdmin(session, mav);
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
	public ModelAndView buscarAgregarAlumnos(HttpSession session){
		ModelAndView mav = new ModelAndView();
		//mav.addObject("objeto", new ExpedientePorNomApellidoDTO());
		mav.setViewName("../templates_coordinador/buscar_o_agregar_alumnos");
		verifyCoord(session, mav);
		return mav;
	}
	
	
	/*@RequestMapping("/filtrar_por_nom_ape")
	public ModelAndView filtrar(@ModelAttribute ExpedientePorNomApellidoDTO pclave, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		List<ExpedientePorNomApellidoDTO> estudiantes = null;
		System.out.println(pclave.getNombres()+pclave.getApellidos());
		try {
			if(pclave.getNombres().isEmpty()) {
				
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
		verifyCoord(session, mav);
		return mav;
	}*/

	@RequestMapping("/filtrar_por_nom_ape")
	public ModelAndView filtrar(@RequestParam String nomApe, @RequestParam String clave, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		List<ExpedientePorNomApellidoDTO> estudiantes = null;
		clave = clave + "%";
		if(nomApe.equals("n")){
			System.out.println(nomApe);
			try{
				estudiantes = service.expedientePorNombreOApellido(clave, " ");
			} catch(Exception e ) {
				e.printStackTrace();
			}
		} else{
			try{
				estudiantes = service.expedientePorNombreOApellido(" ", clave);
			} catch(Exception e ) {
				e.printStackTrace();
			}
		}
		System.out.println(estudiantes.size());
		mav.addObject("estudiantes", estudiantes);
		mav.setViewName("../templates_coordinador/tabla_estudiantes");
		verifyCoord(session, mav);
		return mav;
	}

	@RequestMapping("/editar_expediente_existente/{idEstudiante}")
	public ModelAndView editarExpediente(@PathVariable Integer idEstudiante, HttpSession session){
		ModelAndView mav = new ModelAndView();
		Estudiante est = new Estudiante();
		est = service.findByIdEstudiante(idEstudiante);
		List<Departamento> deps = null;
		try {
			deps = service.findAllDepartaments();
		} catch(Exception e){
			e.printStackTrace();
		}
		mav.addObject("dep", deps);
		mav.addObject("estudianteNuevo", est);
		mav.setViewName("../templates_coordinador/editar_expediente_existente");
		verifyCoord(session, mav);
		return mav;
	}
	
	@RequestMapping("/agregar_expediente_nuevo" )
	public ModelAndView nuevoExpediente(HttpSession session){
		ModelAndView mav = new ModelAndView();
		List<Departamento> deps = null;
		try {
			deps = service.findAllDepartaments();
		} catch(Exception e){
			e.printStackTrace();
		}
		mav.addObject("dep", deps);
		mav.addObject("estudianteNuevo", new Estudiante());
		mav.setViewName("../templates_coordinador/editar_expediente_existente");
		verifyCoord(session, mav);
		return mav;
	}
	
	@RequestMapping("/expediente_guardado" )
	public ModelAndView nuevoExpedienteGuardado(@Valid @ModelAttribute Estudiante estudiante ,BindingResult result, HttpSession session){
		ModelAndView mav = new ModelAndView();
		if(!result.hasErrors()) {
            try {
                service.insertarOeditarEstudiante(estudiante);
            }catch(Exception e) {
                e.printStackTrace();
            }
            mav.setViewName("redirect:/buscar_o_agregar_alumnos");
        }
        else {
    		List<Departamento> deps = null;
    		try {
    			deps = service.findAllDepartaments();
    		} catch(Exception e){
    			e.printStackTrace();
    		}
    		mav.addObject("dep", deps);
    		mav.addObject("estudianteNuevo", estudiante);
        	mav.setViewName("../templates_coordinador/editar_expediente_existente");
        }
		verifyCoord(session, mav);
        return mav;
	}
	
	@RequestMapping("/materias_cursadas/{idEstudiante}")
	public ModelAndView materiasCursadas(@PathVariable Integer idEstudiante, HttpSession session){
		ModelAndView mav = new ModelAndView();
		List<MateriasPorEstudianteDTO> matCursadas = null;
		Estudiante est = null;
		try {
			matCursadas = service.materiasPorEstudiante(idEstudiante);
			est = service.findByIdEstudiante(idEstudiante);
		}catch(Exception e ) {
			e.printStackTrace();
		}
		mav.addObject("matCursadas", matCursadas);
		mav.addObject("nombreEst", est.getNombres());
		mav.addObject("idEst", est.getIdEstudiante());
		mav.setViewName("../templates_coordinador/materias_cursadas");
		verifyCoord(session, mav);
		return mav;
	}
	
	@RequestMapping("/agregar_nueva_materia_cursada/{id}")
	public ModelAndView nuevaMatCursada(@PathVariable Integer id, HttpSession session){
		ModelAndView mav = new ModelAndView();
		List<Materia> mats = null;
		try {
			mats = service.catalogoMaterias();
		}catch(Exception e ) {
			e.printStackTrace();
		}
		System.out.println(mats.size());
		EstudianteMateria estMat = new EstudianteMateria();
		estMat.setIdEstudiante(id);
		mav.addObject("estMat", estMat);
		mav.addObject("materias", mats);
		mav.addObject("dto", new MateriasPorEstudianteDTO());
		mav.setViewName("../templates_coordinador/agregar_editar_materia");
		verifyCoord(session, mav);
		return mav;
	}
	
	@RequestMapping("/editar_materia_cursada/{id}")
	public ModelAndView editarMarCursada(@PathVariable int id, HttpSession session){
		ModelAndView mav = new ModelAndView();
		EstudianteMateria estMat = null;
		MateriasPorEstudianteDTO dto = null;
		try {
			estMat = service.findEstudianteMateriaById(id);
			dto = service.findMateriaEstudianteDTOById(id);
		}catch(Exception e ) {
			e.printStackTrace();
		}
		mav.setViewName("../templates_coordinador/agregar_editar_materia");
		mav.addObject("estMat", estMat);
		mav.addObject("dto", dto);
		verifyCoord(session, mav);
		return mav;
	}
	
	@RequestMapping("/materia_cursada_guardada")
	public ModelAndView nuevaMatGuardada(@Valid @ModelAttribute Materia materia ,BindingResult result, HttpSession session){
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
		verifyCoord(session, mav);
        return mav;
	}

	@RequestMapping("/guardar_materia")
	public ModelAndView guardarMateria(@Valid @ModelAttribute EstudianteMateria estMat, BindingResult result, HttpSession session){
		ModelAndView mav = new ModelAndView();
		Estudiante est = new Estudiante();
		System.out.println(estMat.getIdEstudiante());
		if(!result.hasErrors()){
			mav.setViewName("../templates_coordinador/materias_cursadas");
			List<MateriasPorEstudianteDTO> matCursadas = null;
			try {
				est = service.findByIdEstudiante(estMat.getIdEstudiante());
				estMat.setEstudiante(est);
				service.agregarOeditarMateriaCursada(estMat);
				matCursadas = service.materiasPorEstudiante(estMat.getIdEstudiante());
			}catch(Exception e ) {
				e.printStackTrace();
			}
			mav.addObject("nombreEst", est.getNombres());
			mav.addObject("idEst", est.getIdEstudiante());
			mav.addObject("matCursadas", matCursadas);
			}
		else{
			mav.setViewName("../templates_coordinador/agregar_editar_materia");
			MateriasPorEstudianteDTO dto = null;
			try {
				dto = service.findMateriaEstudianteDTOById(estMat.getIdEstudianteMateria());
			}catch(Exception e ) {
				e.printStackTrace();
			}
			mav.addObject("estMat", estMat);
			mav.addObject("dto", dto);
		}
		verifyCoord(session, mav);
		return mav;
	}
}
