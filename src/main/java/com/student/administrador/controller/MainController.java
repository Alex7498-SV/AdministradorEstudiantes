package com.student.administrador.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.student.administrador.domain.CentroEscolar;
import com.student.administrador.domain.Materia;
import com.student.administrador.services.TodoService;


@Controller
public class MainController {

	@Autowired
	private TodoService service;
	
	@RequestMapping("/login")
	public ModelAndView initMain(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("login");
		return mav;
	}
	
	
	@RequestMapping("/register")
	public ModelAndView nCuenta(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("nueva_cuenta");
		return mav;
	}
	
	@RequestMapping("/menu_admin")
	public ModelAndView menuAdmin(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("menu_admin");
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
			materias = service.catalogoEscuelas();
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
}
