package com.student.administrador.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
	
	@OneToMany(mappedBy="departamento", fetch=FetchType.LAZY)
	private List<Municipio> municipios;

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

	public List<Municipio> getMunicipios() {
		return municipios;
	}

	public void setMunicipios(List<Municipio> municipios) {
		this.municipios = municipios;
	}
}
