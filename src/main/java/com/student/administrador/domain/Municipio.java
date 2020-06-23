package com.student.administrador.domain;

import java.util.List;
import java.util.Set;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(schema="public", name="municipio")
public class Municipio {
	
	@Id
	@Column(name="idMunicipio")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idMunicipio;
	
	@Column(name="nombre")
	private String nombre;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="idDepartamento")
	private Departamento departamento;
	
	@OneToMany(mappedBy="municipio", fetch=FetchType.EAGER)
	private Set<CentroEscolar> centrosEscolares;
	
	@OneToMany(fetch=FetchType.EAGER)
	private Set<Usuario> usuarios;

	public Integer getIdMunicipio() {
		return idMunicipio;
	}

	public void setIdMunicipio(Integer idMunicipio) {
		this.idMunicipio = idMunicipio;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public Set<CentroEscolar> getCentrosEscolares() {
		return centrosEscolares;
	}

	public void setCentrosEscolares(Set<CentroEscolar> centrosEscolares) {
		this.centrosEscolares = centrosEscolares;
	}

	public Set<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Set<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
}
