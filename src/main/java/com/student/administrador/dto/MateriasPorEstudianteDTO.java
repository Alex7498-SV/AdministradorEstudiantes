package com.student.administrador.dto;

public class MateriasPorEstudianteDTO {
	
	private String nombreMateria;
	private String nombreUsuario;
	private Integer anio;
	private Integer ciclo;
	private float nota;
	private String resultado;
	private Integer idEstudianteMateria;
	private Integer idEstudiante;
	private Integer idMateria;
	public String getNombreMateria() {
		return nombreMateria;
	}
	public void setNombreMateria(String nombreMateria) {
		this.nombreMateria = nombreMateria;
	}
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	public Integer getCiclo() {
		return ciclo;
	}
	public void setCiclo(Integer ciclo) {
		this.ciclo = ciclo;
	}
	public float getNota() {
		return nota;
	}
	public void setNota(float nota) {
		this.nota = nota;
	}
	public String getResultado() {
		return resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	public Integer getIdEstudianteMateria() {
		return idEstudianteMateria;
	}
	public void setIdEstudianteMateria(Integer idEstudianteMateria) {
		this.idEstudianteMateria = idEstudianteMateria;
	}
	public Integer getIdEstudiante() {
		return idEstudiante;
	}
	public void setIdEstudiante(Integer idEstudiante) {
		this.idEstudiante = idEstudiante;
	}
	public Integer getIdMateria() {
		return idMateria;
	}
	public void setIdMateria(Integer idMateria) {
		this.idMateria = idMateria;
	}
}
