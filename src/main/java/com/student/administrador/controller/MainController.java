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
import org.springframework.web.bind.annotation.PostMapping;
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
    public ModelAndView CatUsuarioGuardado(@ModelAttribute Usuario usuario, HttpSession session) {
        ModelAndView mav = new ModelAndView();
		if((usuario.getNombre() == "" || usuario.getNombre().length() > 30) || (usuario.getApellido() == "" || usuario.getApellido().length() > 30) ||
			(usuario.getDireccion() == "" || usuario.getDireccion().length() > 75) || (usuario.getUsuario() == "" || usuario.getUsuario().length() > 15) || 
			(usuario.getContra() == "" || usuario.getContra().length() > 15) || usuario.getmunicipio().getIdMunicipio() == 0) {
			List<Departamento> deps = null;
			mav.setViewName("nuevo_catalogo_usuario");
			mav.addObject("catalogoUsuario", usuario);
			try {
				deps = service.findAllDepartaments();
			} catch(Exception e){
				e.printStackTrace();
			}
			mav.addObject("dep", deps);

			if(usuario.getNombre() == ""){
				mav.addObject("nombreError", "El campo Nombre no puede ir vacio");
			} else if(usuario.getNombre().length() > 30){
				mav.addObject("nombreError", "El nombre no debe exceder los 30 caracteres");
			}
			if(usuario.getApellido() == ""){
				mav.addObject("apellidoError", "El campo Apellido no puede ir vacio");
			} else if(usuario.getApellido().length() > 30){
				mav.addObject("apellidoError", "El apellido no debe exceder los 30 caracteres");
			}
			if(usuario.getDireccion() == ""){
				mav.addObject("direccionError", "El campo Direccion no puede ir vacio");
			} else if(usuario.getDireccion().length() > 75){
				mav.addObject("direccionError", "la direccion no debe exceder los 75 caracteres");
			}
			if(usuario.getmunicipio().getIdMunicipio() == 0){
				mav.addObject("munError", "Debe seleccionar un municipio");
			} 
			if(usuario.getUsuario() == ""){
				mav.addObject("usuarioError", "El campo Usuario no puede ir vacio");
			} else if(usuario.getUsuario().length() > 15){
				mav.addObject("usuarioError", "El usuario no debe exceder los 15 caracteres");
			}
			if(usuario.getContra() == ""){
				mav.addObject("passError", "La Contrasenia no puede ir vacio");
			} else if(usuario.getContra().length() > 15){
				mav.addObject("passError", "La contrasenia no debe exceder los 15 caracteres");
			}
        }
        else {
			List<Usuario> usuarios = null;
			try {
				service.insertarOeditarUsuario(usuario);
				usuarios = service.catalogoUsuarios();
            }catch(Exception e) {
                e.printStackTrace();
			}
            mav.setViewName("redirect:/catalogo_usuario");
        }
        verifyAdmin(session, mav);
        return mav;
    }
	
	@RequestMapping("/catalogo_materia_guardado")
    public ModelAndView catMateriaGuardado(@Valid @ModelAttribute Materia mat ,BindingResult result, HttpSession session) {
        ModelAndView mav = new ModelAndView();
        if(!result.hasErrors()) {
        	service.insertarOeditarMateria(mat);
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

	@RequestMapping("/filtrar_por_nom_ape")
	public ModelAndView filtrar(@RequestParam String nomApe, @RequestParam String clave, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		List<ExpedientePorNomApellidoDTO> estudiantes = null;
		clave = clave.toLowerCase();
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
		if(estudiantes.size() == 0){
			mav.addObject("msg", "No se ha encontrado estudiantes");
			mav.setViewName("../templates_coordinador/buscar_o_agregar_alumnos");
		}
		else{
			mav.addObject("estudiantes", estudiantes);
			mav.setViewName("../templates_coordinador/tabla_estudiantes");
		}
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
		System.out.println(idEstudiante);
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
	public ModelAndView nuevoExpedienteGuardado(@ModelAttribute Estudiante estudiante, HttpSession session){
		ModelAndView mav = new ModelAndView();
		if((estudiante.getNombres() == "" || estudiante.getNombres().length() > 60) || (estudiante.getApellidos() == "" || estudiante.getApellidos().length() > 60) ||
			(estudiante.getCarnet() == "" || estudiante.getCarnet().length() != 9) || /*estudiante.getFechaNac() == null ||*/ 
			(estudiante.getDireccion() == "" || estudiante.getDireccion().length() > 50) || (estudiante.getTel_fijo() == "" || estudiante.getTel_fijo().length() != 8) ||
			(estudiante.getTel_movil() == "" || estudiante.getTel_movil().length() != 8) || (estudiante.getNombrePadre() == "" || estudiante.getNombrePadre().length() > 30) ||
			(estudiante.getNombreMadre() == "" || estudiante.getNombreMadre().length() > 30) || estudiante.getcentroEscolar().getIdEscolar() == 0) {
            List<Departamento> deps = null;
    		try {
    			deps = service.findAllDepartaments();
    		} catch(Exception e){
    			e.printStackTrace();
			}
			if(estudiante.getNombres() == ""){
				mav.addObject("nombreError", "El campo Nombres no puede ir vacio");
			}
			else if(estudiante.getNombres().length() > 60){
				mav.addObject("nombreError", "El campo Nombres no puede exceder de 60 caracteres");
			}
			if(estudiante.getApellidos() == ""){
				mav.addObject("apellidoError", "El campo Apellidos no puede ir vacio");
			}
			else if(estudiante.getApellidos().length() > 60){
				mav.addObject("apellidoError", "El campo Apellidos no puede exceder de 60 caracteres");
			}
			if(estudiante.getCarnet() == ""){
				mav.addObject("carnetError", "El campo Carnet no puede ir vacio");
			}
			else if(estudiante.getCarnet().length() != 9){
				mav.addObject("carnetError", "El carnet ingresado no es valido");
			}
			/*if(estudiante.getFechaNac() == null){
				mav.addObject("fechaError", "El campo Fecha de Nacimiento no puede ir vacio");
			}*/
			if(estudiante.getDireccion() == ""){
				mav.addObject("direccionError", "El campo Direccion no puede ir vacio");
			}
			else if(estudiante.getDireccion().length() > 50){
				mav.addObject("direccionError", "El campo Direccion no puede exceder de 50 caracteres");
			}
			if(estudiante.getTel_movil() == ""){
				mav.addObject("tMovilError", "El campo Telefono Movil no puede ir vacio");
			}
			else if(estudiante.getTel_movil().length() != 8){
				mav.addObject("tMovilError", "El numero de telefono ingresado no es valido");
			}
			if(estudiante.getTel_fijo() == ""){
				mav.addObject("tFijoError", "El campo Telefono Fijo no puede ir vacio");
			}
			else if(estudiante.getTel_fijo().length() != 8){
				mav.addObject("tFijoError", "El numero de telefono ingresado no es valido");
			}
			if(estudiante.getNombrePadre() == ""){
				mav.addObject("nPadreError", "El ombre del padre no puede ir vacio");
			}
			else if(estudiante.getNombrePadre().length() > 30){
				mav.addObject("nPadreError", "El nombre no puede exceder de 30 caracteres");
			}
			if(estudiante.getNombreMadre() == ""){
				mav.addObject("nMadreError", "El nombre de la madre no puede ir vacio");
			}
			else if(estudiante.getNombreMadre().length() > 30){
				mav.addObject("nMadreError", "El nombre no puede exceder de 30 caracteres");
			}
			if(estudiante.getcentroEscolar().getIdEscolar() == 0){
				mav.addObject("escolarError", "No ha seleccionado ningun centro escolar");
			}
    		mav.addObject("dep", deps);
    		mav.addObject("estudianteNuevo", estudiante);
			mav.setViewName("../templates_coordinador/editar_expediente_existente");
        }
        else {
			try {
                service.insertarOeditarEstudiante(estudiante);
            }catch(Exception e) {
                e.printStackTrace();
			}
			mav.setViewName("redirect:/buscar_o_agregar_alumnos");
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
		Estudiante  est = new Estudiante();
		try {
			mats = service.materiasActivas();
			est = service.findByIdEstudiante(id);
		}catch(Exception e ) {
			e.printStackTrace();
		}
		EstudianteMateria estMat = new EstudianteMateria();
		estMat.setIdEstudiante(id);
		mav.addObject("estMat", estMat);
		mav.addObject("materias", mats);
		MateriasPorEstudianteDTO dto = new MateriasPorEstudianteDTO();
		dto.setNombreEstudiante(est.getNombres());
		mav.addObject("dto", dto);
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
	public ModelAndView guardarMateria(@ModelAttribute EstudianteMateria estMat, HttpSession session){
		ModelAndView mav = new ModelAndView();
		List<MateriasPorEstudianteDTO> matCursadas = null;
		Estudiante est = null;
		boolean flag = false;
		if(estMat.getIdEstudianteMateria() == null) {
			int id = estMat.getMateria().getIdMateria();
			try {
				matCursadas = service.materiasPorEstudiante(estMat.getIdEstudiante());
				est = service.findByIdEstudiante(estMat.getIdEstudiante());
				for(int i = 0; i < matCursadas.size(); i++) {
					if(id == matCursadas.get(i).getIdMateria()) {
						flag = true;
					}
				}
			}catch(Exception e ) {
				e.printStackTrace();
			}
		}
		if(flag || estMat.getAnio() == null || estMat.getCicloCursado() == null || (estMat.getNota() == null || (estMat.getNota() > 10 || estMat.getNota() < 0))){
			mav.setViewName("../templates_coordinador/agregar_editar_materia");
			MateriasPorEstudianteDTO dto = null;
			List<Materia> mats = null;
			try {
				mats = service.materiasActivas();
				if(estMat.getIdEstudianteMateria() == null){
					dto = new MateriasPorEstudianteDTO();
					dto.setNombreEstudiante(est.getNombres());
				} else{
					dto = service.findMateriaEstudianteDTOById(estMat.getIdEstudianteMateria());
				}
			}catch(Exception e ) {
				e.printStackTrace();
			}
			if(flag) {
				mav.addObject("materiaError", "La materia seleccionada ya fue cursada por el estudiante");
			}
			if(estMat.getAnio() == null){
				mav.addObject("anioError", "El campo Anio no puede ir vacio");
			}
			if(estMat.getCicloCursado() == null){
				mav.addObject("cicloError", "El campo Ciclo Cursado no puede ir vacio");
			}
			if(estMat.getNota() == null){
				mav.addObject("notaError", "El campo nota no puede ir vacio");
			}
			else if(estMat.getNota() > 10 || estMat.getNota() < 0){
				mav.addObject("notaError", "La nota ingresada no es valida");
			}
			mav.addObject("estMat", estMat);
			mav.addObject("materias", mats);
			mav.addObject("dto", dto);
		}
		else{
			mav.setViewName("../templates_coordinador/materias_cursadas");
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
		verifyCoord(session, mav);
		return mav;
	}
}