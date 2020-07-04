package com.student.administrador.dto;

public class MateriasPorEstudianteDTO {
	
	private String nombreMateria;
	private String nombreEstudiante;
	private Integer anio;
	private Integer ciclo;
	private float nota;
	private String resultado;
	private Integer idEstudianteMateria;
	
	public String getNombreMateria() {
		return nombreMateria;
	}
	public void setNombreMateria(String nombreMateria) {
		this.nombreMateria = nombreMateria;
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
	public String getNombreEstudiante() {
		return nombreEstudiante;
	}
	public void setNombreEstudiante(String nombreEstudiante) {
		this.nombreEstudiante = nombreEstudiante;
	}
}
