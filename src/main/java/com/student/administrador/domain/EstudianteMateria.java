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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(schema="public", name="estudianteMateria")
public class EstudianteMateria {
	
	@Id
	@Column(name="idEstudianteMateria")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idEstudianteMateria;
	
	@Column(name="nota")
	@NotEmpty(message="El campo Nota no puede ir vacio")
	private Float nota;

	@Column(name="anio")
	@NotEmpty(message="El campo Año no puede ir vacio")
	private Integer anio;
	
	@Column(name="ciclo_cursado")
	@NotEmpty(message="El campo Ciclo no puede ir vacio")
	private Integer cicloCursado;
	
	@ManyToOne
	@MapsId("idEstudiante")
	@JoinColumn(name="idEstudiante")
	private Estudiante estudiante; 
	
	@ManyToOne
	@MapsId("idMateria")
	@JoinColumn(name="idMateria")
	private Materia materia; 
}
