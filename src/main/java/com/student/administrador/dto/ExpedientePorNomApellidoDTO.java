package com.student.administrador.dto;

public class ExpedientePorNomApellidoDTO {

	private String nombres;
	private String apellidos;
	private Integer aprobadas;
	private Integer reprobadas;
	private Float promedio;
	private Integer idEstudiante;

	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public Integer getAprobadas() {
		return aprobadas;
	}
	public void setAprobadas(Integer aprobadas) {
		this.aprobadas = aprobadas;
	}
	public Integer getReprobadas() {
		return reprobadas;
	}
	public void setReprobadas(Integer reprobadas) {
		this.reprobadas = reprobadas;
	}
	public Float getPromedio() {
		return promedio;
	}
	public void setPromedio(Float promedio) {
		this.promedio = promedio;
	}
	public Integer getIdEstudiante() {
		return idEstudiante;
	}
	public void setIdEstudiante(Integer idEstudiante) {
		this.idEstudiante = idEstudiante;
	}
}
