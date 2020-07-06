package com.student.administrador.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(schema="public", name="estudiantemateria")
public class EstudianteMateria {
	
	@Id
	@Column(name="idEstudianteMateria")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idEstudianteMateria;
	
	@Column(name="nota")
	//@NotEmpty(message="El campo Nota no puede ir vacio")
	private Float nota;

	@Column(name="anio")
	//@NotEmpty(message="El campo Año no puede ir vacio")
	private Integer anio;
	
	@Column(name="ciclo_cursado")
	//@NotEmpty(message="El campo Ciclo no puede ir vacio")
	private Integer cicloCursado;
	
	@ManyToOne
	@JoinColumn(name="idEstudiante")
	private Estudiante estudiante; 

	@Transient
	private Integer idEstudiante;
	
	@ManyToOne
	@JoinColumn(name="idMateria")
	private Materia materia;

	@Transient
	private Integer idMateria;

	public Integer getIdEstudianteMateria() {
		return idEstudianteMateria;
	}

	public void setIdEstudianteMateria(Integer idEstudianteMateria) {
		this.idEstudianteMateria = idEstudianteMateria;
	}

	public Float getNota() {
		return nota;
	}

	public void setNota(Float nota) {
		this.nota = nota;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Integer getCicloCursado() {
		return cicloCursado;
	}

	public void setCicloCursado(Integer cicloCursado) {
		this.cicloCursado = cicloCursado;
	}

	public Estudiante getEstudiante() {
		return estudiante;
	}

	public void setEstudiante(Estudiante estudiante) {
		this.estudiante = estudiante;
	}

	public Materia getMateria() {
		return materia;
	}

	public void setMateria(Materia materia) {
		this.materia = materia;
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
