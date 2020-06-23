package com.student.administrador.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema="public", name="departamento")
public class Departamento {
	
	@Id
	@Column(name="idDepartamento")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idDepartamento;
	
	@Column(name="nombre")
	private String nombre;

	public Integer getIdDepartamento() {
		return idDepartamento;
	}

	public void setIdDepartamento(Integer idDepartamento) {
		this.idDepartamento = idDepartamento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
